# 高并发秒杀系统
一个简单的电商秒杀系统。业务包含：登陆，商品列表，商品详情页，秒杀。
技术要点：
1. 基于Redis的分布式Session、页面静态化、（页面、URL、对象）缓存、预加载
2. 基于RabbitMQ的异步下单
3. 使用Jmeter测试：多用户并发下单场景
4. 测试账户:
    - 账号:10000000000~10000004999
    - 密码:123456
    
并发重要参数：参考https://www.cnblogs.com/data2value/p/6220859.html


## 项目总结
### 项目框架
- Spring Boot搭建
- Result结果封装
- 继承Jedis+Redis安装+通用缓存Key的封装

- JSR303参数校验+全局异常处理器
  - 前端：有效性的校验
  - 后端：防恶意用户
- 拦截器

### 业务

- 明文密码两次MD5加密

- 数据库设计
  - 有冗余，不严格遵守三大范式
  - 方便业务查询

### 优化

- 分布式Session：Redis保存用户token
- 页面静态化，前后端分离
- 页面缓存+URL缓存+对象缓存

### 秒杀优化

- 预加载商品库存至Redis
- 内存标记售完商品

### 接口优化

- 异步下单（RabbitMQ消息队列）

- Nginx水平扩展



### 并发测试

- JMeter模拟多用户，压测


### 安全优化

- 秒杀接口地址隐藏
- 数学公式验证码
- 接口防刷（拦截器）

## 常用命令
mybatis自动生成: mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate



