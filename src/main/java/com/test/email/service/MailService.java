package com.test.email.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class MailService {


    //这是一个日志对象，运行的时候会显示在输出台中
    private final Logger log=LoggerFactory.getLogger(this.getClass());

    @Value("${spring.mail.username}")//将配置中的属性注入到这个类中
    private String from;//发送人

    @Autowired
    private JavaMailSender mailSender;
    /**
     * 发送文本邮件
     * @param to  发给谁
     * @param subject  发送主题
     * @param content  发送内容
     */
    public void sendSimpleMail(String to,String subject,String content){

        SimpleMailMessage message=new SimpleMailMessage();//这个是一个发送文本邮件的对象
        message.setTo(to);//发给谁
        message.setSubject(subject);//发送主题
        message.setText(content);//发送内容
        System.out.println(from);
        message.setFrom(from);//谁发的

        mailSender.send(message);//通过mailSender对象将邮件发给目标人
    }

    /**
     * 发送HTML邮件
     * @param to  发给谁
     * @param subject  发送主题
     * @param content  发送内容
     */
    public void sendHtmlMail(String to,String subject,String content)  {
        //创建可以发送h't'm'l类型邮件的类
        MimeMessage message=mailSender.createMimeMessage();



        MimeMessageHelper helper= null;
        try {
            helper = new MimeMessageHelper(message,true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content,true);
            helper.setFrom(from);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        mailSender.send(message);
    }

    /**
     * 发送附件邮件
     * @param to  发给谁
     * @param subject  发送主题
     * @param content  发送内容
     * @param filePath 附件的地址
     */
    public void sendAtachmentsMail(String to,String subject,String content,String filePath)  {
        MimeMessage mimeMessage=mailSender.createMimeMessage();

        MimeMessageHelper helper= null;
        try {
            helper = new MimeMessageHelper(mimeMessage,true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom(from);
            helper.setText(content,true);

            //构建文件流对象
            FileSystemResource file=new FileSystemResource(new File(filePath));
            //获取文件名
            String fileName=file.getFilename();
            helper.addAttachment(fileName,file);
            //如果发送多份则再添加即可
            //helper.addAttachment(fileName,file);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        mailSender.send(mimeMessage);
    }


    /**
     * 发送带图片的邮件
     * @param to  发给谁
     * @param subject  发送主题
     * @param content  发送内容
     * @param rscPath  资源路径
     * @param rscId  资源id--自定义即可
     */
    public void sendInLineResourceMail(String to,String subject,String content,
                                       String rscPath,String rscId)  {
        MimeMessage message=mailSender.createMimeMessage();
        log.info("发送今天邮件开始：{},{},{},{}",to,subject,content,rscPath,rscId);
        MimeMessageHelper helper= null;
        try {
            helper = new MimeMessageHelper(message,true);
            helper.setTo(to);
            helper.setText(content,true);
            helper.setFrom(from);
            helper.setSubject(subject);

            FileSystemResource res=new FileSystemResource(new File(rscPath));
            helper.addInline(rscId,res);
            //需要多张图片时，应该多一个addInline方法
            //helper.addInline(rscId,res);
            log.info("发送静态图片邮件成功");
        } catch (MessagingException e) {
            log.error("发送静态图片邮件失败",e);
        }


        mailSender.send(message);
    }



}
