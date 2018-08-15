package nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.nio.channels.Pipe.SinkChannel;
import java.nio.channels.Pipe.SourceChannel;

import org.junit.Test;

public class TestPipe {
	@Test
	public void testPipe() throws IOException {
		// 获取管道
		Pipe pipe = Pipe.open();
		
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
		
		// 将缓冲区中的数据写入管道
		SinkChannel sinkChannel = pipe.sink();
		byteBuffer.put("通过单向管道发送数据".getBytes());
		byteBuffer.flip();
		sinkChannel.write(byteBuffer);
		
		// 读取缓冲区中的数据
		SourceChannel sourceChannel = pipe.source();
		byteBuffer.flip();
		int len = sourceChannel.read(byteBuffer);
		System.out.println(new String(byteBuffer.array(), 0, len));
		
		// 关闭资源
		sourceChannel.close();
		sinkChannel.close();
	}
}
