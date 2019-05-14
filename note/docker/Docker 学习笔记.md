## Docker 入门笔记
### 前言
> 之前听说过 Docker，可以实现虚拟化，相比于平时我们使用的虚拟机，启动的速度更快，占用资源更少。最近看视频学习了一些，在此做一下笔记。
> 
> Docker 在我理解相当于 Java 虚拟机（JVM），只要在 Windows，Linux，OSX 上安装了 Docker，在这里能使用，打包后，直接到另一处也可以直接使用（例如 Java 代码）。
> 
> 在百度百科上 [Docker](https://baike.baidu.com/item/Docker/13344470) 的介绍是：Docker 是一个开源的应用容器引擎，让开发者可以打包他们的应用以及依赖包到一个可移植的容器中，然后发布到任何流行的 Linux 机器上，也可以实现虚拟化。容器是完全使用沙箱机制，相互之间不会有任何接口。
> 
> 我们可以将软件进行配置，然后编译成一个镜像发布出去。其他人便可以直接使用这个镜像，直接实例化一个容器来使用。例如以前重装系统时，使用别人打包好的 Ghost 系统，而我们直接拿过来用即可，相比于自己一步步从官网下载系统，一步步自己配置（打补丁，安装软件），那可是快了很多。

---

### 为什么要使用 Docker？
> 主要是与传统的虚拟机进行比较
> 
> * 更高效的利用系统资源：不需要进行硬件虚拟以及运行完成的操作系统，相比于虚拟机，同样的配置，Docker 可以运行更多数量的应用
> * 更快速的启动时间：可以做到秒级，毫秒级别
> * 一致的运行环境：镜像提供了除内核外完整的运行时环境
> * 持续交付和部署：一次创建，即可在任何地方正常使用

---

### Docker 常见名词解释
* Host（主机）：安装了 Docker 程序的机器。Docker 目前提供了 Windows，Linux，OSX 各个系统的版本。不过对系统的版本可能有所要求。
* Client（客户端）：用来连接 Docker 主机进行操作的客户端软件
* Images（镜像）：一个特殊的文件系统，提供了容器运行时所需要的环境以及配置。
* Container（容器）：镜像启动后的实例称为一个容器。容器的实质是进程
* Registry（仓库）：保存镜像的地方

---

### 安装 Docker
> 这里以 CentOS7 安装 Docker 为例
>
> 系统要求：Docker CE 支持 64 位版本 CentOS7，并且要求内核版本不低于 3.10、CentOS7 满足最低内核的要求
>
> 在线安装 Docker：```yum install docker```，中途询问是否安装之类的，输入 ```y```，然后回车即可
>
> 验证是否完成成功，在命令行中输入：```docker -v```。若显示如下内容：
> ```
> [root@localhost ~]# docker -v
> Docker version 1.13.1, build 94f4240/1.13.1
> ```


> 则代表安装成功了
> 
> 接下来我们就可以启动 Docker 进行操作了
> 
> * 启动 Docker（这里以 CentOS7 为例）：```systemctl start docker```
> * 设置开机启动 Docker：```systemctl enable docker```

---

### Docker 的基本使用
> 1. 在 Docker 仓库中查找需要的镜像（[Docker Hub：Docker 官方维护的公共仓库](https://hub.docker.com/)）
> 2. 使用 Docker 运行镜像，则这个镜像就会实例化一个 Docker 容器

#### 镜像的基本操作命令
1. 搜索镜像：```docker search 镜像名```
		例如：```docker search tomcat```。
		默认去 Docker 官方维护的仓库中查找
2. 下载镜像：```docker pull 镜像名:tag```
	 	```:tag``` 是可选参数，tag 表示标签，多为软件的版本，一般在 Docker 官方维护的仓库中查看有哪些版本
		默认下载 latest 版本
3. 查看所有本地的镜像：```docker images```
4. 删除镜像：```docker rmi image_id```
5. 删除指定的本地镜像

#### 容器的基本操作命令
1. 运行：```docker run --name container-name -d image-name```
		--name：自定义容器名
		-d：后台运行
		image-name：指定镜像模板
2. 启动：```docker start container-name/container-id```
		根据容器名或容器 id 启动容器
3. 查看：```docker ps```
		查看运行中的容器
		加上 ```-a```，查看所有容器
4. 停止：```docker stop container-name/container-id```
		停止指定的容器
5. 删除：```docker rm container-id```
		删除指定的容器
		删除之前需要先停止该容器
6. 端口映射：```-p 主机端口:容器内部端口```
		将主机端口映射到容器内部的端口
		启动的时候，设置端口映射
        补充：为了方便外部连接，需要关闭防火墙。
            CentOS7 关闭防火墙：
            	systemctl stop firewalld.service
			禁止防火墙开机启动
            	systemctl disable firewalld.service
7. 查看容器日志：```docker logs container-name/container-id```

*更多命令可以从官方文档中查看：[https://docs.docker.com/engine/reference/commandline/docker/]()*

---

### 参考资料
> * [Docker 官网](https://www.docker.com/)
> * [Docker Hub：Docker 官方维护的公共仓库](https://hub.docker.com/)
> * [Docker 官方文档](https://docs.docker.com/)
> * [Docker 百度百科](https://baike.baidu.com/item/Docker/13344470)

---

### 后记
> 这仅仅只是最基础的入门，方便大家以及自己先克服心中对未知的恐惧，之后有什么问题，大多则是查看官方文档或者使用搜索引擎。有时候不得不感慨，大多数的官方文档其实都很详细，只不过自己并没有好好的利用而已。
> 
> 在实践中成长！
> 
> HochenChong
> 
> 时间：2018-06-04