package pattern.observer;

import org.junit.Test;

import pattern.observer.demo01.CurrentConditionsDisplay;
import pattern.observer.demo01.WeatherData;

/**
 * @Description: 观察者模式 Demo，学习自《Head First 设计模式》
 * @author: HochenChong
 * @date: 2018-11-15
 * @version v0.1
 */
public class ObserverDemo01 {

	/**
	 * 结论：
	 * 	优点：
	 * 		使用 Java 内置的观察者模式实现简单
	 * 	缺点：
	 * 		Observable 类，需要使用继承（违反了 OO 的设计原则：面向接口编程）
	 * 		Observable 类，将 setChanged() 方法保护起来，必须继承才能使用该方法（违反了 OO 设计原则：多用组合，少用继承）
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
