package eti.italiviocorrea.paises.util;

public class DBSemConexaoException extends Throwable {
    private static final long serialVersionUID = 1L;

    public DBSemConexaoException() {
    }

    public DBSemConexaoException(String message) {
        super(message);
    }

    public DBSemConexaoException(Throwable cause) {
        super(cause);
    }

    public DBSemConexaoException(String message, Throwable cause) {
        super(message, cause);
    }



}
