package com.uuola.commons.exception;

import com.uuola.commons.constant.HTTP_STATUS_CODE;

/**
 * 业务异常基类
 * @author tangxiaodong
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 8837159417583848987L;
    /**
     * 返回给客户端的http状态码
     */
    private int httpCode = HTTP_STATUS_CODE.SC_BZ_ERROR;
    
    private Object[] params;

    public BusinessException() {
        super();
    }

    /**
     *
     * @param cause
     */
    public BusinessException(Throwable cause) {
        super(cause);
    }

    /**
     *
     * @param cause
     * @param message
     */
    public BusinessException(Throwable cause, String message) {
        super(message, cause);
    }

    /**
     *
     * @param message
     * @param params
     */
    public BusinessException(String message, Object... params) {
        super(String.format(message, params));
    }

    public BusinessException(Throwable cause, String message, Object... params) {
        super(String.format(message, params), cause);
    }

    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    /**
     * @return the params
     */
    public Object[] getParams() {
        return params;
    }

    /**
     * @param params the params to set
     */
    public void setParams(Object[] params) {
        this.params = params;
    }
}
