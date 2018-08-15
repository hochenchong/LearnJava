package nio;

import java.nio.ByteBuffer;

import org.junit.Test;

public class TestBuffer {
	
	@Test
	public void test01() {
		// 创建一个 ByteBuffer 对象
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
		
		// 查看当前位置，容量，界限
		System.out.println(byteBuffer.position());
		System.out.println(byteBuffer.limit());
		System.out.println(byteBuffer.capacity());
		
		// 写入数据
		byteBuffer.put("abc".getBytes());
		System.out.println("----------- 往 ByteBuffer 放入 abc 字符串 -----------");
		System.out.println(byteBuffer.position());
		System.out.println(byteBuffer.limit());
		System.out.println(byteBuffer.capacity());
		
		// 切换成读取模式
		byteBuffer.flip();
		System.out.println("----------- 切换成读取模式 -----------");
		System.out.println(byteBuffer.position());
		System.out.println(byteBuffer.limit());
		System.out.println(byteBuffer.capacity());
		
		// 读取数据
		byte[] dst = new byte[byteBuffer.limit()];
		byteBuffer.get(dst);
		System.out.println("----------- get 方法使用 -----------");
		System.out.println(byteBuffer.position());
		System.out.println(byteBuffer.limit());
		System.out.println(byteBuffer.capacity());
		
		// 重新读数据
		byteBuffer.rewind();
		System.out.println("----------- rewind 方法使用 -----------");
		System.out.println(byteBuffer.position());
		System.out.println(byteBuffer.limit());
		System.out.println(byteBuffer.capacity());
		
		// 清空缓冲区，只是标记为没有而已，并不会真正的清除数据
		byteBuffer.clear();
		System.out.println("----------- clear 方法使用 -----------");
		System.out.println(byteBuffer.position());
		System.out.println(byteBuffer.limit());
		System.out.println(byteBuffer.capacity());
		byteBuffer.put("d".getBytes());
		System.out.println(byteBuffer.position());
		System.out.println(byteBuffer.limit());
		System.out.println(byteBuffer.capacity());
		System.out.println((char)byteBuffer.get(0));
	}
	
	@Test
	public void test02() {
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
		byteBuffer.put("abcdefgh".getBytes());
		
		// 从 Buffer 中读取数据
		byteBuffer.flip();
		byte[] dst = new byte[byteBuffer.limit()];
		byteBuffer.get(dst, 0, 2);
		System.out.println(new String(dst));
		System.out.println(byteBuffer.position());
		
		// mark 方法的使用，标记当前位置
		byteBuffer.mark();
		byteBuffer.get(dst, 2, 2);
		System.out.println(new String(dst));
		System.out.println(byteBuffer.position());
		// reset 方法回到标记的位置
		byteBuffer.reset();
		System.out.println(byteBuffer.position());
		
		System.out.println("--------- 查看是否还有数据 ---------");
		// 查看是否还有数据
		if (byteBuffer.hasRemaining()) {
			// Returns the number of elements between the current position and the limit.
			System.out.println(byteBuffer.remaining());
			System.out.println(byteBuffer.position());
			System.out.println(byteBuffer.limit());
		}
	}
	
	/**
	 * 直接缓冲区
	 * 非直接缓冲区
	 */
	@Test
	public void test03() {
		System.out.println("创建一个直接缓冲区对象");
		// 创建一个直接缓冲区对象
		ByteBuffer allocateDirect = ByteBuffer.allocateDirect(1024);
		// 判断一个缓冲区是否是直接缓冲区对象
		System.out.println(allocateDirect.isDirect());
		
		System.out.println("创建一个非直接缓冲区对象");
		ByteBuffer allocate = ByteBuffer.allocate(1024);
		System.out.println(allocate.isDirect());
	}
}
