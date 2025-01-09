package validations;

import java.util.regex.Pattern;

public class UserValidation {

    public boolean validarCpf(String cpf) {
        if (cpf == null || cpf.length() != 11 || !cpf.matches("\\d{11}")) {
            System.out.println("CPF inválido! O CPF deve conter exatamente 11 dígitos numéricos. Verifique e tente novamente.");
            return false;
        } else if (cpf.chars().distinct().count() == 1) {
            System.out.println("CPF inválido! O CPF não pode conter todos os dígitos iguais. Verifique e tente novamente.");
            return false;
        } else {
            int fDigit = calcularDigitoVerificadorCpf(cpf, 10, 9);
            int sDigit = calcularDigitoVerificadorCpf(cpf, 11, 10);
            if (!(fDigit == (cpf.charAt(9) - '0') && sDigit == (cpf.charAt(10) - '0'))) {
                System.out.println("CPF inválido! O CPF fornecido não é válido. Verifique os números e tente novamente.");
                return false;
            } else {
                return true;
            }
        }
    }

    private int calcularDigitoVerificadorCpf(String cpf, int pesoInicial, int tamanho) {
        int soma = 0;
        for (int i = 0; i < tamanho; i++) {
            soma += (cpf.charAt(i) - '0') * pesoInicial;
            pesoInicial--;
        }
        int resto = 11 - (soma % 11);
        return (resto >= 10) ? 0 : resto;
    }

    public boolean validarNome(String nome) {
        if (nome.length() > 3 && nome.matches("[a-zA-Z ]+") && !(nome.contains("  "))) {
            return true;
        } else {
            System.out.println("Nome inválido! O nome deve ter mais de 3 caracteres e não pode conter números ou caracteres especiais.");
            return false;
        }
    }

    public boolean validarEmail(String email, String padrao) {
        if (Pattern.matches(padrao, email)) {
            return true;
        } else {
            System.out.println("E-mail inválido! Verifique o formato do e-mail fornecido e tente novamente.");
            return false;
        }
    }

    public boolean validarTelefone(String telefone, String padrao) {
        if (Pattern.matches(padrao, telefone)) {
            return true;
        } else {
            System.out.println("Telefone inválido! Verifique o número fornecido e tente novamente.");
            return false;
        }
    }

    public boolean validarSenha(String senha) {
        if (senha.contains(" ")) {
            System.out.println("Senha inválida! A senha não pode conter espaços em branco. Verifique e tente novamente.");
            return false;
        }
        if (senha.length() < 8) {
            System.out.println("Senha inválida! A senha deve ter pelo menos 8 caracteres. Tente novamente.");
            return false;
        } else {
            return true;
        }
    }

    
}
