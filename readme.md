# EZLinker
---
轻量级物联网应用
---
## 当前进度
0I==>[1%]===========================================I100
## 前端地址
https://github.com/ssloth/ezlinker-frontend.git
## 其他
- COAP协议测试工具:https://github.com/wwhai/EZCoapTester.git
- Arduino SDK :https://github.com/wwhai/ezlinker_arduino_sdk.git
- ESP8266简单Demo:https://github.com/wwhai/ESP8266_Simple_cli.git
## 一些开发规范

### 包名规范
- controller:WEB控制器;
- model:数据模型;
- mapper:MyBatis映射;
- service:Service层;
- pojo:普通的Java类,一般起辅助作用;
- form:前端的Form表单接收;
- utils:模块私有工具代码;
- resource:模块私有资源文件,例如配置.

### 表名规范
- 表名用名词单数形式,比如User,Student;
- 中间关系表用 `relation`开头,后面跟关联的主表,从表,比如用户和博客的关系表:`relation_user_blog`,统一放进relation模块.