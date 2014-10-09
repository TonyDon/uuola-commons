package com.uuola.commons.exception;


/**
 * 业务异常基类
 * @author tangxiaodong
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 8837159417583848987L;
    /**
     * 返回给客户端的错误码
     */
    private int errorCode = 0;
    
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
        super(message);
        setParams(params);
    }

    public BusinessException(Throwable cause, String message, Object... params) {
        super(message, cause);
        setParams(params);
    }

    
    public int getErrorCode() {
        return errorCode;
    }

    
    public BusinessException setErrorCode(int errorCode) {
        this.errorCode = errorCode;
        return this;
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
    public BusinessException setParams(Object[] params) {
        this.params = params;
        return this;
    }
}
