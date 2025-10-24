package exceptions;

public class SemEstadoFinalException extends Exception {

    @Override
    public String getMessage() {
        return "[Autômato sem estado final]";
    }
}
