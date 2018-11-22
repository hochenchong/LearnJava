## Maven 仓库私服搭建（CentOS7）

### 前言

> 实际生产中，很少需要自己搭建 Maven 私服仓库（在一个视频中看到这么一句话：除非公司就你一个程序员）。需要的时候，也就直接网上搜一篇教程照着操作即可。
>
> 本次则只是为了自己练习一下，稍微熟悉一下。主要的目的是为了后面配置 Jenkins 时使用。简单做一下笔记。

----

### 环境

> * CentOS 7 
> * JDK 1.8（Nexus 依赖于 JDK）
> * 下载 Nexus OSS 3.14.0 的 Linux 压缩包版本

---

### 前期准备

> Nexus 是 Maven 仓库管理器。Nexus Pro（专业版）是收费的，所以我们安装开源版，即 OSS 版本，官方链接：[https://www.sonatype.com/nexus-repository-oss](https://www.sonatype.com/nexus-repository-oss) 。目前的最新版为 3.14.0。当然，也可以安装 2.X 的版本（官方链接：[https://help.sonatype.com/repomanager2/download#Download-NexusRepositoryManager2OSS](https://help.sonatype.com/repomanager2/download#Download-NexusRepositoryManager2OSS)）。

---

### 开始搭建

> 1. 将 nexus-3.14.0-04-unix.tar.gz 通过 Winscp 之类的工具上传到 CentOS 服务器上的 /usr/local 目录下，并通过命令进入到该文件夹下：``` cd /usr/local```。
> 2. 解压到当前文件夹，命令：```tar zxvf nexus-3.14.0-04-unix.tar.gz```。解压后出两个文件夹：*nexus-3.14.0-04* 和 *sonatype-work*。
> 3. 运行 *nexus-3.14.0-04* 文件夹下的 bin 目录下的脚本命令：```./nexus-3.14.0-04/bin/nexus start```，即可启动 Nexus 服务（注：该命令在 /usr/local 目录下时输入）。如图：![启动nexus服务.png](https://i.loli.net/2018/11/20/5bf41a4d87b89.png)
> 4. 在浏览器中访问：[http://192.168.229.100:8081/#browse/welcome](http://192.168.229.100:8081/#browse/welcome)。（这里我的虚拟机 IP 地址为 192.168.229.100，端口为 8081），登录默认的用户名与密码：admin/admin123
> 5. 配置阿里远程仓库：登录后，点击顶部的齿轮图标进行配置，左边的 Repository - Repositories，然后点击 *Create repository* 按钮进行创建![创建仓库.png](https://i.loli.net/2018/11/22/5bf6b13b56991.png)
> 6. 选择格式为 *maven2(proxy)*。在创建页面中的 Name 中输入仓库名字，例如：```aliyun-central```，URL 中输入远程仓库的链接，如：```http://maven.aliyun.com/nexus/content/groups/public/```，其它配置默认即可。移到页面最下，点击创建按钮![配置阿里远程仓库.jpg](https://i.loli.net/2018/11/22/5bf6b16d70a91.jpg)
> 7. 在 maven-public 仓库中，将新配置的阿里远程仓库移动到 *Members* 那边的最上方，保存即可。

### 本地使用

> 修改本地 Maven 的配置文件 —— settings.xml 文件，在 *mirrors* 标签内如下配置：

```
	<!-- 个人私服测试 -->
	<mirror>
      <id>mymaven</id>
      <name>my maven</name>
      <url>
          http://192.168.229.100:8081/repository/maven-public/
      </url>
      <mirrorOf>central</mirrorOf>        
    </mirror>
```



### 后记

> Maven 私服安装相当的简单，很简易就可以使用了。接下来则是搭建 SVN 的服务器和 Jenkins 服务器，构建一个持续集成的自动化环境。
>
> 在实践中成长！
>
> HochenChong
>
> 2018-11-22