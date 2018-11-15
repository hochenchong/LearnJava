package pattern.observer;

import org.junit.Test;

import pattern.observer.demo02.CurrentConditionsDisplay;
import pattern.observer.demo02.WeatherData;

/**
 * @Description: 观察者模式 Demo，学习自《Head First 设计模式》
 * @author: HochenChong
 * @date: 2018-11-15
 * @version v0.1
 */
public class ObserverDemo02 {

	/**
	 * 结论：
	 * 	优点：
	 * 		封装变化
	 * 		面向接口编程
	 * 		多用组合，少用继承
	 * 		松耦合
	 * 	缺点：需要自己实现观察者模式
	 * 	观察者模式：在对象之间定义一对多的依赖，这样一来，当一个对象改变状态，依赖它的对象都会收到通知，病自动更新。
	 * 	观察者模式的代表案例：MVC
	 */
	@Test
	public void test() {
		WeatherData weatherData = new WeatherData();
		CurrentConditionsDisplay currentConditionsDisplay = new CurrentConditionsDisplay(weatherData);
		
		weatherData.setMeasurements(80, 65, 30.4f);
		weatherData.setMeasurements(82, 62, 29.4f);
		weatherData.setMeasurements(78, 60, 29.2f);
	}

}
