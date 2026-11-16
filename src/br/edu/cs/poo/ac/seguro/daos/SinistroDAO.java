package br.edu.cs.poo.ac.seguro.daos;

import br.edu.cs.poo.ac.seguro.entidades.Sinistro;

public class SinistroDAO extends DAOGenerico<Sinistro> {

	@Override
	public Class<Sinistro> getClasseEntidade() {
		return Sinistro.class;
	}

	@Override
	public Sinistro buscar(String numero) {
		return (Sinistro) super.buscar(numero);
	}
}
