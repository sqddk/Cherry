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
        this.currentTimePoint = TimePoint.getCurrentTimePoint();
    }

    /**
     * �ύһ���µĶ�ʱ����
     * <p>
     * ������������Ѿ��������߳�ռ���ˣ���ô�����ȴ�������У������������һ��ʱ�䣬�Ͳ��ٳ��Ի�ȡ��������
     * ������������Ļ�ȡ���Բ�ȡͬ���Ĳ���
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

        Map<Integer, TaskList> bucket = this.getSpecBucket(ticks);
        if (bucket == null) {
            return new int[]{0};
        }

        TaskList taskList = bucket.get(round);
        if (taskList == null) {
            taskList = new TaskList();
            bucket.put(round, taskList);
        }

        AtomicBoolean monitor = taskList.getMonitor();
        long startTime = System.nanoTime();
        while (monitor.compareAndSet(false, true)) {
            if (System.nanoTime() - startTime >= DEFAULT_MAX_WAIT_TIME) {
                return new int[]{0};
            }
        }
        taskList.add(task);
        monitor.set(false);
        return new int[]{1, task.getTaskID()};
    }

    /**
     * ��������ID���Ƴ�һ����ʱ����
     * <p>
     * ������������Ѿ��������߳�ռ���ˣ���ô�����ȴ�������У������������һ��ʱ�䣬�Ͳ��ٳ��Ի�ȡ��������
     * ������������Ļ�ȡ���Բ�ȡͬ���Ĳ���
     *
     * @param timePoint ����ʱ���
     * @param id ����ID
     * @return �����Ƿ�ɾ���ɹ�
     */
    @Override
    public boolean remove(TimePoint timePoint, String id) {
        int difference = TimeUtils.calDifference(this.currentTimePoint, timePoint, this.interval);
        if (difference <= 0) {
            return false;
        }
        int round = difference / this.totalTicks;
        int ticks = difference % this.totalTicks;

        Map<Integer, TaskList> bucket = this.getSpecBucket(ticks);
        if (bucket == null) {
            return false;
        }
        TaskList taskList = bucket.get(round);
        if (taskList == null) {
            return false;
        }

        AtomicBoolean monitor = taskList.getMonitor();
        long startTime = System.nanoTime();
        while (monitor.compareAndSet(false, true)) {
            if (System.nanoTime() - startTime >= DEFAULT_MAX_WAIT_TIME) {
                return false;
            }
        }
        boolean result = taskList.removeTask(id);
        monitor.set(false);
        return result;
    }

    /**
     * ʱ���ֽ���һ��ת���������ֳ��Ի�ȡ���������ʱ���Բ���ʱ��������Ƽ�����ʧ�ͻָ����������ִ��
     */
    @Override
    public void turn() {
        AtomicBoolean monitor = this.linkedRing.getMonitor();
        long startTime = System.nanoTime();

        while (monitor.compareAndSet(false, true)) {}

        long intervalNanos = this.interval * 1000000L;
        long timeWasteNanos = System.nanoTime() - startTime;
        if (timeWasteNanos > intervalNanos) {
            this.recoverTimeWheel((int) (timeWasteNanos / intervalNanos));
        } else {
            this.linkedRing.moveNext();
        }

        Map<Integer, TaskList> map = this.linkedRing.getPointer();
        monitor.set(false);

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
        AtomicBoolean monitor = this.linkedRing.getMonitor();
        long startTime = System.nanoTime();

        while (monitor.compareAndSet(false, true)) {
            if (System.nanoTime() - startTime >= DEFAULT_MAX_WAIT_TIME) {
                return null;
            }
        }

        for (int i = 0; i < ticks; i++) {
            this.linkedRing.moveNext();
        }
        bucket = this.linkedRing.getPointer();
        for (int i = 0; i < ticks; i++) {
            this.linkedRing.movePrevious();
        }

        monitor.set(false);
        return bucket;
    }

    /**
     * ����ʱ��������ƣ����㳬ʱʱ�䣬�������ڵ�����ָ�ִ�У����Ұ��ճ�ʱʱ����������round
     *
     * @param wasteTicks ��ʧ�Ŀ̶���
     */
    private void recoverTimeWheel(int wasteTicks) {

    }

}
