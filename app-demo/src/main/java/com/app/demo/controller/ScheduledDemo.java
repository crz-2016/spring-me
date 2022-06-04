package com.app.demo.controller;

import com.app.demo.RedisUtil;
import com.app.demo.mail.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


/**
 * 功能描述: 邮件数据同步 * * @author caoxiaohuan * @date 2022/4/18
 */
//@Configuration
@Slf4j
public class ScheduledDemo  {

    @Scheduled(cron = "0 0/1 * * * ?")
    public void taskReceiveMails() throws Exception {
        Email.getEamil();
//        Email.sendMail("abc", "123", "hshhcxh_2016@163.com");
    }

}