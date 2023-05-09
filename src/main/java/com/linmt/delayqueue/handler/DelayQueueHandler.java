package com.linmt.delayqueue.handler;

import com.linmt.delayqueue.enums.DelayQueueTypes;
import org.springframework.plugin.core.Plugin;

public interface DelayQueueHandler extends Plugin<DelayQueueTypes> {
    void execute(Object data);
}
