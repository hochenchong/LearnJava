package pattern.observer.demo02;

public class CurrentConditionsDisplay implements Observer {
	private float temperature;
	private float humidity;
	Subject subject;
	
	public CurrentConditionsDisplay(Subject subject) {
		this.subject = subject;
		// 将自己注册为被观察者
		subject.registerObserver(this);
	}
	
	// 当被观察者通知观察者时，该方法将会被调用
	@Override
	public void update(float temperature, float humidity, float pressure) {
		this.temperature = temperature;
		this.humidity = humidity;
		display();
	}
	
	public void display() {
		System.out.println("CurrentConditionsDisplay: " + temperature + "F degrees and " + humidity + "% humidity");
	}
}
