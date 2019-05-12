package sample.thorntail.jpajaxrscdijta;

public class ApplicationException extends RuntimeException {
    public ApplicationException() {}

    public ApplicationException(String msg) {
        super(msg);
    }

    public ApplicationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
