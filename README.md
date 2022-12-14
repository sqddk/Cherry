# Cherry
&emsp;&emsp;Cherry是一个高性能的定时任务调度服务，由Cherry-Core和Cherry-Server组成，能够为存在大批量的定时计划的业务提供可靠支持，包括高并发的发布任务和删除任务、对已发布任务的分布和执行状况进行监控。<br/>
&emsp;&emsp;Cherry-Core包括了Cherry调度引擎内核和提供拓展性的组件，允许你在本地项目中引用和启动一个Cherry服务，提交Task的实现类实现定时执行。<br/>
&emsp;&emsp;Cherry-Server包含了Cherry-Core，是一个可以独立运行的中间件，使用Netty构建网络通信层的服务支持，能够修改外部配置。你可以通过我们提供的客户端Cherry-Client与Cherry-Server建立TCP信道，实现远程任务发布和删除（目前仅支持到点在线提醒，RPC调用等待以后的版本~）。

## 1、使用准备
&emsp;&emsp;别急，快了快了

## 2、本地使用
