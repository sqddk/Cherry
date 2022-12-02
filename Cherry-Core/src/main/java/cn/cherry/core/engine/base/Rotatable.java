package cn.cherry.core.engine.base;

/**
 * {@link Rotatable}接口定义了{@link #turn()}方法，其实现类是允许被调用{@link #turn()}方法转动的
 *
 * @author realDragonKing
 */
public interface Rotatable {

    /**
     * 时间轮进行一次转动
     */
    void turn();

}
