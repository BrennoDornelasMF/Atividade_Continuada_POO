package br.edu.cs.poo.ac.seguro.telas;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;
import br.edu.cs.poo.ac.seguro.mediators.SeguradoEmpresaMediator;

/**
 * Tela única do CRUD de Segurado Empresa (buscar, incluir, alterar e excluir).
 * Toda validação e todo acesso aos dados passam pelo SeguradoEmpresaMediator.
 */
public class TelaSeguradoEmpresa extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final String TITULO = "Segurado Empresa";

	// Mediator usado por esta tela
	private final SeguradoEmpresaMediator mediator = SeguradoEmpresaMediator.getInstancia();

	// Campos escolhidos pela natureza do dado
	private final JFormattedTextField txtCnpj = Campos.criarCampoComMascara("##.###.###/####-##");
	private final JTextField txtNome = new JTextField(30);
	private final JSpinner spnDataAbertura = Campos.criarSpinnerData();
	private final JSpinner spnFaturamento = Campos.criarSpinnerValor();
	private final JCheckBox chkLocadora = new JCheckBox("Sim, é locadora de veículos");
	private final JTextField txtBonus = new JTextField(12);
	private final PainelEndereco painelEndereco = new PainelEndereco();

	private final JButton btnBuscar = new JButton("Buscar");
	private final JButton btnIncluir = new JButton("Incluir");
	private final JButton btnAlterar = new JButton("Alterar");
	private final JButton btnExcluir = new JButton("Excluir");
	private final JButton btnLimpar = new JButton("Limpar");
	private final JButton btnFechar = new JButton("Fechar");

	public TelaSeguradoEmpresa() {
		super(TITULO);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		txtBonus.setEditable(false);
		txtBonus.setHorizontalAlignment(JTextField.RIGHT);
		txtCnpj.setColumns(16);

		JPanel conteudo = new JPanel();
		conteudo.setLayout(new BoxLayout(conteudo, BoxLayout.Y_AXIS));
		conteudo.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
		conteudo.add(criarPainelDados());
		conteudo.add(Box.createVerticalStrut(6));
		conteudo.add(painelEndereco);
		conteudo.add(Box.createVerticalStrut(6));
		conteudo.add(criarPainelBotoes());
		setContentPane(conteudo);

		configurarEventos();
		limpar();
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
	}

	// ---------------------------------------------------------------- layout

	private JPanel criarPainelDados() {
		JPanel p = new JPanel(new GridBagLayout());
		p.setBorder(new TitledBorder("Dados do segurado"));
		adicionarLinha(p, 0, "CNPJ:", linha(txtCnpj, btnBuscar));
		adicionarLinha(p, 1, "Nome:", txtNome);
		adicionarLinha(p, 2, "Data de abertura:", spnDataAbertura);
		adicionarLinha(p, 3, "Faturamento (R$):", spnFaturamento);
		adicionarLinha(p, 4, "Locadora de veículos:", chkLocadora);
		adicionarLinha(p, 5, "Bônus (somente leitura):", txtBonus);
		return p;
	}

	private static JPanel linha(java.awt.Component... itens) {
		JPanel l = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
		for (java.awt.Component c : itens) {
			l.add(c);
		}
		return l;
	}

	private static void adicionarLinha(JPanel p, int linha, String rotulo, java.awt.Component campo) {
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(3, 4, 3, 4);
		c.gridy = linha;
		c.gridx = 0;
		c.anchor = GridBagConstraints.LINE_END;
		p.add(new JLabel(rotulo), c);
		c = new GridBagConstraints();
		c.insets = new Insets(3, 4, 3, 4);
		c.gridy = linha;
		c.gridx = 1;
		c.weightx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = campo instanceof JTextField ? GridBagConstraints.HORIZONTAL : GridBagConstraints.NONE;
		p.add(campo, c);
	}

	private JPanel criarPainelBotoes() {
		JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 4));
		p.add(btnLimpar);
		p.add(btnIncluir);
		p.add(btnAlterar);
		p.add(btnExcluir);
		p.add(btnFechar);
		return p;
	}

	private void configurarEventos() {
		btnBuscar.addActionListener(e -> buscar());
		btnIncluir.addActionListener(e -> incluir());
		btnAlterar.addActionListener(e -> alterar());
		btnExcluir.addActionListener(e -> excluir());
		btnLimpar.addActionListener(e -> limpar());
		btnFechar.addActionListener(e -> dispose());
	}

	// ------------------------------------------------------------ operações

	private void buscar() {
		String cnpj = Campos.somenteDigitos(txtCnpj);
		String erro = mediator.validarCnpj(cnpj);
		if (erro != null) {
			mostrarErro(erro);
			return;
		}
		SeguradoEmpresa seg = mediator.buscarSeguradoEmpresa(cnpj);
		if (seg == null) {
			mostrarErro("Não existe segurado empresa com este CNPJ.");
			return;
		}
		preencher(seg);
	}

	private void incluir() {
		SeguradoEmpresa seg = lerFormulario(BigDecimal.ZERO);
		if (seg == null) {
			return;
		}
		String erro = mediator.incluirSeguradoEmpresa(seg);
		if (erro != null) {
			mostrarErro(erro);
		} else {
			mostrarInfo("Segurado empresa incluído com sucesso.");
		}
	}

	private void alterar() {
		// A tela não edita o bônus; ao alterar, mantém o bônus que já está gravado.
		SeguradoEmpresa seg = lerFormulario(bonusGravado(Campos.somenteDigitos(txtCnpj)));
		if (seg == null) {
			return;
		}
		String erro = mediator.alterarSeguradoEmpresa(seg);
		if (erro != null) {
			mostrarErro(erro);
		} else {
			mostrarInfo("Segurado empresa alterado com sucesso.");
		}
	}

	private void excluir() {
		String cnpj = Campos.somenteDigitos(txtCnpj);
		String erro = mediator.validarCnpj(cnpj);
		if (erro != null) {
			mostrarErro(erro);
			return;
		}
		if (!confirmar("Excluir o segurado empresa de CNPJ " + txtCnpj.getText() + "?")) {
			return;
		}
		erro = mediator.excluirSeguradoEmpresa(cnpj);
		if (erro != null) {
			mostrarErro(erro);
		} else {
			mostrarInfo("Segurado empresa excluído com sucesso.");
			limpar();
		}
	}

	// ------------------------------------------------- formulário <-> objeto

	/** Lê a tela e monta o SeguradoEmpresa. Devolve null (após avisar) se data ou faturamento estiverem ilegíveis. */
	private SeguradoEmpresa lerFormulario(BigDecimal bonus) {
		LocalDate dataAbertura;
		double faturamento;
		try {
			dataAbertura = Campos.lerData(spnDataAbertura);
		} catch (ParseException e) {
			mostrarErro("Data de abertura inválida. Use o formato dd/mm/aaaa.");
			return null;
		}
		try {
			faturamento = Campos.lerValor(spnFaturamento);
		} catch (ParseException e) {
			mostrarErro("Faturamento inválido.");
			return null;
		}
		return new SeguradoEmpresa(Campos.texto(txtNome.getText()), painelEndereco.getEndereco(), dataAbertura, bonus,
				Campos.somenteDigitos(txtCnpj), faturamento, chkLocadora.isSelected());
	}

	private void preencher(SeguradoEmpresa seg) {
		Campos.escreverDigitos(txtCnpj, seg.getCnpj());
		txtNome.setText(seg.getNome());
		if (seg.getDataAbertura() != null) {
			Campos.escreverData(spnDataAbertura, seg.getDataAbertura());
		}
		Campos.escreverValor(spnFaturamento, seg.getFaturamento());
		chkLocadora.setSelected(seg.isEhLocadoraDeVeiculos());
		mostrarBonus(seg.getBonus());
		painelEndereco.setEndereco(seg.getEndereco());
	}

	private BigDecimal bonusGravado(String cnpj) {
		if (mediator.validarCnpj(cnpj) == null) {
			SeguradoEmpresa gravado = mediator.buscarSeguradoEmpresa(cnpj);
			if (gravado != null && gravado.getBonus() != null) {
				return gravado.getBonus();
			}
		}
		return BigDecimal.ZERO;
	}

	private void mostrarBonus(BigDecimal bonus) {
		BigDecimal valor = bonus == null ? BigDecimal.ZERO : bonus;
		txtBonus.setText(NumberFormat.getCurrencyInstance(Locale.of("pt", "BR")).format(valor));
	}

	private void limpar() {
		Campos.escreverDigitos(txtCnpj, null);
		txtNome.setText("");
		Campos.escreverData(spnDataAbertura, LocalDate.now());
		Campos.escreverValor(spnFaturamento, 0.0);
		chkLocadora.setSelected(false);
		mostrarBonus(BigDecimal.ZERO);
		painelEndereco.limpar();
		txtCnpj.requestFocusInWindow();
	}

	// ----------------------------------------------------------- mensagens

	protected void mostrarErro(String mensagem) {
		JOptionPane.showMessageDialog(this, mensagem, TITULO, JOptionPane.ERROR_MESSAGE);
	}

	protected void mostrarInfo(String mensagem) {
		JOptionPane.showMessageDialog(this, mensagem, TITULO, JOptionPane.INFORMATION_MESSAGE);
	}

	protected boolean confirmar(String pergunta) {
		return JOptionPane.showConfirmDialog(this, pergunta, TITULO, JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
	}
}
