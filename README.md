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
