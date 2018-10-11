package pattern.singleton;

import static org.junit.Assert.*;

import java.lang.reflect.Constructor;

import org.junit.Test;

/**
 * @Description: SingletonDemo01 饿汉式单例模式单元测试类
 * @author: HochenChong
 * @date: 2018-10-09
 * @version v0.1
 */

public class SingletonDemo01Test {

	/**
	 * 获取单例对象
	 */
	@Test
	public void testGetInstance() {
		SingletonDemo01 s1 = SingletonDemo01.getInstance();
		SingletonDemo01 s2 = SingletonDemo01.getInstance();
		
		// s1 和 s2 不为空
		assertNotNull(s1);
		assertNotNull(s2);
		
		// s1 == s2 是否为 true，即 s1 与 s2 是否为同一个对象
		assertSame(s1, s2);
	}
	
	/**
	 * 使用反射创建多个对象
	 * @throws Exception 这里简单处理，直接将异常抛出
	 */
	@Test
	public void testReflectBug() throws Exception {
		SingletonDemo01 s1 = SingletonDemo01.getInstance();
		
		Class<SingletonDemo01> clz = (Class<SingletonDemo01>) s1.getClass();
		Constructor<SingletonDemo01> constructor = clz.getDeclaredConstructor(null);
		// 设置 accessible 为 true，跳过权限检查
		constructor.setAccessible(true);
		SingletonDemo01 s2 = constructor.newInstance();

		assertNotEquals(s1, s2);
	}
}
