package unipos.data.components.company;

/**
 * Created by dominik on 04.09.15.
 */
public class CompanySaveMoreThanOneException extends RuntimeException {

    public CompanySaveMoreThanOneException() {
    }

    public CompanySaveMoreThanOneException(String message) {
        super(message);
    }
}
