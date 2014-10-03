package com.uuola.commons.mail;

import java.io.Serializable;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;


public class SmtpParam implements Serializable {

    private static final long serialVersionUID = 4348532079195187484L;
    private String host;
    private String port = "25";
    private boolean enableSSL = false;
    private boolean startTLS = false;
    private String fromMail;
    private String replyTo;
    private String loginMail;
    private String loginPass;
    private String connectionTimeout = "12000";
    private String timeout = "12000";
    private String charset = "utf-8";
    private boolean enableBase64 = false;
    private String mailerCode;
    

    
    /**
     * 邮件级别1(highest)~ 5(lowest)
     */
    private String priority;
    private transient Session session;

    public SmtpParam() {
    }

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @param host the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @return the port
     */
    public String getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(String port) {
        this.port = port;
    }

    /**
     * @return the enableSSL
     */
    public boolean isEnableSSL() {
        return enableSSL;
    }

    /**
     * @param enableSSL the enableSSL to set
     */
    public void setEnableSSL(boolean enableSSL) {
        this.enableSSL = enableSSL;
    }

    
    public boolean isStartTLS() {
        return startTLS;
    }

    
    public void setStartTLS(boolean startTLS) {
        this.startTLS = startTLS;
    }

    /**
     * @return the fromMail
     */
    public String getFromMail() {
        return fromMail;
    }

    /**
     * @param fromMail the fromMail to set
     */
    public void setFromMail(String fromMail) {
        this.fromMail = fromMail;
    }

    
    public String getReplyTo() {
        return replyTo;
    }

    
    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    /**
     * @return the loginMail
     */
    public String getLoginMail() {
        return loginMail;
    }

    /**
     * @param loginMail the loginMail to set
     */
    public void setLoginMail(String loginMail) {
        this.loginMail = loginMail;
    }

    /**
     * @return the loginPass
     */
    public String getLoginPass() {
        return loginPass;
    }

    /**
     * @param loginPass the loginPass to set
     */
    public void setLoginPass(String loginPass) {
        this.loginPass = loginPass;
    }

    /**
     * @return the connectionTimeout
     */
    public String getConnectionTimeout() {
        return connectionTimeout;
    }

    /**
     * @param connectionTimeout the connectionTimeout to set
     */
    public void setConnectionTimeout(String connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    /**
     * @return the timeout
     */
    public String getTimeout() {
        return timeout;
    }

    /**
     * @param timeout the timeout to set
     */
    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    /**
     * @return the charset
     */
    public String getCharset() {
        return charset;
    }

    /**
     * @param charset the charset to set
     */
    public void setCharset(String charset) {
        this.charset = charset;
    }

    public void initConfig() {
        Properties mailProps = new Properties();
        if (this.enableSSL) {
            mailProps.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            mailProps.setProperty("mail.transport.protocol", "smtps");
        } else {
            mailProps.setProperty("mail.transport.protocol", "smtp");
        }
        if(this.startTLS){
            mailProps.setProperty("mail.smtp.starttls.enable", "true");
        }
        mailProps.setProperty("mail.smtp.host", this.host);
        mailProps.setProperty("mail.smtp.connectiontimeout", this.connectionTimeout);
        mailProps.setProperty("mail.smtp.timeout", this.timeout);
        mailProps.setProperty("mail.mime.address.strict", "false");
        mailProps.setProperty("mail.smtp.socketFactory.fallback", "false");
        mailProps.setProperty("mail.smtp.port", this.port);
        mailProps.setProperty("mail.smtp.socketFactory.port", this.port);
        mailProps.setProperty("mail.smtp.auth", "true");
        mailProps.setProperty("mail.mime.charset", this.charset);
        this.session = Session.getInstance(mailProps, new SmtpAuthenticator(this.loginMail, this.loginPass));
    }

    static class SmtpAuthenticator extends Authenticator {

        private String loginMail;
        private String loginPass;

        public SmtpAuthenticator(String loginMail, String loginPass) {
            this.loginMail = loginMail;
            this.loginPass = loginPass;
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new javax.mail.PasswordAuthentication(loginMail, loginPass);
        }
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

    
    public String getPriority() {
        return priority;
    }

    
    public void setPriority(String priority) {
        this.priority = priority;
    }

    /**
     * @return the session
     */
    public Session getSession() {
        return session;
    }

    /**
     * @param session the session to set
     */
    public void setSession(Session session) {
        this.session = session;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mailerCode != null ? mailerCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SmtpParam)) {
            return false;
        }
        SmtpParam other = (SmtpParam) obj;
        if ((this.mailerCode == null && other.mailerCode != null)
                || (this.mailerCode != null && !this.mailerCode.equals(other.mailerCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return SmtpParam.class.getCanonicalName().concat("[mailerCode=").concat(mailerCode).concat("]");
    }

    /**
     * @return the enableBase64
     */
    public boolean isEnableBase64() {
        return enableBase64;
    }

    /**
     * @param enableBase64 the enableBase64 to set
     */
    public void setEnableBase64(boolean enableBase64) {
        this.enableBase64 = enableBase64;
    }


}