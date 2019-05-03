package unipos.data.components.exception;

/**
 * Created by domin on 28.02.2016.
 */
public class DataNotFoundException extends RuntimeException{

    public DataNotFoundException() {
        super();
    }

    public DataNotFoundException(String s) {
        super(s);
    }
}
