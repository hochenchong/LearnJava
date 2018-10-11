package pattern.singleton;

/**
 * @Description: 静态内部类单例模式
 * 		线程安全，延迟加载，并发效率高
 * 		存在的问题：
 * 			存在反射或者反序列化创建多个对象的漏洞
 * @author HochenChong
 * @date 2018-10-11
 * @version v0.1
 */

public class SingletonDemo04 {
	// 构造方法私有化
	private SingletonDemo04() {
	}
	
	private static class SingletonDemo04Inner {
		private static final SingletonDemo04 INSTANCE = new SingletonDemo04();
	}
	
	public static SingletonDemo04 getInstance() {
		return SingletonDemo04Inner.INSTANCE;
	}

}
