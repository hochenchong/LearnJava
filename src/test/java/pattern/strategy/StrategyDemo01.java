package pattern.strategy;

import org.junit.Test;

/**
 * @Description: 策略模式 Demo，学习自《Head First 设计模式》
 * @author: HochenChong
 * @date: 2018-11-14
 * @version v0.1
 */
public class StrategyDemo01 {

	/**
	 * 结论：面向接口编程
	 * 		多用组合，少用继承
	 * 		封装变化
	 * 		单一职责
	 * 策略模式：定义算法族，分别封装起来，让它们之间可以互相替换，此模式让算法的变化独立于使用算法的客户
	 */
	@Test
	public void testDuck() {
		Duck duck = new ModelDuck();
		duck.performFly();
		duck.performQuack();
		
		// 改变行为
		System.out.println("-----------改变行为-----------");
		duck.setFlyBehavior(new FlyWithWings());
		duck.setQuackBehavior(new Quack());
		duck.performFly();
		duck.performQuack();
	}

}
