package fun.baozi.boot.core.exception;

/**
 * 应用错误处理类
 * @author baozi
 * 2019-05-23
 */
public class AppException extends RuntimeException {

    private static final long serialVersionUID = -7455556413670313345L;

    private String errorCode;

    private String errorMsg;

    private AppException() {

    }
    public AppException(String errorCode, String errorMsg) {
        super();
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return " AppException|errorMsg=" +  errorMsg + "|errorCode" +  errorCode;
    }

    @Override
    public String getMessage() {
        return " AppException|errorCode=" + errorCode + "|errorMsg=" + errorMsg;
    }
}
