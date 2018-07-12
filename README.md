# miaosha
慕课网秒杀方案

## 注意点
- 一般来说，服务层返回给控制层的数据含义应该是明白的，譬如Bean类、或者自定义异常
- 全局异常处理
- config里面配置参数内容
    - Controller方法里面含有对应的参数时，进行一些特殊处理
- 分布式session(保存用户信息)
    - 在redis中保存用户的会话信息，如果是浏览器端则将生成的token作为cookie的值返回给web端，如果是app则直接将token返回给app(可作传输加密)，当用户token未失效时，自动获取用户信息
- 一般在service里面要引入其他的mapper则引入其他需要引入其他service
- 并发在多少时qps为多少这种说法更准确
    - qps与tps的区别：tps每秒事务处理量
- GET和POST的区别
    - GET：幂等
    - POST：用于对服务器数据的修改，明显不是幂等


## 优化
1. 页面级高并发秒杀解决方案（Redis缓存+静态化分离）
    - 缓存
        - 页面级缓存
        - URL级缓存
        - 对象级别缓存
    - 页面静态化（特点：利用浏览器的缓存）
        - 前后端分离
        - 后端负责接口，前端负责对数据进行处理
        - 页面静态化只是其中一种手段
        - 解决卖超
            - 修改update，当stock_count <= 0 时不更新数据库
            - 通过对用户的orderInfo里面的(userId,goodsId)设置唯一索引,预防用户同时刷新两个秒杀请求进入方法
    - 静态资源优化（Webpack、Tengine）
        - JS/CSS压缩，减少流量
        - 多个JS/CSS合并，减少连接数
        - CDN
    - 秒杀接口优化
        1. 系统初始化，把商品库存加载到Redis
        2. 收到请求，Redis预减库存，库存不足，直接返回，否则进入3
        3. 请求入队，立即返回排队中



