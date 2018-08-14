## Git 提交时忽略特定文件
### 前言
> 并非所有文件都需要使用 Git 来进行版本控制。对于我而言，主要用于管理我的笔记以及练习时敲的 Java 项目。故对于 IDE 自动生成的文件则需要忽略掉，不进行提交。例如 Eclipse 自己生成的一些特定文件，或者编译所产生的 class 文件等等。

---

### 配置文件
> Git 已经提供有相关的配置文件，在 [https://github.com/github/gitignore](https://github.com/github/gitignore) ，在这里，我们找到自己需要的配置文件，进行修改即可。例如：Java.gitignore
> 
> 里面的内容如下：

```
# Compiled class file
*.class

# Log file
*.log

# BlueJ files
*.ctxt

# Mobile Tools for Java (J2ME)
.mtj.tmp/

# Package Files #
*.jar
*.war
*.nar
*.ear
*.zip
*.tar.gz
*.rar

# virtual machine crash logs, see http://www.java.com/en/download/help/error_hotspot.xml
hs_err_pid*
```
> 我们下载下来这个文件（或者直接复制里面的内容），在后面追加我们要忽略的文件即可，如下：

```
# Eclipse 特定文件
.settings
.classpath
.project
# 项目编译的文件
target
```

---

### 配置
> 该配置文件好后，在 git 全局配置中进行配置。Windows 下默认全局配置为：```C:\Users\用户名\.gitconfig```。我们用编辑器打开，追加以下内容：

```
[core]
	excludesfile = Java.gitignore的绝对路径
```
**注意：**在 Windows 下路径是以 **\** 分割的，这里的配置需要将 **\** 都改为 **/**。

---

### 后记
> 我们可以将 Java.gitignore 这个配置文件也放在我们的 Git 仓库进行版本管理。
> 
> 在实践中成长！
> 
> HochenChong
> 
> 2018-8-14