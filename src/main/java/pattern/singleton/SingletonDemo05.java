package pattern.singleton;

/**
 * @Description: 枚举类单例模式
 * 		线程安全，并发效率高
 * 		避免了反射与反序列化的漏洞，由 JVM 底层保证了单例
 * 		不能延迟加载
 * @author HochenChong
 * @date 2018-10-11
 * @version v0.1
 */

public enum SingletonDemo05 {
	INSTANCE;
}
