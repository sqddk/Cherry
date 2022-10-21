package cn.hdudragonking.cherry.bootstrap;

import cn.hdudragonking.cherry.bootstrap.remote.server.CherryServer;
import cn.hdudragonking.cherry.engine.base.DefaultTimingWheel;
import cn.hdudragonking.cherry.engine.base.TimePoint;
import cn.hdudragonking.cherry.engine.base.TimingWheel;
import cn.hdudragonking.cherry.engine.base.executor.ScheduleExecutor;
import cn.hdudragonking.cherry.engine.base.executor.TimingWheelExecutor;
import cn.hdudragonking.cherry.engine.task.Task;
import cn.hdudragonking.cherry.engine.utils.BaseUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * cherry��ʱ�����������ı������������࣬�����������������ط���
 * <p>
 * socket ������� {@link CherryServer}
 * ������������Ҳ���������ط��񣬲��ṩ����ͨ������
 *
 * @since 2022/10/17
 * @author realDragonKing
 */
public class CherryLocalStarter {
    private static final class CherryLocalStarterHolder{
        private static final CherryLocalStarter INSTANCE = new CherryLocalStarter();
    }
    public static CherryLocalStarter getInstance() {
        return CherryLocalStarterHolder.INSTANCE;
    }
    private CherryLocalStarter() {}

    /**
     * ʱ���֣�
     */
    private TimingWheel timingWheel;

    /**
     * Ĭ�ϵ�ÿ���̶�֮���ʱ����
     */
    private final static int DEFAULT_INTERVAL = 60000;

    /**
     * ��־��ӡ��
     */
    private final Logger logger = LogManager.getLogger("Cherry");

    /**
     * Ϊcherry������������г�ʼ����ʹ��Ĭ�ϵ�ʱ����
     */
    public void initial() {
        this.initial(DEFAULT_INTERVAL);
    }

    /**
     * Ϊcherry������������г�ʼ��
     *
     * @param interval ʱ����
     */
    public void initial(int interval) {
        ExecutorService threadPool = Executors.newFixedThreadPool(2);
        this.timingWheel = new DefaultTimingWheel(interval);
        BlockingQueue<Integer> blockingQueue = new LinkedBlockingQueue<>(1);
        threadPool.submit(new ScheduleExecutor(DEFAULT_INTERVAL, blockingQueue));
        threadPool.submit(new TimingWheelExecutor(this.timingWheel, blockingQueue));
        BaseUtils.printLogo();
        this.logger.info("��ʱ������������Ѿ��ڱ��سɹ����������ṩ����");
    }

    /**
     * ͨ�����ؽ�����API����ʱ�������ύ����
     *
     * @param task ��ִ�е�����
     * @return �Ƿ�ɹ��ύ | ����ID
     */
    public int[] submit(Task task) {
        return this.timingWheel.submit(task);
    }

    /**
     * ��������ִ��ʱ��������ID��ͨ�����ؽ�����API����ʱ������ɾ��һ������
     *
     * @param timePoint ����ִ��ʱ���
     * @param taskID ����ID
     * @return �Ƿ�ɹ�ɾ��
     */
    public boolean remove(TimePoint timePoint, String taskID) {
        return this.timingWheel.remove(timePoint, taskID);
    }

}
