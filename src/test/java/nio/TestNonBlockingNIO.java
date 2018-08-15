package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

import org.junit.Test;

/*
 * 一、使用 NIO 完成网络通信的三个核心：
 * 
 * 1. 通道（Channel）：负责连接
 * 	   java.nio.channels.Channel 接口：
 * 			|--SelectableChannel
 * 				|--SocketChannel
 * 				|--ServerSocketChannel
 * 				|--DatagramChannel
 * 
 * 				|--Pipe.SinkChannel
 * 				|--Pipe.SourceChannel
 * 
 * 2. 缓冲区（Buffer）：负责数据的存取
 * 
 * 3. 选择器（Selector）：是 SelectableChannel 的多路复用器。用于监控 SelectableChannel 的 IO 状况
 * 
 */

public class TestNonBlockingNIO {
	// 客户端
	@Test
	public void client() throws IOException {
		// 获取通道
		SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));

		// 切换非阻塞模式
		socketChannel.configureBlocking(false);

		// 分配指定大小的缓冲区
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

		// 发送数据给服务端
		Scanner scanner = new Scanner(System.in);

		while (scanner.hasNext()) {
			String str = scanner.next();
			byteBuffer.put((new Date().toString() + "\n" + str).getBytes());
			byteBuffer.flip();
			socketChannel.write(byteBuffer);
			byteBuffer.clear();
		}

		// 关闭通道
		socketChannel.close();
	}

	// 服务端
	@Test
	public void server() throws IOException {
		// 获取通道
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

		// 切换非阻塞模式
		serverSocketChannel.configureBlocking(false);

		// 绑定连接
		serverSocketChannel.bind(new InetSocketAddress(9898));

		// 获取选择器
		Selector selector = Selector.open();

		// 将通道注册到选择器上, 并且指定 “监听接收事件”
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

		// 轮询式的获取选择器上已经 “准备就绪” 的事件
		while (selector.select() > 0) {

			// 获取当前选择器中所有注册的 “选择键(已就绪的监听事件)”
			Iterator<SelectionKey> it = selector.selectedKeys().iterator();

			while (it.hasNext()) {
				// 获取准备 “就绪” 的是事件
				SelectionKey selectionKey = it.next();

				// 判断具体是什么事件准备就绪
				if (selectionKey.isAcceptable()) {
					// 若 “接收就绪”，获取客户端连接
					SocketChannel socketChannel = serverSocketChannel.accept();

					// 切换非阻塞模式
					socketChannel.configureBlocking(false);

					// 将该通道注册到选择器上
					socketChannel.register(selector, SelectionKey.OP_READ);
				} else if (selectionKey.isReadable()) {
					// 获取当前选择器上 “读就绪” 状态的通道
					SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

					// 读取数据
					ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

					int len = 0;
					while ((len = socketChannel.read(byteBuffer)) > 0) {
						byteBuffer.flip();
						System.out.println(new String(byteBuffer.array(), 0, len));
						byteBuffer.clear();
					}
					socketChannel.close();
				}

				// 取消选择键 SelectionKey
				it.remove();
			}
		}
		selector.close();
		serverSocketChannel.close();
	}
}
