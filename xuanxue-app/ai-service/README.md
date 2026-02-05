# 玄学平台 AI 服务（Python + ChatGPT）

本服务通过调用 OpenAI ChatGPT API，为玄学平台提供**起名**、**星座运势**、**风水**等内容的智能生成，使内容更丰富、不模板化。

## 环境要求

- Python 3.9+
- OpenAI API Key

## 配置

1. 复制环境变量示例并填入你的 API Key：

```bash
cp .env.example .env
```

编辑 `.env`：

```
OPENAI_API_KEY=sk-your-openai-api-key-here
OPENAI_MODEL=gpt-4o-mini
```

- **OPENAI_API_KEY**：必填。在 [OpenAI 平台](https://platform.openai.com/api-keys) 创建。
- **OPENAI_MODEL**：可选，默认 `gpt-4o-mini`（成本低、响应快）。可改为 `gpt-4o` 等获得更好效果。

## 安装与运行

```bash
cd ai-service
pip install -r requirements.txt
python main.py
```

或使用 uvicorn：

```bash
uvicorn main:app --host 0.0.0.0 --port 9000
```

服务默认在 **http://localhost:9000** 启动。

## 接口说明

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/ai/names | 根据姓氏、性别、八字喜用神生成名字 |
| POST | /api/ai/zodiac | 生成指定星座的今日/本周/本月运势 |
| POST | /api/ai/fengshui | 生成风水常识内容 |
| GET  | /health | 健康检查，可查看是否已配置 API Key |

## 与后端协作

- Java 后端在 `application.yml` 中配置 `ai.service.enabled: true` 和 `ai.service.url: http://localhost:9000`。
- 当 AI 服务可用时，起名、星座运势、风水接口会优先使用 ChatGPT 结果；若请求失败或未配置，则回退到本地规则生成。

## 关闭 AI 仅用本地规则

- 将 `ai.service.enabled` 设为 `false`，或关闭本 Python 服务即可。
