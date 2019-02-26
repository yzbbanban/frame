package com.yzb;

import com.yzb.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FrameApplicationTests {

    @Test
    public void contextLoads() {
        User user = new User();
        user.setMobile("121312");
        user.setNickName("afsefe");
        user.setPassword("2rwsef");
        user.setUsername("24142");
        user.setLock(false);
        System.out.println(user.toString());
    }


    public static void main(String[] args) {

        SemaphoreService service = new SemaphoreService();
        for (int i = 0; i < 10; i++) {
            MyThread t = new MyThread("thread" + (i + 1), service);
            t.start();// 这里使用 t.run() 也可以运行，但是不是并发执行了
            System.out.println("可用通路数：" + service.availablePermits());
        }

    }
}

