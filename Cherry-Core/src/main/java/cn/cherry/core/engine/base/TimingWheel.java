package cn.cherry.core.engine.base;

/**
 * 时间轮的顶层接口，仅允许被转动
 *
 * @author realDragonKing
 */
public interface TimingWheel {

    /**
     * 时间轮进行一次转动
     */
    void turn();

}
