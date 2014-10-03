/*
 * @(#)Envelope.java 2013-11-3
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.commons.mail;

import java.util.Date;


/**
 * <pre>
 * 用于设置发送邮件的参数
 * @author tangxiaodong
 * 创建日期: 2013-11-3
 * </pre>
 */
public class Envelope {

    /**
     * 邮件接收地址
     */
    private String toMail;
    
    /**
     * 发送人名称
     */
    private String personal;
    
    /**
     * 邮件主题
     */
    private String subject;
    
    /**
     * 邮件正文
     */
    private String content;
    
    /**
     * 邮件类型
     */
    private SmtpMailer.Type type;
    
    /**
     * 发送邮件的时间
     */
    private Date sentDate;
    
    /**
     * 邮件附件
     */
    private String[] files;
    
    /**
     * 多个发件人名， 可以任选一个设置给personal
     */
    private String[] personals;
    
    /**
     * 多个主题，可以任选一个 设置给subject
     */
    private String[] subjects;

    /**
     * 用于取消订阅的地址，设置到header
     */
    private String unsubscribeAddr;

    
    public String getToMail() {
        return toMail;
    }

    
    public void setToMail(String toMail) {
        this.toMail = toMail;
    }

    
    public String getPersonal() {
        return personal;
    }

    
    public void setPersonal(String personal) {
        this.personal = personal;
    }

    
    public String getSubject() {
        return subject;
    }

    
    public void setSubject(String subject) {
        this.subject = subject;
    }

    
    public String getContent() {
        return content;
    }

    
    public void setContent(String content) {
        this.content = content;
    }

    
    public SmtpMailer.Type getType() {
        return type;
    }

    
    public void setType(SmtpMailer.Type type) {
        this.type = type;
    }

    
    
    public Date getSentDate() {
        return sentDate;
    }


    
    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }


    public String[] getFiles() {
        return files;
    }

    
    
    public String[] getPersonals() {
        return personals;
    }


    
    public void setPersonals(String[] personals) {
        this.personals = personals;
    }


    
    public String[] getSubjects() {
        return subjects;
    }


    
    public void setSubjects(String[] subjects) {
        this.subjects = subjects;
    }


    public void setFiles(String[] files) {
        this.files = files;
    }


    
    public String getUnsubscribeAddr() {
        return unsubscribeAddr;
    }


    
    public void setUnsubscribeAddr(String unsubscribeAddr) {
        this.unsubscribeAddr = unsubscribeAddr;
    }
}
