package util;

/**
 * @author yanfb
 */
public interface Listenable<T,E extends Enum<E>> {

    /**
     * 添加监听器
     * @param listener 监听器
     * @param eventType 事件类型
     * @param async 异步执行
     * @return
     */
    boolean addListener(Listener<T> listener, E eventType, boolean async);

    /**
     * 删除监听器
     * @param listener 监听器
     * @param eventType 事件类型
     * @return
     */
    boolean removeListener(Listener<T> listener, E eventType);

    /**
     * 通知事件
     * @param t 监听对象关联对象
     * @param eventType 事件类型
     */
    void notify(T t, Enum<E> eventType);


}
