package pattern.strategy;

/**
 * @Description: 鸭子抽象类
 * @author: HochenChong
 * @date: 2018-11-14
 * @version v0.1
 */
public abstract class Duck {
	FlyBehavior flyBehavior;
	QuackBehavior quackBehavior;
	
	public abstract void display();
	
	public void performFly() {
		// 委托给行为类
		flyBehavior.fly();
	}
	public void performQuack() {
		// 委托给行为类
		quackBehavior.quack();
	}
	
	// 改变行为
	public void setFlyBehavior(FlyBehavior flyBehavior) {
		this.flyBehavior = flyBehavior;
	}
	public void setQuackBehavior(QuackBehavior quackBehavior) {
		this.quackBehavior = quackBehavior;
	}
}
