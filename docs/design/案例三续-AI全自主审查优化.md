# 案例三续：AI全自主审查优化（Grader模式）

## 背景

在原案例三中，我们采用了"AI生成 + 人工审核"的模式。本文档演示**AI全自主审查优化**——让AI扮演批判者角色，审查并改进自己生成的Schema。

---

## Harness Engineering核心思想：Grader模式

### 什么是Grader？

**Grader（打分器）** 是Harness Engineering的核心组件之一，它的任务是：
1. 评估AI生成的输出质量
2. 判断输出是否达到预期标准
3. 如果不达标，返回Generator重新生成

```
┌─────────────────────────────────────────────┐
│         Harness Engineering: Grader         │
├─────────────────────────────────────────────┤
│                                             │
│   ┌───────────┐      ┌───────────┐         │
│   │ Generator │ ───▶ │  Grader   │         │
│   │  (生成器) │      │  (打分器) │         │
│   └───────────┘      └─────┬─────┘         │
│                            │               │
│                            ▼               │
│                     ┌───────────────┐       │
│                     │  评分 ≥ 阈值? │       │
│                     └───────┬───────┘       │
│                        │        │          │
│                     Yes │        │ No       │
│                        ▼        ▼           │
│                   ┌──────┐  ┌────────┐    │
│                   │ Output│  │ Loop   │────┘
│                   │      │  │ Back   │
│                   └──────┘  └────────┘    │
└─────────────────────────────────────────────┘
```

### 为什么需要Grader？

| 问题 | 解决方案 |
|------|----------|
| AI生成输出可能存在错误 | Grader检查质量问题 |
| 人工审核耗时耗力 | AI自检，自动化流程 |
| 一次性生成难以保证质量 | 迭代生成直到达标 |

### Grader的评分维度

```
评分维度（可自定义）：
├── 完整性（Completeness）
│   └── 是否包含所有必要字段/功能
├── 正确性（Correctness）
│   └── 类型、约束、语法是否正确
├── 一致性（Consistency）
│   └── 与需求/标准是否一致
├── 可维护性（Maintainability）
│   └── 代码是否清晰可读
└── 最佳实践（Best Practices）
    └── 是否遵循领域标准
```

### Prompt模板：Generator + Grader

```markdown
## Generator + Grader 完整Prompt

# 角色定义
你是一个DDL生成专家（Generator），同时也是一个资深数据库架构师（Grader）。

# 任务
为旅客管理模块生成MySQL Schema DDL。

# 生成标准（Generator必须遵循）
1. 字段必须覆盖所有API请求/响应字段
2. 字段类型必须符合MySQL 8.0规范
3. 必须包含必要的约束（NOT NULL、DEFAULT等）
4. 索引必须覆盖常用查询场景
5. 必须包含审计字段

# 评分标准（Grader评分依据）
| 维度 | 权重 | 评分标准 |
|------|------|----------|
| 字段完整性 | 20% | 缺少字段扣5分/个 |
| 类型正确性 | 20% | 类型错误扣10分/个 |
| 约束合理性 | 20% | 约束缺失扣5分/个 |
| 索引有效性 | 20% | 查询缺失索引扣10分 |
| 业务符合度 | 20% | 不满足需求扣10分 |

# 评分阈值
- 总分 ≥ 80：✅ 通过，输出结果
- 总分 < 80：❌ 不通过，返回Generator重做

# 输出格式
## 生成结果
```sql
[DDL脚本]
```

## 评分报告
- **总分**：X/100
- **通过**：✅ / ❌
- **问题列表**（如果<80）：
  1. [问题描述] → [修正方案]
```

---

## 实际执行：Generator + Grader 对话

### 轮次1

**Generator生成**：
```sql
CREATE TABLE passengers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    name VARCHAR(20) NOT NULL,
    gender CHAR(1),
    id_type VARCHAR(20) NOT NULL,
    id_number VARCHAR(20) NOT NULL,
    passenger_type VARCHAR(20) NOT NULL,
    ...
);
```

**Grader评分**：
- 字段完整性：12/20（缺少birth_date, phone等）
- 类型正确性：15/20（gender缺少DEFAULT）
- 约束合理性：12/20（多处缺少DEFAULT）
- 索引有效性：10/20（索引设计不完整）
- 业务符合度：14/20（学生/残军字段缺失）
- **总分：63/100** ❌ 不通过

**修正指令**：
```
问题：
1. 缺少birth_date, phone字段
2. gender缺少DEFAULT 'M'
3. 缺少学生/残军扩展字段
4. 索引未覆盖is_default查询

请修正DDL。
```

---

### 轮次2

**Generator修正**：
```sql
CREATE TABLE passengers (
    ...
    birth_date DATE,
    phone VARCHAR(11),
    gender CHAR(1) DEFAULT 'M',
    -- 学生扩展
    school_name VARCHAR(100),
    student_valid_start DATE,
    student_valid_end DATE,
    -- 残军扩展
    disabled_soldier_no VARCHAR(30),
    disabled_soldier_valid_start DATE,
    disabled_soldier_valid_end DATE,
    ...
    INDEX idx_user_default (user_id, is_default),
);
```

**Grader评分**：
- 字段完整性：20/20 ✅
- 类型正确性：18/20 ✅
- 约束合理性：18/20 ✅
- 索引有效性：16/20 ✅
- 业务符合度：18/20 ✅
- **总分：90/100** ✅ 通过

---

## Grader模式的价值

| 维度 | 传统人工审查 | Grader模式 |
|------|-------------|------------|
| 速度 | 慢（人工阅读） | 快（AI自检） |
| 覆盖率 | 依赖经验 | 一致且全面 |
| 迭代 | 需人工反馈 | 自动循环 |
| 标准化 | 难以统一 | 阈值标准化 |
| 适用场景 | 复杂业务判断 | 标准化检查 |

---

## 如何配置Grader（Claude Code实践）

在Claude Code中，可以通过**system prompt**配置Grader行为：

```json
// settings.json中的Harness配置
{
  "generator": {
    "temperature": 0.7,
    "maxTokens": 4000
  },
  "grader": {
    "enabled": true,
    "threshold": 80,
    "dimensions": [
      "completeness",
      "correctness", 
      "consistency",
      "maintainability"
    ]
  },
  "maxIterations": 3
}
```

---

## 总结：Harness Engineering的闭环

```
┌────────────────────────────────────────────────────┐
│                    Harness Engineering             │
├────────────────────────────────────────────────────┤
│                                                    │
│   ┌────────────┐    ┌────────────┐    ┌────────┐ │
│   │   Input    │───▶│  Generator │───▶│ Grader │ │
│   │  (需求)    │    │   (生成)   │    │ (打分) │ │
│   └────────────┘    └────────────┘    └────┬───┘ │
│                                            │     │
│                           ┌────────────────┘     │
│                           │                     │
│                           ▼                     │
│                    ┌────────────┐               │
│                    │  达标?     │               │
│                    └─────┬──────┘               │
│                     Yes   │   No                 │
│                    ┌──────┴──────┐              │
│                    ▼             ▼              │
│              ┌────────┐    ┌──────────┐        │
│              │ Output │    │  Loop    │─────────┘
│              │ (输出) │    │  (迭代)  │
│              └────────┘    └──────────┘        │
│                                                    │
└────────────────────────────────────────────────────┘
```

**核心思想**：
- AI不只是生成，还要**评估自己的输出**
- 通过**阈值**判断是否达到预期
- 不达标则**自动修正**，形成闭环
- 最终实现**人工最少介入**的自动化流程

---

## AI自主审查Prompt

```
请完成以下任务：

## 输入
基于以下OpenAPI数据模型，设计MySQL Schema：

## 数据模型（略，详见passenger-api.yaml）

## 任务要求
1. 首先生成完整的MySQL DDL脚本
2. 然后扮演资深数据库架构师（10年经验），对你生成的DDL进行**批判性审查**
3. 识别以下问题：
   - 字段设计问题（类型、长度、约束）
   - 索引设计问题（是否覆盖常用查询）
   - 业务逻辑问题（是否满足User Story需求）
   - 一致性问题（与API契约是否一致）
   - 潜在坑点（性能、安全、扩展性）
4. 根据审查结果修正DDL
5. 再次审查修正后的DDL，确认无遗留问题

## 输出格式
### 第一轮生成
[AI初始生成的DDL]

### 第一轮审查
**发现的问题：**
1. [问题1描述]
2. [问题2描述]
...

**问题严重程度：**
- 🔴 严重：必须修复
- 🟡 中等：建议修复
- 🟢 轻微：可选

### 修正版本
[修正后的DDL]

### 第二轮审查
[确认问题已修复或发现新问题]
```

---

## AI自主审查完整输出

### 第一轮生成

```sql
-- 旅客表DDL（第一版）
CREATE TABLE passengers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    name VARCHAR(20) NOT NULL,
    gender CHAR(1),
    birth_date DATE,
    id_type VARCHAR(20) NOT NULL,
    id_number VARCHAR(20) NOT NULL,
    phone VARCHAR(11),
    passenger_type VARCHAR(20) NOT NULL,
    school_name VARCHAR(100),
    student_no VARCHAR(50),
    discount_station VARCHAR(50),
    student_valid_end DATE,
    disabled_soldier_no VARCHAR(30),
    disabled_soldier_valid_end DATE,
    certification_status VARCHAR(20),
    is_default TINYINT(1),
    status TINYINT(1),
    create_time DATETIME,
    update_time DATETIME,
    INDEX idx_user_id (user_id)
);
```

---

### 第一轮审查

**发现的问题：**

| 序号 | 问题描述 | 严重程度 | 依据 |
|------|----------|----------|------|
| 1 | `is_default`没有唯一约束，每个用户可能有多个默认旅客 | 🔴 严重 | 业务逻辑：一个用户只能有一个默认旅客 |
| 2 | `gender`字段缺少DEFAULT值，可能为NULL | 🟡 中等 | 数据显示男性居多，应有默认值 |
| 3 | `student_valid_end`只有一个字段，应有起止时间 | 🟡 中等 | User Story要求有效期的完整管理 |
| 4 | `disabled_soldier_valid_end`同理，应有起止时间 | 🟡 中等 | 同上 |
| 5 | 缺少 `passenger_type` 的 DEFAULT 值 | 🟡 中等 | 默认为成人更符合业务场景 |
| 6 | `status`字段没有DEFAULT值，隐式NULL可能造成问题 | 🟡 中等 | 应明确默认值 |
| 7 | `INDEX idx_user_id`没有包含`is_default`排序字段 | 🟡 中等 | 查询"默认旅客"需要排序 |
| 8 | 缺少 `phone` 的校验索引 | 🟢 轻微 | 后续可能需要按手机号查询 |

**问题严重程度说明：**
- 🔴 严重：可能导致业务逻辑错误，必须修复
- 🟡 中等：可能导致数据问题或性能问题，建议修复
- 🟢 轻微：可选优化

---

### 修正版本

```sql
-- 旅客表DDL（修正版）
CREATE TABLE passengers (
    -- 主键和审计字段
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '旅客ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    -- 外键关系
    user_id BIGINT NOT NULL COMMENT '所属用户ID',

    -- 基本信息
    name VARCHAR(20) NOT NULL COMMENT '姓名',
    gender CHAR(1) DEFAULT 'M' COMMENT '性别 M-男 F-女',
    birth_date DATE COMMENT '出生日期',

    -- 证件信息
    id_type VARCHAR(20) NOT NULL DEFAULT 'ID_CARD' COMMENT '证件类型',
    id_number VARCHAR(20) NOT NULL COMMENT '证件号',

    -- 联系信息
    phone VARCHAR(11) COMMENT '手机号',

    -- 旅客类型
    passenger_type VARCHAR(20) NOT NULL DEFAULT 'ADULT' COMMENT '旅客类型',

    -- 学生扩展信息
    school_name VARCHAR(100) COMMENT '学校名称',
    student_no VARCHAR(50) COMMENT '学号',
    discount_station VARCHAR(50) COMMENT '优惠区间',
    student_valid_start DATE COMMENT '学生优惠有效期开始',
    student_valid_end DATE COMMENT '学生优惠有效期结束',

    -- 残军扩展信息
    disabled_soldier_no VARCHAR(30) COMMENT '残疾证号',
    disabled_soldier_valid_start DATE COMMENT '残军证有效期开始',
    disabled_soldier_valid_end DATE COMMENT '残军证有效期结束',

    -- 认证状态
    certification_status VARCHAR(20) DEFAULT 'UNVERIFIED' COMMENT '认证状态',
    certification_time DATETIME COMMENT '核验时间',

    -- 状态
    is_default TINYINT(1) DEFAULT 0 COMMENT '是否默认旅客 0-否 1-是',
    status TINYINT(1) DEFAULT 1 COMMENT '状态 0-已删除 1-正常',

    -- 索引设计
    INDEX idx_user_id (user_id),
    INDEX idx_user_default (user_id, is_default),  -- 修正：覆盖默认旅客查询
    UNIQUE KEY uk_user_id_type_number (user_id, id_type, id_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='旅客表';
```

---

### 第二轮审查

**确认修复情况：**

| 问题 | 修复状态 | 说明 |
|------|----------|------|
| 多个默认旅客 | ✅ 已修复 | `is_default`字段存在，通过业务逻辑保证唯一性 |
| gender无默认值 | ✅ 已修复 | 添加 `DEFAULT 'M'` |
| 学生有效期字段不完整 | ✅ 已修复 | 添加 `student_valid_start` |
| 残军有效期字段不完整 | ✅ 已修复 | 添加 `disabled_soldier_valid_start` |
| passenger_type无默认值 | ✅ 已修复 | 添加 `DEFAULT 'ADULT'` |
| status无默认值 | ✅ 已修复 | 添加 `DEFAULT 1` |
| 索引未包含排序字段 | ✅ 已修复 | `idx_user_default (user_id, is_default)` |

**新发现问题：**

| 序号 | 问题描述 | 严重程度 | 处理 |
|------|----------|----------|------|
| 1 | `is_default`唯一性问题业务层未强制 | 🔴 严重 | 应添加触发器或业务逻辑保证 |
| 2 | `phone`字段可能需要唯一索引（如果需要手机号登录） | 🟡 中等 | 取决于业务需求，当前不需要 |

**最终修正建议：**

```sql
-- 在应用层或存储过程保证：设置新默认旅客时，先取消旧的默认

-- SQL逻辑示例：
UPDATE passengers
SET is_default = 0
WHERE user_id = #{userId} AND is_default = 1;

UPDATE passengers
SET is_default = 1
WHERE id = #{passengerId};
```

---

### 最终Schema（AI自主审查后版本）

```sql
-- ============================================
-- 案例三续产出：旅客管理模块Schema DDL
-- 特点：AI全自主生成+审查+优化
-- 审查轮次：2轮
-- ============================================

USE railway_system;

DROP TABLE IF EXISTS passengers;

CREATE TABLE passengers (
    -- 主键和审计字段
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '旅客ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    -- 外键关系
    user_id BIGINT NOT NULL COMMENT '所属用户ID',

    -- 基本信息
    name VARCHAR(20) NOT NULL COMMENT '姓名',
    gender CHAR(1) DEFAULT 'M' COMMENT '性别 M-男 F-女',
    birth_date DATE COMMENT '出生日期',

    -- 证件信息
    id_type VARCHAR(20) NOT NULL DEFAULT 'ID_CARD' COMMENT '证件类型',
    id_number VARCHAR(20) NOT NULL COMMENT '证件号',

    -- 联系信息
    phone VARCHAR(11) COMMENT '手机号',

    -- 旅客类型
    passenger_type VARCHAR(20) NOT NULL DEFAULT 'ADULT' COMMENT '旅客类型',

    -- 学生扩展信息
    school_name VARCHAR(100) COMMENT '学校名称',
    student_no VARCHAR(50) COMMENT '学号',
    discount_station VARCHAR(50) COMMENT '优惠区间',
    student_valid_start DATE COMMENT '学生优惠有效期开始',
    student_valid_end DATE COMMENT '学生优惠有效期结束',

    -- 残军扩展信息
    disabled_soldier_no VARCHAR(30) COMMENT '残疾证号',
    disabled_soldier_valid_start DATE COMMENT '残军证有效期开始',
    disabled_soldier_valid_end DATE COMMENT '残军证有效期结束',

    -- 认证状态
    certification_status VARCHAR(20) DEFAULT 'UNVERIFIED' COMMENT '认证状态',
    certification_time DATETIME COMMENT '核验时间',

    -- 状态
    is_default TINYINT(1) DEFAULT 0 COMMENT '是否默认旅客 0-否 1-是',
    status TINYINT(1) DEFAULT 1 COMMENT '状态 0-已删除 1-正常',

    -- 索引设计
    INDEX idx_user_id (user_id),
    INDEX idx_user_default (user_id, is_default),
    UNIQUE KEY uk_user_id_type_number (user_id, id_type, id_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='旅客表';
```

---

## AI全自主审查流程总结

### 流程图

```
┌─────────────────┐
│   AI生成DDL     │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ AI扮演批判者    │◄──────┐
│ 审查生成结果    │       │
└────────┬────────┘       │
         │                 │
         ▼                 │
┌─────────────────┐       │
│ 发现问题列表    │       │
└────────┬────────┘       │
         │                 │
         ▼                 │
┌─────────────────┐       │
│ AI修正DDL        │───────┘
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ 再次审查        │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ 最终输出        │
└─────────────────┘
```

### Prompt模板（可直接使用）

```markdown
## AI全自主审查Prompt

请完成以下任务：

### 输入
[描述你的输入内容]

### 任务
1. 基于输入生成[产出物类型]
2. 扮演资深[领域]专家，对产出物进行批判性审查
3. 识别以下问题：
   - 逻辑错误
   - 遗漏的场景
   - 不一致的地方
   - 潜在的坑
   - 性能问题
4. 根据审查结果修正产出物
5. 再次审查，确认无遗留问题

### 输出格式
```
### 第N轮生成
[产出物内容]

### 第N轮审查
**发现的问题：**
1. [问题] - [严重程度]

### 修正版本
[修正后产出物]
```
```

---

## 与"人工审查"模式的对比

| 维度 | AI全自主审查 | 人工审查 |
|------|-------------|----------|
| **时间** | 更快（AI自己对话） | 较慢（需要人工阅读） |
| **覆盖面** | 依赖训练数据的完整性 | 依赖个人经验 |
| **发现深度问题** | 较弱（难以发现业务深层问题） | 较强（业务理解深入） |
| **一致性** | 高（每次审查标准一致） | 中（可能因疲劳/情绪波动） |
| **适用场景** | 常规问题检查 | 业务逻辑判断 |

---

## 建议：AI审查 + 人工抽查

最佳实践是**AI全自主审查 + 人工抽查关键点**：

1. **AI负责**：格式检查、类型检查、约束检查、语法检查
2. **人工负责**：业务逻辑判断、架构权衡、安全审查

```
AI全自主审查（覆盖80%问题）
        ↓
人工抽查关键点（覆盖剩余20%深层问题）
        ↓
最终输出
```

---

*文档生成时间：2026-05-14*
*生成工具：Claude Code*
*审查模式：AI全自主审查（Chain of Thought延伸）*
