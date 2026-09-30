package br.edu.cs.poo.ac.seguro.mediators;

public class ValidadorCpfCnpj {
	public static boolean ehCnpjValido(String cnpj) {
		if (cnpj == null || cnpj.isBlank()) {
			return false;
		}
		
	    cnpj = cnpj.replaceAll("[^0-9]", "");
	    
	    if (cnpj.length() != 14) {
	        return false;
	    }
	    
	    if (cnpj.matches("(\\d)\\1{13}")) {
	        return false;
	    }

	    int[] peso1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

	    int soma = 0;

	    for (int i = 0; i < 12; i++) {
	        soma += Character.getNumericValue(cnpj.charAt(i)) * peso1[i];
	    }

	    int resto = soma % 11;
	    int digito1 = resto < 2 ? 0 : 11 - resto;

	    if (digito1 != Character.getNumericValue(cnpj.charAt(12))) {
	        return false;
	    }

	    int[] peso2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

	    soma = 0;

	    for (int i = 0; i < 13; i++) {
	        soma += Character.getNumericValue(cnpj.charAt(i)) * peso2[i];
	    }

	    resto = soma % 11;
	    int digito2 = resto < 2 ? 0 : 11 - resto;

	    return digito2 == Character.getNumericValue(cnpj.charAt(13));
		
	}
	public static boolean ehCpfValido(String cpf) {
		 if (cpf == null || cpf.isBlank()) {
		        return false;
		    }

		    cpf = cpf.replaceAll("[^0-9]", "");

		    if (cpf.length() != 11) {
		        return false;
		    }

		    if (cpf.matches("(\\d)\\1{10}")) {
		        return false;
		    }

		    int soma = 0;

		    for (int i = 0; i < 9; i++) {
		        soma += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
		    }

		    int resto = soma % 11;
		    int digito1 = resto < 2 ? 0 : 11 - resto;

		    if (digito1 != Character.getNumericValue(cpf.charAt(9))) {
		        return false;
		    }

		    soma = 0;

		    for (int i = 0; i < 10; i++) {
		        soma += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
		    }

		    resto = soma % 11;
		    int digito2 = resto < 2 ? 0 : 11 - resto;

		    return digito2 == Character.getNumericValue(cpf.charAt(10));
	}
}