import os
from pathlib import Path

from dotenv import load_dotenv

# 从 ai-service 目录加载 .env，避免因启动目录不同而读不到配置
_env_path = Path(__file__).resolve().parent / ".env"
load_dotenv(_env_path)

# AI 提供商：openai（付费）| deepseek（低价/有免费额度，兼容 OpenAI 接口）
AI_PROVIDER = os.getenv("AI_PROVIDER", "deepseek").lower().strip()
if AI_PROVIDER not in ("openai", "deepseek"):
    AI_PROVIDER = "deepseek"

# OpenAI（ChatGPT）
OPENAI_API_KEY = os.getenv("OPENAI_API_KEY", "")
OPENAI_MODEL = os.getenv("OPENAI_MODEL", "gpt-4o-mini")

# DeepSeek（可用 OpenAI 同款 SDK，新用户有免费额度，价格远低于 OpenAI）
# 获取 Key：https://platform.deepseek.com → API Keys
DEEPSEEK_API_KEY = os.getenv("DEEPSEEK_API_KEY", "").strip() or OPENAI_API_KEY.strip()
DEEPSEEK_MODEL = os.getenv("DEEPSEEK_MODEL", "deepseek-chat")
DEEPSEEK_BASE_URL = "https://api.deepseek.com"
