package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Scanner;

import org.junit.Test;

public class TestNonBlockingNIO2 {

	@Test
	public void send() throws IOException {
		DatagramChannel datagramChannel = DatagramChannel.open();
		
		// 切换成非阻塞状态
		datagramChannel.configureBlocking(false);
		
		// 分配指定缓冲区大小
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
		
		Scanner scanner = new Scanner(System.in);
		
		while (scanner.hasNext()) {
			String line = scanner.nextLine();
			byteBuffer.put(line.getBytes());
			byteBuffer.flip();
			datagramChannel.send(byteBuffer, new InetSocketAddress("127.0.0.1", 9898));
			byteBuffer.clear();
		}
		
		datagramChannel.close();
	}
	
	@Test
	public void receive() throws IOException {
		DatagramChannel datagramChannel = DatagramChannel.open();
		
		// 切换成非阻塞状态
		datagramChannel.configureBlocking(false);
		
		// 绑定端口
		datagramChannel.bind(new InetSocketAddress(9898));
		
		// 获取选择器
		Selector selector = Selector.open();
		
		// 将通道注册到选择器上，并且指定 “监听接收事件”
		datagramChannel.register(selector, SelectionKey.OP_READ);
		
		while (selector.select() > 0) {
			Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
			
			while (iterator.hasNext()) {
				SelectionKey selectionKey = iterator.next();
				
				if (selectionKey.isReadable()) {
					ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
					
					datagramChannel.receive(byteBuffer);
					byteBuffer.flip();
					System.out.println(new String(byteBuffer.array(), 0, byteBuffer.limit()));
					byteBuffer.clear();
				}
			}
			
			iterator.remove();
		}
		
		selector.close();
		datagramChannel.close();
	}
}
