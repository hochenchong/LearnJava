package pattern.singleton;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @Description: 静态内部类单例模式单元测试类
 * @author HochenChong
 * @date 2018-10-11
 * @version v0.1
 */

public class SingletonDemo04Test {

	@Test
	public void testGetInstance() {
		SingletonDemo04 s1 = SingletonDemo04.getInstance();
		SingletonDemo04 s2 = SingletonDemo04.getInstance();
		
		// s1 和 s2 不为空
		assertNotNull(s1);
		assertNotNull(s2);
		// 断言，判断 s1 和 s2 为同一个对象，即 s1 == s2 为 true
		assertSame(s1, s2);
	}
}
