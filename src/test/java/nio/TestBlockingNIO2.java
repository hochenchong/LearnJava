package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.junit.Test;

public class TestBlockingNIO2 {
	@Test
	public void client() throws IOException {
		// 获取通道
		SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));
		
		FileChannel inChannel = FileChannel.open(Paths.get("1.txt"), StandardOpenOption.READ);
		
		// 设置缓冲区大小
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
		
		// 读取本地文件，并发送给服务端
		while (inChannel.read(byteBuffer) != -1) {
			byteBuffer.flip();
			socketChannel.write(byteBuffer);
			byteBuffer.clear();
		}
		
		// 告诉服务端发送完成
		socketChannel.shutdownOutput();
		
		// 接收服务器的反馈
		int len = 0;
		while ((len = socketChannel.read(byteBuffer)) != -1) {
			byteBuffer.flip();
			System.out.println(new String(byteBuffer.array(), 0, len));
		}
		
		// 关闭通道
		inChannel.close();
		socketChannel.close();
	}
	
	@Test
	public void Server() throws IOException {
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		// 绑定端口
		serverSocketChannel.bind(new InetSocketAddress(9898));
		
		FileChannel outChannel = FileChannel.open(Paths.get("2.txt"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
		
		// 设置缓冲区大小
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
		
		// 监听端口
		SocketChannel socketChannel = serverSocketChannel.accept();
		
		// 接收数据并写到本地
		while (socketChannel.read(byteBuffer) != -1) {
			byteBuffer.flip();
			outChannel.write(byteBuffer);
			byteBuffer.clear();
		}
		
		// 接收完毕后，向客户端发送反馈
		byteBuffer.put("服务器接收数据成功".getBytes());
		byteBuffer.flip();
		socketChannel.write(byteBuffer);
		
		// 关闭通道
		socketChannel.close();
		outChannel.close();
		serverSocketChannel.close();
	}
}
