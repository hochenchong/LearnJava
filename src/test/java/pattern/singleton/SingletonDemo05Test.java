package pattern.singleton;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @Description: 枚举类单例模式单元测试类
 * @author HochenChong
 * @date 2018-10-11
 * @version v0.1
 */

public class SingletonDemo05Test {

	@Test
	public void testGetInstance() {
		SingletonDemo05 s1 = SingletonDemo05.INSTANCE;
		SingletonDemo05 s2 = SingletonDemo05.INSTANCE;
		
		// s1 和 s2 不为空
		assertNotNull(s1);
		assertNotNull(s2);
		// 断言，判断 s1 和 s2 为同一个对象，即 s1 == s2 为 true
		assertSame(s1, s2);
	}
}
