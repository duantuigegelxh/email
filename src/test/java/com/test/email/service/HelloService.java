package com.test.email.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * 邮件测试类
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class HelloService {

    private String to="18773239391@126.com";
    @Resource
    MailService mailService;

    @Resource
    TemplateEngine templateEngine;

    //测试发送文本文件
    @Test
    public void sendSimpleTest(){
        mailService.sendSimpleMail(to,"这是第一封邮件","你好");
    }

    //测试发送html类型的邮件
    @Test
    public void sendHtmlMailTest(){
        String content="<html>\n"+
                "<body>\n"+
                "<h3>helloworld,这是一个html邮件</h3>\n"+
                "</body>\n"+
                "</html>";
            mailService.sendHtmlMail(to,"这是第一封html邮件",content);

    }

    /**
     * 测试发送附件邮件
     */
    @Test
    public void sendAtachmentsMail(){
        //找一个文件
        String filePath="E:\\知名互联网公司校招中常见的算法题.docx";

            mailService.sendAtachmentsMail(to,"这是一个带附件的邮件","" +
                    "这是一篇带附件的邮件内容",filePath);



    }


    @Test
    public void sendInLineResourceMailTest() throws MessagingException {
        String imgPath="E:\\banner_1.jpg";
        String rscId="neo001";
        String content="<html><body>这是有图片的邮件;<img src=\'cid:"+rscId+"\'></img></body></html>";
        //需要加多张图片时应该再加impPath和rscId
        /**另外加如下的代码
         * String imgPath="E:\\banner_1.jpg";
         String rscId="neo001";
         String content="<html><body>这是有图片的邮件;<img src=\'cid:"+rscId+"\'></img></body></html>";
         */
        mailService.sendInLineResourceMail(to,"这是一个图片邮件",content,imgPath,rscId);
    }


    /**
     * 测试我们使用的模板引擎，实际上后面使用的html邮件
     */
    @Test
    public void testTemplateMailTest() throws MessagingException {
        Context context=new Context();
        //传入参数
        context.setVariable("id",006);

        //写入模板引擎的名字,emailContent是一个带html格式的文本
        String emailContent=templateEngine.process("emailTemplate.html",context);

        mailService.sendHtmlMail(to,"这是一个模板邮件",emailContent);
    }
}
