package br.edu.cs.poo.ac.seguro.entidades;

import java.io.Serializable;

public class Endereco implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String logradouro;
	private String cep;
	private String numero;
	private String complemento;
	private String pais;
	private String estado;
	private String cidade;
	
	public Endereco(String logradouro, String cep, String numero, String complemento, String pais, String estado,
			String cidade) {
		super();
		this.logradouro = logradouro;
		this.cep = cep;
		this.numero = numero;
		this.complemento = complemento;
		this.pais = pais;
		this.estado = estado;
		this.cidade = cidade;
	}

	public String getLogradouro() {
		return logradouro;
	}

	protected void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getCep() {
		return cep;
	}

	protected void setCep(String cep) {
		this.cep = cep;
	}

	public String getNumero() {
		return numero;
	}

	protected void setNumero(String numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	protected void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getPais() {
		return pais;
	}

	protected void setPais(String pais) {
		this.pais = pais;
	}

	public String getEstado() {
		return estado;
	}

	protected void setEstado(String estado) {
		this.estado = estado;
	}

	public String getCidade() {
		return cidade;
	}

	protected void setCidade(String cidade) {
		this.cidade = cidade;
	}
	
	
	

}
