package nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;

public class TestChannel {
	
	// 利用通道完成文件的复制
	@Test
	public void test01() throws IOException {
		FileInputStream fis = new FileInputStream("1.jpg");
		FileOutputStream fos = new FileOutputStream("2.jpg");
		
		// 获取通道
		FileChannel inChannel = fis.getChannel();
		FileChannel outChannel = fos.getChannel();

		// 分配指定大小的缓冲区
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
		
		// 将通道中的数据存入缓冲区中
		while(inChannel.read(byteBuffer) != -1) {
			// 切换成读取模式
			byteBuffer.flip();
			// 将缓冲区的数据写入通道中
			outChannel.write(byteBuffer);
			// 清空缓冲区
			byteBuffer.clear();
		}
		
		// 关闭资源
		outChannel.close();
		inChannel.close();
		fos.close();
		fis.close();
	}
	
	// 使用直接缓冲区完成文件的复制（内存映射文件）
	@Test
	public void test02() throws IOException {
		FileChannel inChannel = FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);
		FileChannel outChannel = FileChannel.open(Paths.get("3.jpg"), StandardOpenOption.READ,StandardOpenOption.WRITE,StandardOpenOption.CREATE);
		
		// 内存映射文件
		MappedByteBuffer inMappedByteBuffer = inChannel.map(MapMode.READ_ONLY, 0, inChannel.size());
		MappedByteBuffer outMappedByteBuffer = outChannel.map(MapMode.READ_WRITE, 0, inChannel.size());
		
		// 直接对缓冲区进行数据的读写操作
		byte[] dst = new byte[inMappedByteBuffer.limit()];
		inMappedByteBuffer.get(dst);
		outMappedByteBuffer.put(dst);
		
		outChannel.close();
		inChannel.close();
	}
	
	// 通道之间数据传输
	@Test
	public void test03() throws IOException {
		FileChannel inChannel = FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);
		FileChannel outChannel = FileChannel.open(Paths.get("4.jpg"), StandardOpenOption.READ,StandardOpenOption.WRITE,StandardOpenOption.CREATE);
		
		// inChannel.transferTo(0, inChannel.size(), outChannel);
		outChannel.transferFrom(inChannel, 0, inChannel.size());
		
		outChannel.close();
		inChannel.close();
	}
	
	// 分散读取与聚集写入
	@Test
	public void test04() throws IOException {
		RandomAccessFile raf1 = new RandomAccessFile("1.txt", "rw");
		
		// 获取通道
		FileChannel readChannel = raf1.getChannel();
		
		// 分配指定大小的缓冲区
		ByteBuffer byteBuffer1 = ByteBuffer.allocate(1024);
		ByteBuffer byteBuffer2 = ByteBuffer.allocate(1024);		

		// 分散读取
		ByteBuffer[] dsts = new ByteBuffer[] {byteBuffer1, byteBuffer2};
		readChannel.read(dsts);
		
		for (ByteBuffer byteBuffer : dsts) {
			byteBuffer.flip();
		}
		
		System.out.println(new String(dsts[0].array(), 0, dsts[0].limit()));
		System.out.println("----------------------------------------------");
		System.out.println(new String(dsts[1].array(), 0, dsts[1].limit()));
		System.out.println("---------------- 读取结束 -----------------------");
		
		// 聚集写入
		RandomAccessFile raf2 = new RandomAccessFile("2.txt", "rw");
		FileChannel writeChannel = raf2.getChannel();
		writeChannel.write(dsts);
	}
	
	// Java 中支持的字符集
	@Test
	public void testGetCharsets() {
		Map<String,Charset> availableCharsets = Charset.availableCharsets();
		
		// map 转换为 set
		Set<Entry<String,Charset>> entrySet = availableCharsets.entrySet();
		
		for (Entry<String, Charset> entry : entrySet) {
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}
	}
	
	// 编码与解码
	@Test
	public void test05() throws IOException {
		Charset charset = Charset.forName("UTF-8");
		
		// 获取编码器
		CharsetEncoder charsetEncoder = charset.newEncoder();
		
		// 获取解码器
		CharsetDecoder charsetDecoder = charset.newDecoder();
		
		CharBuffer charBuffer = CharBuffer.allocate(1024);
		charBuffer.put("编码与解码测试");
		charBuffer.flip();
		
		// 编码
		ByteBuffer byteBuffer = charsetEncoder.encode(charBuffer);
		for (int i = 0; i < byteBuffer.limit(); i++) {
			System.out.println(byteBuffer.get());
		}
		
		byteBuffer.flip();
		// 解码
		CharBuffer decodeCharBuffer = charsetDecoder.decode(byteBuffer);
		System.out.println(decodeCharBuffer.toString());
		
		// 如果解码与编码使用的字符集不同
		Charset charset2 = Charset.forName("GBK");
		// 重新读取
		byteBuffer.rewind();
		CharBuffer charBuffer2 = charset2.decode(byteBuffer);
		System.out.println(charBuffer2.toString());
	}
}
