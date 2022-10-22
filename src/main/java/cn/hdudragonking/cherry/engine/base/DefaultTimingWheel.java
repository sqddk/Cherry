package cn.hdudragonking.cherry.engine.base;

import cn.hdudragonking.cherry.engine.base.struct.DefaultPointerLinkedRing;
import cn.hdudragonking.cherry.engine.base.struct.PointerLinkedList;
import cn.hdudragonking.cherry.engine.base.struct.TaskList;
import cn.hdudragonking.cherry.engine.task.Task;
import cn.hdudragonking.cherry.engine.utils.BaseUtils;
import cn.hdudragonking.cherry.engine.utils.TimeUtils;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * ʱ���ֵ�Ĭ��ʵ���࣬�����������
 *
 * @since 2022/10/17
 * @author realDragonKing
 */
public class DefaultTimingWheel implements TimingWheel {

    /**
     * ��ǰʱ���
     */
    private TimePoint currentTimePoint;

    /**
     * �̶�����
     */
    private final int totalTicks;

    /**
     * ÿ���̶ȼ��ʱ��������λΪ���루ms��
     */
    private final int interval;

    /**
     * ����ִ�ж�ʱ������̳߳�
     */
    private final ExecutorService executor;

    /**
     * Ĭ�ϵĿ̶�����
     */
    private final static int DEFAULT_TOTAL_TICKS = 10;

    /**
     * ���Ի�ȡ������������ʱ�䣬��λΪ���루ns��
     */
    private final static long DEFAULT_MAX_WAIT_TIME = 5000000;

    /**
     * ʱ���ֵ�������������
     */
    private final AtomicBoolean monitor;

    /**
     * ʱ���ֵ����ݽṹʵ�֣���������
     */
    private final PointerLinkedList<Map<Integer, TaskList>> linkedRing;

    public DefaultTimingWheel(int interval) {
        this(interval, DEFAULT_TOTAL_TICKS);
    }

    public DefaultTimingWheel(int interval, int totalTicks) {
        if (interval <= 0 || totalTicks <= 0) {
            throw new RuntimeException();
        }
        this.totalTicks = totalTicks;
        this.interval = interval;
        int coreSize = Runtime.getRuntime().availableProcessors();
        this.executor = BaseUtils.createWorkerThreadPool(2, coreSize * 2, 1000);
        this.linkedRing = new DefaultPointerLinkedRing(this.totalTicks);
        this.monitor = new AtomicBoolean(false);
        this.currentTimePoint = TimePoint.getCurrentTimePoint();
    }

    /**
     * �ύһ���µĶ�ʱ����
     * <p>
     * ���ʱ�������Ѿ��������߳�ռ���ˣ���ô�����ȴ������У������������һ��ʱ�䣬�Ͳ��ٳ��Ի�ȡ���������ύʧ�ܽ����
     *
     * @param task ��ʱ����
     * @return �ύ�Ƿ�ɹ� | ����ID
     */
    @Override
    public int[] submit(Task task) {
        int difference = TimeUtils.calDifference(this.currentTimePoint, task.getTimePoint(), this.interval);
        if (difference <= 0) {
            return new int[]{0};
        }
        int round = difference / this.totalTicks;
        int ticks = difference % this.totalTicks;

        if (!this.tryLock(true)) {
            return new int[]{0};
        }
        Map<Integer, TaskList> bucket = this.getSpecBucket(ticks);
        if (bucket == null) {
            return new int[]{0};
        }
        TaskList taskList = bucket.get(round);
        if (taskList == null) {
            taskList = new TaskList();
            bucket.put(round, taskList);
        }
        taskList.add(task);
        this.unLock();
        return new int[]{1, task.getTaskID()};
    }

    /**
     * ��������ID���Ƴ�һ����ʱ����
     * <p>
     * ���ʱ�������Ѿ��������߳�ռ���ˣ���ô�����ȴ������У������������һ��ʱ�䣬�Ͳ��ٳ��Ի�ȡ��������ɾ��ʧ�ܽ����
     *
     * @param timePoint ����ʱ���
     * @param id ����ID
     * @return �����Ƿ�ɾ���ɹ�
     */
    @Override
    public boolean remove(TimePoint timePoint, int id) {
        int difference = TimeUtils.calDifference(this.currentTimePoint, timePoint, this.interval);
        if (difference <= 0) {
            return false;
        }
        int round = difference / this.totalTicks;
        int ticks = difference % this.totalTicks;

        if (!this.tryLock(true)) {
            return false;
        }
        Map<Integer, TaskList> bucket = this.getSpecBucket(ticks);
        if (bucket == null) {
            return false;
        }
        TaskList taskList = bucket.get(round);
        if (taskList == null) {
            return false;
        }
        boolean result = taskList.removeTask(id);
        this.unLock();
        return result;
    }

    /**
     * ʱ���ֽ���һ��ת���������ֳ��Ի�ȡ���������ʱ���Բ���ʱ��������Ƽ�����ʧ�ͻָ����������ִ��
     */
    @Override
    public void turn() {
        this.tryLock(false);
        // TODO ��ʱ�������Ƶȴ�����
        this.linkedRing.moveNext();
        Map<Integer, TaskList> map = this.linkedRing.getPointer();
        this.currentTimePoint = TimePoint.getCurrentTimePoint();
        for (Map.Entry<Integer, TaskList> entry : map.entrySet()) {
            int round = entry.getKey();
            TaskList list = entry.getValue();
            map.remove(round);
            if (round == 0) {
                for (int i = 0; i < list.size(); i++) {
                    Task task = list.getPointer();
                    this.executor.submit(task::execute);
                    list.moveNext();
                }
            } else {
                map.put(round - 1, list);
            }
        }
        this.unLock();
    }

    /**
     * ���Ի�ȡ��ʱ���ֵĲ���������û����������������������һ��ʱ���򷵻�����ȡʧ�ܵ���Ϣ
     *
     * @param stopNeed �Ƿ�ʱֹͣ
     * @return �����Ƿ�ɹ���ȡ
     */
    @Override
    public boolean tryLock(boolean stopNeed) {
        if (stopNeed) {
            long startWaitingTime = System.nanoTime();
            while (!this.monitor.compareAndSet(false, true)) {
                if (System.nanoTime() - startWaitingTime > DEFAULT_MAX_WAIT_TIME) return false;
            }
        } else {
            while (!this.monitor.compareAndSet(false, true)) {}
        }
        return true;
    }

    /**
     * �ͷ�ʱ���ֵĲ�����
     */
    @Override
    public void unLock() {
        this.monitor.set(false);
    }

    /**
     * �������Ŀ̶�������ȡ����Ӧ�̶ȵ�������������
     * <p>
     * ������������Ѿ��������߳�ռ���ˣ���ô�����ȴ�������У������������һ��ʱ�䣬�Ͳ��ٳ��Ի�ȡ��������
     *
     * @param ticks Ŀ��ʱ����뵱ǰʱ��㣬���Ŀ̶���
     * @return ��Ӧ�̶ȵ�������������
     */
    private Map<Integer, TaskList> getSpecBucket(int ticks) {
        Map<Integer, TaskList> bucket;
        for (int i = 0; i < ticks; i++) {
            this.linkedRing.moveNext();
        }
        bucket = this.linkedRing.getPointer();
        for (int i = 0; i < ticks; i++) {
            this.linkedRing.movePrevious();
        }
        return bucket;
    }

    /**
     * ����ʱ��������ƣ����㳬ʱʱ�䣬�������ڵ�����ָ�ִ�У����Ұ��ճ�ʱʱ����������round
     *
     * @param wasteTicks ��ʧ�Ŀ̶���
     */
    @Override
    public void recover(int wasteTicks) {

    }

}
