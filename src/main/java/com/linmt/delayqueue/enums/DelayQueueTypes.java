package com.linmt.delayqueue.enums;

public enum DelayQueueTypes {
    TASK_1("task-1"),
    TASK_2("task-2");

    public String name;

    DelayQueueTypes(String name) {
        this.name = name;
    }
}
