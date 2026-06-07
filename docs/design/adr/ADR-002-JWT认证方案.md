# ADR-002：JWT Token身份认证方案

**编号**：ADR-002
**标题**：JWT Token身份认证方案
**状态**：已接受
**日期**：2026-05-14
**决策者**：架构团队

---

## 背景

前后端分离架构下，传统的Session-Cookie认证不再适用，需要选择无状态认证方案。

## 决策

采用**JWT（JSON Web Token）**进行身份认证：
- 登录成功后服务端签发JWT Token
- Token包含用户ID、过期时间等信息
- 前端每次请求携带Token（Authorization Header）
- Token有效期：Access Token 2小时，Refresh Token 7天

## Token结构

```json
{
  "header": {
    "alg": "HS256",
    "typ": "JWT"
  },
  "payload": {
    "userId": 12345,
    "phone": "13800138000",
    "exp": 1715000000,
    "iat": 1714996400
  },
  "signature": "xxxxx"
}
```

## 结果

### 正面
- 无状态，水平扩展简单
- Token可跨域使用
- 性能好，无需查Session
- 支持移动端天然集成

### 负面
- Token一旦签发无法撤销（需BlackList）
- Token泄露风险（需HTTPS）
- Payload数据可见（仅签名加密）

## 安全措施

1. HTTPS传输
2. Token存放在HttpOnly Cookie或LocalStorage
3. 敏感操作需二次验证
4. Refresh Token轮换机制

## 相关决策
- ADR-001：前后端分离架构
