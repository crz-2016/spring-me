package com.app.demo.mail;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.protocol.IMAPProtocol;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.ReceivedDateTerm;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 描述：<br>
 * 版权：Copyright (c) 2011-2019<br>
 * 公司：北京活力天汇<br>
 * 作者：曹孝欢<br>
 * 版本：1.0<br>
 * 创建日期：2022/4/18<br>
 */
//@Component
@Data
public class Email {

   static Session mailConnection;
    public static final String HOST = "imap.163.com";
    public static final String OUT_HOST = "smtp.163.com";
    public static final String IN_PROTOCOL = "imap";
    public static final String OUT_PROTOCOL = "smtp";
    public static final String ACCOUNT = "hshhcxh_2016@163.com";
    public static final String CODE = "KGDGVKLATHWJXJWK";
    public static final String PASSWORD = "KGDGVKLATHWJXJWK";
    public static final Integer IN_PORT = 143;
    public static final Integer OUT_PORT = 465;

    static Map<String, Transport> transportMap = new ConcurrentHashMap<>();
    static Map<String, Store> sessionMap = new ConcurrentHashMap<>();
    static SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
    public static void main(String[] args) throws Exception {
//        getEamil();
//        sendMail("x测试邮件","234768678",ACCOUNT);
    }

    public static String getEamil() throws MessagingException {
        Store store = sessionMap.get("hshhcxh_2016@163.com");
        System.out.println("session="+store+" 【 "+sdf.format(new Date())+"】");
        if(store == null){
            store = getStore();
            sessionMap.put("hshhcxh_2016@163.com", store);
        } else {
//            if(!store.isConnected()){
//                store = getStore();
//            }
        }

        Folder folder = store.getFolder("INBOX");
        if (folder instanceof IMAPFolder) {
            IMAPFolder imapFolder = (IMAPFolder)folder;
            //javamail中使用id命令有校验checkOpened, 所以要去掉id方法中的checkOpened();
            imapFolder.doCommand(new IMAPFolder.ProtocolCommand() {
                public Object doCommand(IMAPProtocol p) throws com.sun.mail.iap.ProtocolException {
                    p.id("FUTONG");
                    return null;
                }
            });
        }
        if(folder != null) {
            folder.open(Folder.READ_WRITE);
        }
        // 以下为添加根据时间筛选邮件的条件
        Calendar calendar = Calendar.getInstance();
        // 搜索3天前到今天收到的的所有邮件，根据时间筛选邮件
        calendar.add(Calendar.DAY_OF_MONTH, -90);
        // 创建ReceivedDateTerm对象，ComparisonTerm.GE（大于等于），Date类型的时间 new Date(calendar.getTimeInMillis())----（表示3天前）
        ReceivedDateTerm term = new ReceivedDateTerm(ComparisonTerm.GE, new Date(calendar.getTimeInMillis()));
        // 把时间筛选条件添加到收件箱文件夹里，得到3天前到今天的所有邮件
        Message[] messages = folder.search(term);

        // Message[] message = folder.getMessages(); 这个是获取收件箱里所有邮件
//        Message[] messages = folder.getMessages();
        UIDFolder uf = (UIDFolder)folder;
        Map<Long, Message> map = new HashMap<>();
        for(Message message: messages){

            long uid = uf.getUID(message);
            MimeMessage mess = (MimeMessage) message;;
            System.out.println("邮件【" + uid + "】主题:" + message.getSubject());
            System.out.println("邮件【" + uid + "】发送时间:" + message.getSentDate());
//            System.out.println("邮件【" + uid + "】是否需要回复:" + message.getR());
//            System.out.println("邮件【" + uid + "】是否已读:" + mess.isNew());
//            System.out.println("邮件【" + uid + "】是否包含附件:" + message.isContainAttach( message));
            System.out.println("邮件【" + uid + "】发送人地址:" + message.getFrom());
//            System.out.println("邮件【" + uid + "】收信人地址:" + message.getMailAddress("to"));
//            System.out.println("邮件【" + uid + "】抄送:" + message.getMailAddress("cc"));
//            System.out.println("邮件【" + uid + "】暗抄:" + message.getMailAddress("bcc"));
            System.out.println("邮件【" + uid + "】发送时间:" + message.getSentDate());
            System.out.println("邮件【" + uid + "】邮件ID:" + ((MimeMessage) message).getMessageID());
//            message.getMailContent(message);

//            map.put(uid, message);
        }
//        store.close();
        System.out.println(map);
        return null;
    }

    private static Store getStore() throws MessagingException {
        Session session;
        Store store;
        Properties props = new Properties();
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.debug", "true");
        props.put("mail.debug.auth", "true");
        props.put("mail.imap.auth.xoauth2.disable", "true");
        props.put("mail.imap.minidletime", 120000);
        props.put("mail.imap.pruninginterval", 1400000);
        props.put("mail.imap.statuscachetimeout", 1600000);
        props.put("mail.imap.connectionpooltimeout", 1800000);
        session = Session.getInstance(props);
        store = session.getStore("imap");
        store.connect(HOST, IN_PORT,ACCOUNT,PASSWORD);
        System.out.println("=========> store 连接成功 <========" + sdf.format(new Date()));
        return store;
    }

    /**
     * 发送邮件
     * @param subject  邮件主题
     * @param content  邮件内容（支持HTML）
     * @param toEmailAddres  收件人
     * @throws Exception
     * @author Monk
     * @date 2019年5月22日 下午6:27:27
     */
    public static  void sendMail(String subject, String content, String toEmailAddres) throws Exception {
        String mailDebug = "false";                //是否开启debug
        String contentType = null;                 //邮件正文类型
        Session mailConn = null;
        Properties props = getProperties();

        Transport transport = transportMap.get("hshhcxh_2016@163.com");
        System.out.println("【取出的transport "+transport+"】");
        if(transport == null){
            mailConn = initMailSession();
        } else {
//            if(!transport.isConnected()){
//                mailConn = initMailSession();
//            }
        }
        transport = mailConn.getTransport("smtp");
                // 设置session,和邮件服务器进行通讯。
        MimeMessage message = new MimeMessage(mailConn);
        // 设置邮件主题
        message.setSubject(subject);
        // 设置邮件正文
        message.setContent(content, contentType == null ? "text/html;charset=UTF-8" : contentType);
        // 设置邮件发送日期
        message.setSentDate(new Date());
        InternetAddress address = new InternetAddress(ACCOUNT, "zsz");
        // 设置邮件发送者的地址
        message.setFrom(address);
        // 设置邮件接收方的地址
        message.setRecipients(Message.RecipientType.TO, toEmailAddres);
//        Transport transport = null;
//        transport = mailConn.getTransport("smtp");

        long l = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        transport.connect(OUT_HOST, ACCOUNT, CODE);
        System.out.println("邮件连接耗时："+(System.currentTimeMillis() - l)/1000);
        transport.sendMessage(message, message.getAllRecipients());
        System.out.println("邮件发送成功 耗时："+(System.currentTimeMillis() - l)/1000);
        System.out.println("time1:"+  simpleDateFormat.format(new Date())+"  mailConn: isConnected :"+mailConn.getStore("imap").isConnected());

        Thread.sleep(60000);
        System.out.println("mailConn:"+mailConn);
        System.out.println("mailConn: imap: "+mailConn.getStore("imap"));

        System.out.println("time2:"+  simpleDateFormat.format(new Date())+"  mailConn: isConnected :"+mailConn.getStore("imap").isConnected());
       // transport.close();
    }

    private static Properties getProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.host", OUT_HOST);
        props.put("mail.smtp.auth", "true");
        props.put("mail.transport.protocol",  "smtp" );
        props.put("mail.smtp.port","465");
        props.put("mail.smtp.ssl.enable", "true");

//        props.put("mail.store.protocol", "imap");
        props.put("mail.imap.host", HOST);
        props.put("mail.imap.port","143");
        props.put("mail.imap.auth.xoauth2.disable", "false");
        props.put("mail.imap.minidletime", "600000");
        props.put("mail.debug", "true");
        props.put("mail.debug.auth", "true");
        return props;
    }

    public static Session initMailSession(){
        Properties properties = getProperties();
        mailConnection = Session.getInstance(properties);
        System.out.println("=========> 初始化tansport <======== ["+sdf.format(new Date())+"]");
       return  mailConnection;
    }
}
