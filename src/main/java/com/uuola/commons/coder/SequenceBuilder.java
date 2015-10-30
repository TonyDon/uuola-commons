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

    private int increase = 0;
    private long fixedValue;
    private long beforeTimeSeeds;
    private Lock lock = new ReentrantLock();
    private int maxExtDigit;
    private int extTimes;
    
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
        if (maxExtDigit < 10) {
            this.extTimes = 10;
        } else if (maxExtDigit < 100) {
            this.extTimes = 100;
        } else if (maxExtDigit < 1000) {
            this.extTimes = 1000;
        } else {
            throw new IllegalArgumentException("maxExtDigit at 9~999 range !");
        }
    }
    
    public long getSid() {
        long newTimeSeeds;
        long id;
        lock.lock();
        newTimeSeeds = System.currentTimeMillis();
        try {
            // 不同的线程在相同的时间内获取sid, 尝试扩展位自增
            if (newTimeSeeds == beforeTimeSeeds) {
                if (increase < maxExtDigit) {
                    increase++;
                    id = (newTimeSeeds - fixedValue) * extTimes + increase;
                } else {
                    increase = 0;
                    TimeUnit.MILLISECONDS.sleep(16);
                    newTimeSeeds = System.currentTimeMillis();
                    id = (newTimeSeeds - fixedValue) * extTimes;
                }
            } else {
                // 在不同的时间内获取sid, 直接使用获取的时间获取sid
                increase = 0;
                id = (newTimeSeeds - fixedValue) * extTimes;
            }
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

}
