package timed_task.timer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;

import org.junit.Test;

public class MyTimerTaskTest {

	@Test
	public void testTimer() {
		Timer timer = new Timer();
		MyTimerTask myTimerTask = new MyTimerTask("task001");
		
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println("当前时间为：" + sdf.format(calendar.getTime()));
		
		/*
		 	schedule(task, time) 
			参数：
				task：所要安排的任务
				time：执行任务的时间
			作用：
				在时间等于或超过 time 的时候执行且仅执行一次 task
		 */
		// timer.schedule(myTimerTask, calendar.getTime());
		
		/*
		 	schedule(task, time, period)
			参数：
				task：所要安排的任务
				time：首次执行任务的时间
				period：执行一次 task 的时间间隔，单位是毫秒
			作用：
				时间等于或超过 time 时首次执行 task，之后每隔 period 毫秒重复执行一次 task
		 */
		// timer.schedule(myTimerTask, calendar.getTime(), 1000);
		
		/*
		 	schedule(task, delay)
			参数：
				task：所要安排的任务
				delay：执行任务前的延迟时间，单位是毫秒
			作用：
				等待 delay 毫秒后执行且仅执行一次 task
		 */
		// timer.schedule(myTimerTask, 1000);
		
		/*
		 	schedule(task, delay, period)
			参数：
				task：所要安排的任务
				delay：执行任务前的延迟时间，单位是毫秒
				period：执行一次 task 的时间间隔，单位是毫秒
			作用：
				等待 delay 毫秒后首次执行 task，之后每隔 period 毫秒重复执行一次 task
		 */
		// timer.schedule(myTimerTask, 1000, 2000);
		
		/*
		 	scheduleAtFixedRate(task, time, period)
			参数：
				task：所要安排的任务
				time：首次执行任务的时间
				period：执行一次 task 的时间间隔，单位是毫秒
			作用：
				时间等于或超过 time 时首次执行 task，之后每隔 period 毫秒重复执行一次 task
		 */
		// timer.scheduleAtFixedRate(myTimerTask, calendar.getTime(), 1000);
		
		/*
		 	scheduleAtFixedRate(task, delay, period)
			参数：
				task：所要安排的任务
				delay：执行任务前的延迟时间，单位是毫秒
				period：执行一次 task 的时间间隔，单位是毫秒
			作用：
				等待 delay 毫秒后首次执行 task，之后每隔 period 毫秒重复执行一次 task
		 */
		timer.scheduleAtFixedRate(myTimerTask, 1000, 2000);
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// 终止此计时器，丢弃所有当前已安排的任务
		timer.cancel();
	}

}
