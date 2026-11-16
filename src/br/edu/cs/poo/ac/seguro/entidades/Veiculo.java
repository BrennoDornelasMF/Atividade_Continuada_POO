package br.edu.cs.poo.ac.seguro.entidades;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Veiculo implements Registro {
	
	private static final long serialVersionUID = 1L;
	private String placa;
	private int ano;
	private Segurado proprietario;
	private CategoriaVeiculo categoria;

	@Override
	public String getIdUnico() {
		return placa;
	}

}
