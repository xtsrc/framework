package core.exception;

/**
 *
 * @author tao.xiong
 */
public class BizRuntimeException extends RuntimeException {

    private static final long serialVersionUID = -2705800449026361534L;

    public BizRuntimeException() {
        super();
    }

    public BizRuntimeException(String msg) {
        super(msg);
    }

    public BizRuntimeException(Throwable cause) {
        super(cause);
    }

    public BizRuntimeException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
