# -*- coding: utf-8 -*-
"""
玄学平台 AI 服务：通过 ChatGPT 生成起名、星座运势、风水内容
"""
import json
import re
from typing import Optional, List

from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel

from config import OPENAI_API_KEY, OPENAI_MODEL

app = FastAPI(title="玄学平台 AI 服务", version="1.0")
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=False,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Lazy init client
_client = None

def get_client():
    global _client
    if not OPENAI_API_KEY:
        raise HTTPException(status_code=503, detail="未配置 OPENAI_API_KEY，请在 .env 中设置")
    if _client is None:
        from openai import OpenAI
        _client = OpenAI(api_key=OPENAI_API_KEY)
    return _client


def chat(prompt: str, system: str = "你是一位专业、友好的玄学与命理顾问，回答简洁有条理。") -> str:
    client = get_client()
    r = client.chat.completions.create(
        model=OPENAI_MODEL,
        messages=[
            {"role": "system", "content": system},
            {"role": "user", "content": prompt},
        ],
        temperature=0.8,
    )
    return (r.choices[0].message.content or "").strip()


# ---------- 请求体 ----------
class GenerateNamesRequest(BaseModel):
    surname: str = "王"
    gender: int = 1  # 1男 2女
    birth_year: Optional[int] = None
    birth_month: Optional[int] = None
    birth_day: Optional[int] = None
    favorable_elements: Optional[str] = None  # 八字喜用神，如 "金,水"
    count: int = 6


class ZodiacFortuneRequest(BaseModel):
    sign: str  # 如 白羊座
    period: str = "today"  # today | weekly | monthly


class FengshuiRequest(BaseModel):
    category: Optional[str] = None  # 如 大门、客厅、卧室


class CalendarYiJiRequest(BaseModel):
    date: str  # YYYY-MM-DD


# ---------- 今日宜忌（万年历） ----------
@app.post("/api/ai/calendar")
def api_calendar_yiji(req: CalendarYiJiRequest):
    """根据日期用 ChatGPT 生成今日宜忌（黄历风格）。"""
    prompt = f"""请为公历日期【{req.date}】写一段「今日宜忌」（黄历风格），要求：
1. 宜：列出 5-8 条适宜做的事（如嫁娶、动土、开市、出行等），用顿号或逗号分隔，或分条列出
2. 忌：列出 5-8 条不宜做的事
3. 可加一句简短吉凶或冲煞说明（如冲某生肖、宜某方位）
4. 直接以 JSON 格式输出，不要 markdown 代码块，键为：yi（宜，字符串或数组）、ji（忌，字符串或数组）、summary（简短说明，可选）
例如：{{"yi":"嫁娶、开市、动土、...", "ji":"破土、安葬、...", "summary":"..."}}"""
    try:
        text = chat(prompt)
        text = text.strip()
        if text.startswith("```"):
            text = re.sub(r"^```\w*\n?", "", text).strip()
            text = re.sub(r"\n?```\s*$", "", text).strip()
        m = re.search(r"\{[\s\S]*\}", text)
        if m:
            data = json.loads(m.group())
            yi = data.get("yi")
            ji = data.get("ji")
            if isinstance(yi, list):
                yi = "、".join(str(x) for x in yi)
            if isinstance(ji, list):
                ji = "、".join(str(x) for x in ji)
            return {
                "date": req.date,
                "yi": yi or "诸事不宜",
                "ji": ji or "无",
                "summary": data.get("summary", ""),
            }
        return {"date": req.date, "yi": "诸事不宜", "ji": "无", "summary": text[:200]}
    except HTTPException:
        raise
    except json.JSONDecodeError:
        return {"date": req.date, "yi": "诸事不宜", "ji": "无", "summary": ""}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))


# ---------- 起名 ----------
@app.post("/api/ai/names")
def api_generate_names(req: GenerateNamesRequest):
    """根据姓氏、性别、八字喜用神等，用 ChatGPT 生成 2-3 字名字并打分。"""
    prompt = f"""请为新生儿起名，要求：
1. 姓氏：{req.surname}（只取第一个字）
2. 性别：{"男" if req.gender == 1 else "女"}
3. 名字总长度 2-3 个字（姓+名），即名为 1 字或 2 字，要好听、顺口、寓意吉祥
"""
    if req.favorable_elements:
        prompt += f"4. 八字喜用神（优先选用字五行）：{req.favorable_elements}\n"
    prompt += f"""
请直接输出 {req.count} 个推荐名字，每个占一行，格式严格为：
名字|分数(0-99)|一句话简评（如：五格吉、寓意好、顺口）
例如：
王梓涵|92|梓为乔木寓意成才，涵为包容，音韵顺口
不要输出其他解释，只输出上述格式的 {req.count} 行。"""
    try:
        text = chat(prompt)
        names = []
        for line in text.split("\n"):
            line = line.strip()
            if not line or "|" not in line:
                continue
            parts = line.split("|", 2)
            if len(parts) >= 2:
                name = parts[0].strip()
                score = 85
                try:
                    score = min(99, max(0, int(re.sub(r"\D", "", parts[1]) or "85")))
                except Exception:
                    pass
                analysis = parts[2].strip() if len(parts) > 2 else "寓意吉祥，读音响亮"
                names.append({"name": name, "score": score, "analysis": analysis})
        return {"names": names[: req.count]}
    except HTTPException:
        raise
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))


# ---------- 星座运势 ----------
@app.post("/api/ai/zodiac")
def api_zodiac_fortune(req: ZodiacFortuneRequest):
    """用 ChatGPT 生成指定星座的今日/本周/本月运势。"""
    period_cn = {"today": "今日", "weekly": "本周", "monthly": "本月"}.get(req.period, "今日")
    prompt = f"""请为【{req.sign}】写一段{period_cn}运势，要求：
1. 内容生动、有个人化建议，不要模板化
2. 包含：综合运势、爱情、事业、财运、健康、幸运颜色、幸运数字、幸运方位
3. 给出 1-100 的分数：综合、爱情、事业、财运、健康
4. 直接以 JSON 格式输出，不要 markdown 代码块，只输出一个 JSON 对象，键为：
content（运势正文字符串）、overallScore、loveScore、careerScore、wealthScore、healthScore、luckyColor、luckyNumber、luckyDirection
例如：{{"content":"...", "overallScore":85, "loveScore":78, ...}}"""
    try:
        text = chat(prompt)
        # 尝试从回复中提取 JSON
        text = text.strip()
        if text.startswith("```"):
            text = re.sub(r"^```\w*\n?", "", text).strip()
            text = re.sub(r"\n?```\s*$", "", text).strip()
        m = re.search(r"\{[\s\S]*\}", text)
        if m:
            data = json.loads(m.group())
            return {
                "zodiacSign": req.sign,
                "fortuneType": req.period if req.period in ("today", "weekly", "monthly") else "daily",
                "content": data.get("content", ""),
                "overallScore": int(data.get("overallScore", 80)),
                "loveScore": int(data.get("loveScore", 75)),
                "careerScore": int(data.get("careerScore", 78)),
                "wealthScore": int(data.get("wealthScore", 76)),
                "healthScore": int(data.get("healthScore", 82)),
                "luckyColor": str(data.get("luckyColor", "蓝色")),
                "luckyNumber": int(data.get("luckyNumber", 7)),
                "luckyDirection": str(data.get("luckyDirection", "东方")),
                "element": "",
                "rulingPlanet": "",
            }
        return {
            "zodiacSign": req.sign,
            "fortuneType": "daily",
            "content": text,
            "overallScore": 80,
            "loveScore": 75,
            "careerScore": 78,
            "wealthScore": 76,
            "healthScore": 82,
            "luckyColor": "蓝色",
            "luckyNumber": 7,
            "luckyDirection": "东方",
            "element": "",
            "rulingPlanet": "",
        }
    except HTTPException:
        raise
    except json.JSONDecodeError as e:
        raise HTTPException(status_code=500, detail=f"AI 返回格式解析失败: {e}")
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))


# ---------- 风水 ----------
@app.post("/api/ai/fengshui")
def api_fengshui(req: FengshuiRequest):
    """用 ChatGPT 生成风水常识或指定类别的内容。"""
    category = req.category or "通用家居风水"
    prompt = f"""请写一段关于【{category}】的风水常识，要求：
1. 实用、易懂，适合普通读者
2. 包含 3-6 条具体规则或建议，每条带简短解释
3. 语言亲切，不要过于玄乎
4. 直接输出正文，不要标题写「风水」二字，不要用 markdown 标题，用「一、二、三」或「◆」分条即可。"""
    try:
        content = chat(prompt)
        return {"content": content, "category": category}
    except HTTPException:
        raise
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))


@app.get("/health")
def health():
    return {"status": "ok", "openai_configured": bool(OPENAI_API_KEY)}


if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=9000)
