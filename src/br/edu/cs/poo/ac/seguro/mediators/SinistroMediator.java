package br.edu.cs.poo.ac.seguro.mediators;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import br.edu.cs.poo.ac.seguro.daos.ApoliceDAO;
import br.edu.cs.poo.ac.seguro.daos.SinistroDAO;
import br.edu.cs.poo.ac.seguro.daos.VeiculoDAO;
import br.edu.cs.poo.ac.seguro.entidades.Apolice;
import br.edu.cs.poo.ac.seguro.entidades.Registro;
import br.edu.cs.poo.ac.seguro.entidades.Sinistro;
import br.edu.cs.poo.ac.seguro.entidades.TipoSinistro;
import br.edu.cs.poo.ac.seguro.entidades.Veiculo;
import br.edu.cs.poo.ac.seguro.excecoes.ExcecaoValidacaoDados;

public class SinistroMediator {

	private static final SinistroMediator instancia = new SinistroMediator();

	private final VeiculoDAO daoVeiculo = new VeiculoDAO();
	private final ApoliceDAO daoApolice = new ApoliceDAO();
	private final SinistroDAO daoSinistro = new SinistroDAO();

	private SinistroMediator() {
	}

	public static SinistroMediator getInstancia() {
		return instancia;
	}

	public synchronized String incluirSinistro(DadosSinistro dados, LocalDateTime dataHoraAtual)
			throws ExcecaoValidacaoDados {
		Veiculo veiculo = validarDados(dados, dataHoraAtual);
		Apolice apolice = buscarApoliceVigente(dados.getPlaca(), dados.getDataHoraSinistro());
		if (apolice == null) {
			throw new ExcecaoValidacaoDados(List.of("Não existe apólice vigente para o veículo"));
		}

		BigDecimal valor = BigDecimal.valueOf(dados.getValorSinistro()).stripTrailingZeros();
		valor = valor.setScale(Math.max(0, valor.scale()));
		if (apolice.getValorMaximoSegurado() == null) {
			throw new ExcecaoValidacaoDados(List.of("Valor máximo segurado da apólice deve ser informado"));
		}
		if (valor.compareTo(apolice.getValorMaximoSegurado()) > 0) {
			throw new ExcecaoValidacaoDados(List.of(
					"Valor do sinistro não pode ultrapassar o valor máximo segurado constante na apólice"));
		}

		int sequencial = calcularProximoSequencial(apolice.getNumero());
		String numero = "S" + apolice.getNumero() + String.format(Locale.ROOT, "%03d", sequencial);
		Sinistro sinistro = new Sinistro(numero, veiculo, dados.getDataHoraSinistro(), dataHoraAtual,
				dados.getUsuarioRegistro(), valor, TipoSinistro.getTipoSinistro(dados.getCodigoTipoSinistro()));
		sinistro.setNumeroApolice(apolice.getNumero());
		sinistro.setSequencial(sequencial);

		try {
			if (!daoSinistro.incluir(sinistro)) {
				throw new ExcecaoValidacaoDados(List.of("Não foi possível incluir o sinistro"));
			}
		} catch (RuntimeException e) {
			throw new ExcecaoValidacaoDados(List.of("Não foi possível incluir o sinistro"), e);
		}
		return numero;
	}

	private Veiculo validarDados(DadosSinistro dados, LocalDateTime dataHoraAtual) throws ExcecaoValidacaoDados {
		if (dados == null) {
			throw new ExcecaoValidacaoDados(List.of("Dados do sinistro devem ser informados"));
		}
		if (dataHoraAtual == null) {
			throw new ExcecaoValidacaoDados(List.of("Data/hora atual deve ser informada"));
		}

		List<String> mensagens = new ArrayList<>();
		if (dados.getDataHoraSinistro() == null) {
			mensagens.add("Data/hora do sinistro deve ser informada");
		} else if (!dados.getDataHoraSinistro().isBefore(dataHoraAtual)) {
			mensagens.add("Data/hora do sinistro deve ser menor que a data/hora atual");
		}

		Veiculo veiculo = null;
		if (StringUtils.ehNuloOuBranco(dados.getPlaca())) {
			mensagens.add("Placa do Veículo deve ser informada");
		} else {
			veiculo = daoVeiculo.buscar(dados.getPlaca());
			if (veiculo == null) {
				mensagens.add("Veículo não cadastrado");
			}
		}
		if (StringUtils.ehNuloOuBranco(dados.getUsuarioRegistro())) {
			mensagens.add("Usuário do registro de sinistro deve ser informado");
		}
		if (!Double.isFinite(dados.getValorSinistro()) || dados.getValorSinistro() <= 0) {
			mensagens.add("Valor do sinistro deve ser maior que zero");
		}
		if (TipoSinistro.getTipoSinistro(dados.getCodigoTipoSinistro()) == null) {
			mensagens.add("Código do tipo de sinistro inválido");
		}
		if (!mensagens.isEmpty()) {
			throw new ExcecaoValidacaoDados(mensagens);
		}
		return veiculo;
	}

	private Apolice buscarApoliceVigente(String placa, LocalDateTime dataHoraSinistro) {
		for (Registro registro : daoApolice.buscarTodos()) {
			Apolice apolice = (Apolice) registro;
			if (apolice.getVeiculo() == null || apolice.getDataInicioVigencia() == null
					|| !placa.equals(apolice.getVeiculo().getPlaca())) {
				continue;
			}
			LocalDateTime inicio = apolice.getDataInicioVigencia().atStartOfDay();
			// A cobertura começa no início da vigência e termina ao completar um ano.
			if (!dataHoraSinistro.isBefore(inicio) && dataHoraSinistro.isBefore(inicio.plusYears(1))) {
				return apolice;
			}
		}
		return null;
	}

	private int calcularProximoSequencial(String numeroApolice) {
		List<Sinistro> sinistros = new ArrayList<>();
		for (Registro registro : daoSinistro.buscarTodos()) {
			Sinistro sinistro = (Sinistro) registro;
			if (numeroApolice.equals(sinistro.getNumeroApolice())) {
				sinistros.add(sinistro);
			}
		}
		Collections.sort(sinistros, new ComparadorSinistroSequencial());
		return sinistros.isEmpty() ? 1 : sinistros.get(sinistros.size() - 1).getSequencial() + 1;
	}
}
