# SSM整合小结（超简易书籍管理系统）

## 1.项目结构

![image-20210918085448109](C:\Users\workfight\AppData\Roaming\Typora\typora-user-images\image-20210918085448109.png)

![image-20210918085518518](C:\Users\workfight\AppData\Roaming\Typora\typora-user-images\image-20210918085518518.png)

## 2.项目依赖配置文件

pom.xml:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.study</groupId>
    <artifactId>ssmbuild</artifactId>
    <version>1.0-SNAPSHOT</version>

    <dependencies>
        <!--依赖: junit,servlet,mysqlDriver,jsp等等-->
        <!-- https://mvnrepository.com/artifact/junit/junit -->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
            <scope>test</scope>
        </dependency>

        <!-- https://mvnrepository.com/artifact/mysql/mysql-connector-java -->
        <!--数据库驱动-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.26</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/com.mchange/c3p0 -->
        <!--数据库连接池-->
        <dependency>
            <groupId>com.mchange</groupId>
            <artifactId>c3p0</artifactId>
            <version>0.9.5.2</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/javax.servlet/javax.servlet-api -->
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <version>4.0.1</version>
            <scope>provided</scope>
        </dependency>

        <!-- https://mvnrepository.com/artifact/javax.servlet.jsp/javax.servlet.jsp-api -->
        <dependency>
            <groupId>javax.servlet.jsp</groupId>
            <artifactId>javax.servlet.jsp-api</artifactId>
            <version>2.3.3</version>
            <scope>provided</scope>
        </dependency>

        <!-- https://mvnrepository.com/artifact/javax.servlet.jsp/jsp-api -->
        <dependency>
            <groupId>javax.servlet.jsp</groupId>
            <artifactId>jsp-api</artifactId>
            <version>2.2</version>
            <scope>provided</scope>
        </dependency>


        <!-- https://mvnrepository.com/artifact/javax.servlet/jstl -->
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>jstl</artifactId>
            <version>1.2</version>
        </dependency>

        <!--Mybatis-->
        <!-- https://mvnrepository.com/artifact/org.mybatis/mybatis -->
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.5.7</version>
        </dependency>

        <!--Mybatis-Spring-->
        <!-- https://mvnrepository.com/artifact/org.mybatis/mybatis-spring -->
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis-spring</artifactId>
            <version>2.0.6</version>
        </dependency>


        <!--Spring-->
        <!-- https://mvnrepository.com/artifact/org.springframework/spring-webmvc -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-webmvc</artifactId>
            <version>5.3.9</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/org.springframework/spring-jdbc -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-jdbc</artifactId>
            <version>5.3.9</version>
        </dependency>

        <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjweaver</artifactId>
            <version>1.8.13</version>
        </dependency>

        <!--lombok-->
        <!-- https://mvnrepository.com/artifact/org.projectlombok/lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.20</version>
            <scope>provided</scope>
        </dependency>


    </dependencies>

    <!--静态资源导出-->
    <!--在build中配置resource，来防止我们资源导出失败的问题-->
    <build>
        <resources>
            <resource>
                <directory>src/main/resources</directory>
                <includes>
                    <include>**/*.properties</include>
                    <include>**/*.xml</include>
                </includes>
                <filtering>true</filtering>
            </resource>

            <resource>
                <directory>src/main/java</directory>
                <includes>
                    <include>**/*.properties</include>
                    <include>**/*.xml</include>
                </includes>
                <filtering>true</filtering>
            </resource>
        </resources>
    </build>
</project>
```

## 3.ssm的整合配置文件

### 	3.1	mybatis层

#### 			3.1.1	数据库属性文件

属性文件需要添加数据库的相关驱动，数据库url，用户和密码信息，数据库属性文件配置在spring-dao.xml整合配置文件中。

```properties
jdbc.driver=com.mysql.cj.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/ssmbuild?useSSL=true&useUnicode=true&characterEncoding=utf8
jdbc.username=root
jdbc.password=root
```

<font color='red'>需要注意的是，上述驱动信息，是基于本项目所导入的mysql-connector为8.0+版本，虽然数据库为mysql 5.7，但是根据项目运行的报错信息，需要替换成com.mysql.cj.jdbc.Driver</font>

#### 			3.1.2	mybatis配置文件

这里的mybatis-config.xml文件，主要配置了标准日志，类型别名，以及注册项目中的*Mapper.java文件信息。

<font color='red'>注意：此配置文件对不同的配置信息有顺序问题，如果记不清顺序，可以根据标签的爆红信息来更改此文件中的各项配置顺序。另外，项目所涉及到的所有*Mapper.java文件都需要在此文件中进行注册绑定。</font>

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>


    
    <settings>
        <setting name="logImpl" value="STDOUT_LOGGING"/>
    </settings>

    <typeAliases>
        <package name="com.study.pojo"/>
    </typeAliases>

    <mappers>
        <mapper class="com.study.mapper.BookMapper"/>
    </mappers>
</configuration>
```

### 	3.2	SpringMVC层

SpringMVC层的配置信息，主要设置在web.xml文件之中，主要包括前端控制器，乱码过滤，设置会话对象时间间隔。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">

    <!--DispatcherServlet-->
    <servlet>
        <servlet-name>springmvc</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:applicationContext.xml</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>
    <servlet-mapping>
        <servlet-name>springmvc</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>


    <!--乱码过滤-->
    <filter>
        <filter-name>encodingFilter</filter-name>
        <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
        <init-param>
            <param-name>encoding</param-name>
            <param-value>utf-8</param-value>
        </init-param>
    </filter>
    <filter-mapping>
        <filter-name>encodingFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>

    <!--Session-->
    <session-config>
        <session-timeout>15</session-timeout>
    </session-config>
</web-app>
```

spring-mvc整合配置文件：在此文件中主要配置了注解驱动，对controller下的包文件进行扫描，以及配置视图解析器和静态资源过滤信息。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xmlns:context="http://www.springframework.org/schema/context"

       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/mvc
       https://www.springframework.org/schema/mvc/spring-mvc.xsd
       http://www.springframework.org/schema/context
       https://www.springframework.org/schema/context/spring-context.xsd">

    <!--1.注解驱动-->
    <mvc:annotation-driven/>
    <!--2.静态资源过滤-->
    <mvc:default-servlet-handler/>
    <!--3.扫描包:controller-->
    <context:component-scan base-package="com.study.controller"/>
    <!--4.视图解析器-->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <property name="prefix" value="/WEB-INF/jsp/"/>
        <property name="suffix" value=".jsp"/>
    </bean>

</beans>
```



### 	3.3	Spring层

Spring层除了包含applicationContext.xml文件之外，还需要配置spring-dao,spring-service整合配置文件。

applicationContext.xml作为整合的总文件，导入了spring-dao，spring-service，spring-mvc整合文件。

![image-20210917113038341](C:\Users\workfight\AppData\Roaming\Typora\typora-user-images\image-20210917113038341.png)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd">

    <import resource="classpath:spring-dao.xml"/>
    <import resource="classpath:spring-service.xml"/>
    <import resource="classpath:spring-mvc.xml"/>
</beans>
```

spring-dao.xml，将Spring与dao层进行整合。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd">


    <!--整合-->

    <!--1.关联数据库配置文件-->

    <context:property-placeholder location="classpath:db.properties"/>

    <!--2.连接池
        dbcp:半自动化操作，不能自动选择
        c3p0:自动化操作(自动化的加载配置文件，并且可以自动设置到对象中!)
        druid;
        hikari;
    -->
    <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
        <property name="driverClass" value="${jdbc.driver}"/>
        <property name="jdbcUrl" value="${jdbc.url}"/>
        <property name="user" value="${jdbc.username}"/>
        <property name="password" value="${jdbc.password}"/>

        <!--c3p0连接池的私有属性-->
        <property name="maxPoolSize" value="30"/>
        <property name="minPoolSize" value="10"/>
        <!--关闭连接后不自动commit-->
        <property name="autoCommitOnClose" value="false"/>
        <!--获取连接超时时间-->
        <property name="checkoutTimeout" value="10000"/>
        <!--当获取连接失败后重试的次数-->
        <property name="acquireRetryAttempts" value="2"/>
    </bean>

    <!--3.sqlSessionFactory-->
    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="dataSource"/>
        <!--绑定Mybatis的配置文件-->
        <property name="configLocation" value="classpath:mybatis-config.xml"/>
    </bean>

    <!--配置dao接口扫描包，动态的实现了Dao接口可以注入到Spring容器中!-->
    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <!--注入sqlSessionFactory-->
        <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"/>
        <!--要扫描的dao包-->
        <property name="basePackage" value="com.study.mapper"/>
    </bean>
</beans>
```

spring-service：扫描service层下的包，将所有的业务类注入到Spring中，对事务进行配置。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       https://www.springframework.org/schema/context/spring-context.xsd
       http://www.springframework.org/schema/aop
       https://www.springframework.org/schema/aop/spring-aop.xsd
       http://www.springframework.org/schema/tx
        http://www.springframework.org/schema/tx/spring-tx.xsd">

    <!--1.扫描service下的包-->
    <context:component-scan base-package="com.study.service"/>

    <!--2.将所有的业务类注入到Spring，可以通过配置，或者注解实现-->
    <bean id="BookServiceImpl" class="com.study.service.BookServiceImpl">
        <property name="bookMapper" ref="bookMapper"/>
    </bean>

    <!--3.声明式事务配置-->
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <!--注入数据源-->
        <property name="dataSource" ref="dataSource"/>
    </bean>

    <!--4.aop事务支持-->

    <!--配置事务通知-->
    <tx:advice id="txAdivice" transaction-manager="transactionManager">
        <tx:attributes>
            <tx:method name="*" propagation="REQUIRED"/>
        </tx:attributes>
    </tx:advice>
    <aop:config>
        <aop:pointcut id="txPointcut" expression="execution(* com.study.mapper.*.*(..))"/>
        <aop:advisor advice-ref="txAdivice" pointcut-ref="txPointcut"/>
    </aop:config>
</beans>
```

<font color='red'>需要特别注意的是，在类似于如下代码中，最好由后续代码的不断写入来让其自动添加，如果自动添加有误，要保证其和下属标签内容的一致性，否则会出现无法....http://www.springframework.org...的错误</font>

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       https://www.springframework.org/schema/context/spring-context.xsd
       http://www.springframework.org/schema/aop
       https://www.springframework.org/schema/aop/spring-aop.xsd
       http://www.springframework.org/schema/tx
        http://www.springframework.org/schema/tx/spring-tx.xsd">
```

## 4.各功能层介绍

BookController层：调用serivce层，并且接收来自前端的请求，将结果返回给前端。

```java
/**
 * @author zengyujie
 * @version 1.0
 * @Description: TODO
 * @Date 2021/9/16 15:29
 */
@Controller
@RequestMapping("/book")
public class BookController {
    //controller调service层
    @Autowired
    @Qualifier("BookServiceImpl")
    private BookService bookService;

    //查询全部的书籍，并且返回到一个书籍展示页面
    @RequestMapping("/allBook")
    public String list(Model model){
        List<Books> list = bookService.queryAllBook();
        model.addAttribute("list",list);
        return "allBook";
    }

    //跳转到增加书籍页面
    @RequestMapping("/toAddBook")
    public String toAddPaper(){
        return "addBook";
    }

    //添加书籍的请求
    @RequestMapping("/addBook")
    public String addBook(Books books){
        System.out.println("addBooks=>"+books);
        bookService.addBook(books);
        return "redirect:/book/allBook";
    }

    //跳转到修改书籍的页面
    @RequestMapping("/toUpdate")
    public String toUpdatePaper(int id,Model model){
        Books books = bookService.queryBookById(id);
        model.addAttribute("QBook",books);
        return "updateBook";
    }

    //修改书籍
    @RequestMapping("/updateBook")
    public String updateBook(Books books){
        System.out.println("updateBook=>"+books);
        bookService.updateBook(books);
        return "redirect:/book/allBook";
    }

    //删除书籍
    @RequestMapping("/deleteBook/{bookId}")
    public String deleteBook(@PathVariable ("bookId")int id){
        bookService.deleteBookById(id);
        return "redirect:/book/allBook";
    }

    //查询书籍
    @RequestMapping("/queryBook")
    public String queryBook(String queryBookName,Model model){
        Books books = bookService.queryBookByName(queryBookName);
        List<Books> list = new ArrayList<Books>();
        list.add(books);

        if (books==null){
            list=bookService.queryAllBook();
            model.addAttribute("error","查无此书");
        }

        //list返回到前端，并且进行遍历
        model.addAttribute("list",list);
        return "allBook";
    }

}

```

mapper层：定义接口方法，

```java
public interface BookMapper {

    //增加一本书
    int addBook(Books books);

    //删除一本书
    int deleteBookById(@Param("bookId") int id);

    //更新一本书
    int updateBook(Books books);

    //查询一本书
    Books queryBookById(@Param("bookId") int id);

    //查询全部的书
    List<Books> queryAllBook();

    //根据书名查书籍
    Books queryBookByName(@Param("bookName")String bookName);
}

```

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.study.mapper.BookMapper">
    <insert id="addBook" parameterType="com.study.pojo.Books">
        insert into ssmbuild.books(bookName,bookCounts,detail) values (#{bookName},#{bookCounts},#{detail})
    </insert>

    <delete id="deleteBookById" parameterType="int">
        delete from ssmbuild.books where bookID=#{bookId}
    </delete>

    <update id="updateBook" parameterType="com.study.pojo.Books">
        update ssmbuild.books
        set bookName=#{bookName},bookCounts=#{bookCounts},detail=#{detail}
        where bookID=#{bookID}
    </update>

    <select id="queryBookById" resultType="com.study.pojo.Books">
        select * from ssmbuild.books
        where bookID=#{bookId}
    </select>

    <select id="queryAllBook" resultType="com.study.pojo.Books">
        select * from ssmbuild.books
    </select>

    <select id="queryBookByName" resultType="com.study.pojo.Books">
        select * from ssmbuild.books where bookName=#{bookName}
    </select>
</mapper>
```

pojo层：实体类

```java
/**
 * @author zengyujie
 * @version 1.0
 * @Description: 实体类
 * @Date 2021/9/16 9:47
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Books {

    private int bookID;
    private String bookName;
    private int bookCounts;
    private String detail;
}

```

service层：

```java
public interface BookService {
    //增加一本书
    int addBook(Books books);
    //删除一本书
    int deleteBookById(int id);
    //更新一本书
    int updateBook(Books books);
    //查询一本书
    Books queryBookById(int id);
    //查询全部的书
    List<Books> queryAllBook();
    //根据书名查书籍
    Books queryBookByName(String bookName);
}
```

```java
/**
 * @author zengyujie
 * @version 1.0
 * @Description: TODO
 * @Date 2021/9/16 11:22
 */
public class BookServiceImpl implements BookService{

    //Service层调用Dao层
    private BookMapper bookMapper;

    public void setBookMapper(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    public int addBook(Books books) {
        return bookMapper.addBook(books);
    }

    public int deleteBookById(int id) {
        return bookMapper.deleteBookById(id);
    }

    public int updateBook(Books books) {
        return bookMapper.updateBook(books);
    }

    public Books queryBookById(int id) {
        return bookMapper.queryBookById(id);
    }

    public List<Books> queryAllBook() {
        return bookMapper.queryAllBook();
    }

    public Books queryBookByName(String bookName) {
        return bookMapper.queryBookByName(bookName);
    }
}

```

index.jsp

```jsp
<%--
  Created by IntelliJ IDEA.
  User: workfight
  Date: 2021/9/16
  Time: 14:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page deferredSyntaxAllowedAsLiteral="true" contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>首页</title>

    <style>
      a{
        text-decoration: none;
        color:black;
        font-size:18px;
      }
      h3{
        width:180px;
        height:38px;
        margin:100px auto;
        text-align:center;
        line-height:38px;
        background: deepskyblue;
        border-radius:5px;
      }
    </style>
  </head>
  <body>
  <h3>
    <a href="${pageContext.request.contextPath}/book/allBook">进入书籍页面</a>
  </h3>
  </body>
</html>
```

allBook.jsp

```jsp
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--
  Created by IntelliJ IDEA.
  User: workfight
  Date: 2021/9/16
  Time: 15:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page deferredSyntaxAllowedAsLiteral="true" contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>书籍展示</title>

    <!--使用bootstrap-->

    <link href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
</head>


<body>

<div class="container">

    <div class="row_clearfix">
        <div class="col-md-12 column">
            <div class="page-header">
                <h1>
                    <small>显示所有书籍列表</small>
                </h1>
            </div>
        </div>

        <div class="row">
            <div class="col-md-4 column">
                <a class="btn btn-primary" href="${pageContext.request.contextPath}/book/toAddBook">增加书籍</a>
                <a class="btn btn-primary" href="${pageContext.request.contextPath}/book/allBook">显示全部书籍</a>
            </div>
            <div class="col-md-8 column">
                <%--查询书籍--%>
                <form class="form-inline" action="${pageContext.request.contextPath}/book/queryBook" method="post" style="float: right">
                    <span style="color:red;font-weight:bold">${error}</span>
                    <input type="text" name="queryBookName" class="form-control">
                    <input type="submit" value="查询" class="btn btn-primary">
                </form>
            </div>

        </div>
    </div>

    <div class="row_clearfix">
        <div class="col-md-12 column">
            <table class="table table-hover table-striped">
                <thead>
                    <tr>
                        <th>书籍编号</th>
                        <th>书籍名称</th>
                        <th>书籍数量</th>
                        <th>书籍描述</th>
                        <th>进行操作</th>
                    </tr>
                </thead>

                <%--书籍从数据库中查询出来，从这个list中遍历:foreach--%>
                <tbody>
                    <c:forEach var="book" items="${list}">
                        <tr>
                            <td>${book.bookID}</td>
                            <td>${book.bookName}</td>
                            <td>${book.bookCounts}</td>
                            <td>${book.detail}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/book/toUpdate?id=${book.bookID}">修改</a>
                                &nbsp; | &nbsp;
                                <a href="${pageContext.request.contextPath}/book/deleteBook?id=${book.bookID}">删除</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

</body>
</html>
```

addBook.jsp

```jsp
<%--
  Created by IntelliJ IDEA.
  User: workfight
  Date: 2021/9/17
  Time: 1:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page deferredSyntaxAllowedAsLiteral="true" contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <!--使用bootstrap-->
    <link href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container">

    <div class="row_clearfix">
        <div class="col-md-12 column">
            <div class="page-header">
                <h1>
                    <small>新增书籍</small>
                </h1>
            </div>
        </div>
    </div>
    <form action="${pageContext.request.contextPath}/book/addBook" method="post">
        <div class="form-group">
            <label>书籍名称:</label>
            <input type="text" name="bookName" class="form-control" required>
        </div>
        <div class="form-group">
            <label>书籍数量:</label>
            <input type="text" namne="bookCounts" class="form-control" required>
        </div>
        <div class="form-group">
            <label>书籍描述:</label>
            <input type="text" name="detail" class="form-control" required>
        </div>
        <div class="form-group">
            <input type="submit" class="form-control" value="添加">
        </div>
    </form>
</div>
</body>
</html>

```

updateBook.jsp

```jsp
<%--
  Created by IntelliJ IDEA.
  User: workfight
  Date: 2021/9/17
  Time: 1:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page deferredSyntaxAllowedAsLiteral="true" contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>修改书籍</title>

    <!--使用bootstrap-->

    <link href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

<div class="container">

    <div class="row_clearfix">
        <div class="col-md-12 column">
            <div class="page-header">
                <h1>
                    <small>修改书籍</small>
                </h1>
            </div>
        </div>
    </div>

    <form action="${pageContext.request.contextPath}/book/updateBook" method="post">

        <%--前端传递隐藏域--%>
        <input type="hidden" name="bookID" value="${QBook.bookID}">

        <div class="form-group">
            <label>书籍名称:</label>
            <input type="text" name="bookName" class="form-control" value="${QBook.bookName}" required>
        </div>
        <div class="form-group">
            <label>书籍数量:</label>
            <input type="text" name="bookCounts" class="form-control" value="${QBook.bookCounts}" required>
        </div>
        <div class="form-group">
            <label>书籍描述:</label>
            <input type="text" name="detail" class="form-control" value="${QBook.detail}" required>
        </div>
        <div class="form-group">
            <input type="submit" class="form-control" value="修改">
        </div>

    </form>
</div>
</body>
</html>

```

## 5.功能介绍以及实现过程

### 	5.1	查询所有书籍并通过前端页面显示

进入书籍页面，即会跳转到显示所有书籍列表的页面。

```jsp
 <h3>
    <a href="${pageContext.request.contextPath}/book/allBook">进入书籍页面</a>
  </h3>
```

```java
//查询全部的书籍，并且返回到一个书籍展示页面
    @RequestMapping("/allBook")
    public String list(Model model){
        List<Books> list = bookService.queryAllBook();
        model.addAttribute("list",list);
        return "allBook";
    }
```

前端显示书籍列表：

```jsp
 <div class="row_clearfix">
        <div class="col-md-12 column">
            <table class="table table-hover table-striped">
                <thead>
                    <tr>
                        <th>书籍编号</th>
                        <th>书籍名称</th>
                        <th>书籍数量</th>
                        <th>书籍描述</th>
                        <th>进行操作</th>
                    </tr>
                </thead>

                <%--书籍从数据库中查询出来，从这个list中遍历:foreach--%>
                <tbody>
                    <c:forEach var="book" items="${list}">
                        <tr>
                            <td>${book.bookID}</td>
                            <td>${book.bookName}</td>
                            <td>${book.bookCounts}</td>
                            <td>${book.detail}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/book/toUpdate?id=${book.bookID}">修改</a>
                                &nbsp; | &nbsp;
                                <a href="${pageContext.request.contextPath}/book/deleteBook?id=${book.bookID}">删除</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
```

![image-20210918101542819](C:\Users\workfight\AppData\Roaming\Typora\typora-user-images\image-20210918101542819.png)

前端点击进入书籍，根据链接地址映射到controller层中的@RequestMapping匹配到相应的方法，调用service层的queryAllBook方法，并且以List集合形式存储，通过model.addAttribute("list",list);向前台传list，前台通过forEach将查询出来的各个属性遍历出来，最后页面显示仍然是allBook页面，即是点击进入书籍之后显示的页面。

### 	5.2	增加书籍

增加书籍包括在显示书籍页面的增加书籍按钮，跳转之后的增加书籍页面，以及事务控制。

```xml
<a class="btn btn-primary" href="${pageContext.request.contextPath}/book/toAddBook">增加书籍</a>
```

点击增加书籍按钮，通过地址映射到controller层的相关方法，跳转到增加书籍页面。

```xml
<div class="container">

    <div class="row_clearfix">
        <div class="col-md-12 column">
            <div class="page-header">
                <h1>
                    <small>新增书籍</small>
                </h1>
            </div>
        </div>
    </div>
    <form action="${pageContext.request.contextPath}/book/addBook" method="post">
        <div class="form-group">
            <label>书籍名称:</label>
            <input type="text" name="bookName" class="form-control" required>
        </div>
        <div class="form-group">
            <label>书籍数量:</label>
            <input type="text" namne="bookCounts" class="form-control" required>
        </div>
        <div class="form-group">
            <label>书籍描述:</label>
            <input type="text" name="detail" class="form-control" required>
        </div>
        <div class="form-group">
            <input type="submit" class="form-control" value="添加">
        </div>
    </form>
</div>
```

![image-20210918110125551](C:\Users\workfight\AppData\Roaming\Typora\typora-user-images\image-20210918110125551.png)

增加书籍的相关信息填写之后，点击添加按钮，根据映射信息匹配到controller层的方法，调用service层的addBook()方法，返回一个book对象，并且传到前端，跳转到allBook()页面。

```xml
 <!--3.声明式事务配置-->
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <!--注入数据源-->
        <property name="dataSource" ref="dataSource"/>
    </bean>

    <!--4.aop事务支持-->

    <!--配置事务通知-->
    <tx:advice id="txAdivice" transaction-manager="transactionManager">
        <tx:attributes>
            <tx:method name="*" propagation="REQUIRED"/>
        </tx:attributes>
    </tx:advice>
    <aop:config>
        <aop:pointcut id="txPointcut" expression="execution(* com.study.mapper.*.*(..))"/>
        <aop:advisor advice-ref="txAdivice" pointcut-ref="txPointcut"/>
    </aop:config>
```

### 	5.3	修改书籍信息

​	修改书籍信息，需要注意的是，跳转到修改书籍页面之后，页面需要展示所选书籍的相关信息，这样就需要先查找书籍信息然后进行显示，之后再进行修改提交，返回到allBook页面，显示修改之后的所有书籍信息，同时需要事务支持，确保进行有效的修改。

```java
	//跳转到修改书籍的页面
    @RequestMapping("/toUpdate")
    public String toUpdatePaper(int id,Model model){
        Books books = bookService.queryBookById(id);
        model.addAttribute("QBook",books);
        return "updateBook";
    }

    //修改书籍
    @RequestMapping("/updateBook")
    public String updateBook(Books books){
        System.out.println("updateBook=>"+books);
        bookService.updateBook(books);
        return "redirect:/book/allBook";
    }
```

```xml
 <form action="${pageContext.request.contextPath}/book/updateBook" method="post">

        <%--前端传递隐藏域--%>
        <input type="hidden" name="bookID" value="${QBook.bookID}">

        <div class="form-group">
            <label>书籍名称:</label>
            <input type="text" name="bookName" class="form-control" value="${QBook.bookName}" required>
        </div>
        <div class="form-group">
            <label>书籍数量:</label>
            <input type="text" name="bookCounts" class="form-control" value="${QBook.bookCounts}" required>
        </div>
        <div class="form-group">
            <label>书籍描述:</label>
            <input type="text" name="detail" class="form-control" value="${QBook.detail}" required>
        </div>
        <div class="form-group">
            <input type="submit" class="form-control" value="修改">
        </div>

    </form>
```

![image-20210918142805257](C:\Users\workfight\AppData\Roaming\Typora\typora-user-images\image-20210918142805257.png)

### 	5.4	删除书籍信息

```java
//删除书籍
@RequestMapping("/deleteBook/{bookId}")
public String deleteBook(@PathVariable ("bookId")int id){
    bookService.deleteBookById(id);
    return "redirect:/book/allBook";
}
```

### 	5.5	通过搜索框，输入书籍名称，查询出该书籍相关信息

```java
//查询书籍
    @RequestMapping("/queryBook")
    public String queryBook(String queryBookName,Model model){
        Books books = bookService.queryBookByName(queryBookName);
        List<Books> list = new ArrayList<Books>();
        list.add(books);

        if (books==null){
            list=bookService.queryAllBook();
            model.addAttribute("error","查无此书");
        }

        //list返回到前端，并且进行遍历
        model.addAttribute("list",list);
        return "allBook";
    }
```

```xml
 <div class="col-md-8 column">
                <%--查询书籍--%>
                <form class="form-inline" action="${pageContext.request.contextPath}/book/queryBook" method="post" style="float: right">
                    <span style="color:red;font-weight:bold">${error}</span>
                    <input type="text" name="queryBookName" class="form-control">
                    <input type="submit" value="查询" class="btn btn-primary">
                </form>
            </div>
```

![image-20210918142715721](C:\Users\workfight\AppData\Roaming\Typora\typora-user-images\image-20210918142715721.png)

