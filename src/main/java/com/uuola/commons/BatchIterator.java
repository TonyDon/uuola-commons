/*
 * @(#)BatchIterator.java 2014-9-13
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.commons;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * <pre>
 * 集合分批迭代
 * @author tangxiaodong
 * 创建日期: 2014-9-13
 * </pre>
 */
public class BatchIterator<E> implements Iterator<List<E>> {

    /**
     * 当前的批次集合返回的元素个数
     */
    private int batchSize;

    private List<E> srcList;

    private int startIndex = 0;
    
    private int endIndex = 0;

    private List<E> result;
    
    private int size = 0;
    
    public static final int CYCLE_TAKE_NUM = 8;

    public BatchIterator(List<E> srcList, int expectBatchSize) {
        if (0 >= expectBatchSize) {
            throw new RuntimeException(
                    "Please do not be set less than or equal to '0' for GenericBatchIterator's batchSize !");
        }
        int times = (expectBatchSize / CYCLE_TAKE_NUM);
        this.srcList = srcList;
        this.batchSize = ((expectBatchSize % CYCLE_TAKE_NUM == 0) ? times : times + 1) * CYCLE_TAKE_NUM;
        this.size = null == srcList ? 0 : srcList.size();
        result = new ArrayList<E>(this.batchSize);
    }

    @Override
    public boolean hasNext() {
        return endIndex < size;
    }

    @Override
    public List<E> next() {
        result.clear();
        endIndex = startIndex + batchSize;
        if (endIndex > size) {
            for (int i = startIndex; i < size; i++) {
                result.add(srcList.get(i));
            }
        } else {
            for (int i = startIndex; i < endIndex; startIndex += CYCLE_TAKE_NUM) {
                result.add(srcList.get(i++));
                result.add(srcList.get(i++));
                result.add(srcList.get(i++));
                result.add(srcList.get(i++));
                result.add(srcList.get(i++));
                result.add(srcList.get(i++));
                result.add(srcList.get(i++));
                result.add(srcList.get(i++));
            }
        }
        return result;
    }

    /**
     * 暂不支持移除子集合方法
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

}
