## FastDFS 集群搭建
### 前言
> 于前几天搭建了 FastDFS 单机版来使用，而实际应用中，一般使用的都是集群版本的 FastDFS，相比于单机版，大体的搭建差不多，只不过配置文件的内容有所不同。


----


### 前期规划

> * Nginx 服务器一台，用于作为 HTTP 访问的入口
>   * 本次虚拟机的 IP 为 192.168.229.167
> * Tracker 集群，这里配置两台 Tracker 服务器
>   * IP 分别为 192.168.229.170，192.168.229.171
>   * 在 Tracker 上部署 Nginx，提供 HTTP 访问的反向代理、负载均衡以及缓存等
> * Storage 集群，这里配置两台 Storage 服务器，分为两个组
>   * 同一组的 Storage 服务器存储的数据相同
>   * 同一组的数据存储容量取决于该组中存储容量最小的那台
>   * IP 为 192.168.229.175，192.168.229.176
>   * 部署 Nginx 以及 FastDFS 的 Nginx 扩展模块，提供访问与下载服务等
>
> 操作文件时，将直接使用相关的 api 连接到 Tracker 服务器中进行增删改查。访问时，则使用 Nginx 服务器做负载均衡转发请求到 Tracker 服务器，由 Tracker 服务器中的 Nginx 再转发给 Storage 服务器进行处理。



本次练习的环境
> - 虚拟机工具 ：VMware Workstations 14 Pro
> - 操作系统 ：CentOS 7 64位
>   - 为了方便起见，这里已经将防火墙关闭了
>   - 实际应用中，则开启防火墙，开放相应的端口即可

前期准备的安装包有：

> **libfastcommon**（这里使用 1.0.38 版本）：[https://github.com/happyfish100/libfastcommon/releases](https://github.com/happyfish100/libfastcommon/releases)
>
> **FastDFS**（这里使用 5.11 版本）：[https://github.com/happyfish100/fastdfs/releases](https://github.com/happyfish100/fastdfs/releases)
>
> **fastdfs-nginx-module**（这里使用 1.16 版本）：[https://github.com/happyfish100/fastdfs-nginx-module/releases](https://github.com/happyfish100/fastdfs-nginx-module/releases)
>
> **ngx_cache_purge**（这里使用 2.3 版本）：[http://labs.frickle.com/files/](http://labs.frickle.com/files/)
>
> **Nginx**（这里使用 1.15.1 版本）：[http://nginx.org/en/download.html](http://nginx.org/en/download.html)

安装软件

> * Nginx 服务器
>   * ngx_cache_purge
>   * Nginx
> * Tracker 服务器
>   * libfastcommon
>   * FastDFS
>   * Nginx
>   * ngx_cache_purge
> * Storage 服务器
>   * libfastcommon
>   * FastDFS
>   * Nginx
>   * ngx_cache_purge
>   * fastdfs-nginx-module

根据上面的列表将相关的软件拷贝到各个服务器中的 /root 目录下，并进行解压操作（由于以上的软件我下载的都是 tar.gz 压缩格式的，故统一使用命令：```tar zxvf XXX.tar.gz``` 来进行解压操作。

---

### 安装 FastDFS

> FastDFS 的安装其实很简单，在上一回的文章已经讲过了（[FastDFS 单机版环境搭建](https://blog.csdn.net/hochenchong/article/details/81008229)），Tracker 服务器和 Storage 服务器都需要安装 FastDFS，这里快速的过一遍。

```
# 在线安装依赖环境
yum install gcc gcc-c++ make automake autoconf libtool pcre* zlib openssl openssl-devel
# 进入 root 目录下
cd /root
# 解压 libfastcommon 压缩包
tar zxvf libfastcommon-1.0.38.tar.gz
# 进入 libfastcommon 文件夹中，编译 libfastcommon 以及安装
cd libfastcommon-1.0.38
./make.sh && ./make.sh install
# 由于 libfastcommon 安装的路径在 /usr/lib64/ 
# 但是 FastDFS 主程序设置的 lib 目录是在 /usr/local/lib，所以需要创建软链接
# 不过试了一下，不创建也没问题，可能是使用的版本更新了，亦或者因为我的 CentOS7 是 64 位的
# 如果 FastDFS 安装有问题，则运行以下四条命令重新安装 FastDFS
# ln -s /usr/lib64/libfastcommon.so /usr/local/lib/libfastcommon.so 
# ln -s /usr/lib64/libfastcommon.so /usr/lib/libfastcommon.so 
# ln -s /usr/lib64/libfdfsclient.so /usr/local/lib/libfdfsclient.so 
# ln -s /usr/lib64/libfdfsclient.so /usr/lib/libfdfsclient.so

# FastDFS 安装
cd /root
# 解压 FastDFS 压缩包，编译以及安装
tar zxvf fastdfs-5.11.tar.gz
cd fastdfs-5.11
./make.sh && ./make.sh install
```

到此，FastDFS 就安装好了，接下来就是配置了。

---

### Tracker 服务器配置

> 两台 Tracker 服务器配置是相同的，这里以其中一台为例

```
# 创建 Tracker 的存储日志和数据的根目录
mkdir -p /home/fastdfs/tracker
cd /etc/fdfs
cp tracker.conf.sample tracker.conf
# 配置 tracker.conf
vi tracker.conf
```

> 在这里，tracker.conf 只是修改一下 Tracker 存储日志和数据的路径 

```
# 启用配置文件（默认为 false，表示启用配置文件）
disabled=false
# Tracker 服务端口（默认为 22122）
port=22122
# 存储日志和数据的根目录
base_path=/home/fastdfs/tracker
```

>  这里，Tracker 服务就可以正常使用了.不过我们也可以顺带配置一下 Tracker 服务器中客户端配置文件

```
mkdir -p /home/fastdfs/client
# 修改 Tracker 服务器客户端配置文件
cp /etc/fdfs/client.conf.sample /etc/fdfs/client.conf
vi /etc/fdfs/client.conf
```

> client.conf 中修改 base_path 和 Tracker 服务器的 IP 地址与端口号即可 

```
# 存储日志文件的基本路径
base_path=/home/fastdfs/client
# Tracker 服务器 IP 地址与端口号
tracker_server=192.168.229.170:22122
tracker_server=192.168.229.171:22122
```

---

### Storage 服务器配置

> 这里的两台 Storage 服务器配置大体上相同，不同的分组，主要就是 ```group_name``` 的配置不同，其它配置基本一致。同一组的 Storage 服务器配置大体上一致。

```
# 创建 Storage 的存储日志和数据的根目录
mkdir -p /home/fastdfs/storage
cd /etc/fdfs
cp storage.conf.sample storage.conf
# 配置 storage.conf
vi storage.conf
```

> 在这里，storage.conf 只是修改一下 storage 存储日志和数据的路径 

```
# 启用配置文件（默认为 false，表示启用配置文件）
disabled=false
# 组名，第一组为 group1，以此递增
group_name=group1
# Storage 服务端口（默认为 23000）
port=23000
# 数据和日志文件存储根目录
base_path=/home/fastdfs/storage
# 存储路径，访问时路径为 M00
# store_path1 则为 M01，以此递增到 M99（如果配置了多个存储目录的话，这里只指定 1 个）
store_path0=/home/fastdfs/storage
# Tracker 服务器 IP 地址和端口，单机搭建时也不要写 127.0.0.1
# tracker_server 可以多次出现，如果有多个，则配置多个
tracker_server=192.168.229.170:22122
tracker_server=192.168.229.171:22122
```

> 在这里，192.168.229.175 的 group_name 为 group1，192.168.229.175 为 group2

---



### 启动 Tracker 服务以及 Storage 服务
> 启动服务时，先启动 Tracker 服务，再启动 Storage 服务器
```
# 启动 Tracker 服务
# 其它操作则把 start 改为 stop、restart、reload、status 即可。Storage 服务相同
/etc/init.d/fdfs_trackerd start
# 启动 Storage 服务
/etc/init.d/fdfs_storaged start
# 可以通过 fdfs_monitor 查看集群的情况
# 查看 Storage 是否已经注册到 Tracker 服务器中
# 当查看到 ip_addr = 192.168.229.175 (localhost.localdomain)  ACTIVE
# ACTIVE 表示成功
/usr/bin/fdfs_monitor /etc/fdfs/storage.conf
```

> 此时应该可以看到有两个分组，而且都是 ACTIVE 状态的。

---



### Storage 服务器安装 Nginx

> 接下来，每台 Storage 都如下安装与配置 Nginx

```
# 配置 fastdfs-nginx-module 模块，这里使用的是 1.16 版本
# 修改 fastdfs-nginx-module 的 config 配置文件
vi /root/fastdfs-nginx-module/src/config
```

> 将 ```CORE_INCS="$CORE_INCS /usr/local/include/fastdfs /usr/local/include/fastcommon/"``` 中的 local 去掉，修改为  ```CORE_INCS="$CORE_INCS /usr/include/fastdfs /usr/include/fastcommon/"``` 。然后保存即可

```
# Nginx 以及模块安装
cd /root/nginx-1.15.1
# 添加 fastdfs-nginx-module 模块和 ngx_cache_purge 模块
./configure --add-module=/root/fastdfs-nginx-module/src --add-module=/root/ngx_cache_purge-2.3
# 编译以及安装
make && make install

# fastdfs-nginx-module 和 FastDFS 配置文件修改
# 复制 FastDFS 的部分配置文件到 /etc/fdfs
cd /root/fastdfs-5.11/conf/
cp http.conf mime.types /etc/fdfs/
# 复制 fastdfs-nginx-module 源码中的配置文件到  /etc/fdfs 中
cp /root/fastdfs-nginx-module/src/mod_fastdfs.conf /etc/fdfs/
vi /etc/fdfs/mod_fastdfs.conf
```

> mod_fastdfs.conf 配置如下 

```
# Storage 日志文件
base_path=/home/fastdfs/storage
# Tracker 服务器IP和端口修改，如果有多个就配置多个
tracker_server=192.168.229.170:22122
tracker_server=192.168.229.171:22122
# Storage 服务器端口（默认为 23000）
storage_server_port=23000

# 当前 Storage 服务器的组名
group_name=group1

# 请求的 url 中是否包含 group 名称，改为 true，包含 group
url_have_group_name = true
# 配置 Storage 存储目录信息，修改 store_path0 的信息，有多个存储目录就配置多个
# 必须和 storage.conf 中的配置一致
store_path0=/home/fastdfs/storage

# 有多少个分组
group_count = 2
# 每个小组的信息
[group1]
group_name=group1
storage_server_port=23000
store_path_count=1
store_path0=/home/fastdfs/storage
[group2]
group_name=group2
storage_server_port=23000
store_path_count=1
store_path0=/home/fastdfs/storage
```

> 以上需要注意的还是组名那里，在这里，192.168.229.175 的 group_name 为 group1，192.168.229.175 为 group2
>
> 接下来就是配置 Nginx 了

```vi /usr/local/nginx/conf/nginx.conf ```

> 在配置文件中添加一个 server

```
server {
	listen       8888;
	server_name  localhost;

	# 配置为支持 group0-group9，以及 M00-M99，以便于以后扩容
	# 由于配置了 ngx_fastdfs_module 模块，当该服务器找不到文件时，则会去文件的源服务器中寻找
	location ~/group([0-9])/M([0-9])([0-9]) {
		ngx_fastdfs_module;
	}
}
```

> 到此，Storage 服务器就配置完毕了

---

### Tracker 服务器安装 Nginx
> Tracker 服务器的安装与配置就简单些，两台 Tracker 服务器的配置执行相同的操作即可

```
# Nginx 以及模块安装
cd /root/nginx-1.15.1
# 添加 ngx_cache_purge 模块
./configure --add-module=/root/ngx_cache_purge-2.3
# 编译以及安装
make && make install

# 配置 Nginx
vi /usr/local/nginx/conf/nginx.conf
```

> 配置如下

```
# 设置 group1 的服务器
upstream fdfs_group1 {
	server 192.168.229.175:8888 weight=1 max_fails=2 fail_timeout=30s;
	# server 192.168.229.177:8888 weight=1 max_fails=2 fail_timeout=30s;
}
# 设置 group2 的服务器
upstream fdfs_group2 {
	server 192.168.229.176:8888 weight=1 max_fails=2 fail_timeout=30s;
	# server 192.168.229.178:8888 weight=1 max_fails=2 fail_timeout=30s;
}

server {
	listen  8000;
    server_name localhost;

	# 请求为 group1 的转发给 group1 的服务器组进行处理
    location ~/group1/M([0-9])([0-9]) {
    	proxy_pass http://fdfs_group1;
    }

	# 请求为 group2 的转发给 group2 的服务器组进行处理
    location ~/group2/M([0-9])([0-9]) {
    	proxy_pass http://fdfs_group2;
    }
}
```

---

### Nginx 服务器配置

> Nginx 服务器作为该 FastDFS 集群的统一入口，进行负载均衡处理

```
# 安装 Nginx 依赖环境
yum install gcc gcc-c++ make automake autoconf libtool pcre* zlib openssl openssl-devel
# Nginx 以及模块安装
cd /root/nginx-1.15.1
# 添加 ngx_cache_purge 模块
./configure --add-module=/root/ngx_cache_purge-2.3
# 编译以及安装
make && make install

# 配置 Nginx
vi /usr/local/nginx/conf/nginx.conf
```

> Nginx 配置如下

```
# 设置 tracker
upstream fdfs_tracker {
    server 192.168.229.170:8000 weight=1 max_fails=2 fail_timeout=30s;
    server 192.168.229.171:8000 weight=1 max_fails=2 fail_timeout=30s;
}

server {
    listen  80;
    server_name localhost;

	# 将所有 FastDFS 请求转发给 Tracker 集群处理
    location ~/group([0-9])/M([0-9])([0-9]) {
    	proxy_pass http://fdfs_tracker;
    }
}
```

---

### 启动 Nginx 服务
```
# 启动 Nginx
/usr/local/nginx/sbin/nginx
# 重启 Nginx
/usr/local/nginx/sbin/nginx -s reload
# 停止 Nginx
/usr/local/nginx/sbin/nginx -s stop
```

---

### 集群扩容

> 关于扩容，有两种方式。
>
> 1. 第一种常见于 Storage 服务器添加新的硬盘，然后修改配置文件进行扩容。不过要注意的是，同一组的最大容量取决于容量最小的那台服务器。
> 2. 第二种扩容则是添加服务器，设置为新的组。

---

### 参考资料及扩展资料

> * [高可用高性能分布式文件系统FastDFS进阶keepalived+nginx对多tracker进行高可用热备](https://www.cnblogs.com/zhangs1986/p/8269175.html)
> * [使用FastDFS的内置防盗链功能](http://bbs.chinaunix.net/thread-1916999-1-1.html)

---

### 后记
> 搭建好了就是启动各个服务器中的服务即可。在此就不测试了，和单机版的使用类似。集群版到此也就告一段落了。建议是先试试配置单机版，然后再来配置一下集群的版本。关于 FastDFS，计划还会码一篇 Java 的使用笔记。
>
> 在实践中成长！
>
> HochenChong
>
> 2018-7-13