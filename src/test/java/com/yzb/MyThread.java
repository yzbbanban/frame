package com.yzb;

/**
 * Created by Administrator on 2019/2/25.
 */
public class MyThread extends Thread {
    private SemaphoreService service;

    public MyThread(String name, SemaphoreService service) {
        super();
        this.setName(name);
        this.service = service;
    }

    @Override
    public void run() {
        this.service.doSomething();
    }
}
