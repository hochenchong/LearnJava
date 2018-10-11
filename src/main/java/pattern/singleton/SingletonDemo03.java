package pattern.singleton;

/**
 * @Description: 双重检测锁实现单例模式 
 * 		线程安全，延迟加载（lazy load），资源利用率高 
 * 		存在的问题：
 * 			存在反射或者反序列化创建多个对象的漏洞
 * @author HochenChong
 * @date 2018-10-11
 * @version v0.1
 */

public class SingletonDemo03 {
	// 私有属性
	// volatile 禁止指令重排序
	private static volatile SingletonDemo03 instance;

	// 构造方法私有化
	private SingletonDemo03() {
		}

	// 加锁，避免并发环境下创建多个对象
	public static SingletonDemo03 getInstance() {
		if (null == instance) {
			synchronized (SingletonDemo03.class) {
				if (null == instance) {
					// 非原子操作，可能会被 JVM 进行指令重排序
					// 并发环境下，可能会导致 instance 不为空，但是对象还未初始化，所以需要对 instance 属性加上 volatile 关键字，防止指令重排序
					instance = new SingletonDemo03(); 
				}
			}
		}
		return instance;
	}
}
