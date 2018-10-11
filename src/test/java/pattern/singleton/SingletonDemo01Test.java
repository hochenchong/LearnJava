package pattern.singleton;

import static org.junit.Assert.*;
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
}
