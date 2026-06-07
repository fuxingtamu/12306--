# ADR-003：引入Redis缓存层

**编号**：ADR-003
**标题**：引入Redis缓存
**状态**：已接受
**日期**：2026-05-14
**决策者**：架构团队

---

## 背景

车次查询是高频操作，直接查询数据库性能差，需要引入缓存层提升查询性能。

## 决策

采用**Redis**作为缓存层：

| 缓存数据 | TTL | 说明 |
|----------|-----|------|
| 车次余票数据 | 5分钟 | 高频变化，需较短TTL |
| 用户认证信息 | 2小时 | 与JWT Token同步 |
| 座位锁定记录 | 10分钟 | 购票锁定 |
| 车站基础数据 | 1天 | 几乎不变 |

## 缓存策略

```java
// 缓存读取
public TrainInventory getInventory(Long trainId, Date date) {
    String key = "inventory:" + trainId + ":" + date;
    TrainInventory cached = redis.get(key);
    if (cached != null) {
        return cached;
    }
    TrainInventory inventory = db.query(trainId, date);
    redis.setex(key, 300, inventory); // 5分钟
    return inventory;
}
```

## 结果

### 正面
- 大幅提升查询性能
- 减轻数据库压力
- 支持分布式Session
- 座位锁定功能

### 负面
- 缓存一致性问题
- 运维复杂度增加
- Redis成为单点（需集群）

## 相关决策
- ADR-002：JWT认证方案（使用Redis存储Token BlackList）
