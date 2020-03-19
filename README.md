## 码匠社区

## 资料
[spring各种组件](https://spring.io/guides/gs/serving-web-content/)

[bootstrap](https://www.bootcss.com)

[模仿的网站](https://elasticsearch.cn)
## 工具
[Github](https://github.com/)  
[使用github授权登陆开发流程](https://developer.github.com/apps/building-oauth-apps/authorizing-oauth-apps/)
[mybatis逆向工程](http://mybatis.org/generator/running/runningWithMaven.html)
使用直接生成
mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate

使用mvn flyway:migrate可直接执行sql脚本，并控制版本

## 开发遇到的问题
在页面引入bootstrap的js之前一定要引入jq否则有些功能不会生效，引入之后一定要在maven中重新导入 否则引入也不会生效  

session和cookie就相当于账户和银行卡，一个在银行一个在用户，服务端和用户端。当服务端写入session中后，用户端访问会时候，服务端会自动（不需要自己编码）给用户也就是浏览器发送一个cookie，名字是jsessionid,值是随机的数，当下次浏览器再访问服务器，就可以携带这个数直接不用登陆了

在使用mvn flyway:migrate的时候 该插件要求jdk必须是8以上的 所以需要切换到11，修改～/..bash_profile 中加入环境变量，我使用的是idea自带的mvn和jdk 将他们都加入到环境变量中就可以在外部命令行使用了

h2数据库是区分大小写的 url 和 URL 完全不一样，修改的时候需要重新创建一个Flyway文件,并执行mvn flyway:migrate

flyway使用注意 1。所有的操作都记录在history表中，该表用于版本控制记录每个操作，成功执行的操作第二次就不会执行 2。任何修改都必须追加一个新文件，不能修改原来的文件，因为有校验码，不同的话就不能生效

引入css资源的时候/css表示绝对路径是相对于整个站点的而css表示相对路径是相对于**请求**的，比如localhost:8080/publish那就会在站点根目录下找，但是localhost:8080/publish/question这是二级目录他就会在publish文件下查找，这才是它的同级目录

thymeleaf中动态拼接href:@{}代表内容为一个连接所有@{'/profile/'+${section}(page=${page})}就代表/profile/question?page=3这样（）里面是参数

拦截器必须要实现hHandlerInterceptor接口 然后注入到springboot的自动配置类中 也就是自定义的配置类实现WebMvcConfigurer这个接口
[spring文档](https://docs.spring.io/spring/docs/5.0.3.RELEASE/spring-framework-reference/web.html#mvc-config-interceptors)


在做登出的时候需要删除cookie和session cookie中携带token，在登陆的时候会到数据库中查找 然后登陆，所以要删除，seesion中存有用户信息，所以也要删除
删除cookie的方法是 重新创建一个同名的cookie 然后传入null，并设置最大存活时间为0

## 快捷键
alt + 拖移 = 选中一列快速编辑  
fn + command + f9 = 编译当前文件（配合热部署）  
fn + command + shift + f12 = 全屏当前画面  
command + alt + v = 在方法参数外面快速new对象
command + e = 调出最近编辑的文档 回车快速切换
shift + fn + f6 = 修改所有相同的变量名
遍历数组可以用数组名.for 自动生成循环体  
alt + command + l = 自动格式化代码  
command + shift + 方向 = 上下移动  
alt + fn + f7 = 找到当前方法被哪些方法引用的地方  
control + alt + o =删除无用的导入的包  
command +fn + 12 = 看源码的时候可以快速查看所有方法  
command + alt + 左右 = 查看源码时候可以快速回到之前或者之后看的地方  
command + alt + b = 可以看到选中类到所有子类

## MBG使用  
1. 引入依赖
2. 配置generatorConfig.xml文件 
3. 执行mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate 生成 模型（pojo）mapper(接口) sql map（真正执行sql的文件） example(拼装条件)
4. 使用的时候要告诉springboot需要扫描的包和上哪找mapper和sql map文件所以主配置类上加上@MapperScan(basePackages = "life.majiang.community.mapper") 主配置文件中加入mybatis.type-aliases-package和mybatis.mapper-locations

## 异常处理
异常有两种：一种是业务逻辑中比如查询的id不存在，一种是浏览器请求服务器的资源不存在如localhost:8080/123  
当产生了异常时，浏览器可以看到跳转到了一个/error页面，这个跳转是boot完成的，但是页面需要我们自己定义而且叫error，自定义异常处理器的作用就是将异常信息传到页面展示出来  
异常处理器的作用就是处理指定异常，而异常是我们手动抛出来的，可以自己定义  
而其他未处理的异常（请求资源没有或者服务器异常500等）都需要basicerrorcontroller处理，我们也可以自定义，也是将信息传到页面的思路  
总结：产生任何异常后boot都会帮我们产生一个/error请求，默认情况下处理这个请求的是basicerrorocntroller,它处理的方式是调用视图解析器然后就可以跳转到指定页面了/error/404这种页面，而我们可以自定义一个controller替换调basicerrorcontroller