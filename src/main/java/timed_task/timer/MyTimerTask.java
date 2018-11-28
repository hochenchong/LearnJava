package timed_task.timer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimerTask;

public class MyTimerTask extends TimerTask {
	private String name; 
	
	public MyTimerTask(String name) {
		this.name = name;
	}

	@Override
	public void run() {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(sdf.format(calendar.getTime()) + " 任务 " + name + " 运行中。。。");
		
		// 停止该任务
		// cancel();
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
