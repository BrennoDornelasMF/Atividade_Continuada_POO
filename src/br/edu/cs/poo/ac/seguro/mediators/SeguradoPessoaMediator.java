package br.edu.cs.poo.ac.seguro.mediators;

import java.time.LocalDate;

import br.edu.cs.poo.ac.seguro.daos.SeguradoPessoaDAO;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoPessoa;

public class SeguradoPessoaMediator {
	
	private static final SeguradoPessoaMediator instancia = new SeguradoPessoaMediator();
	
	private SeguradoMediator seguradoMediator = SeguradoMediator.getInstancia();
	private SeguradoPessoaDAO dao = new SeguradoPessoaDAO();
	
	private SeguradoPessoaMediator() {
		
	}
	
	public static SeguradoPessoaMediator getInstancia() {
		return instancia;
	}
	
	public String validarCpf(String cpf) {
		if (StringUtils.ehNuloOuBranco(cpf)) {
			return "CPF deve ser informado";
		}
		if (cpf.length() != 11) {
			return "CPF deve ter 11 caracteres";
		}
		if (!ValidadorCpfCnpj.ehCpfValido(cpf)) {
			return "CPF com dígito inválido";
		}
		return null;
	}
	
	public String validarRenda(double renda) {
		if (renda < 0) {
			return "Renda deve ser maior ou igual à zero";
		}
		return null;
	}
	
	public String incluirSeguradoPessoa(SeguradoPessoa seg) {
		String msg = validarSeguradoPessoa(seg);
		if (msg != null) {
			return msg;
		}
		if (!dao.incluir(seg)) {
			return "CPF do segurado pessoa já existente";
		}
		return null;
		
	}
	
	public String alterarSeguradoPessoa(SeguradoPessoa seg) {
		String msg = validarSeguradoPessoa(seg);
		if (msg != null) {
			return msg;
		}
		if (!dao.alterar(seg)) {
			return "CPF do segurado pessoa não existente";
		}
		return null;	
	}
	
	
	public String excluirSeguradoPessoa(String cpf) {
		if (!dao.excluir(cpf)) {
			return "CPF do segurado pessoa não existente";
		}
		return null;
	}

	public SeguradoPessoa buscarSeguradoPessoa(String cpf) {
		return dao.buscar(cpf);
	}
	public String validarSeguradoPessoa(SeguradoPessoa seg) {
		String msg = seguradoMediator.validarNome(seg.getNome());
		if (msg != null) {
			return msg;
		}
		msg = seguradoMediator.validarEndereco(seg.getEndereco());
		if (msg != null) {
			return msg;
		}

		msg = validarCpf(seg.getCpf());
		if (msg != null) {
			return msg;
		}
		
		if (seg.getDataNacimento() == null) {
			return "Data do nascimento deve ser informada";
		}
		if (seg.getDataNacimento().isAfter(LocalDate.now())) {
			return "Data do nascimento deve ser menor ou igual à data atual";
		}
	
		return validarRenda(seg.getRenda());
	}
}