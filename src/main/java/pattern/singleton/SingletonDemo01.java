package pattern.singleton;

/**
 * @Description: 饿汉式单例模式
 * @author: HochenChong
 * @date: 2018-10-10
 * @version v0.1
 */
public class SingletonDemo01 {
	// 类初始化时，立即加载此对象
	private static SingletonDemo01 instance = new SingletonDemo01();
	
	// 构造方法私有化
	private SingletonDemo01() {
	}

	public static SingletonDemo01 getInstance() {
		return instance;
	}
	
}
