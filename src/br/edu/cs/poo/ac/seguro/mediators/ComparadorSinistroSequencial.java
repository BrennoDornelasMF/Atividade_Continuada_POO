package br.edu.cs.poo.ac.seguro.mediators;

import java.util.Comparator;

import br.edu.cs.poo.ac.seguro.entidades.Sinistro;

public class ComparadorSinistroSequencial implements Comparator<Sinistro> {

	@Override
	public int compare(Sinistro primeiro, Sinistro segundo) {
		return Integer.compare(primeiro.getSequencial(), segundo.getSequencial());
	}
}
