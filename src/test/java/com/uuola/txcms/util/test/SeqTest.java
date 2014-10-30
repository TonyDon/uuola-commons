/*
 * @(#)SeqTest.java 2014-10-27
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.txcms.util.test;

import com.uuola.commons.coder.SequenceBuilder;


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
        long fixedValue = 1414424012812L;
        SequenceBuilder sb = new SequenceBuilder(fixedValue, 9);
        for(int k=0; k<100; k++){
            System.out.println(sb.getSid());
            //(new DemoGet(sb)).start();
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
    }
    
}