package com.miao;

import com.miao.send.Sender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MqApplication.class)
public class MqApplicationTests {

	@Autowired
	private Sender sender;
	@Autowired
	private JavaMailSender mailSender;
	@Value("${spring.mail.username}")
	private String username;

	@Test
	public void contextLoads() {
		sender.send();
	}

	//简单邮件
	@Test
	public void test1(){
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("953275329@qq.com");
		//message.setTo("15215694122@163.com");
		message.setTo("1669651407@qq.com");
		message.setSubject("主题：666的邮件");
		message.setText("123456789");
		mailSender.send(message);
	}

	//发送附件
	@Test
	public void test2() throws Exception {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
		helper.setFrom("953275329@qq.com");
		helper.setTo("1669651407@qq.com");
		helper.setSubject("主题：有附件");
		helper.setText("有附件的邮件");
		FileSystemResource file = new FileSystemResource(new File("帅哥.png"));
		helper.addAttachment("附件-1.jpg", file);
		helper.addAttachment("附件-2.jpg", file);
		mailSender.send(mimeMessage);
	}
	//设置发送人名称
	@Test
	public void test3() throws Exception {
		MimeMessage message = null;
		try {
			message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(new InternetAddress(username, "老公", "UTF-8"));
			helper.setTo("1669651407@qq.com");
			helper.setSubject("标题：发送Html内容");

			StringBuffer sb = new StringBuffer();
			sb.append("<h1>大标题-h1</h1>")
					.append("<p style='color:#F00'>红色字</p>")
					.append("<p style='text-align:right'>右对齐</p>");
			helper.setText(sb.toString(), true);
		} catch (Exception e) {
			e.printStackTrace();
		}

		mailSender.send(message);
	}


	//嵌入静态资源
	@Test
	public void sendInlineMail() throws Exception {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
		helper.setFrom(username);
		helper.setTo("15215694122@163.com");
		helper.setSubject("主题：嵌入静态资源");
		helper.setText("<html><body><img src=\"cid:weixin\" ></body></html>", true);
		FileSystemResource file = new FileSystemResource(new File("帅哥.png"));
		helper.addInline("weixin", file);
		mailSender.send(mimeMessage);
	}

}
