package util.argmanager;
public class ArgNotFoundException extends RuntimeException{
    public ArgNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
