### :european_castle:线上地址：[晴川社区](123.57.60.151:8080)  
### :bridge_at_night:技术栈
1. 前端框架：Bootstrap，Jquery，Ajax，Layer，Thymeleaf
2. 后端框架：SpringBoot，Mybatis
3. 数据库：MySql
4. 文件上传：OSS对象存储
5. 富文本编辑器：Editormd
6. OAuth2授权登入（QQ登录）
7. Redis分布式内存数据库，实现热点数据缓存。
8. Swagger接口文档可视化
9. Druid数据监控


### :rainbow:目录结构

src目录如下：

~~~
├─src
    └─main
        ├─java
        │  └─com
        │      └─qingchuan
        │          └─coderman
        │              ├─admin        #后台代码
        │              ├─config       #配置文件
        │              └─web 	      #前端代码
        └─resources    	              #详细配置
            ├─mapper	              #sql映射文件
            ├─static	              #静态资源
            └─templates               #html页面
~~~~
### :suspension_railway:快速运行
必备工具：  Maven, Redis，Git，Mysql5.7

1. 将项目克隆到本地
2. 建好数据库（字符编码设置为utf8mb4）
3. 修改源码
- 修改`application.properties`和`mbg.xml`中的数据库用户名，密码
- 如果想用阿里云的oss存储，需要去阿里云申请，得到`id`和`Secret`，在`application.properties`中修改。
4. 启动并访问项目localhost:8080
5. swagger访问地址：http://localhost:8080/swagger-ui.html
6. Druid数据监控，在`config`的`DruidConfig`中修改登录名和密码，访问网址：http://localhost:8080/druid/login.html

### :construction:主要功能
1. 注册，登录，修改个人信息
2. 发帖（匿名），搜索
4. 评论，收藏，点赞
5. 通知，关注
10. 话题讨论
13. 聊天室
14. 每日签到


## :railway_car:联系
QQ：2829025551

## :slot_machine:友情链接
[我的博客空间](https://daqwt.top)


