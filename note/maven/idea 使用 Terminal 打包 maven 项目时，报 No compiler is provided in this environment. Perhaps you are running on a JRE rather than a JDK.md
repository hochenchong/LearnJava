## idea 使用 Terminal 打包 maven 项目时，报 No compiler is provided in this environment. Perhaps you are running on a JRE rather than a JDK?

### 遇到的问题

#### 命令行下 Maven 环境未配置

> 虽然在 idea 中配置了 maven，不过在使用 idea 中的 Terminal，默认时调用系统自带的 cmd.exe 工具，此时，使用 maven 命令打包时，可能会提示以下错误。

```
'mvn' 不是内部或外部命令，也不是可运行的程序或批处理文件。
```

> 配置 maven 环境即可解决。以 Windows 10 为例
>
> * 右键计算机 —— 属性 —— 高级系统设置 —— 高级 —— 环境变量 —— 系统变量 —— 新建
> * 变量名：**MAVEN_HOME**，变量值：**E:\maven\apache-maven-3.6.2（自己本地的 maven 文件路径）**，然后点击 “确定” 按钮
> * 双击原本有的系统变量 **Path**，在里面添加 **%MAVEN_HOME%\bin**，然后确定即可
> * 如果要配置本地 Maven 的环境编码，默认跟随系统为 GBK。则添加一个环境变量，变量名是：**MAVEN_OPTS**，变量值：**-Xms256m -Xmx512m -Dfile.encoding=UTF-8**，保持即可
>
> 以上操作后，重新打开 cmd 窗口（如果是在 idea 中的 Terminal 中操作则重启 idea），结果如下图

![](https://github.com/hochenchong/learnJava/blob/master/images/jdk%E5%92%8Cmaven%E7%8E%AF%E5%A2%83%E5%8F%98%E9%87%8F%E9%85%8D%E7%BD%AE1.png?raw=true)



#### No compiler is provided in this environment. Perhaps you are running on a JRE rather than a JDK?

> Windows10 在安装好 Java 8 后，使用 ```java -version``` 命令时已经查看到 Java 的环境。然而，在我们使用 maven 命令打包时，还是遇到了如下错误。

```
[ERROR] Failed to execute goal org.apache.maven.plugins:maven-compiler-plugin:3.1:testCompile (default-testCompile) on project hello-spring: Compilation failure
[ERROR] No compiler is provided in this environment. Perhaps you are running on a JRE rather than a JDK?
[ERROR]
[ERROR] -> [Help 1]
[ERROR]
[ERROR] To see the full stack trace of the errors, re-run Maven with the -e switch.
[ERROR] Re-run Maven using the -X switch to enable full debug logging.
[ERROR]
[ERROR] For more information about the errors and possible solutions, please read the following articles:
[ERROR] [Help 1] http://cwiki.apache.org/confluence/display/MAVEN/MojoFailureException
```

> 仔细看一下上图，就会发现，我们查看 maven 版本信息中，有这么一行内容：*runtime: C:\Program Files\Java\jre1.8.0_202*
>
> 所以需要手动配置一下 Java 的环境变量。以 Windows10 为例
>
> * 新建系统变量，变量名：**JAVA_HOME**，变量值：**C:\Program Files\Java\jdk1.8.0_202（jdk 安装位置）**
> * 双击原本有的系统变量 **Path**，在里面添加 **%JAVA_HOME%\bin**，以及 **%JAVA_HOME%\jre\bin**，然后确定即可
> * 新建系统变量，变量名：**CLASSPATH**，变量值：**.;%JAVA_HOME%\lib\dt.jar;%JAVA_HOME%\lib\tools.jar;**
>
> 配置后重新打开 cmd 窗口，此时查看 maven 版本信息，如下图

![](https://github.com/hochenchong/learnJava/blob/master/images/jdk%E5%92%8Cmaven%E7%8E%AF%E5%A2%83%E5%8F%98%E9%87%8F%E9%85%8D%E7%BD%AE2.png?raw=true)

> 此时再使用 cmd 打包 maven 项目，或者使用 idea 中的 Terminal 打包 maven 项目时，就不会再报 ```No compiler is provided in this environment. Perhaps you are running on a JRE rather than a JDK?``` 错误了。

### 后记

> 安装完 JDK 后，使用命令发现能查看到 Java 版本信息之后就没有去理了，以前安装都晓得去配置一下环境变量。而在遇到此问题后，开始觉得，有些东西还是不能偷懒啊，系统帮你配置好的，不一定是你所需要的。
>
> 2019-12-1
>
> HochenChong