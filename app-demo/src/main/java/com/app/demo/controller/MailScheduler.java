package com.app.demo.controller;

import com.app.demo.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
//import com.sungo.common.core.domain.entity.SysUser;
//import com.sungo.common.enums.EnableEnum;
//import com.sungo.common.exception.CustomException;
//import com.sungo.common.utils.DateUtils;
//import com.sungo.daf.common.util.lang.StringUtil;
//import com.sungo.dingtalk.handler.DingtalkUserHandler;
//import com.sungo.system.domain.MailConfig;
//import com.sungo.system.domain.MailProperties;
//import com.sungo.system.domain.MailReceive;
//import com.sungo.system.domain.UserNotify;
//import com.sungo.system.service.IMailConfigService;
//import com.sungo.system.service.IMailInboxService;
//import com.sungo.system.service.ISysUserService;
//import com.sungo.system.service.MailUserNotifyService;
//import com.sungo.system.service.impl.MailReceiveServiceImpl;
import lombok.extern.slf4j.Slf4j;
//import org.redisson.api.RBloomFilter;
//import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.AuthenticationFailedException;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 功能描述: 邮件数据同步 * * @author caoxiaohuan * @date 2022/4/18
 */
//@Configuration
//@EnableScheduling
//@EnableAsync
@Slf4j
public class MailScheduler implements InitializingBean {
//    @Resource
//    private IMailConfigService mailConfigService;
//    @Resource
//    private MailReceiveServiceImpl mailReceiveService;
//    @Resource
//    private IMailInboxService mailInboxService;
//    @Resource
//    private ISysUserService userService;
//    @Resource
//    private DingtalkUserHandler dingtalkUserHandler;

    @Autowired
    private RedisUtil util;
//    @Autowired
//    MailUserNotifyService mailUserNotifyService;
//    @Autowired
//    private RedissonClient redissonClient;
    public static final String SYN_MAIL_KEY = "mail_key";

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(5);
        return taskScheduler;
    }

//    public boolean isWait() {
//        synchronized (this) {
//            if (redisTemplate.hasKey(SYN_MAIL_KEY)) {
//                log.error("当前等待线程id:{} ：tname:{}", Thread.currentThread().getId(), Thread.currentThread().getName());
//                return true;
//            } else {
//                log.error("当前执行线程id:{} ：tname:{}", Thread.currentThread().getId(), Thread.currentThread().getName());
//                redisTemplate.opsForValue().set(SYN_MAIL_KEY, "mail", 30, TimeUnit.MINUTES);
//                return false;
//            }
//        }
//    }

//    public void clearKey() {
//        redisTemplate.delete(SYN_MAIL_KEY);
//    }

    /**
     * 定时获取所有用户的收件箱未读邮件
     */
//    @Scheduled(cron = "0/5 * * * * ?")
//    @Async
    public void taskReceiveMails() throws Exception {
        if (util.getLock("getData","getting!",5)) {
//            List<MailConfig> list = mailConfigService.getConfig();
            Map<String, List<String>> newMailNoticeMap = new HashMap<>();
            Set<String> authenticationFailedSet = new HashSet<>();
            List<String> storeMailsList = new ArrayList<>();
//            for (MailConfig mailConfig : list) {
//                Long accountId = mailConfig.getAccountId();
//                String mailName = mailConfig.getMailName();
//                MailProperties.Mail mailProps = new MailProperties.Mail();
//                mailProps.setInProtocol(mailConfig.getInProtocol());
//                mailProps.setInPort(mailConfig.getInPort());
//                mailProps.setInHost(mailConfig.getInHost());
//                List<MailReceive> mailReceives = new ArrayList<>();
//                QueryWrapper<SysUser> userQuery = Wrappers.query();
//                userQuery.select("ding_talk_user_id dingTalkUserId").in("user_id", accountId);
//                SysUser currentUser = userService.getOne(userQuery);
//                String dingTalkUserId = currentUser.getDingTalkUserId();
//                // 暂时先用自己邮箱测试
//                if (mailName.equals("caoxiaohuan@cncharity.com")) {
//                    try {
//                        mailReceives = mailReceiveService.readMail(accountId, mailConfig, false);
//                    } catch (Exception e) {
//                        log.error("[定时任务 | 邮件读取异常]", e);
//                        if (e instanceof AuthenticationFailedException) {
//                            authenticationFailedSet.add(currentUser);
//                        }
//                        clearKey();
//                    }
//                    storeMailsList.addAll(mailReceives);
//                }
//                newMailNoticeMap.put(dingTalkUserId, mailReceives);
//            }
//            RBloomFilter<Object> mailFilter = redissonClient.getBloomFilter("bloom-filter");
//            storeMailsList = storeMailsList.stream().filter(mail -> !mailFilter.contains(mail.getMailUid())).collect(Collectors.toList());
//            System.out.println("l2:" + storeMailsList);
//            storeMailsList.stream().forEach(storeMail -> mailFilter.add(storeMail.getMailUid()));
//            System.out.println("l3:" + storeMailsList);
            try { // 分批保存用户
//                ForkJoinTask task = new MailTask(0, storeMailsList.size(), storeMailsList, mailInboxService);
//                ForkJoinPool pool = ForkJoinPool.commonPool();
//                pool.submit(task);
                // 提醒用户新邮件
//                notifyUserNewMail(newMailNoticeMap);
                // 用户密码更改时提醒
//                QueryWrapper<UserNotify> query = Wrappers.query();
//                query.eq("enable", EnableEnum.ENABLE.getValue());
//                List<Long> noticeUserIds = mailUserNotifyService.list(query).stream().map(userNotify -> userNotify.getAccountId()).collect(Collectors.toList());
//                QueryWrapper<SysUser> userQuery = Wrappers.query();
//                userQuery.select("user_name username, ding_talk_user_id dingTalkUserId");
//                userQuery.in("user_id", noticeUserIds);
//                List<SysUser> userList = userService.list(userQuery);
//                Map<Long, String> userIdMapTalkId = userList.stream().collect(Collectors.toMap(SysUser::getUserId, SysUser::getDingTalkUserId));
//                for (SysUser user : authenticationFailedSet) {
//                    StringBuilder messageAppender = new StringBuilder();
//                    messageAppender.append(DateUtils.getTime()).append("\n 邮件用户为").append(user.getUserName()).append(" 用户名或密码不正确，请及时处理！");
//                    dingtalkUserHandler.sendDingTalkMailNotify(messageAppender.toString(), userIdMapTalkId.get(user.getUserId()));
//                    log.info("邮件用户:{} 用户名或密码不正确!", user.getUserName());
//                }
                int waitTime = ThreadLocalRandom.current().nextInt(10);
                System.out.println("30开始执行，随机等待时间："+waitTime);
//                Thread.sleep(waitTime*1000);

                System.out.println("醒来了。。。");
            } catch (Exception e) {
                log.error("消息发送失败！", e);
            } finally {
                newMailNoticeMap.clear();
                authenticationFailedSet.clear();
                storeMailsList.clear();
//                clearKey();

            }
            util.releaseLock("getData");
            System.out.println("30释放了锁");
            newMailNoticeMap.clear();
            authenticationFailedSet.clear();
            storeMailsList.clear();
//            clearKey();
        }
    }

//    private void notifyUserNewMail(Map<String, List<MailReceive>> newMailNoticeMap) {
//        try {
//            for (Map.Entry entry : newMailNoticeMap.entrySet()) {
//                String dingTalkUserId = (String) entry.getKey();
//                List<MailReceive> mailReceiveList = (List<MailReceive>) entry.getValue();
//                for (MailReceive receive : mailReceiveList) {
//                    StringBuilder messageAppender = new StringBuilder();
//                    messageAppender.append(DateUtils.getTime()).append(" 您有一封新的邮件，请查收！\n").append(receive.getSenderMail()).append("\n ").append(receive.getSubject()).append("\n ");
//                    if (StringUtil.isNotEmpty(dingTalkUserId)) {
//                        dingtalkUserHandler.sendDingTalkMailNotify(messageAppender.toString(), dingTalkUserId);
//                        log.info("发送钉钉消息成功！");
//                    }
//                }
//            }
//        } catch (Exception e) {
//            log.error("新邮件钉钉通知发送失败", e);
//            throw new CustomException("新邮件钉钉通知发送失败！", e);
//        } finally {
//            clearKey();
//        }
//    }


    /**
     * @Description: 全量同步邮件 * @return void * @Author caoxiaohuan * @Date 2022/4/18
     **/
//    @Scheduled(cron = "* * * * * ?")
//    @Async
    public void mailTask() throws Exception {
        if (util.getLock("getData","getting!",5)) {
//            List<MailConfig> list = mailConfigService.getConfig();
//            List<MailReceive> mailReceives = new ArrayList<>();
//            List<MailReceive> storeMailList = new ArrayList<>();
            try {
//                for (MailConfig mailConfig : list) {
//                    Long accountId = mailConfig.getAccountId();
//                    String mailName = mailConfig.getMailName();
//                    MailProperties.Mail mailProps = new MailProperties.Mail();
//                    mailProps.setInProtocol(mailConfig.getInProtocol());
//                    mailProps.setInPort(mailConfig.getInPort());
//                    mailProps.setInHost(mailConfig.getInHost()); // 暂时先用自己邮箱测试
//                    if (mailName.equals("caoxiaohuan@cncharity.com")) {
//                        mailReceives = mailReceiveService.readMail(accountId, mailConfig, true);
//                        storeMailList.addAll(mailReceives);
//                    }
//                }
//                RBloomFilter<Object> mailFilter = redissonClient.getBloomFilter("bloom-filter");
//                storeMailList.stream().forEach(mail -> {
//                    log.info("======================================");
//                    log.info("是否包含" + mail.getMailUid() + ":{}", mailFilter.contains(mail.getMailUid()));
//                    log.info("插入个数：", mailFilter.count());
//                    log.info("======================================");
//                });
//                storeMailList = storeMailList.stream().filter(mail -> !mailFilter.contains(mail.getMailUid())).collect(Collectors.toList());
//                storeMailList.stream().forEach(storeMail -> {
//                    mailFilter.add(storeMail.getMailUid());
//                });
//                if (CollectionUtils.isEmpty(storeMailList)) {
//                    log.info("暂无需要同步的数据");
//                    return;
//                } // forkjoin拆分任务调用
//                ForkJoinTask task = new MailTask(0, storeMailList.size(), storeMailList, mailInboxService);
//                ForkJoinPool pool = ForkJoinPool.commonPool();
//                pool.submit(task);
//                storeMailList.clear();
//                mailReceives.clear();
                int waitTime = ThreadLocalRandom.current().nextInt(5, 10);
//                System.out.println("50开始执行, 随机等待时间："+waitTime);
//                Thread.sleep(waitTime*1000);
//                System.out.println("50醒来");
//                util.releaseLock("getData");
                System.out.println("50释放了锁");
            } catch (Exception e) {
                log.error("入库失败！", e);
            } finally {
//                storeMailList.clear();
//                mailReceives.clear();
//                clearKey();
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        util.releaseLock(SYN_MAIL_KEY);
    }
}