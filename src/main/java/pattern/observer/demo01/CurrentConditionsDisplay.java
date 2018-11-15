package pattern.observer.demo01;

import java.util.Observable;
import java.util.Observer;

public class CurrentConditionsDisplay implements Observer {
	private float temperature;
	private float humidity;
	// 持有一个被观察者
	Observable observable;
	
	public CurrentConditionsDisplay(Observable observable) {
		this.observable = observable;
		// 将自己注册为被观察者
		observable.addObserver(this);
	}
	
	// 当被观察者通知观察者时，该方法将会被调用
	@Override
	public void update(Observable observable, Object object) {
		if (observable instanceof WeatherData) {
			WeatherData weatherData = (WeatherData)observable;
			this.temperature = weatherData.getTemperature();
			this.humidity = weatherData.getHumidity();
			display();
		}
	}
	
	public void display() {
		System.out.println("CurrentConditionsDisplay: " + temperature + "F degrees and " + humidity + "% humidity");
	}
}
