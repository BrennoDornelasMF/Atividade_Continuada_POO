package br.edu.cs.poo.ac.seguro.testes;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import br.edu.cs.poo.ac.seguro.daos.SinistroDAO;
import br.edu.cs.poo.ac.seguro.entidades.Sinistro;
import br.edu.cs.poo.ac.seguro.entidades.TipoSinistro;

public class TesteSinistroDAO extends TesteDAO {
	private SinistroDAO dao = new SinistroDAO();

	protected Class getClasse() {
		return Sinistro.class;
	}

	private Sinistro criar(String numero, BigDecimal valor, TipoSinistro tipo) {
		Sinistro s = new Sinistro(null, LocalDateTime.now().minusDays(1), LocalDateTime.now(),
				"USUARIO_TESTE", valor, tipo);
		s.setNumero(numero);
		return s;
	}

	@Test
	public void teste01() {
		String numero = "00000000";
		cadastro.incluir(criar(numero, new BigDecimal("1000.00"), TipoSinistro.COLISAO), numero);
		Sinistro si = dao.buscar(numero);
		Assertions.assertNotNull(si);
	}

	@Test
	public void teste02() {
		String numero = "10000000";
		cadastro.incluir(criar(numero, new BigDecimal("1001.00"), TipoSinistro.COLISAO), numero);
		Sinistro si = dao.buscar("11000000");
		Assertions.assertNull(si);
	}

	@Test
	public void teste03() {
		String numero = "20000000";
		cadastro.incluir(criar(numero, new BigDecimal("1002.00"), TipoSinistro.FURTO), numero);
		boolean ret = dao.excluir(numero);
		Assertions.assertTrue(ret);
	}

	@Test
	public void teste04() {
		String numero = "30000000";
		cadastro.incluir(criar(numero, new BigDecimal("1003.00"), TipoSinistro.FURTO), numero);
		boolean ret = dao.excluir("31000000");
		Assertions.assertFalse(ret);
	}

	@Test
	public void teste05() {
		String numero = "40000000";
		boolean ret = dao.incluir(criar(numero, new BigDecimal("1004.00"), TipoSinistro.INCENDIO));
		Assertions.assertTrue(ret);
		Sinistro si = dao.buscar(numero);
		Assertions.assertNotNull(si);
	}

	@Test
	public void teste06() {
		String numero = "50000000";
		Sinistro si = criar(numero, new BigDecimal("1005.00"), TipoSinistro.ENCHENTE);
		cadastro.incluir(si, numero);
		boolean ret = dao.incluir(si);
		Assertions.assertFalse(ret);
	}

	@Test
	public void teste07() {
		String numero = "60000000";
		cadastro.incluir(criar(numero, new BigDecimal("1006.00"), TipoSinistro.ENCHENTE), numero);
		boolean ret = dao.alterar(criar(numero, new BigDecimal("1006.00"), TipoSinistro.DEPREDACAO));
		Assertions.assertTrue(ret);
		Sinistro si = dao.buscar(numero);
		Assertions.assertNotNull(si);
	}

	@Test
	public void teste08() {
		String numero = "70000000";
		cadastro.incluir(criar(numero, new BigDecimal("1007.00"), TipoSinistro.COLISAO), numero);
		Sinistro alterado = criar(numero, new BigDecimal("2500.00"), TipoSinistro.FURTO);
		boolean ret = dao.alterar(alterado);
		Assertions.assertTrue(ret);
		// conferencia extra: o dado gravado realmente mudou
		Sinistro si = dao.buscar(numero);
		Assertions.assertEquals(new BigDecimal("2500.00"), si.getValorSinistro());
		Assertions.assertEquals(TipoSinistro.FURTO, si.getTipo());
	}
}