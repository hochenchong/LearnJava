package pattern.observer.demo02;

/**
 * @Description: 定义被观察者接口
 * @author: HochenChong
 * @date: 2018-11-15
 * @version v0.1
 */
public interface Subject {
	/**
	 * 注册观察者
	 * @param observer
	 */
	public void registerObserver(Observer observer);
	
	/**
	 * 移除观察者
	 * @param observer
	 */
	public void removeObserver(Observer observer);
	
	/**
	 * 通知所有注册的观察者
	 */
	public void notifyObservers();
}
