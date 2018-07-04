package com.ejiahe.app.license.uitls;
import com.ejiahe.app.license.conf.LicenseConfig;
import com.google.common.base.Strings;
import com.sun.mail.util.MailSSLSocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.Security;
import java.util.Properties;

/**
 * 邮件发送服务
 */
public class EmailService {

	Logger logger = LoggerFactory.getLogger(getClass());

	private Properties props; //系统属性
	private Session session; //邮件会话对象
	LicenseConfig config = LicenseConfig.getInstance();
//	private MimeMessage mimeMsg; //MIME邮件对象
//	private Multipart mp; //Multipart对象,邮件内容,标题,附件等内容均添加到其中后再生成MimeMessage对象

	private static class MailSendHolder{
		private static EmailService instance = new EmailService();
	}

	public static EmailService getInstance(){
		return MailSendHolder.instance;
	}

	/**
	 * Constructor
	 */
	public EmailService(){



		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
		props = System.getProperties();
		MailSSLSocketFactory sf = null;
		try {
			sf = new MailSSLSocketFactory();
		} catch (GeneralSecurityException e) {
		}
		sf.setTrustAllHosts(true);
		props.put("mail.smtp.auth","true");
		props.put("mail.smtp.host", config.mailSmtp());
		props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
		props.put("mail.smtp.socketFactory.fallback", "false");

		props.put("mail.smtp.ssl.enable", "true");
		props.put("mail.smtp.port", config.mailPort());
		props.put("mail.smtp.ssl.socketFactory", sf);

		session = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(config.mailUserName(), config.mailPassword());
			}
		});
		session.setDebug(true);

	}

	/**
	 * 发送邮件
	 * @param from    邮件发送者
	 * @param to	  邮件接收者
	 * @param subject 主题
	 * @param content 邮件内容
	 * @return
	 */
	public boolean send(String from,String[] to,String subject,String content){
		return send(from, to, subject, content, null);
	}


	public boolean send(String from,
							String[] to,
							String subject,
							String content,
							String filename) {
		try {

			MimeMessage mimeMsg = new MimeMessage(session);
			MimeMultipart mp = new MimeMultipart();
			//设置发信人
			mimeMsg.setFrom(new InternetAddress(from));
			//设置接收人
			for (int i = 0; i < to.length; i++) {
				mimeMsg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to[i]));
			}
//			//设置抄送人
//            for (int i = 0; i < copyto.length; i++) {
//                mimeMsg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(copyto[i]));
//            }
			//设置主题
			mimeMsg.setSubject(subject);
			//设置正文
			BodyPart bp = new MimeBodyPart();
			bp.setContent(content, "text/html;charset=utf-8");
			mp.addBodyPart(bp);
			if(!Strings.isNullOrEmpty(filename)){
				//设置附件
				bp = new MimeBodyPart();
				FileDataSource fileds = new FileDataSource(filename);
				bp.setDataHandler(new DataHandler(fileds));
				bp.setFileName(MimeUtility.encodeText(fileds.getName(), "UTF-8", "B"));
				mp.addBodyPart(bp);
			}
			mimeMsg.setContent(mp);
			mimeMsg.saveChanges();
			//发送邮件
			if(props.get("mail.smtp.auth").equals("true")){
				Transport transport = session.getTransport("smtp");
				transport.connect((String)props.get("mail.smtp.host"), config.mailUserName(), config.mailPassword());
				transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.TO));
//                transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.CC));
				transport.close();
			}else{
				Transport.send(mimeMsg);
			}
			logger.info("邮件发送成功");
			return true;
		} catch (MessagingException e) {
			logger.error("邮件发送失败",e);
		} catch (UnsupportedEncodingException e) {
			logger.error("邮件发送失败",e);
		}
		return false;
	}

//    public static void main(String[] args) {
//
//        String from = "test@mygzb.com";
//        String[] to = {"zy-gao@mygzb.com"};
//        String subject = "生命数据";
//        String content = "<html><body><span style='color:red'>i have something <br/>to look up </span></body></html>";
//        String filename = "E:/工作使用/进行中/cache.html";
//		getInstance().send(from, to, subject, content, filename);
//
//    }

}