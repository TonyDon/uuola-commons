/*
 * @(#)CollectionUtil.java 2013-8-31
 * 
 * Copy Right@ uuola
 */

package com.uuola.commons;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *
 * @author tangxiaodong
 * 创建日期: 2013-8-31
 * </pre>
 */
public class CollectionUtil {

    /**
     * 判断map是否非空
     * @param <K>
     * @param <V>
     * 
     * @param map
     * @return
     */
    public static <K, V> boolean isNotEmpty(Map<K,V>  map) {
        return !isEmpty(map);
    }

    /**
     * 判断map是否为空
     * 
     * @param map
     * @return
     */
    public static <K, V> boolean isEmpty(Map<K,V> map) {
        return (map == null || map.isEmpty());
    }

    /**
     * 判断集合是否非空
     * 
     * @param coll
     * @return
     */
    public static <E> boolean isNotEmpty(Collection<E> coll) {
        return !isEmpty(coll);
    }

    /**
     * 判断集合是否为空
     * 
     * @param coll
     * @return
     */
    public static <E> boolean isEmpty(Collection<E> coll) {
        return (coll == null || coll.isEmpty());
    }

    /**
     * 得到List集合 批量迭代器<br/>
     * 如果expectBatchSize %  BatchIterator.CYCLE_TAKE_NUM  !=0 <br/>
     * 则按 CYCLE_TAKE_NUM倍数扩增为适当的batchSize
     * @param srcList
     * @param expectBatchSize
     * @return
     */
    public static <E> Iterator<List<E>> makeBatchIterator(List<E> srcList, int expectBatchSize) {
        return new BatchIterator<E>(srcList, expectBatchSize);
    }

    /**
     * 根据 hashmap 默认因子 0.75 生成一个合适的初始尺寸
     * @param size
     * @return
     */
    public static int preferedMapSize(int size) {
        return (int) (size / 0.75f) + 1;
    }
}
