package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.*;

/**
 * @author yanfb
 */
public final class Listeners<T, E extends Enum<E>> {

    private static final Logger logger = LoggerFactory.getLogger(Listeners.class);

    private final ConcurrentHashMap<Enum<E>, List<Listener<T>>> listenersMap = new ConcurrentHashMap<Enum<E>, List<Listener<T>>>();
    private final ConcurrentHashMap<Enum<E>, List<Listener<T>>> asyncListenersMap = new ConcurrentHashMap<Enum<E>, List<Listener<T>>>();

    /**
     * 添加监听器
     *
     * @param listener
     * @param eventType
     * @param async
     * @return
     */
    public boolean addListener(Listener<T> listener, Enum<E> eventType, boolean async) {
        synchronized (eventType) {
            if (async) {
                return addListener(asyncListenersMap, eventType, listener);
            }

            return addListener(listenersMap, eventType, listener);
        }
    }

    private boolean addListener(ConcurrentHashMap<Enum<E>, List<Listener<T>>> listenersMap, Enum<E> eventType, Listener<T> listener) {
        List<Listener<T>> listeners = listenersMap.get(eventType);
        if (listeners == null) {
            listeners = new CopyOnWriteArrayList<Listener<T>>();
            listenersMap.put(eventType, listeners);
        }
        return listeners.add(listener);
    }

    /**
     * 删除监听器
     *
     * @param listener
     * @param eventType
     * @return
     */
    public boolean removeListener(Listener<T> listener, Enum<E> eventType) {
        synchronized (eventType) {
            List<Listener<T>> listeners = listenersMap.get(eventType);
            if (listeners != null) {
                return listeners.remove(listener);
            }
        }
        return false;
    }

    private static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

    /**
     * 通知事件
     *
     * @param t
     * @param eventType
     */
    public void notify(T t, Enum<E> eventType) {
        List<Listener<T>> listeners = listenersMap.get(eventType);
        if (listeners != null) {
            for (Listener<T> listener : listeners) {
                try {
                    listener.notify(t);
                } catch (Exception e) {
                    logger.error("同步执行监听" + listener.getClass().getName() + "事件" + eventType + "失败", e);
                }
            }
        }

        listeners = asyncListenersMap.get(eventType);
        if (listeners != null) {
            for (Listener<T> listener : listeners) {
                executor.schedule(() -> {
                    try {
                        listener.notify(t);
                    } catch (Exception e) {
                        logger.error("异步执行监听" + listener.getClass().getName() + "事件" + eventType + "失败", e);
                    }
                }, 5L, TimeUnit.SECONDS); // 延迟5s执行，避免事务未提交
            }
        }
    }

}
