# SQL 建表脚本

本目录集中存放 SSA-Project 所有建表语句。

- **init.sql**：MySQL 全量初始化（库 + 表 + 测试数据），供 Docker MySQL 挂载或生产环境执行。
- 各服务（auth-service、xuanxue-service 等）开发环境若用 H2，可自行建表或引用本目录表结构。

## Docker 使用

在 `docker-compose.yml` 中 MySQL 挂载示例：

```yaml
volumes:
  - ../sql/init.sql:/docker-entrypoint-initdb.d/init.sql:ro
```

## 手动执行

```bash
mysql -u root -p < sql/init.sql
# 或登录后
source /path/to/sql/init.sql;
```
