/*
 * @(#)SeqTest.java 2014-10-27
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.txcms.util.test;

import com.uuola.commons.DateUtil;
import com.uuola.commons.coder.SequenceBuilder;
import com.uuola.commons.constant.CST_DATE_FORMAT;


/**
 * <pre>
 *
 * @author tangxiaodong
 * 创建日期: 2014-10-27
 * </pre>
 */
public class SeqTest {

    public static void main(String... args){
        System.out.println(System.currentTimeMillis());
        System.out.println(DateUtil.parseTimeMillis("2014-11-01 12:12:12", CST_DATE_FORMAT.YYYY_MM_DD_HH_MM_SS));
        long fixedValue = 1414424012812L;
        SequenceBuilder sb = new SequenceBuilder(fixedValue, 99);
        for(int k=0; k<30; k++){
            //System.out.println(sb.getSid());
            (new DemoGet(sb)).start();
        }
    }
}


class DemoGet extends Thread{
    
    private SequenceBuilder seqBuilder;
    
    public DemoGet(SequenceBuilder seqBuilder){
        this.seqBuilder = seqBuilder;
    }

    @Override
    public void run() {
        System.out.println(seqBuilder.getSid());
        //System.out.println(IdGenerator.INSTANCE.nextId());
    }
    
}