# 天气预报app
19软微移动端课程个人项目
## 开发文档
### 技术路线
* 数据库(litepal数据库)

    litepal数据库，分别创建province、city和county三个表存放于本机

* 网络请求(OkHttp3)

    使用封装的OkHttp3发送http请求获得全国各个城市的信息

* 数据解析(JSON&GSON)

    1. 获取的城市信息比较简单，直接用android studio自带的json解析数据
    
    2. 获取的天气信息用GSON库解析

* api

    - 和风天气api
    - guolin api(获取全国各地省市县信息)

### 功能模块
* 选择城市

    1. 分省市县三个级别选择城市，初次选择发送http请求并用**json**解析数据，存放到数据库中，以后直接去数据库中查找对应信息，数据库中没有才会发送http请求

    2. 滑动菜单     
    使用v4的DrawerLayout布局实现侧滑选择城市功能

* 显示和更新天气

    1. 天气获取     
    发送http请求，用**gson**解析数据，并将其存入SharedPreference缓存

    2. UI 主要有5个部分————title,now,forecast,aqi,suggestion    
    分开写成5个不同的布局文件，最后include到主布局文件中

    3. 下拉刷新     
    在天气布局的ScrollView外嵌套一层SwipeRefreshLayout

    4. 后台自动更新
    添加一个Service，每半个小时请求天气数据和新的背景图片，然后在weatherActivity中添加intent激活该服务

* 背景图片自动刷新

    获取必应每日一图，将其存入SharedPreference缓存中，然后用一个ImageView控件将其加载为背景

### 课程总结
