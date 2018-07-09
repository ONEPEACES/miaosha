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
