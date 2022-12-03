package cn.cherry.core.engine.base;

import cn.cherry.core.engine.base.struct.DefaultPointerLinkedList;
import cn.cherry.core.infra.Task;
import java.util.ArrayList;

/**
 * 作为装载{@link Task}任务的容器，{@link TaskList}继承了{@link DefaultPointerLinkedList}，使用pointer指针来读写节点。<br/>
 * 同为线性数据结构，这里不使用{@link ArrayList}的原因是：从读写操作上来看，链表的写操作非常快；而两者的顺序读操作几乎没有性能差异。
 *
 * @author realDragonKing
 */
public class TaskList extends DefaultPointerLinkedList<Task> {
}
