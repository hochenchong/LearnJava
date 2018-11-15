package pattern.observer.demo01;

import java.util.Observable;

/**
 * @Description: 使用 Java 内置观察者模式
 * @author: HochenChong
 * @date: 2018-11-15
 * @version v0.1
 */
public class WeatherData extends Observable {
	public WeatherData() {
	}
	
	private float temperature;
	private float humidity;
	private float pressure;
	
	public void measurementChanged() {
		// 标记状态已经改变
		setChanged();
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
