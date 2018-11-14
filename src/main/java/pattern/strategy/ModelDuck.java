package pattern.strategy;

public class ModelDuck extends Duck {
	public ModelDuck() {
		// 初始是一只不会飞也不会叫的模型鸭
		flyBehavior = new FlyNoWay();
		quackBehavior = new MuteQuack();
	}
	
	@Override
	public void display() {
		System.out.println("这是一只模型鸭子！");
	}

}
