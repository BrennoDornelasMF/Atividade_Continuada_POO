package br.edu.cs.poo.ac.seguro.daos;

import br.edu.cs.poo.ac.seguro.entidades.Segurado;

public abstract class SeguradoDAO extends DAOGenerico<Segurado> {

	@Override
	public Segurado buscar(String numero) {
		return (Segurado) super.buscar(numero);
	}
}
