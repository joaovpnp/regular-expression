package exceptions;

public class ExpressaoIncorretaException extends Exception {

    @Override
    public String getMessage() {
        return "[Expressão Regular está sintaticamente incorreta]";
    }
}
