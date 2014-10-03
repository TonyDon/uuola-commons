package com.uuola.commons.mail;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uuola.commons.ObjectUtil;
import com.uuola.commons.StringUtil;
import com.uuola.commons.file.FileUtil;

/**
 * 发送邮件类
 * 
 * <pre>
 * SmtpParam param = new SmtpParam();
 * param.setHost("smtp.163.com");
 * param.setFromMail("txdnetmailer@163.com");
 * param.setEnableSSL(false);
 * param.setPort("25");
 * param.setLoginMail("txdnetmailer@163.com");
 * param.setLoginPass("I3vHgd30");
 * param.setMailerCode("Mailer-001");
 * param.initConfig();// important !!!
 *
 * SmtpMailer.sendHtmlMail(
 * "372578374@qq.com", "uobai", "javamail测试邮件(重要)",
 * "<html><body><b>javamail测试邮件.请重置密码ddd.</b></body></html>",
 * param);
 * </pre>
 * 
 * @author txdnet
 */
public final class SmtpMailer {

    private static Logger logger = LoggerFactory.getLogger(SmtpMailer.class);

    private SmtpMailer() {
    }

    public static enum Type {
        TEXT, HTML
    };

    /*
     * 可以发送附件邮件
     */
    public static boolean sendMail(Envelope envelope, final SmtpParam smtpParam) {
        boolean flag = true;
        try {
            Session sn = smtpParam.getSession();
            MimeMessage msg = new MailMessage(sn, smtpParam.getMailerCode());
            msg.setFrom(new InternetAddress(smtpParam.getFromMail(), envelope.getPersonal()));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(envelope.getToMail(), false));
            msg.setSubject(envelope.getSubject(), smtpParam.getCharset());
            if (null != envelope.getSentDate()) {
                msg.setSentDate(envelope.getSentDate());
            }
            setMimeMessageHeaderParams(envelope, smtpParam, msg);
            BodyPart mdp = makeBodyPart(envelope, smtpParam);
            Multipart mm = makeMimeMultipart(mdp, envelope);
            msg.setContent(mm);
            msg.saveChanges();
            // Transport t=sn.getTransport(ssl ? "smtps" : "smtp");
            // t.connect(mailhost, frommail , frommailpk);
            // t.sendMessage(msg, msg.getAllRecipients());
            Transport.send(msg);
            // t.close();
        } catch (Exception ex) {
            logger.error("SmtpParam.mailerCode=".concat(smtpParam.getMailerCode()), ex);
            flag = false;
        }
        return flag;
    }

    private static void setMimeMessageHeaderParams(Envelope envelope, SmtpParam smtpParam, MimeMessage msg)
            throws UnsupportedEncodingException, MessagingException {
        if (StringUtil.isNotEmpty(smtpParam.getReplyTo())) {
            // 是否设置回复邮件
            Address[] address = { new InternetAddress(smtpParam.getReplyTo(), envelope.getPersonal()) };
            msg.setReplyTo(address);
            msg.setHeader("Return-Path", smtpParam.getReplyTo());
            msg.setHeader("X-Return-Path", smtpParam.getReplyTo());
        }
        if (StringUtil.isNotEmpty(smtpParam.getPriority())) {
            msg.setHeader("X-Priority", smtpParam.getPriority());
        }
        msg.setHeader("X-Authenticated-Sender", smtpParam.getFromMail());
        msg.setHeader("X-Mailer", "SmtpMailer 1.0");
        if (StringUtil.isNotEmpty(envelope.getUnsubscribeAddr())) {
            msg.setHeader("List-Unsubscribe", envelope.getUnsubscribeAddr());
        }
    }

    /**
     * 构建附件，并将bodypart加入邮件
     * 
     * @param mdp
     * @param envelope
     * @return
     * @throws MessagingException
     * @throws IOException
     */
    private static Multipart makeMimeMultipart(BodyPart mdp, Envelope envelope) throws MessagingException, IOException {
        MimeMultipart mm = new MimeMultipart();
        mm.addBodyPart(mdp);
        if (!ObjectUtil.isEmpty(envelope.getFiles())) {
            for (String file : envelope.getFiles()) {
                MimeBodyPart mbpfile = new MimeBodyPart();
                mbpfile.attachFile(file);
                mbpfile.setFileName(MimeUtility.encodeWord(FileUtil.getFileName(file)));
                mm.addBodyPart(mbpfile);
            }
        }
        return mm;
    }

    /**
     * 根据信封类型，邮件参数构建MimeBodyPart
     * 
     * @param envelope
     * @param smtpParam
     * @return
     * @throws MessagingException
     */
    private static BodyPart makeBodyPart(Envelope envelope, SmtpParam smtpParam) throws MessagingException {
        MimeBodyPart mdp = new MimeBodyPart();
        if (envelope.getType() == SmtpMailer.Type.HTML) {
            // 发送HTML格式邮件
            mdp.setContent(envelope.getContent(), "text/html;charset=".concat(smtpParam.getCharset()));
        } else if (envelope.getType() == SmtpMailer.Type.TEXT) {
            // 发送文本格式邮件
            mdp.setText(envelope.getContent());
        } else {
            throw new RuntimeException("Mail Type Error. SmtpParam.mailerCode=".concat(smtpParam.getMailerCode()));
        }
        if (smtpParam.isEnableBase64()) {
            mdp.setHeader("Content-Transfer-Encoding", "base64");
        }
        return mdp;
    }
}