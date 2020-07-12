/*
 * @(#)SequenceBuilder.java 2014-10-27
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.commons.coder;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <pre>
 * 序列生成器<br/>
 * 可扩展为多JVM的序列生成器,<br/>
 * 每个jvm负责一个 seq的生成，如jvm标记为01的app负责生成  SequenceBuilder.getSid()+“01”
 * @author tangxiaodong
 * 创建日期: 2014-10-27
 * </pre>
 */
public class SequenceBuilder {
    
    private Logger log = LoggerFactory.getLogger(getClass());

    private int incr = 0;
    private long fixedValue;
    private long beforeTimeSeeds;
    private Lock lock = new ReentrantLock();
    private int maxExtDigit;
    private int leftShiftBit;
    
    /**
     * 
     * @param fixedValue 修正值，一般取System.currentTimeMillis()的一个过去值
     * @param maxExtDigit 当毫秒时间值相同时，进行扩展自增累加的最大值,超过该值，将使得线程等待16ms,在下个周期进行获取序列
     * <br/>扩展倍数，只能是10, 100, 1000, 1000, 分别对应maxExtDigit: 9, 99, 999, 999
     */
    public SequenceBuilder(long fixedValue, int maxExtDigit) {
        this.fixedValue = fixedValue;
        this.beforeTimeSeeds = System.currentTimeMillis();
        this.maxExtDigit = maxExtDigit;
        if (maxExtDigit < 16) {
            this.leftShiftBit = 4 ;
        } else if (maxExtDigit < 256) {
            this.leftShiftBit = 8 ;
        } else if (maxExtDigit < 1024) {
            this.leftShiftBit = 10 ;
        } else {
            throw new IllegalArgumentException("maxExtDigit at 9~1023 range !");
        }
    }
    
    public long getSid() {
        long id;
        lock.lock();
        long currTimeDiffValue = 0L;
        long newTimeSeeds = System.currentTimeMillis();
        try {
            // 不同的线程在相同的时间内获取sid, 尝试扩展位自增
            if (newTimeSeeds == beforeTimeSeeds) {
                if (incr < maxExtDigit) {
                    incr++;
                    currTimeDiffValue = (newTimeSeeds - fixedValue);
                } else {
                    incr = 0;
                    newTimeSeeds = nextMillis(beforeTimeSeeds); 
                    currTimeDiffValue = (newTimeSeeds - fixedValue);
                }
            } else {
                // 在不同的时间内获取sid, 直接使用获取的时间获取sid
                incr = 0;
                currTimeDiffValue = (newTimeSeeds - fixedValue);
            }
            id = ( currTimeDiffValue << leftShiftBit ) | incr ;
            // 更新之前的更新时间种子为最新时间戳
            beforeTimeSeeds = newTimeSeeds;  
        } catch (Exception e) {
            log.error("SequenceBuilder.getSid()", e);
            id = Long.MIN_VALUE;
        } finally {
            lock.unlock();
        }
        return id;
    }
    
    private long nextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

}
