package com.opcooc.storage.utils;

import com.opcooc.storage.support.StorageAttribute;
import org.springframework.core.NamedThreadLocal;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 核心基于ThreadLocal的切换存储配置工具类
 */
public final class StorageAttributeContextHolder {

    /**
     * 为什么要用链表存储(准确的是栈)
     * <pre>
     * 为了支持嵌套切换，如ABC三个service都是不同的存储配置
     * 其中A的某个业务要调B的方法，B的方法需要调用C的方法。一级一级调用切换，形成了链。
     * 传统的只设置当前线程的方式不能满足此业务需求，必须使用栈，后进先出。
     * </pre>
     */
    private static final ThreadLocal<Deque<StorageAttribute>> LOOKUP_KEY_HOLDER = new NamedThreadLocal<Deque<StorageAttribute>>("opcooc-storage") {
        @Override
        protected Deque<StorageAttribute> initialValue() {
            return new ArrayDeque<>();
        }
    };

    private StorageAttributeContextHolder() {
    }

    /**
     * 获得当前线程存储配置
     *
     * @return 存储配置名称
     */
    public static StorageAttribute peek() {
        return LOOKUP_KEY_HOLDER.get().peek();
    }

    /**
     * 获得当前线程Client配置
     *
     * @return Client配置名称
     */
    public static String client() {
        StorageAttribute attr = peek();
        return attr == null ? null : attr.getClient();
    }

    /**
     * 获得当前线程Bucket配置
     *
     * @return Bucket配置名称
     */
    public static String bucket() {
        StorageAttribute attr = peek();
        return attr == null ? null : attr.getBucket();
    }

    /**
     * 设置当前线程存储配置
     * <p>
     * 如非必要不要手动调用，调用后确保最终清除
     * </p>
     *
     * @param attr 存储配置名称
     */
    public static void push(StorageAttribute attr) {
        LOOKUP_KEY_HOLDER.get().push(attr == null ? StorageAttribute.builder().build() : attr);
    }

    /**
     * 清空当前线程存储配置
     * <p>
     * 如果当前线程是连续切换存储配置 只会移除掉当前线程的存储配置名称
     * </p>
     */
    public static void poll() {
        Deque<StorageAttribute> deque = LOOKUP_KEY_HOLDER.get();
        deque.poll();
        if (deque.isEmpty()) {
            LOOKUP_KEY_HOLDER.remove();
        }
    }

    /**
     * 强制清空本地线程
     * <p>
     * 防止内存泄漏，如手动调用了push可调用此方法确保清除
     * </p>
     */
    public static void clear() {
        LOOKUP_KEY_HOLDER.remove();
    }
}
