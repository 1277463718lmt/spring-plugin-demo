package com.linmt.delayqueue;

import com.linmt.delayqueue.enums.DelayQueueTypes;
import com.linmt.delayqueue.handler.DelayQueueHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


@Component
public class DelayQueueHandlerRunner implements CommandLineRunner {
    private ExecutorService delayQueueHandleThreadPool = new ThreadPoolExecutor(5, 20, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    @Autowired
    private PluginRegistry<DelayQueueHandler, DelayQueueTypes> delayQueueTypesPluginRegistry;

    @Autowired
    private RedisDelayQueueUtils redisDelayQueueUtils;

    @Override
    public void run(String... args) throws Exception {
        // 这边添加数据到延迟队列主要是为了测试
        redisDelayQueueUtils.addDelayQueue("这是任务一的数据", 5, TimeUnit.SECONDS, DelayQueueTypes.TASK_1);
        redisDelayQueueUtils.addDelayQueue("这是任务一的数据", 10, TimeUnit.SECONDS, DelayQueueTypes.TASK_1);
        redisDelayQueueUtils.addDelayQueue("这是任务二的数据", 15, TimeUnit.SECONDS, DelayQueueTypes.TASK_2);

        if (delayQueueTypesPluginRegistry == null) return;

        DelayQueueTypes[] delayQueueTypes = DelayQueueTypes.values();
        for (DelayQueueTypes delayQueueType : delayQueueTypes) {
            delayQueueHandleThreadPool.submit(() -> {
                Object data = null;
                try {
                    data = redisDelayQueueUtils.getDelayQueue(delayQueueType);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                List<DelayQueueHandler> delayQueueHandlerList = delayQueueTypesPluginRegistry.getPluginsFor(delayQueueType);
                for (DelayQueueHandler delayQueueHandler : delayQueueHandlerList) {
                    delayQueueHandler.execute(data);
                }
            });
        }
    }
}
