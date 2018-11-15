package pattern.observer.demo02;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 使用 Java 内置观察者模式
 * @author: HochenChong
 * @date: 2018-11-15
 * @version v0.1
 */
public class WeatherData implements Subject {
	private float temperature;
	private float humidity;
	private float pressure;
	private List<Observer> observers;

	public WeatherData() {
		observers = new ArrayList<Observer>();
	}
	
	/**
	 * 注册观察者
	 */
	@Override
	public void registerObserver(Observer observer) {
		observers.add(observer);
	}

	/**
	 * 移除观察者
	 */
	@Override
	public void removeObserver(Observer observer) {
		// 获取该观察者的位置
		int i = observers.indexOf(observer);
		// 判断是否存在
		if (i >= 0) {
			observers.remove(i);
			// observers.remove(observer);
		}
	}

	/**
	 * 通知所有观察者
	 */
	@Override
	public void notifyObservers() {
		for (Observer observer : observers) {
			observer.update(temperature, humidity, pressure);
		}
	}
	
	public void measurementChanged() {
		// 通知观察者
		notifyObservers();
	}
	
	public void setMeasurements(float temperature, float humidity, float pressure) {
		this.temperature = temperature;
		this.humidity = humidity;
		this.pressure = pressure;
		// 更新属性后通知观察者
		measurementChanged();
	}

	public float getTemperature() {
		return temperature;
	}

	public float getHumidity() {
		return humidity;
	}

	public float getPressure() {
		return pressure;
	}
}
