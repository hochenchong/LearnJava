## MyBatis 全局映射文件配置
### 特定结构配置
> configuration 中的配置，按以下顺序配置：Content Model : (properties?, settings?, typeAliases?, typeHandlers?, objectFactory?, 
 objectWrapperFactory?, reflectorFactory?, plugins?, environments?, databaseIdProvider?, mappers?)
> 
> 以上都是选配，可以不配置，但是配置则必须按以上顺序进行配置

---

### properties 属性
> 使用该标签来引入外部的 properties 配置文件的内容，或通过该标签的子元素 property 来进行配置，从而动态配置全局配置文件中的一些属性值。
> 
> 配置的优先级（从高到低）：
> * 通过方法参数传递的属性具有最高优先级
> * resource/url 属性中指定的配置文件
> * properties 属性中子元素 property 指定的属性
> 
> 常见于配置外部 jdbc.properties 文件，动态配置 JDBC 的属性值。如下：

#### jdbc.properties
```
jdbc.driver=com.mysql.cj.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/mybatis?serverTimezone=UTC&characterEncoding=utf8&useUnicode=true&useSSL=false
jdbc.username=root
jdbc.password=123456
```

#### MyBatis 全局配置文件
```
<!-- 
	MyBatis 使用 properties 标签来引入外部的 properties 配置文件的内容
		resource：引入类路径下的资源
		url：引入网络路径或者磁盘路径下的资源
	也可以直接配置 property 来使用
-->
<properties resource="dbconfig.properties" >
	<!-- 尝试删掉  jdbc.properties 中的 jdbc.driver=com.mysql.cj.jdbc.Driver -->
	<property name="jdbc.driver" value="com.mysql.cj.jdbc.Driver"/>
</properties>

<environments default="development">
	<environment id="development">
		<transactionManager type="JDBC" />
		<dataSource type="POOLED">
			<property name="driver" value="${jdbc.driver}" />
			<property name="url" value="${jdbc.url}" />
			<property name="username" value="${jdbc.username}" />
			<property name="password" value="${jdbc.password}" />
		</dataSource>
	</environment>
</environments>
```

---

### settings 设置

---

### typeAliases 类型别名

---

### typeHandlers 类型处理器

---

### objectFactory 对象工厂

---

### plugins 插件

--- 

### environments 环境

---

### databaseIdProvider 数据库厂商标识

---

### mappers 映射器