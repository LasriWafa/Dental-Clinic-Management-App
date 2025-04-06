package ma.VitaCareApp.dao.exceptions;

public class DaoException extends Exception {


    /**
     * Exception to handle file errors
     *
     * @param message : Exception message
     */
    public DaoException(String message) {
        super(message);
    }

}
