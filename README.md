# Cherry
&emsp;&emsp;Cherry是一个高性能的定时任务调度服务，由Cherry-Core和Cherry-Server组成，能够为存在大批量的定时计划的业务提供可靠支持，包括高并发的发布任务和删除任务、对已发布任务的分布和执行状况进行监控。<br/>
&emsp;&emsp;Cherry-Core包括了Cherry调度引擎内核和提供拓展性的组件，允许你在本地项目中引用和启动一个Cherry服务，提交Task的实现类，等待定时执行。<br/>
&emsp;&emsp;Cherry-Server包含了Cherry-Core，是一个可以独立运行的中间件，使用Netty构建网络通信层的服务支持，能够修改外部配置。你可以通过我们提供的客户端Cherry-Client与Cherry-Server建立TCP信道，实现远程任务发布和删除（目前仅支持到点在线提醒，RPC调用等待以后的版本~）。

## 1、使用准备
&emsp;&emsp;别急，快了快了

## 2、本地使用
&emsp;&emsp;时间轮TimingWheel为Cherry提供了定时调度的能力，在本地使用中，我们需要先实例化一个Timingwheel：
```java
        TimingWheel timingWheel = new DefaultTimingWheel(100, 100, 20_000, 10_000, 4, 8);
```
&emsp;&emsp;DefaultTimingWheel是我们提供的TimingWheel默认实现类，采用异步转动更新的方式，最小化误差。以下是DefaultTimingWheel的构造函数：
```java
        /**
        * 默认构造函数
        *
        * @param interval 每次转动的间隔，单位为 ms
        * @param totalTicks 一轮的转动点总数，也就是{@link TimeSlot}的总数
        * @param waitTimeout {@link TimeSlot}自旋锁竞争的超时时间，单位为 ns
        * @param taskSize 单个转动点可以承载的最大任务数量
        * @param minThreadNumber 任务执行线程池的核心线程数
        * @param maxThreadNumber 任务执行线程池的最大线程数
        */
        public DefaultTimingWheel(long interval, int totalTicks, long waitTimeout, int taskListSize,
                              int minThreadNumber, int maxThreadNumber)
```
&emsp;&emsp;接下来，我们需要实现一个简易的Task
```java
        Task task = new Task() {
        
            private final Map<String, Object> taskConfig = new JSONObject();
            
            @Override
            public Map<String, Object> getTaskConfig() {
                return taskConfig;
            }

            @Override
            public void execute() {
                System.out.println("任务执行成功！");
            }
        };
```
&emsp;&emsp;在有了一个Task以后，我们还需要提供执行Task的时间点，并转换成TimingWheel需要的相对时间距离。<br/>
&emsp;&emsp;时间点可以是满足"yyyyMMddhhmmSSsss"的17位String字符串，也可以是long类型的距1970年毫秒时间。这里我们采用Cherry提供的TimeParser完成前者到后者的转换。
```java
        String time = "2022" + "12" + "14" + "17" + "30" + "00" + "000";
        TimeParser parser = new TimeParser();
        long timeValue = parser.time2TimeValue(time, TimeZone.getDefault());
```
&emsp;&emsp;接下来，我们通过TimingWheel提供的api，计算出相对时间距离。
```java
        long distance = timingWheel.calDistance(timeValue);
```
&emsp;&emsp;最后，通过相对时间距离，我们可以从TimingWheel里面取出对应的TimeSlot时间槽位，然后调用其API完成Task任务的发布（会返回Task的发布顺序）。
```java
        TimeSlot slot = timingWheel.getSlot(distance);
        long taskId = slot.submitTask(task, distance);
```
&emsp;&emsp;完整过程如下
```java
        TimingWheel timingWheel = new DefaultTimingWheel(100, 100, 20_000, 10_000, 4, 8);
        Task task = new Task() {

            private final Map<String, Object> taskConfig = new JSONObject();

            @Override
            public Map<String, Object> getTaskConfig() {
                return taskConfig;
            }

            @Override
            public void execute() {
                System.out.println("任务执行成功！");
            }
        };

        String time = "2022" + "12" + "14" + "17" + "30" + "00" + "000";
        TimeParser parser = new TimeParser();
        long timeValue = parser.time2TimeValue(time, TimeZone.getDefault());

        long distance = timingWheel.calDistance(timeValue);

        TimeSlot slot = timingWheel.getSlot(distance);
        long taskId = slot.submitTask(task, distance);
```

## 3、远程使用
&emsp;&emsp;来不及啦
