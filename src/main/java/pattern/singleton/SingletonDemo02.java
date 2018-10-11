package pattern.singleton;

/**
 * @Description: 懒汉式单例模式  
 * 		线程安全，延迟加载（lazy load），资源利用率高
 * 		存在的问题：
 * 			并发效率低
 * 			存在反射或者反序列化创建多个对象的漏洞
 * @author: HochenChong
 * @date: 2018-10-11
 * @version v0.1
 */

public class SingletonDemo02 {
	// 私有属性
	private static SingletonDemo02 instance;
	
	// 构造方法私有化
	private SingletonDemo02() {
	}
	
	// 加锁，避免并发环境下创建多个对象
	public static synchronized SingletonDemo02 getInstance() {
		if (null == instance) {
			instance = new SingletonDemo02();
		}
		return instance;
	}

}
