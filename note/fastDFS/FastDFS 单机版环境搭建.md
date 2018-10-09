## FastDFS 单机版环境搭建
### 前言
> 之前看视频学习一个商城项目的时候，使用过 FastDFS 来当做图片服务器，直接拿别人搭建好的单机版 FastDFS 环境来使用（之前的文章中有提到：[FastDFS 使用及遇到的问题](https://blog.csdn.net/hochenchong/article/details/79967859)）。
> 
> 如今，由于生产需要，故搜索相关的资料来搭建一下环境，顺带做一下笔记（毕竟还是自己的笔记最顺手，O(∩_∩)O哈哈~）。

---

### 搭建环境
> 本次搭建依旧是在虚拟机上练手（做好一些步骤后，创建个快照，方便后面操作出现问题时，恢复到快照，省着从头开始）
> 
> * 虚拟机工具 ：VMware Workstations 14 Pro
> * 操作系统 ：CentOS 7 64位
> * IP ： 192.168.229.166
> 
> 为了简单起见，这里默认关闭了防火墙。实际生产中，则不这样子做，而是开放指定的端口，以增强安全性。
> 
> 之后我会上传我搭建好的单机版供大家使用，根据自己的虚拟机配置环境修改一下网卡，主机名，DNS 等信息即可。（可参照另一篇文章：[WinSCP 连接本地虚拟机 CentOS 7](https://blog.csdn.net/hochenchong/article/details/79779347)）

---

### 工具的准备
> 建议直接从官方地址下载
> 
> **libfastcommon**
> * 从 FastDFS 和 FastDHT 中提取出来的公共 C 函数库，基础环境
> * 在安装 FastDFS 前需要先安装这个
> * 下载地址址：[https://github.com/happyfish100/libfastcommon/releases](https://github.com/happyfish100/libfastcommon/releases)
> 
> **FastDFS**
> * FastDFS 安装包
> * 下载地址：[https://github.com/happyfish100/fastdfs/releases](https://github.com/happyfish100/fastdfs/releases)
> 
> **fastdfs-nginx-module**
> * 为了实现通过 HTTP 服务访问和下载 FastDFS 服务器中的文件
> * 可以重定向文件链接到源服务器取文件，避免同一组 Storage 服务器同步延迟导致文件访问错误
> * 下载地址：[https://github.com/happyfish100/fastdfs-nginx-module/releases](https://github.com/happyfish100/fastdfs-nginx-module/releases)
> * 注：这个目前最新版是 V1.20，不过由于配置时出了点问题，故这里改为使用 V1.16 版本
> 
> **Nginx**
> * 实现 HTTP 访问，负载均衡和缓存等功能
> * 下载地址：[http://nginx.org/en/download.html](http://nginx.org/en/download.html)
> 
> **本次采用的安装包版本如下**
> * libfastcommon ：1.0.38
> * FastDFS ：5.11
> * fastdfs-ninx-module ：1.16
> * Nginx ： 1.15.1
> 
> 将以上安装包拷贝到 CentOS7 下的 /root 目录下

---
### FastDFS 安装与配置
#### 安装 libfastcommon
> libfastcommon 安装依赖于 gcc 和 perl，故要先安装这两个

```
# 在线安装 gcc
yum install make cmake gcc gcc-c++
# 在线安装 perl
yum -y install perl*
# 进入 root 目录下
cd /root
# 解压 libfastcommon 压缩包
tar zxvf libfastcommon-1.0.38.tar.gz
# 进入 libfastcommon 文件夹中，编译 libfastcommon 以及安装
cd libfastcommon-1.0.38
./make.sh && ./make.sh install
```

> 以下创建软链接的操作，在最新版本（5.11）的 FastDFS 和 libfastcommon-1.0.38 下不用操作了（亲测）。查阅别人的教程时（使用 5.05 版本的 FastDFS + 1.0.7 版本的 libfastcommon ），普遍做了创建软链接这一步，故使用之前版本的可以操作以下命令（当然，事实上使用新版本操作以下命令也没什么影响）。
> 
> 故如果和我一样使用最新版本的，可以跳过创建软链接的操作，直接进行 FastDFS 的安装（有问题再来找解决办法）。

```
# 由于 libfastcommon 安装的路径在 /usr/lib64/ 
# 但是 FastDFS 主程序设置的 lib 目录是在 /usr/local/lib，所以需要创建软链接
ln -s /usr/lib64/libfastcommon.so /usr/local/lib/libfastcommon.so 
ln -s /usr/lib64/libfastcommon.so /usr/lib/libfastcommon.so 
ln -s /usr/lib64/libfdfsclient.so /usr/local/lib/libfdfsclient.so 
ln -s /usr/lib64/libfdfsclient.so /usr/lib/libfdfsclient.so
```

#### 安装 FastDFS
```
cd /root
# 解压 FastDFS 压缩包，编译以及安装
tar zxvf fastdfs-5.11.tar.gz
cd fastdfs-5.11
./make.sh && ./make.sh install
```

#### 配置 Tracker
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

#### 配置 Storage
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
# Storage 服务端口（默认为 23000）
port=23000
# 数据和日志文件存储根目录
base_path=/home/fastdfs/storage
# 存储路径，访问时路径为 M00
# store_path1 则为 M01，以此递增到 M99（如果配置了多个存储目录的话，这里只指定 1 个）
store_path0=/home/fastdfs/storage
# Tracker 服务器 IP 地址和端口，单机搭建时也不要写 127.0.0.1
# tracker_server 可以多次出现，如果有多个，则配置多个
tracker_server=192.168.229.166:22122
# 设置 HTTP 访问文件的端口。这个配置已经不用配置了，配置了也没什么用
# 这也是为何 Storage 服务器需要 Nginx 来提供 HTTP 访问的原因
http.server_port=8888
```

#### 启动 Tracker 和 Storage 服务
```
# 启动 Tracker 服务
# 其它操作则把 start 改为 stop、restart、reload、status 即可。Storage 服务相同
/etc/init.d/fdfs_trackerd start
# 启动 Storage 服务
/etc/init.d/fdfs_storaged start
# 可以通过 fdfs_monitor 查看集群的情况
# 查看 Storage 是否已经注册到 Tracker 服务器中
# 当查看到 ip_addr = 192.168.229.166 (localhost.localdomain)  ACTIVE
# ACTIVE 表示成功
/usr/bin/fdfs_monitor /etc/fdfs/storage.conf
```

![1.png](https://i.loli.net/2018/07/11/5b460bc96a646.png)

#### 测试上传文件
```
# 修改 Tracker 服务器客户端配置文件
cp /etc/fdfs/client.conf.sample /etc/fdfs/client.conf
vi /etc/fdfs/client.conf
```
> client.conf 中修改 base_path 和 Tracker 服务器的 IP 地址与端口号即可

```
# 存储日志文件的基本路径
base_path=/home/fastdfs/tracker
# Tracker 服务器 IP 地址与端口号
tracker_server=192.168.229.166:22122
```
> 拷贝一张图片到 root 目录下

```
# 存储到 FastDFS 服务器中
/usr/bin/fdfs_upload_file /etc/fdfs/client.conf /root/test.png
```
> 当返回文件 ID 号，如 ```group1/M00/00/00/wKjlpltF-K-AZQQsAABhhboA1Kk469.png``` 则表示上传成功

![2.png](https://i.loli.net/2018/07/11/5b460d2682d5a.png)

以上则完成了 FastDFS 的安装与配置，可以使用 api 来完成文件的上传、同步和下载。

当然，接下来我们还会安装 Nginx。目的如下：
> * Storage 安装 Nginx，为了提供 http 的访问和下载服务，同时解决 group 中 Storage 服务器的同步延迟问题
> * Tracker 安装 Nginx，主要是为了提供 http 访问的反向代理、负载均衡以及缓存服务

---

### 安装与配置 Nginx
#### Nginx 依赖环境安装
> Nginx 依赖环境有 GCC（这个在安装 libfastcommon 时就安装了）、PCRE、zlib 和 openssl。可以通过以下几条命令 or 一条命令进行在线安装

```
# 安装 GCC
yum install -y gcc-c++ 
# 安装 PCRE
yum install -y pcre pcre-devel
# 安装 zlib
yum install -y zlib zlib-devel
# 安装 openssl
yum install -y openssl openssl-devel

# 以上命令也可以使用一条命令来安装
yum install gcc gcc-c++ make automake autoconf libtool pcre* zlib openssl openssl-devel
```

#### 配置 fastdfs-nginx-module 模块
> **所有 storage 节点都要安装 fastdfs-nginx-module 模块**
> 
> 这里我使用的是 1.16 版本的 fastdfs-nginx-module。使用最新版本（1.20）时，不晓得该怎么配置 o(╥﹏╥)o

```
cd /root
# 解压 fastdfs-nginx-module 模块
tar zxvf fastdfs-nginx-module_v1.16.tar.gz
# 修改 fastdfs-nginx-module 的 config 配置文件
vi fastdfs-nginx-module/src/config
```
将 ```CORE_INCS="$CORE_INCS /usr/local/include/fastdfs /usr/local/include/fastcommon/"
``` 中的 local 去掉，修改为 ```CORE_INCS="$CORE_INCS /usr/include/fastdfs /usr/include/fastcommon/"```

![3.png](https://i.loli.net/2018/07/11/5b46106cd84d5.png)

#### 编译安装 nginx

```
cd /root
tar zxvf nginx-1.15.1.tar.gz
cd nginx-1.15.1
# 给 Nginx 添加 fastdfs-nginx-module 模块
./configure --add-module=/root/fastdfs-nginx-module/src
make && make install
```

#### fastdfs-nginx-module 和 FastDFS 配置文件修改
```
# 复制 FastDFS 的部分配置文件到 /etc/fdfs
cd /root/fastdfs-5.11/conf/
cp http.conf mime.types /etc/fdfs/
# 复制 fastdfs-nginx-module 源码中的配置文件到  /etc/fdfs 中
cp /root/fastdfs-nginx-module/src/mod_fastdfs.conf /etc/fdfs/
vi /etc/fdfs/mod_fastdfs.conf
```
> mod_fastdfs.conf 配置如下

```
# Tracker 服务器IP和端口修改
tracker_server=192.168.229.166:22122
# url 中是否包含 group 名称，改为 true，包含 group
url_have_group_name = true
# 配置 Storage 信息，修改 store_path0 的信息
store_path0=/home/fastdfs/storage
# 其它的一般默认即可，例如
base_path=/tmp
group_name=group1 
storage_server_port=23000 
store_path_count=1
```

#### 配置 Nginx 
```
vi /usr/local/nginx/conf/nginx.conf
```
> 在 server 中添加以下代码

```
# 配置为支持 group0-group9，以及 M00-M99，以便于以后扩容
# 本单机环境下其实配置为 ~/group1/M00 就可以了
location ~/group([0-9])/M([0-9])([0-9]) {
    ngx_fastdfs_module;
}
```

![4.png](https://i.loli.net/2018/07/11/5b46143b802d4.png)

#### 启动 Nginx 
```
# 启动 Nginx
/usr/local/nginx/sbin/nginx
# 重启 Nginx
/usr/local/nginx/sbin/nginx -s reload
# 停止 Nginx
/usr/local/nginx/sbin/nginx -s stop
```

#### 通过 HTTP 访问文件
> 根据 URL 访问之前上传的那张图片
```
http://192.168.229.166:80/group1/M00/00/00/wKjlpltF-K-AZQQsAABhhboA1Kk469.png
```

![5.png](https://i.loli.net/2018/07/11/5b4614ed9c16f.png)

> Yeah！成功啦！FastDFS 单机版到此就告一段落了。可以将 Nginx、Tracker 和 Storage 服务设置为开机自启。这里不探讨（先不了解了），交给运维的去做吧，哈哈哈

```
# 每次开机时，手动打开 Tracker 服务
/etc/init.d/fdfs_trackerd start
# 打开 Storage 服务
/etc/init.d/fdfs_storaged start
# 启动 Nginx
/usr/local/nginx/sbin/nginx
```

--- 

### 参考资料
> * [FastDFS FAQ（FastDFS 开发者 yuqing 总结的 FAQ）：http://bbs.chinaunix.net/thread-1920470-1-1.html](http://bbs.chinaunix.net/thread-1920470-1-1.html )
> * [FastDFS 配置文件详解(修订版1)：http://bbs.chinaunix.net/thread-1941456-1-1.html](http://bbs.chinaunix.net/thread-1941456-1-1.html)

--- 

### 后记
> FastDFS 单机版安装与配置到此也就结束了，不得不说 FastDFS 配置起来还是蛮折腾的。单机版也只是单纯配置来玩玩而已，用来使用一下 fastdfs-client-java，集群是一样的使用方式，只不过配置文件有所不同。有空再配置一下集群版。
> 
> 之后则会码一篇 fastdfs-client-java 的使用笔记（先挖个坑，以后再填）
> 
> 在实践中成长！
> 
> HochenChong
> 
> 2018-7-11
