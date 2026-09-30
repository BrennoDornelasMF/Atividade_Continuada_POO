package br.edu.cs.poo.ac.seguro.testes;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import br.edu.cs.poo.ac.seguro.daos.ApoliceDAO;
import br.edu.cs.poo.ac.seguro.entidades.Apolice;

public class TesteApoliceDAO extends TesteDAO {
	private ApoliceDAO dao = new ApoliceDAO();

	protected Class getClasse() {
		return Apolice.class;
	}

	
	private Apolice criar(String numero, String franquia, String premio, String maximo) {
		Apolice a = new Apolice(null, new BigDecimal(franquia), new BigDecimal(premio), new BigDecimal(maximo));
		a.setNumero(numero);
		return a;
	}

	@Test
	public void teste01() {
		String numero = "00000000";
		cadastro.incluir(criar(numero, "500.00", "1200.00", "50000.00"), numero);
		Apolice ap = dao.buscar(numero);
		Assertions.assertNotNull(ap);
	}

	@Test
	public void teste02() {
		String numero = "10000000";
		cadastro.incluir(criar(numero, "501.00", "1201.00", "50001.00"), numero);
		Apolice ap = dao.buscar("11000000");
		Assertions.assertNull(ap);
	}

	@Test
	public void teste03() {
		String numero = "20000000";
		cadastro.incluir(criar(numero, "502.00", "1202.00", "50002.00"), numero);
		boolean ret = dao.excluir(numero);
		Assertions.assertTrue(ret);
	}

	@Test
	public void teste04() {
		String numero = "30000000";
		cadastro.incluir(criar(numero, "503.00", "1203.00", "50003.00"), numero);
		boolean ret = dao.excluir("31000000");
		Assertions.assertFalse(ret);
	}

	@Test
	public void teste05() {
		String numero = "40000000";
		boolean ret = dao.incluir(criar(numero, "504.00", "1204.00", "50004.00"));
		Assertions.assertTrue(ret);
		Apolice ap = dao.buscar(numero);
		Assertions.assertNotNull(ap);
	}

	@Test
	public void teste06() {
		String numero = "50000000";
		Apolice ap = criar(numero, "505.00", "1205.00", "50005.00");
		cadastro.incluir(ap, numero);
		boolean ret = dao.incluir(ap);
		Assertions.assertFalse(ret);
	}

	@Test
	public void teste07() {
		String numero = "60000000";
		boolean ret = dao.alterar(criar(numero, "506.00", "1206.00", "50006.00"));
		Assertions.assertFalse(ret);
		Apolice ap = dao.buscar(numero);
		Assertions.assertNull(ap);
	}

	@Test
	public void teste08() {
		String numero = "70000000";
		cadastro.incluir(criar(numero, "507.00", "1207.00", "50007.00"), numero);
		Apolice alterada = criar(numero, "800.00", "1900.00", "90000.00");
		boolean ret = dao.alterar(alterada);
		Assertions.assertTrue(ret);
		Apolice ap = dao.buscar(numero);
		Assertions.assertEquals(new BigDecimal("1900.00"), ap.getValorPremio());
		Assertions.assertEquals(new BigDecimal("90000.00"), ap.getValorMaximoSegurado());
	}
}