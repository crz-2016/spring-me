package com.app;

import org.junit.Test;

import java.util.Date;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendMail {
    public SendMail(){}
    private Session session;        //会话
    private Transport transport;    //发送邮件
    private final String MAIL_SMTP_HOST = "mail.smtp.host";
    private final String MAIL_SMTP_AUTH = "mail.smtp.auth";
    private static Properties props = new Properties();
    static SendMail sendMail = null;
    static {
        sendMail = new SendMail("hshhcxh_2016@163.com","KGDGVKLATHWJXJWK","smtp.163.com", true);

    }
    public static void main(String[] args) throws MessagingException {
//        new SendMail("hshhcxh_2016@163.com","KGDGVKLATHWJXJWK","smtp.163.com", true);
        Message message = sendMail.createMessage("hshhcxh_2016@163.com","hshhcxh_2016@163.com","我是title","我是内容","",true,false,false, "","");
        sendMail.send(message);
    }

    @Test
    public  void props() throws MessagingException {
//        createMessage("hshhcxh_2016@163.com","hshhcxh_2016@163.com","我是title","我是内容","",true,false,false, "","");

//         SendMail sendMail = new SendMail(Config.getSmtpName(), Config.getSmtpPassword(), Config.getSmtpHost(), true);
//        SendMail sendMail = new SendMail("hshhcxh_2016@163.com","KGDGVKLATHWJXJWK","smtp.163.com", true);
        // 发送邮件，可以发送任意多次
//         Message message = sendMail.createMessage(Config.getSmtpFrom(), email, title, body, null, true, false, true,"","");
        Message message = sendMail.createMessage("hshhcxh_2016@163.com","hshhcxh_2016@163.com","我是title","我是内容","",true,false,false, "","");
        sendMail.send(message);//最后关闭连接sendMail.close();

    }

    static {
        if (props == null) {
            props = System.getProperties();
        }
    }

    public   SendMail(String userName, String password, String host, boolean needAuth) {
        init(userName, password, host, needAuth);
    }

    /**
     * 初始化<code> Session, Transport </code>
     */
    private void init(String userName, String password, String host, boolean needAuth) {
        SmtpAuth sa = null;
        if (needAuth) {
            sa = new SmtpAuth(userName, password);            // 需要经过授权，也就是有户名和密码的校验，这样才能通过验证（一定要有这一条）
            props.put(MAIL_SMTP_AUTH, "true");
        } else {
            props.put(MAIL_SMTP_AUTH, "false");
        }       // 设置发送邮件的邮件服务器的属性（这里使用网易的smtp服务器）
        props.put(MAIL_SMTP_HOST, host);       // 用刚刚设置好的props对象构建一个session
        session = Session.getDefaultInstance(props, sa);
        try {
            transport = session.getTransport("smtp");           // 连接服务器的邮箱
            transport.connect(host, userName, password);
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        System.out.println("与 " + host + " 成功建立会话");
    }

    /**
     * <b>登录验证</b>
     */
    class SmtpAuth extends Authenticator {
        private String user, password;

        public SmtpAuth(String getuser, String getpassword) {
            user = getuser;
            password = getpassword;
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(user, password);
        }
    }

    /**
     * 构造邮件的内容     *     * @return     * @throws AddressException     * @throws MessagingException
     */
    public Message createMessage(String from, String to, String title, String content, String cc, boolean isContentFlag, boolean isAffixFlag, boolean isMimeContent, String affix, String affixName) throws AddressException, MessagingException {       // 用session为参数定义消息对象
        MimeMessage message = new MimeMessage(session);       // 加载发件人地址
        message.setFrom(new InternetAddress(from));
        message.setSentDate(new Date());       // 加载收件人地址
        message.addRecipients(Message.RecipientType.TO, getAddress(to));
        if (cc != null)
            message.addRecipients(Message.RecipientType.CC, getAddress(cc));       // 加载标题
        message.setSubject(title);
        if (isContentFlag || isAffixFlag) {           // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
            Multipart multipart = new MimeMultipart();
            if (isContentFlag) {              // 设置邮件的文本内容
                MimeBodyPart contentPart = new MimeBodyPart();
                if (isMimeContent)
                    contentPart.setContent(content, "text/html;charset=GBK");
                else contentPart.setText(content);
                multipart.addBodyPart(contentPart);
            }
            if (isAffixFlag) {              // 添加附件
                BodyPart affixBody = new MimeBodyPart();
                DataSource source = new FileDataSource(affix);              // 添加附件的内容
                affixBody.setDataHandler(new DataHandler(source));
                //   添加附件的标题这里很重要，通过下面的Base64编码的转换可以保证你的
                // 中文附件标题名在发送时不会变成乱码
                sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
                String fileName = "=?GBK?B?" + enc.encode(affixName.getBytes()) + "?=";
                affixBody.setFileName(fileName);
                multipart.addBodyPart(affixBody);
            }           // 将multipart对象放到message中
            message.setContent(multipart);
        }       // 保存邮件
        message.saveChanges();
        return message;
    }

    /**
     * 发送邮件，包含：邮件正文、（1个附件）     *     * @param debug     *            调试设置
     */
    public void send(Message message) {       // 有了这句便可以在发送邮件的过程中在console处显示过程信息，供调试使
        // 用（你可以在控制台（console)上看到发送邮件的过程）
        session.setDebug(true);
        try {
            transport.sendMessage(message, message.getAllRecipients());
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭资源     *     * @throws MessagingException
     */
    public void close() throws MessagingException {
        if (null != transport)
            try {
                transport.close();
                System.out.println("成功关闭会话");
            } catch (Exception e) {
                transport = null;
            }
    }

    public Address[] getAddress(String address) throws AddressException {
        Address[] addr = InternetAddress.parse(address);
        return addr;
    }

    public Address[] getAddress(String[] address) throws AddressException {
        Address[] addrs = new InternetAddress[address.length];
        for (int i = 0; i < address.length; i++)
            addrs[i] = new InternetAddress(address[i]);
        return addrs;
    }



}