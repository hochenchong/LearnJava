## CentOS  7 设置 Redis 开机自启

### 前言

> 在之前的文章（[CentOS 7 配置 Redis](https://blog.csdn.net/hochenchong/article/details/80084771)）我们已经学习了怎么安装配置 Redis，不过在那篇文章中并没有讲如何设置 Redis 开机自启。故这次来补充一下 Redis 开机自启的内容。



### 环境准备

> * CentOS 7 64 位
> * 按之前文章安装好的 Redis 4.0.9 （下面的路径为我的环境，实际操作以自己的为准，下同）
>   * Redis 源码压缩包解压路径为 /root/redis-4.0.9
>   * Redis 的安装路径在 /usr/local/redis
>   * Redis 配置文件 redis.conf 存放在 /usr/local/redis/bin 目录下，且修改为后端启动



### 复制 Redis 启动脚本

```
# 进入 Redis 源码压缩包，拷贝 Redis 官方提供的启动脚本
cd /root/redis-4.0.9/utils
# 复制启动脚本
cp redis_init_script /etc/init.d/redis
```

>  我们可以看到默认的脚本配置如下：

```
#!/bin/sh
#
# Simple Redis init.d script conceived to work on Linux systems
# as it does use of the /proc filesystem.

### BEGIN INIT INFO
# Provides:     redis_6379
# Default-Start:        2 3 4 5
# Default-Stop:         0 1 6
# Short-Description:    Redis data structure server
# Description:          Redis data structure server. See https://redis.io
### END INIT INFO

REDISPORT=6379
EXEC=/usr/local/bin/redis-server
CLIEXEC=/usr/local/bin/redis-cli

PIDFILE=/var/run/redis_${REDISPORT}.pid
CONF="/etc/redis/${REDISPORT}.conf"

case "$1" in
    start)
        if [ -f $PIDFILE ]
        then
                echo "$PIDFILE exists, process is already running or crashed"
        else
                echo "Starting Redis server..."
                $EXEC $CONF
        fi
        ;;
    stop)
        if [ ! -f $PIDFILE ]
        then
                echo "$PIDFILE does not exist, process is not running"
        else
                PID=$(cat $PIDFILE)
                echo "Stopping ..."
                $CLIEXEC -p $REDISPORT shutdown
                while [ -x /proc/${PID} ]
                do
                    echo "Waiting for Redis to shutdown ..."
                    sleep 1
                done
                echo "Redis stopped"
        fi
        ;;
    *)
        echo "Please use start or stop as first argument"
        ;;
esac
```

> 主要关注以下几行代码：

```
# Redis 端口，默认为 6379，故这里不去修改
REDISPORT=6379
# redis-server 以及 redis-cli 存放在地方，在 Redis 安装目录下的 bin 目录下，这里以个人环境为准
EXEC=/usr/local/bin/redis-server
CLIEXEC=/usr/local/bin/redis-cli

# 配置文件读取为 /etc/redis/6379.conf
PIDFILE=/var/run/redis_${REDISPORT}.pid
CONF="/etc/redis/${REDISPORT}.conf"
```



### 配置脚本

```
# 编辑脚本
vi /etc/init.d/redis
```

> 修改以下 redis-server 以及 redis-cli 存放在地方为自己 Redis 安装的目录这里为 /usr/local/redis，按 ```i``` 键进行编辑，修改后的脚本如下：

```
# Redis 端口，默认为 6379，故这里不去修改
REDISPORT=6379
# redis-server 以及 redis-cli 存放在地方，在 Redis 安装目录下的 bin 目录下，这里以个人环境为准
EXEC=/usr/local/redis/bin/redis-server
CLIEXEC=/usr/local/redis/bin/redis-cli

# 默认配置文件读取路径为 /etc/redis/6379.conf
# 可以直接修改配置文件的读取路径，亦或者如下复制一份新的配置文件在该路径下
PIDFILE=/var/run/redis_${REDISPORT}.pid
CONF="/etc/redis/${REDISPORT}.conf"
```

> 按 ```Esc``` 键，然后输入 ```:wq``` 然后按 ```Enter```(回车) 键保存文件
>
> 这里不去修改读取配置文件的路径，而是复制一份配置文件到 /etc/redis 文件夹下，这个文件夹默认是没有的，故要创建一下

```
# 创建 /etc/redis 文件夹
mkdir /etc/redis
# 复制之前配置 redis.conf 为 /etc/redis/6379.conf
cp /usr/local/redis/bin/redis.conf /etc/redis/6379.conf
```

### 设置为开机启动

>  运行以下命令设置 Redis 为开机自启：

```
chkconfig redis on
```

> 其他相关的命令

```
# 关闭开机自启
chkconfig redis off
# 启动 redis 服务
service redis start
# 关闭 redis 服务
service redis stop
```



### 可能存在的问题以及解决方案

#### 问题

> 以上设置，个人在 CentOS 7 64位下并没什么问题，可以正常开机自启 Redis。在有些环境下，运行以下命令时会报错：

```
chkconfig redis on
```

> 报错信息如下：

```
service redis does not support chkconfig
```

####  解决方案

> 编辑 /etc/init.d/redis 文件，在靠前面的注释中，添加以下两行注释，然后保存即可

```
# chkconfig: 2345 90 10

# description: Redis is a persistent key-value database
```



### 参考资料

> * [service redis does not support chkconfig的解决办法](https://my.oschina.net/maczhao/blog/322931)



### 后记

> 也可以自己创建脚本，```vi /etc/init.d/redis```，然后把上面脚本的内容复制进去，进行修改保存。不过需要注意的是，需要对脚本设置权限，按照官方提供的脚本来设置权限的话，命令为 ```chmod 755 /etc/init.d/redis```
>
> 在实践中成长
>
> HochenChong
>
> 2018-10-10