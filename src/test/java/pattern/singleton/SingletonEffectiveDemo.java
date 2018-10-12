package pattern.singleton;

import java.util.concurrent.CountDownLatch;

import org.junit.Test;

/**
 * @Description: 单例模式类性能测试
 * 		
 * @author: HochenChong
 * @date: 2018-10-12
 * @version v0.1
 */

public class SingletonEffectiveDemo {

	/*
	 * 结论：
	 * 	单例对象占用资源较少，不需要延迟加载时：
	 * 		枚举类 > 饿汉式
	 * 	单例对象占用资源较大，需要延迟加载：
	 * 		静态内部类式 > 双重检测锁 > 懒汉式
	 */
	@Test
	public void testEffective() throws InterruptedException {
		long start = System.currentTimeMillis();
		
		int threadNum = 10;
		
		// 同步辅助类
		CountDownLatch countDownLatch = new CountDownLatch(threadNum);
		
		for (int i = 0; i < threadNum; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					for (int i = 0; i < 1000000; i++) {
						// Object object = SingletonDemo01.getInstance();
						// Object object = SingletonDemo02.getInstance();
						// Object object = SingletonDemo03.getInstance();
						// Object object = SingletonDemo04.getInstance();
						Object object = SingletonDemo05.INSTANCE;
					}
					// 该线程任务执行完毕后，计数器减 1
					countDownLatch.countDown();
				}
			}).start();
		}
		
		// 阻塞当前线程，直到计数器为 0 时，继续执行当前线程
		countDownLatch.await();
		
		long end = System.currentTimeMillis();
		
		System.out.println("总耗时为：" + (end - start));
	}

}
