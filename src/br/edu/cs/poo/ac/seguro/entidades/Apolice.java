package br.edu.cs.poo.ac.seguro.entidades;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Apolice implements Registro {
	
	private static final long serialVersionUID = 1L;
	private String numero;
	private Veiculo veiculo;
	private BigDecimal valorFranquia;
	private BigDecimal valorPremio;
	private BigDecimal valorMaximoSegurado;
	private LocalDate dataInicioVigencia;
	
	
	public Apolice(Veiculo veiculo, BigDecimal valorFranquia, BigDecimal valorPremio, BigDecimal valorMaximoSegurado) {
		this(null, veiculo, valorFranquia, valorPremio, valorMaximoSegurado, null);
	}

	public Apolice(String numero, Veiculo veiculo, BigDecimal valorFranquia, BigDecimal valorPremio,
			BigDecimal valorMaximoSegurado, LocalDate dataInicioVigencia) {
		this.numero = numero;
		this.veiculo = veiculo;
		this.valorFranquia = valorFranquia;
		this.valorPremio = valorPremio;
		this.valorMaximoSegurado = valorMaximoSegurado;
		this.dataInicioVigencia = dataInicioVigencia;
	}

	@Override
	public String getIdUnico() {
		return numero;
	}
	
	

}
