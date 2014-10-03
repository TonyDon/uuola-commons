/*
 * 更改javamail的messageid生成
 * http://www.oracle.com/technetwork/java/faq-135477.html#msgid
 * v1.2 by tonydon
 */
package com.uuola.commons.mail;

import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import com.uuola.commons.coder.KeyGenerator;
import com.uuola.commons.constant.CST_CHAR;

public class MailMessage extends MimeMessage{
    private String mailerCode; // 邮件发送者代号
    
    public MailMessage(Session sn){
        super(sn);
    }
    
    public MailMessage(Session sn, String mailerCode){
        super(sn);
        this.mailerCode = mailerCode;
    }
    
       
   public static final String getUUID(final String mailerCode){
       StringBuilder sb = new StringBuilder(64);
       sb.append('<').append(UUID.randomUUID())
               .append(CST_CHAR.CHAR_DOT)
               .append(KeyGenerator.getRndChr(4))
               .append(CST_CHAR.CHAR_AT)
               .append(mailerCode == null ? System.nanoTime() : mailerCode)
               .append('>');
        return sb.toString();
    }
   
    @Override
    protected void updateMessageID() throws MessagingException {
	setHeader("Message-ID", MailMessage.getUUID(mailerCode));
    }

    /**
     * @return the mailerCode
     */
    public String getMailerCode() {
        return mailerCode;
    }

    /**
     * @param mailerCode the mailerCode to set
     */
    public void setMailerCode(String mailerCode) {
        this.mailerCode = mailerCode;
    }
}
