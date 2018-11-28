package timed_task.quartz;

import java.util.Date;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class MyQuartzTest {
	public static void main(String[] args) throws SchedulerException {
		// 创建一个 JobDetail 实例，与 HelloJob Class 绑定
		JobDetail jobDetail = JobBuilder.newJob(MyQuartzJob.class)
				.withIdentity("myJob", "group1")
				.build();

		Date triggerStartTime = new Date();
		triggerStartTime.setTime(triggerStartTime.getTime() + 3000L);
		// 创建一个 Trigger 实例，定义 job 何时执行
		Trigger trigger = TriggerBuilder.newTrigger()
				.withIdentity("myTrigger", "triggerGroup1")
				.startAt(triggerStartTime)
				/*
				 *  格式：[秒] [分] [小时] [日] [月] [周] [年]
				 *  年可以省略
				 *  日和周不能同时为 * 或者同时为 ?
				 */
				.withSchedule(CronScheduleBuilder.cronSchedule("* * * ? * *"))
				.build();

		// 创建 Scheduler 实例
		SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		Scheduler scheduler = schedulerFactory.getScheduler();
		scheduler.scheduleJob(jobDetail, trigger);
		scheduler.start();
	}
}
