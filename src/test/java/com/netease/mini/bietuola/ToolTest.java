package com.netease.mini.bietuola;

import com.netease.mini.bietuola.config.redis.RedisService;
import com.netease.mini.bietuola.config.redis.component.RedisLock;
import com.netease.mini.bietuola.web.util.HttpClientUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/5/2
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ToolTest {

    @Autowired
    private RedisService redisService;

    @Test
    public void testRedisLock() {
        Window w = new Window(redisService);
        Thread t1 = new Thread(w);
        Thread t2 = new Thread(w);
        Thread t3 = new Thread(w);

        t1.setName("窗口1");
        t2.setName("窗口2");
        t3.setName("窗口3");

        t1.start();
        t2.start();
        t3.start();

        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRedisLock2() {
        CyclicBarrier cb = new CyclicBarrier(100);
        Runnable task = new Runnable() {
            int n = 0;
            @Override
            public void run() {
                try {
                    cb.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
                RedisLock lock = redisService.getLock("test_key");
                boolean b = lock.tryLock(10, 10);
                if (b) {
                    try {
                        n = n + 1;
//                        try {
//                            Thread.sleep(100);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
                        System.out.println(Thread.currentThread().getName() + ": " + n);
                    } finally {
                        lock.unlock();
                    }
                }
            }
        };
        for (int i = 0; i < 100; i++) {
            new Thread(task).start();
        }
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRedisLockReenter() {
        RedisLock lock = redisService.getLock("reenter_key");
        boolean b = lock.tryLock(10, 10);
        if (b) {
            try {
                System.out.println(Thread.currentThread().getName() + ": enter1 :" + lock.getEnterTime());

                RedisLock lock2 = redisService.getLock("reenter_key");
                System.out.println(lock == lock2);
                boolean b2 = lock2.tryLock(10, 10);
                if (b2) {
                    try {
                        System.out.println(Thread.currentThread().getName() + ": enter2 :" + lock2.getEnterTime());
                    } finally {
                        lock2.unlock();
                        System.out.println(Thread.currentThread().getName() + ": out2 :" + lock2.getEnterTime());
                    }
                }
            } finally {
                lock.unlock();
                System.out.println(Thread.currentThread().getName() + ": out1 :" + lock.getEnterTime());
            }
        }
    }

}

class Window implements Runnable {
    int ticket = 100;
    RedisService redisService;

    public Window(RedisService redisService) {
        this.redisService = redisService;
    }

    public void run() {
        while (true) {
            RedisLock lock = redisService.getLock("ticket_key");
            boolean b = lock.tryLock(10, 10);
            if (b) {
                try {
                    if (ticket > 0) {
                        //放大错误代码
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        System.out.println(Thread.currentThread().getName() + "售票，票号为："
                                + ticket--);
                    } else {
                        break;
                    }
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}
