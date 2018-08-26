package jvm;

/**
 * testGC() 方法执行后，objA 和 objB 会不会被 GC 呢？
 * @date 2018-06-25
 */

public class ReferenceCountingGC {
	public Object instance = null;
	
	private static final int _1MB = 1024 * 1024;
	
	/**
	 * 这个成员属性的唯一意义就是占点内存，以便能在 GC 日志中看清楚是否被回收过
	 */
	private byte[] bigSize = new byte[2 * _1MB];
	
	public static void testGC() {
		ReferenceCountingGC objA = new ReferenceCountingGC();
		ReferenceCountingGC objB = new ReferenceCountingGC();
		objA.instance = objB;
		objB.instance = objA;
		
		// 地址引用设置为空，而堆中的对象仍然相互引用
		objA = null;
		objB = null;
		
		// 假设在这行发生 GC，objA 和 objB 是否能被回收？
		System.gc();
	}
}

/*
 * 运行结果： 

 * 结果分析： 虚拟机并没有因为这两个对象互相引用就不回收它们
 * 		   侧面说明了虚拟机并不是通过引用计数算法来判断对象是否存活的
 */