package pattern.singleton;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * @Description: SingletonDemo02 懒汉式单例模式单元测试类
 * @author: HochenChong
 * @date: 2018-10-11
 * @version v0.1
 */

public class SingletonDemo02Test {

	@Test
	public void testGetInstance() {
		SingletonDemo02 s1 = SingletonDemo02.getInstance();
		SingletonDemo02 s2 = SingletonDemo02.getInstance();
		
		// s1 和 s2 不为空
		assertNotNull(s1);
		assertNotNull(s2);
		// 断言，判断 s1 和 s2 为同一个对象，即 s1 == s2 为 true
		assertSame(s1, s2);
	}
}
