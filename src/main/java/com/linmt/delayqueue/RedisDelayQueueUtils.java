package com.linmt.delayqueue;


import com.linmt.delayqueue.enums.DelayQueueTypes;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;


@Component
public class RedisDelayQueueUtils {
    private static final Logger logger = LoggerFactory.getLogger(RedisDelayQueueUtils.class);

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 加入队列
     *
     * @param t
     * @param l
     * @param unit
     * @param delayQueueType
     * @param <T>
     * @return
     */
    public <T> int addDelayQueue(T t, long l, TimeUnit unit, DelayQueueTypes delayQueueType) {
        try {
            RBlockingQueue<T> rBlockingQueue = redissonClient.getBlockingQueue(delayQueueType.name);
            RDelayedQueue<T> rDelayedQueue = redissonClient.getDelayedQueue(rBlockingQueue);
            rDelayedQueue.offer(t, l, unit);
            logger.info("[{}队列]增加元素[{}], 有效期：{} {}", delayQueueType.name, t.toString(), l, unit.toString());
            return 1;
        } catch (Exception e) {
            logger.error("[{}队列]增加元素失败", delayQueueType.name);
            return -1;
        }
    }

    /**
     * 加入/替换队列（保证唯一）
     *
     * @param t
     * @param l
     * @param unit
     * @param delayQueueType
     * @param <T>
     * @return
     */
    public <T> int addOrUpdateDelayQueue(T t, long l, TimeUnit unit, DelayQueueTypes delayQueueType) {
        try {
            RBlockingQueue<T> rBlockingQueue = redissonClient.getBlockingQueue(delayQueueType.name);
            RDelayedQueue<T> rDelayedQueue = redissonClient.getDelayedQueue(rBlockingQueue);
            // 先清空再加入
            rDelayedQueue.removeAll(Arrays.asList(t));
            rDelayedQueue.offer(t, l, unit);
            logger.info("[{}队列]增加元素[{}], 有效期：{}", delayQueueType.name, t.toString(), l);
            return 1;
        } catch (Exception e) {
            logger.error("[{}队列]增加元素失败", delayQueueType.name);
            return -1;
        }
    }

    /**
     * 获取队列
     *
     * @param delayQueueType
     * @return
     * @throws InterruptedException
     */
    public <T> T getDelayQueue(DelayQueueTypes delayQueueType) throws InterruptedException {
        if (redissonClient != null) {
            RBlockingDeque<T> blockingDeque = redissonClient.getBlockingDeque(delayQueueType.name);
            redissonClient.getDelayedQueue(blockingDeque);
            return blockingDeque.take();
        }
        return null;
    }
}
