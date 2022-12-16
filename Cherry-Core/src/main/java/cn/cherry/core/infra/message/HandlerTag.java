package cn.cherry.core.infra.message;

import java.lang.annotation.*;

/**
 * 给{@link MessageHandler}用的，加上后允许被消息解析器{@link MessageHandler#load()}扫描和加载
 * 
 * @author realDragonKing 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HandlerTag {

    /**
     * 消息的类型标志
     *
     * @return 类型标志
     */
    int type();

}
