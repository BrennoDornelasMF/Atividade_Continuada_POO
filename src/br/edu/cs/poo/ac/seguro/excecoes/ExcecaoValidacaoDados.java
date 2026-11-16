package br.edu.cs.poo.ac.seguro.excecoes;

import java.util.List;

public class ExcecaoValidacaoDados extends Exception {

	private static final long serialVersionUID = 1L;

	private final List<String> mensagens;

	public ExcecaoValidacaoDados(List<String> mensagens) {
		this(mensagens, null);
	}

	public ExcecaoValidacaoDados(List<String> mensagens, Throwable causa) {
		super(String.join(System.lineSeparator(), mensagens), causa);
		this.mensagens = List.copyOf(mensagens);
	}

	public List<String> getMensagens() {
		return mensagens;
	}
}
