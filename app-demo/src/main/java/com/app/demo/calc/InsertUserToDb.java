package com.app.demo.calc;

import com.app.service.UserService;
import com.app.user.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RecursiveTask;

/**
 * 描述：<br>
 * 版权：Copyright (c) 2011-2019<br>
 * 公司：北京活力天汇<br>
 * 作者：曹孝欢<br>
 * 版本：1.0<br>
 * 创建日期：2022/4/3<br>
 */
@Slf4j
@Service
public class InsertUserToDb extends RecursiveTask<Integer> {
    private  int start;
    private  int end;
    @Autowired
    UserService userService;
    private int minTaskNum = 100;
    public InsertUserToDb(){}
    public InsertUserToDb(UserService userService,int start, int end){
        this.start = start;
        this.end = end;
        this.userService = userService;
    }
    public static final int LEN = 1000;
    static List<SysUser> users = new ArrayList<>(LEN);
    static Queue<SysUser> userQueue = new LinkedBlockingQueue<>(LEN);

    @Override
    protected Integer compute() {

        if ((end-start) < minTaskNum){
            List<SysUser> userList = new ArrayList<>();
            for (int i = start; i <= end; i++) {
                userList.add(users.get(i));
            }
            userService.saveBatch(userList);
            log.info("当前插入的用户：{}", userList);
            userList.clear();
        } else {
                int middle = (start + end)/2;
                InsertUserToDb taskLeft = new InsertUserToDb(userService,start, middle);
                InsertUserToDb taskRight = new InsertUserToDb(userService,middle+1, end);
                taskLeft.fork();
                taskRight.fork();
                taskLeft.join();
                taskRight.join();
//            invokeAll(taskLeft, taskRight); 等同于taskLeft.fork();taskRight.fork();
        }
        return null;
    }


    static {
        for (int p = 1; p <= LEN; p++) {
            userQueue.add(new SysUser("张飞—"+p, "1000-"+p));
            users.add(new SysUser("张飞—"+p, "1000-"+p));
        }
    }

    public static void main(String[] args) {
//        ForkJoinPool forkJoinPool = new ForkJoinPool();
//        InsertUserToDb task = new InsertUserToDb(0, users.size());
//        Integer invoke = forkJoinPool.invoke(task);
//        System.out.println(invoke);
    }
}
