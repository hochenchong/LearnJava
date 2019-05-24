## FastDFS java client 简单使用

### 前言

> 前面已经讲了如何搭建 FastDFS 单机版以及集群版。现在来讲讲使用 **fastdfs-client-java** 来对 FastDFS 服务器进行 CRUD 的操作。使用起来相当的简单。

---

### 环境

> * Eclipse 4.7.3
> * fastdfs-client-java（这里使用的是 1.27-SNAPSHOT）：[https://github.com/happyfish100/fastdfs-client-java](https://github.com/happyfish100/fastdfs-client-java)

---

### 使用 fastdfs-client-java

#### pom.xml 引入 fastdfs-client-java 依赖

> 使用 fastdfs-client-java 时，由于 Maven 中央仓库没有收集这个，故我们需要从上面的链接将 fastdfs-client-java 项目下载下来，然后安装到本地的 Maven 仓库中来使用。

```xml
<dependency>
    <groupId>org.csource</groupId>
    <artifactId>fastdfs-client-java</artifactId>
    <version>1.27-SNAPSHOT</version>
</dependency>
```

### 配置 FastDFS 的配置文件

> 在 src/main/resources 资源文件夹下新建配置文件：fdfs_client.conf（或使用其它文件名 xxx_yyy.conf）
>
> 优先从绝对路径读取，没有找到时，才查找项目中的 classpath 路径。

配置如下：

```
connect_timeout = 10
network_timeout = 30
charset = UTF-8
http.tracker_http_port = 80
http.anti_steal_token = no
http.secret_key = FastDFS1234567890

tracker_server = 192.168.229.166:22122
```

> 多个 tracker 服务器就配置多条
>
> tracker_server 是必须配置的，其它配置为可选

也可以配置为 fastdfs-client.properties （或使用其它文件名 xxx-yyy.properties）

> 具体看 fastdfs-client-java 中的 README.md 中的说明。[https://github.com/happyfish100/fastdfs-client-java/blob/master/README.md](https://github.com/happyfish100/fastdfs-client-java/blob/master/README.md)
>
> 这里以在 src/main/resources 下配置 fdfs_client.conf 为例

#### CRUD 操作

```java
package com.hochenchong.learn;

import java.io.FileOutputStream;
import java.io.OutputStream;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.FileInfo;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

/**
 * @description 使用 fastdfs-client-java 对 FastDFS 进行 CRUD 操作
 * @author HochenChong
 * @date 2018-7-18
 * @version 0.1
 */

public class FastFDSCRUD {
	/**
	 * 上传文件
	 * @throws Exception
	 */
	@Test
	public void testUploadFile() throws Exception {
		// 使用全局对象加载配置文件
        ClientGlobal.init("conf/fastdfs_client.conf");
        // System.out.println("ClientGlobal.configInfo(): " + ClientGlobal.configInfo());
        
        // 创建 TrackerClient 对象，直接 new 就可以了
        TrackerClient trackerClient = new TrackerClient();
        // 通过 TrackerClient 获得一个 TrackerServer 对象
        TrackerServer trackerServer = trackerClient.getConnection();
        // 创建一个 StorageServer 的引用，可以是 null
        StorageServer storageServer = null;
        // 创建一个 StorageClient，参数需要 TrackerServer，StorageServer。相当于拿到服务器的 IP 和端口号
        StorageClient storageClient = new StorageClient(trackerServer, storageServer);
        // 使用 StorageClient 上传文件，返回保存信息。upload_file 方法中的三个参数，分别是文件路径，文件后缀名以及元数据
		// 扩展名不带“.”
		String[] strings = storageClient.upload_file("C:\\Users\\HochenChong\\Desktop\\FastDFS\\images\\test.png", "png", null);
		System.out.println("http://192.168.229.166:80/" + strings[0] + "/" + strings[1]);
	}
	
	/**
	 * 查询文件
	 * @throws Exception
	 */
	@Test
	public void testGetFileInfo() throws Exception {
		ClientGlobal.init("fdfs_client.conf");

		TrackerClient trackerClient = new TrackerClient();
		TrackerServer trackerServer = trackerClient.getConnection();
		StorageServer storageServer = null;
		StorageClient storageClient = new StorageClient(trackerServer, storageServer);
		
         // 使用 StorageClient 对象获取文件信息。get_file_info 方法中有两个参数，分别是组名和文件名
		// 例如：group1/M00/00/00/wKjlpltF-K-AZQQsAABhhboA1Kk469.png
		FileInfo file_info = storageClient.get_file_info("group1", "M00/00/00/wKjlpltF-K-AZQQsAABhhboA1Kk469.png");
		System.out.println(file_info.getCrc32());
		System.out.println(file_info.getFileSize());
		System.out.println(file_info.getSourceIpAddr());
		System.out.println(file_info.getCreateTimestamp());
	}
	
	/**
	 * 下载文件
	 * @throws Exception
	 */
	@Test
	public void testDownloadFile() throws Exception {
		ClientGlobal.init("fdfs_client.conf");

		TrackerClient trackerClient = new TrackerClient();
		TrackerServer trackerServer = trackerClient.getConnection();
		StorageServer storageServer = null;
		StorageClient storageClient = new StorageClient(trackerServer, storageServer);
 
		// 使用StorageClient对象下载文件
         // 例如：group1/M00/00/00/wKjlpltF-K-AZQQsAABhhboA1Kk469.png
		byte[] download_file = storageClient.download_file("group1", "M00/00/00/wKjlpltF-K-AZQQsAABhhboA1Kk469.png");
		OutputStream os = new FileOutputStream("C:\\Users\\HochenChong\\Desktop\\FastDFS\\images\\test1.png");
		os.write(download_file);
		os.flush();
		os.close();
	}
	
	/**
	 * 删除文件
	 * @throws Exception
	 */
	@Test
	public void testDelete() throws Exception {
		ClientGlobal.init("fdfs_client.conf");
		
		TrackerClient trackerClient = new TrackerClient();
		TrackerServer trackerServer = trackerClient.getConnection();
		StorageServer storageServer = null;
		StorageClient storageClient = new StorageClient(trackerServer, storageServer);
        
		// 使用StorageClient对象删除文件
         // 例如：group1/M00/00/00/wKjlpltF-K-AZQQsAABhhboA1Kk469.png
		storageClient.delete_file("group1", "M00/00/00/wKjlpltF-K-AZQQsAABhhboA1Kk469.png");
		System.out.println("group1/M00/00/00/wKjlpltF-K-AZQQsAABhhboA1Kk469.png 已删除");
	}
}

```

> 从上面的代码可以看出，增删改查的操作其实都很简单。都是先加载配置文件，然后创建一个 TrackerClient 对象，建立连接后获取 TrackerServer 对象，然后创建一个 StorageServer 的引用，根据 TrackerServer 和 StorageServer 创建一个 StorageClient 对象，用这个对象来增删改查。
>
> 顺带一提，YuQing（也就是 FastDFS 的作者），提供了 StorageClient 和 StorageClient1 让大家使用。StorageClient 是 1.24 版本新增的，在这个类的介绍中，原话是这样子的 ```Storage client for 2 fields file id: group name and filename```，所以这个类的 CRUD 操作，基本上涉及到两个属性，一个是组名，一个是文件名。
>
> 而 StorageClient1（1.21 版本新增的），是这样子介绍了：```Storage client for 1 field file id: combined group name and filename```。这个类的基本操作涉及到的是一个属性，也就是将组名与文件名拼接起来而已。
>
> 至于选用哪个，就看自己觉得哪个方便就使用哪个即可。
>
> 创建 StorageClient 对象前面的内容都是相同的，故我们也可以提取出来。这里就不多说了。

---

### 扩展资料

> * [FastDFS分布文件系统Java客户端使用](https://blog.csdn.net/xyang81/article/details/52847311)



---

### 后记

> 这个工具使用起来相当的简单，可以根据自己的需求，提取出相应的工具类来使用。
>
> 在实践中成长！
>
> HochenChong
>
> 2018-7-18