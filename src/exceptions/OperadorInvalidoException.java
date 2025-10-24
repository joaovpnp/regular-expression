package exceptions;

public class OperadorInvalidoException extends Exception {

    @Override
    public String getMessage() {
        return "[Operador inválido para criação do AFE]";
    }
}
