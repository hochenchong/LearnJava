package pattern.singleton;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @Description: 单例模式模块的测试类
 * @author: HochenChong
 * @date: 2018-10-11
 * @version v0.1
 */

@RunWith(Suite.class)
@SuiteClasses({
		SingletonDemo01Test.class, 
		SingletonDemo02Test.class,
		SingletonDemo03Test.class, 
		SingletonDemo04Test.class,
		SingletonDemo05Test.class
		})
public class SingletonDemoAllTests {

}
