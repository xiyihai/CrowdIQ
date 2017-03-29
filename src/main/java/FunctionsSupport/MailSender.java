package FunctionsSupport;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class MailSender {

	private MimeMessage createMail(Session session, String mailAddress) throws AddressException, MessagingException{
		//设置邮件对象
		MimeMessage mail = new MimeMessage(session);
		//设置发件人
		mail.setFrom(new InternetAddress("xiyihai@yeah.net"));
		//设置收件人
		mail.setRecipient(Message.RecipientType.TO, new InternetAddress(mailAddress));
		
		//设置主题
		mail.setSubject("CrowdIQ验证");
		
		//设置内容
		mail.setContent("欢迎注册CrowdIQ", "text/html;charset=UTF-8");
		
		return mail;
	}
	
	public boolean sendMail (String mailAddress) throws MessagingException {
		
		//设置邮件的基本信息
		Properties prop=new Properties();
		prop.put("mail.host","smtp.yeah.net" );
		prop.put("mail.transport.protocol", "smtp");
		prop.put("mail.smtp.auth", true);
		
		Session session = Session.getInstance(prop);
		
		Transport transport = session.getTransport();
		transport.connect("xiyihai@yeah.net", "xyh201268");
			
		//创建邮件
		Message message = createMail(session, mailAddress);
			
		//发送邮件
		transport.sendMessage(message, message.getAllRecipients());
			
		return true;
	}
	
}
