package com.uuola.commons.coder;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Administrator on 2017/12/12.
 */
public enum IdGenerator {

    INSTANCE;
    private long workerId;
    private long datacenterId = 0L;
    private long sequence = 0L;
    private long twepoch = 1533052800000L; // 2018-08-01 00:00:00
    private long workerIdBits = 8L; //节点ID长度
    private long datacenterIdBits = 2L; //数据中心ID长度
    private long sequenceBits = 12L; //序列号12位
    private long workerIdShift = sequenceBits; //机器节点左移12位
    private long datacenterIdShift = sequenceBits + workerIdBits; //数据中心节点左移17位
    private long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits; //时间毫秒数左移22位
    private long sequenceMask = -1L ^ (-1L << sequenceBits); //4095
    private long lastTimestamp = -1L;

    IdGenerator() {
        /**
         * IP最后一位是255，8位；因为数据中心ID是2位，且为0~3，所以执行或运算时可以忽略
         * 使用IP作为workerId，左移12位
         * 而数据中心ID为0，所以计算无影响。如果要配置数据中心,最多配置 4个 0~3
         * 在数据中心ID为0000的情况下，最多可以使用10位的workerId
         */
        this.datacenterId = Long.parseLong(System.getProperty("datacenterId", "0"));
        if(this.datacenterId<0L || this.datacenterId> 3L){
            throw new RuntimeException("the datacenterId config must be in range [0,3]");
        }
        this.workerId = 0x000000FF & getLastIP();
    }

    public synchronized long nextId() {
        long timestamp = timeGen(); //获取当前毫秒数
        //如果服务器时间有问题(时钟后退) 报错。
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format(
                    "Clock moved backwards. Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }
        //如果上次生成时间和当前时间相同,在同一毫秒内
        if (lastTimestamp == timestamp) {
            //sequence自增，因为sequence只有12bit，所以和sequenceMask相与一下，去掉高位
            sequence = (sequence + 1) & sequenceMask;
            //判断是否溢出,也就是每毫秒内超过4095，当为4096时，与sequenceMask相与，sequence就等于0
            if (sequence == 0) {
                timestamp = nextMillis(lastTimestamp); //自旋等待到下一毫秒
            }
        } else {
            sequence = 0L; //如果和上次生成时间不同,重置sequence，就是下一毫秒开始，sequence计数重新从0开始累加
        }
        lastTimestamp = timestamp;
        // 最后按照规则拼出ID。
        // 000000000000000000000000000000000000000000 00 00000000 000000000000
        // time datacenterId workerId sequence
        return ((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift)
                | (workerId << workerIdShift) | sequence;
    }

    private long nextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }

    private byte getLastIP() {
        byte lastip = 0;
        try {
            InetAddress ip = InetAddress.getLocalHost();
            byte[] ipByte = ip.getAddress();
            lastip = ipByte[ipByte.length - 1];
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return lastip;
    }
}

