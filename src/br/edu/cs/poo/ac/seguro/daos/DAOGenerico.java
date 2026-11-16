package br.edu.cs.poo.ac.seguro.daos;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;

import br.edu.cesarschool.next.oo.persistenciaobjetos.CadastroObjetos;
import br.edu.cs.poo.ac.seguro.entidades.Registro;

public abstract class DAOGenerico<T extends Registro> {

	private final CadastroObjetos cadastro;

	public DAOGenerico() {
		cadastro = new CadastroObjetos(getClasseEntidade());
	}

	public abstract Class<? extends T> getClasseEntidade();

	public Registro buscar(String id) {
		return (Registro) cadastro.buscar(id);
	}

	public Registro[] buscarTodos() {
		Serializable[] objetos = cadastro.buscarTodos();
		Registro[] registros = new Registro[objetos.length];
		for (int i = 0; i < objetos.length; i++) {
			registros[i] = (Registro) objetos[i];
		}
		Arrays.sort(registros, Comparator.comparing(Registro::getIdUnico));
		return registros;
	}

	public boolean incluir(Registro registro) {
		String id = registro.getIdUnico();
		if (buscar(id) != null) {
			return false;
		}
		cadastro.incluir(registro, id);
		return true;
	}

	public boolean alterar(Registro registro) {
		String id = registro.getIdUnico();
		if (buscar(id) == null) {
			return false;
		}
		cadastro.alterar(registro, id);
		return true;
	}

	public boolean excluir(String id) {
		if (buscar(id) == null) {
			return false;
		}
		cadastro.excluir(id);
		return true;
	}
}
