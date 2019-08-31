### Java 8 日期和时间处理

#### 前言

> 近来工作中，涉及到 Unix 日期转换与计算的问题，平日里的习惯则是使用 Java 8 之前的日期和时间处理 api —— java.util.Date，以及 java.util.Calendar 之类的。同事建议我试试 Java 8 的 LocalDate 之类，新增日期 api，于是乎就有了这篇笔记。

---

#### Java 8 日期 API 的诞生背景

* 易用性
  * java.util.Date 月份从 0 开始，一月是 0，十二月是 11
  * java.time.LocalDate 月份和星期都改成了 enum
  * 日期时间的操作，经常是往前推或往后推几天的情况，用 java.util.Date 配合 Calendar 要写好多代码
* 线程安全
  * java.util.Date 和 DateFormatter 都不是线程安全的
  * 而 LocalDate 和 LocalTime 使用了 final 修饰类，是不变类型，线程安全且不能修改

> 如同 [https://www.joda.org/joda-time/](https://www.joda.org/joda-time/) 所说，
>
> ```
> The standard date and time classes prior to Java SE 8 are poor. By tackling this problem head-on, Joda-Time became the de facto standard date and time library for Java prior to Java SE 8. 
> ```
>
> Java SE 8 之前的标准日期类和时间类很差。通过正面解决这个问题，Joda-Time 成为 Java SE 8 之前Java 事实上的标准日期和时间库。
>
> 因此，Java SE 8 借鉴了 Joda Time 的很多特性，从而诞生了新的日期 API。

---

#### Java 8 日期 API

- LocalDate 只包含日期，不包含时间（与时区无关）
- LocalTime 只包含时间，不包含日期（与时区无关）
- LocalDateTime 同时包含日期和时间（与时区无关）
- Instant 时刻，
  距离格林尼治标准时间 1970 年 1 月 1 日 0 时 0 分 0 秒的毫秒数，需要通过时区转换日期和时间信息（与时区无关）
- ZonedDateTime 特定时区日期和时间
- ZoneId/ZoneOffset 时区

---

#### 使用

##### LocalDate 使用

```java
// LocalDate 使用
// 获取当前日期
LocalDate today = LocalDate.now();
// 自己设置日期，参数：年，月，日
LocalDate parseDate = LocalDate.of(2019, 9, 18);
// 或者，格式要求 yyyy-MM-dd，有个重载方法可以指定格式
LocalDate parseDate1 = LocalDate.parse("2018-09-18");

// 获取当前月的第一天
LocalDate firstDay = today.withDayOfMonth(1);
// 获取当前月的最后一天
LocalDate lastDay = today.with(TemporalAdjusters.lastDayOfMonth());
// 获取当前月的第一个星期一
LocalDate firstMonday = 
today.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));

// 日期比较与计算
// 当前日 是否早于 当前月的第一天
boolean isBefore = today.isBefore(firstDay);
boolean isAfter = today.isAfter(firstDay);
// 当前日 与 当前月的第一天 相差多少天
int howManyDays = today.compareTo(firstDay);
// 当前日加 1 天
LocalDate plusOneDay = today.plusDays(1);
// 当前日往前推 1 个月
LocalDate minusOneMonth = today.minusMonths(1);
```

##### LocalTime 使用

```java
// LocalTime 使用
// 获取当前时间
LocalTime nowTime = LocalTime.now();
// 消除毫秒
LocalTime localTime = nowTime.withNano(0);
// 构造时间，按照 ISO 格式即可，例如：01:00，01:00:00，01:00:00.001
LocalTime of = LocalTime.of(0, 2);
LocalTime parse = LocalTime.parse("01:00");
```

##### LocalDateTime 与 Date 或 时间戳的转换

```java
// LocalDateTime 与 Date 或 时间戳的转换，主要借助于 Instant
LocalDateTime localDateTime = LocalDateTime.now();
// LocalDateTime 转时间戳，获取毫秒的时间戳，相当于 Date 的 getTime() 方法
Instant instant = localDateTime.toInstant(ZoneOffset.of("+8"));
long milliseconds = instant.toEpochMilli();
System.out.println("LocalDateTime 转时间戳，毫秒级：" + milliseconds);
// 获取秒的时间戳
long second = localDateTime.toEpochSecond(ZoneOffset.of("+8"));
System.out.println("LocalDateTime 转时间戳，秒级：" + second);
// localDateTime 格式化输出
System.out.println("LocalDateTime 格式化输出：" + localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")));

// 时间戳转 LocalDateTime
Instant instant1 = Instant.ofEpochMilli(milliseconds);
LocalDateTime localDateTime1 = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
System.out.println("时间戳转 LocalDateTime：" + localDateTime1);

// LocalDateTime 转 Date
ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
Instant instant2 = zonedDateTime.toInstant();
Date date = Date.from(instant2);
SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
System.out.println("LocalDateTime 转 Date：" + simpleDateFormat.format(date));

// Date 转 LocalDateTime
Instant instant3 = date.toInstant();
LocalDateTime localDateTime2 = LocalDateTime.ofInstant(instant3, ZoneId.systemDefault());
System.out.println("Date 转 LocalDateTime：" + localDateTime2);
```

---

#### 小练习

##### 练习一：打印本月日历

> 本练习在《Java 核心技术 卷1：基础知识》（原书第 10 版）的 LocalDate 类介绍中看到，便拿来练手一下，顺带和书中的例子对比一下，并进行修改。
>
> 问题描述：打印本月日历，对当日使用 “*” 进行标记
>
> 结果例如：
>
> ![Java 8 日期和时间处理 - 本月日历示范](https://github.com/hochenchong/learnJava/blob/master/images/Java%208%20%E6%97%A5%E6%9C%9F%E5%92%8C%E6%97%B6%E9%97%B4%E5%A4%84%E7%90%86%20-%20%E6%9C%AC%E6%9C%88%E6%97%A5%E5%8E%86%E7%A4%BA%E8%8C%83.png?raw=true)

```java
@Test
public void testLocalDate() {
    LocalDate date = LocalDate.now();
    int today = date.getDayOfMonth();

    // 获取当前月的第一天是周几
    date = date.with(TemporalAdjusters.firstDayOfMonth());
    DayOfWeek dayOfWeek = date.getDayOfWeek();
    int firstDayOfWeek = dayOfWeek.getValue();

    // 获取当前月有多少天
    date = date.with(TemporalAdjusters.lastDayOfMonth());
    int lastDateDayOfMonth = date.getDayOfMonth();

    System.out.println("周一\t\t周二\t\t周三\t\t周四\t\t周五\t\t周六\t\t周日");
    int weekday = 0;
    for (int i = 1; i < firstDayOfWeek ; i++) {
        System.out.print("\t\t");
        weekday ++;
    }

    for (int i = 1; i <= lastDateDayOfMonth; i++) {
        if (i == today) {
            System.out.print(i + "*\t\t");
        } else {
            System.out.print(i + "\t\t");
        }
        weekday++;
        if (weekday == 7) {
            weekday = 0;
            System.out.println();
        }
    }
}
```

##### 练习二：倒计时

> 本练习嘛（我是不会说是在上家公司时倒计下班时间的！之前使用 Date 类实现过），以 6 点下班为例，只与时间有关，而与日期无关，使用 LocalTime 即可。

```java
@Test
public void testCountdown() {
    LocalTime goHomeTime = LocalTime.of(18, 0, 0).withNano(0);
    LocalTime nowTime = LocalTime.now().withNano(0);
    int timeDifference;

    while ((timeDifference = goHomeTime.toSecondOfDay() - nowTime.toSecondOfDay()) > 0) {
        LocalTime timeDiff = LocalTime.ofSecondOfDay(timeDifference);
        System.out.println(timeDiff.format(DateTimeFormatter.ofPattern("距离回家还有 HH 小时 mm 分钟 ss 秒")));

        // 每隔 1 秒停顿一次
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        nowTime = LocalTime.now().withNano(0);
    }
    System.out.println("可以回家了！！！");
}
```

---

#### JDBC 映射关系

| SQL       | Java          |
| --------- | ------------- |
| date      | LocalDate     |
| time      | LocalTime     |
| timestamp | LocalDateTime |

---

#### 参考资料

* 《Java 核心技术 卷1：基础知识》
* Joda Time 介绍：[https://www.joda.org/joda-time/](https://www.joda.org/joda-time/)
* [如何在Java 8中愉快地处理日期和时间 - *廖雪峰*的官方网站](https://www.liaoxuefeng.com/article/991339711823296)

#### 后记

> 在实践中成长
>
> HochenChong
>
> 2019-08-31