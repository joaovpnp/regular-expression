package automato.builders;

import java.util.Stack;

public abstract class AutomatoBuilder {

    protected static final char PAR_ESQUERDA = '(';
    protected static final char PAR_DIREITA = ')';

    protected static final char UNIAO = '+';
    protected static final char CONC = '.';
    protected static final char CONC_SUC = '*';

    protected static final char PALAVRA_VAZIA_ER = '$';

    protected static int prioridade(char c) {
        return switch (c) {
            case UNIAO -> 0;
            case CONC -> 1;
            case CONC_SUC -> 2;
            default -> -1;
        };
    }

    private static boolean isApenasOperador(char c) {
        return prioridade(c) >= 0;
    }

    private static boolean isParentese(char c) {
        return c == PAR_ESQUERDA || c == PAR_DIREITA;
    }

    protected static boolean isOperador(char c) {
        return isApenasOperador(c) || isParentese(c);
    }

    protected static boolean isOperando(char c) {
        return !isOperador(c);
    }

    protected static boolean isERsintaticamenteCorreta(String er) {

        if (er == null) return false;
        if (er.isEmpty() || er.isBlank()) return true;

        Stack<Character> pilhaParenteses = new Stack<>();

        if (isApenasOperador(er.charAt(0)) || er.charAt(0) == PAR_DIREITA)
            return false;
        else if (er.charAt(0) == PAR_ESQUERDA)
            pilhaParenteses.push(PAR_ESQUERDA);

        for (int i = 1; i < er.length(); i++) {

            char anterior = er.charAt(i-1);
            char atual = er.charAt(i);

            if (isOperando(atual)) {
                if (!(isApenasOperador(anterior) || anterior == PAR_ESQUERDA))
                    return false;

            } else if (isApenasOperador(atual) && atual != CONC_SUC) {
                if (!(anterior == PAR_DIREITA || isOperando(anterior) || anterior == CONC_SUC))
                    return false;

            } else if (atual == CONC_SUC) {
                if (!(anterior == PAR_DIREITA || isOperando(anterior)))
                    return false;

            } else if (atual == PAR_ESQUERDA) {
                pilhaParenteses.push(PAR_ESQUERDA);
                if (!(anterior == PAR_ESQUERDA || anterior == UNIAO || anterior == CONC))
                    return false;

            } else {
                if (!(anterior == PAR_DIREITA || isOperando(anterior)))
                    return false;

                if (pilhaParenteses.isEmpty())
                    return false;
                else
                    pilhaParenteses.pop();

            }
        }

        return pilhaParenteses.isEmpty();
    }
}
