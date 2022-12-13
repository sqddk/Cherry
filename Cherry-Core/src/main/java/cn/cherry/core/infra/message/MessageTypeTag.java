package cn.cherry.core.infra.message;

import java.lang.annotation.*;

/**
 * 给{@link MessageResolver}用的，加上后允许被
 * 消息解析器{@link MessageResolver#load()}和消息处理器{@link MessageHandler#load()}扫描和加载
 * 
 * @author realDragonKing 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MessageTypeTag {

    /**
     * 消息{@link Message}的类型标志位
     *
     * @return 类型标志位
     */
    int flag();

}
