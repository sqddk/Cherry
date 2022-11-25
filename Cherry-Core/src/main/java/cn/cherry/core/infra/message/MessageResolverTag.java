package cn.cherry.core.infra.message;

import java.lang.annotation.*;

/**
 * 给{@link MessageResolver}用的，加上后允许命令解析器被{@link MessageResolver#load(String)}扫描和加载
 * 
 * @author realDragonKing 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MessageResolverTag {

    /**
     * 命令{@link Message}的类型标志位
     *
     * @return 类型标志位
     */
    int flag();

}
