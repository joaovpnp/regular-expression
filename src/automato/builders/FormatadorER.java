package automato.builders;

import java.util.Stack;

public class FormatadorER extends AutomatoBuilder {

    public static String inserirConcatenacaoExplicita(String er) {

        StringBuilder s = new StringBuilder();

        for (int i = 0; i < er.length(); i++) {

            char atual = er.charAt(i);
            s.append(atual);

            if (i < er.length()-1) {

                char proximo = er.charAt(i+1);

                boolean atualEhConcatenavel = isOperando(atual) || atual == PAR_DIREITA || atual == CONC_SUC;
                boolean proxEhConcatenavel = isOperando(proximo) || proximo == PAR_ESQUERDA;

                if (atualEhConcatenavel && proxEhConcatenavel)
                    s.append(CONC);

            }
        }

        return s.toString();
    }

    public static String infixaParaSufixa(String er) {

        String erFormatada = inserirConcatenacaoExplicita(er);

        StringBuilder s = new StringBuilder();
        Stack<Character> operadores = new Stack<>();

        for (char c : erFormatada.toCharArray()) {

            if (isOperando(c)) {
                s.append(c);

            } else if (c == PAR_ESQUERDA) {
                operadores.push(c);

            } else if (c == PAR_DIREITA) {

                while (!operadores.isEmpty() && operadores.peek() != PAR_ESQUERDA)
                    s.append(operadores.pop());
                operadores.pop();

            } else {

                while (!operadores.isEmpty() && operadores.peek() != PAR_ESQUERDA &&
                        prioridade(operadores.peek()) >= prioridade(c)) {

                    s.append(operadores.pop());
                }

                operadores.push(c);
            }
        }

        while (!operadores.isEmpty())
            s.append(operadores.pop());

        return s.toString();
    }
}
