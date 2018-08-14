## Nginx 使用 Lua 模块校验 Token
### 前言
> 最近在折腾 FastDFS 系统，用 FastDFS 来存放一些小文件（在之前学习的一个商城项目中，用来做图片服务器，存放商品的图片）。当然，一般情况下，别人都是可以直接访问的。不过后来又想，能不能添加一个验证，对用户的权限进行校验是否可以访问。
> 
> 尝试过使用 FastDFS 内置防盗链功能，不过这样子每台 FastDFS 服务器都需要配置一下。于是乎在想，能不能在统一入口的 Nginx 服务器上进行校验了。这样子是不是省事一些，而且之后也可以用在其它地方。
> 
> 于是乎，就发现了一个 Nginx 的第三方模块 —— lua-nginx-module。将 lua 语言嵌入到 Nginx 配置中，从而增强了 Nginx 的能力（即使从来未接触过 Lua，不过多看看别人的代码，然后不断地实践，总能把问题解决的）。于是乎，就使用 Nginx + lua-nginx-module 来验证 Token 信息。

--- 

### 环境说明
#### 系统环境
> * 虚拟机工具 ：VMware Workstations 14 Pro
> * 操作系统 ：CentOS 7 64位
> * IP 地址：192.168.229.165

#### 相关安装包
> * LuaJIT-2.0.5.tar.gz （下载地址：[http://luajit.org/download.html](http://luajit.org/download.html)）
> * lua-nginx-module-0.10.13.tar.gz （下载地址：[https://github.com/openresty/lua-nginx-module/releases](https://github.com/openresty/lua-nginx-module/releases)）
> * nginx-1.15.1.tar.gz （下载地址：[http://nginx.org/en/download.html](http://nginx.org/en/download.html)）
> 
> **将以上安装包下载后，拷贝到系统 /root 目录下**

---

### Nginx 安装与配置
#### Nginx 及模块安装
```
# 安装 Nginx 依赖环境
yum install gcc gcc-c++ make automake autoconf libtool pcre* zlib openssl openssl-devel
# 解压安装包
cd /root
tar zxvf LuaJIT-2.0.5.tar.gz
tar zxvf lua-nginx-module-0.10.13.tar.gz
tar zxvf nginx-1.15.1.tar.gz
# LuaJIT 安装
cd /root/LuaJIT-2.0.5
make && make install
# Nginx 添加 lua_nginx_module 模块安装
cd /root/nginx-1.15.1
./configure --add-module=../lua-nginx-module-0.10.13/
make && make install
# 查看 Nginx 是否安装成功
/usr/local/nginx/sbin/nginx -v
# nginx version: nginx/1.15.1 则表示安装成功
# 可能出现以下错误，则需要建立软链接：/usr/local/nginx/sbin/nginx: error while loading shared libraries: libluajit-5.1.so.2: cannot open shared object file: No such file or directory
ln -s /usr/local/lib/libluajit-5.1.so.2 /lib64/libluajit-5.1.so.2
```
#### 配置 Nginx
```
vi /usr/local/nginx/conf/nginx.conf
```
> 在 server 中添加以下代码

```
# 设置转发的 url
location @fastDFS {
    proxy_pass http://192.168.229.166:80;
}
location ~/group([0-9])/M([0-9])([0-9]) {
    access_by_lua '
        -- 获取请求路径，不包括参数。例如：/group1/M00/00/00/wKjlpltF-K-AZQQsAABhhboA1Kk469.png
        local uri = ngx.var.uri;
        -- 获取请求参数
        local args = ngx.req.get_uri_args();
        -- 获取请求参数中时间戳信息，传入的是毫秒
        local ts  = args["ts"];
        -- 获取请求参数中 token 信息
        local token1 = args["token"];
        
        -- 更新系统缓存时间戳
        ngx.update_time();
        -- 获取当前服务器系统时间，ngx.time() 获取的是秒
        local getTime = ngx.time() * 1000;
        -- 计算时间差
        local diffTime = tonumber(ts) - getTime;
        -- md5 加盐加密
        local token2 = ngx.md5(tostring(uri) .. "salt" .. tostring(ts));
        
        -- 判断时间是否有效
        if (tonumber(diffTime) > 0) then
            -- 校验 token 是否相等
            if token1 == token2 then
                -- 校验通过则转发请求
                ngx.exec("@fastDFS");
            end
        end
    ';
}
```
> 启动 Nginx：```/usr/local/nginx/sbin/nginx```

--- 

### Java 代码生成可访问的 Url 链接
```
@Test
public void getUrl() {
    // 获取当前系统所在服务器的时间，毫秒单位
    // 注意，当前系统不一定是 Nginx 所在服务器
    long milliseconds = System.currentTimeMillis();
    // 添加有效期时间，假设该链接有效期为 1 天，即 86400000
    // 切记设置为 Long 类型再进行运算，避免超出 int 类型的范围
    // 自己测试时，为了方便，可以设置为 1 分钟之类的
    System.out.println("当前系统时间：" + milliseconds);
    milliseconds += 1L * 24 * 60 * 60 * 1000;
    // milliseconds += 60L * 1000;

    // 计算 token 信息
    // 请求的资源路径
    String requestResources = "/group1/M00/00/00/wKjlpltF-K-AZQQsAABhhboA1Kk469.png";
    // “盐” 值，和 Nginx 服务器上的保持一致即可
    String salt = "salt";
    // 加密前的字符串：请求的资源路径 + “盐” 值 + 时间戳
    String beforeEncryptionString = requestResources + salt + milliseconds;
    // 这里使用 Spring 提供的 md5 加密工具进行 md5 加密
    String token = DigestUtils.md5DigestAsHex(beforeEncryptionString.getBytes());
    String url = requestResources + "?ts=" + milliseconds + "&token=" + token;

    System.out.println("请求的 url 为：");
    System.out.println(url);
}
/*
运行结果：
---------------------------------------------------------------------
当前系统时间：1531659300558
请求的 url 为：
/group1/M00/00/00/wKjlpltF-K-AZQQsAABhhboA1Kk469.png?ts=1531745700558&token=c78bc4365723b52916a99abc628a0d25
 */
```
#### 访问演示
> 无参数访问如下
![Nginx 使用 Lua 模块校验 Token（1）.png](https://i.loli.net/2018/07/15/5b4b4fc8649c5.png)
---

> 带有效的时间戳与 token 访问
![Nginx 使用 Lua 模块校验 Token（2）.png](https://i.loli.net/2018/07/15/5b4b50037f081.png)
---

> 带已过期的时间戳与 token 访问
![Nginx 使用 Lua 模块校验 Token（3）.png](https://i.loli.net/2018/07/15/5b4b5126414cf.png)

---

### 可能存在的问题 —— 系统时间不一致
> 请求 Nginx 服务器时，就会判断时间戳是否有效，token 值是否正确，也可以根据自己情况，进行修改。例如修改 “盐” 值，字符串的拼接方式，加密方式等等。
> 
> 但是，上面的 Java 代码注释也说了，生成 Url 的系统所在服务器，不一定就是安装 Nginx 的服务器，故可能两者时间不一致。从而导致链接访问失效。
> 
> **解决方法：**从 Nginx 服务器中获取时间。下面说一下我的实现方式。

#### 配置 Nginx
```
vi /usr/local/nginx/conf/nginx.conf
```
> 在 server 中添加以下代码
```
# 获取当前系统时间，并返回
location /getTime {
    default_type text/html;
    content_by_lua '
        ngx.say(ngx.time() * 1000);
    ';
}
```
> 重启 Nginx 服务后（重启命令：```/usr/local/nginx/sbin/nginx -s reload```），访问 ```http://192.168.229.165/getTime```，页面返回时间戳。如下图：
![Nginx 使用 Lua 模块校验 Token（4）.png](https://i.loli.net/2018/07/15/5b4b50a017116.png)

#### Java 代码修改
```
package com.hochenchong.learn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import org.junit.Test;
import org.springframework.util.DigestUtils;

/**
 * @description 生成带有效期与 token 的 URL 测试
 * @author HochenChong
 * @date 2018-7-15
 * @version 0.1
 */

public class NginxTest {
	@Test
	public void test() {
		// 获取 Nginx 服务器上的系统时间
		String requestUrl = "http://192.168.229.165/getTime";
		long systemTime = Long.parseLong(getURLContent(requestUrl));
		System.out.println("Nginx 服务器上系统时间：" + systemTime);
		
		// 请求的资源路径
		String requestResources = "/group1/M00/00/00/wKjlpltF-K-AZQQsAABhhboA1Kk469.png";
		String url = getUrl(requestResources, systemTime);
		System.out.println("请求的 url 为：");
		System.out.println("192.168.229.165" + url);
	}

	/**
	 * 获取带时间戳与 token 的 url
	 * @param requestResources 请求的资源路径，不包括 IP 地址与端口，开头有 /，例如 /group1/M00/00/00/wKjlpltF-K-AZQQsAABhhboA1Kk469.png
	 * @param systemTime 系统时间
	 * @return 返回请求的 url 地址，包括有效期与 token
	 */
	public static String getUrl(String requestResources, long systemTime) {
		// 添加有效期时间，假设该链接有效期为 1 天，即 86400000
        // 计算毫秒时，切记转换为 Long 类型进行运算，避免超出 int 类型的范围
		// 有效期，单位：毫秒
		// 自己测试时，为了方便，可以设置为 1 分钟之类的
		long milliseconds = systemTime + 1L * 24 * 60 * 60 * 1000;
		// long milliseconds = systemTime + 60L * 1000;
		
		// 计算 token 信息
		// “盐” 值，和 Nginx 服务器上的保持一致即可
		String salt = "salt";
		// 加密前的字符串：请求的资源路径 + “盐” 值 + 时间戳
		String beforeEncryptionString = requestResources + salt + milliseconds;
		// 这里使用 Spring 提供的 md5 加密工具进行 md5 加密
		String token = DigestUtils.md5DigestAsHex(beforeEncryptionString.getBytes());
		String url = requestResources + "?ts=" + milliseconds + "&token=" + token;
		
		return url;
	}
    
    /**
	 * 获取请求 url 返回的文本
	 * @param requestUrl 请求的 url
	 * @return
	 */
	public static String getURLContent(String requestUrl) {
	    URL url = null;
	    BufferedReader in = null;
	    StringBuffer sb = new StringBuffer(); 
	    
	    try {
	    	url = new URL(requestUrl);     
	    	in = new BufferedReader(new InputStreamReader(url.openStream())); 
	    	String str = null;  
			while ((str = in.readLine()) != null) {
				sb.append(str);     
			} 
		} catch (Exception e) {
			e.printStackTrace();
		} finally{   
			// 关闭资源
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        return sb.toString();
	}
}
```
> 运行结果
```
Nginx 服务器上系统时间：1531661817000
请求的 url 为：
192.168.229.165/group1/M00/00/00/wKjlpltF-K-AZQQsAABhhboA1Kk469.png?ts=1531748217000&token=8950061a099c83e59316afb8e3319c8b
```

---

### 后记
> 配置完 Nginx 环境后，也可以直接自己手动拼接字符串，再到 [在线 MD5加密/解密](http://tool.chinaz.com/tools/md5.aspx) 网站进行 md5 加密尝试一番。
> 
> 有些东西，虽然不完全没接触过（例如上面的 Lua），需要用到时，就去看别人怎么写的，自己尝试的写，有啥问题就去查一下，慢慢地就能把问题解决了。
> 
> 在实践中成长！
> 
> HochenChong
> 
> 2018-7-15