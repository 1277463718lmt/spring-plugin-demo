package com.linmt.delayqueue.handler;

import com.linmt.delayqueue.enums.DelayQueueTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Component
@Order(value = -2)
public class Task2DelayQueueTypesHandler implements DelayQueueHandler {
    private static final Logger log = LoggerFactory.getLogger(Task1DelayQueueTypesHandler.class);

    @Override
    public void execute(Object data) {
        log.info("任务二接收到数据 = {}", data);
    }

    @Override
    public boolean supports(DelayQueueTypes delayQueueType) {
        return DelayQueueTypes.TASK_2 == delayQueueType;
    }
}
