package pattern.singleton;

/**
 * @Description: 饿汉式单例模式
 *		线程安全，立即加载，并发环境下效率高
 * 		存在的问题：
 * 			如果该类一直没使用的话，则造成资源的浪费
 * 			存在反射或者反序列化创建多个对象的漏洞
 * @author: HochenChong
 * @date: 2018-10-10
 * @version v0.1
 */
public class SingletonDemo01 {
	// 类初始化时，立即加载此对象
	private static SingletonDemo01 instance = new SingletonDemo01();
	
	// 构造方法私有化
	private SingletonDemo01() {
		// 解决反射漏洞，当对象已创建过时，再次创建则抛出运行时异常
		/*
		if (null != instance) {
			throw new RuntimeException();
		}
		*/
	}

	public static SingletonDemo01 getInstance() {
		return instance;
	}
}
