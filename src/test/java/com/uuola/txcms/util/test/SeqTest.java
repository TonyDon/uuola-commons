/*
 * @(#)SeqTest.java 2014-10-27
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.txcms.util.test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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

    public static void main(String... args) throws InterruptedException{
        System.out.println(System.currentTimeMillis());
        System.out.println(DateUtil.parseTimeMillis("2020-07-01 12:12:12", CST_DATE_FORMAT.YYYY_MM_DD_HH_MM_SS));
        long fixedValue = 1593576732000L;
        SequenceBuilder sb = new SequenceBuilder(fixedValue, 15);
        Set<Long> id = Collections.synchronizedSet(new HashSet<>());
        for(int k=0; k<128; k++){
            //System.out.println(sb.getSid());
            (new DemoGet(sb, id)).start();
        }
        TimeUnit.SECONDS.sleep(5L);
        System.out.println(id.size());
    }
}


class DemoGet extends Thread{
    
    private SequenceBuilder seqBuilder;
    private Set<Long> id;
    
    public DemoGet(SequenceBuilder seqBuilder, Set<Long> id){
        this.seqBuilder = seqBuilder;
        this.id = id;
    }

    @Override
    public void run() {
        long i = seqBuilder.getSid();
        System.out.println(i);
        id.add(i);
    }
    
}