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
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import br.edu.cs.poo.ac.seguro.entidades.SeguradoPessoa;
import br.edu.cs.poo.ac.seguro.mediators.SeguradoPessoaMediator;

/**
 * Tela única do CRUD de Segurado Pessoa (buscar, incluir, alterar e excluir).
 * Toda validação e todo acesso aos dados passam pelo SeguradoPessoaMediator.
 */
public class TelaSeguradoPessoa extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final String TITULO = "Segurado Pessoa";

	// Mediator usado por esta tela
	private final SeguradoPessoaMediator mediator = SeguradoPessoaMediator.getInstancia();

	// Campos escolhidos pela natureza do dado
	private final JFormattedTextField txtCpf = Campos.criarCampoComMascara("###.###.###-##");
	private final JTextField txtNome = new JTextField(30);
	private final JSpinner spnDataNascimento = Campos.criarSpinnerData();
	private final JSpinner spnRenda = Campos.criarSpinnerValor();
	private final JTextField txtBonus = new JTextField(12);
	private final PainelEndereco painelEndereco = new PainelEndereco();

	private final JButton btnBuscar = new JButton("Buscar");
	private final JButton btnIncluir = new JButton("Incluir");
	private final JButton btnAlterar = new JButton("Alterar");
	private final JButton btnExcluir = new JButton("Excluir");
	private final JButton btnLimpar = new JButton("Limpar");
	private final JButton btnFechar = new JButton("Fechar");

	public TelaSeguradoPessoa() {
		super(TITULO);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		txtBonus.setEditable(false);
		txtBonus.setHorizontalAlignment(JTextField.RIGHT);
		txtCpf.setColumns(12);

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
		adicionarLinha(p, 0, "CPF:", linha(txtCpf, btnBuscar));
		adicionarLinha(p, 1, "Nome:", txtNome);
		adicionarLinha(p, 2, "Data de nascimento:", spnDataNascimento);
		adicionarLinha(p, 3, "Renda mensal (R$):", spnRenda);
		adicionarLinha(p, 4, "Bônus (somente leitura):", txtBonus);
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
		String cpf = Campos.somenteDigitos(txtCpf);
		String erro = mediator.validarCpf(cpf);
		if (erro != null) {
			mostrarErro(erro);
			return;
		}
		SeguradoPessoa seg = mediator.buscarSeguradoPessoa(cpf);
		if (seg == null) {
			mostrarErro("Não existe segurado pessoa com este CPF.");
			return;
		}
		preencher(seg);
	}

	private void incluir() {
		SeguradoPessoa seg = lerFormulario(BigDecimal.ZERO);
		if (seg == null) {
			return;
		}
		String erro = mediator.incluirSeguradoPessoa(seg);
		if (erro != null) {
			mostrarErro(erro);
		} else {
			mostrarInfo("Segurado pessoa incluído com sucesso.");
		}
	}

	private void alterar() {
		// A tela não edita o bônus; ao alterar, mantém o bônus que já está gravado.
		SeguradoPessoa seg = lerFormulario(bonusGravado(Campos.somenteDigitos(txtCpf)));
		if (seg == null) {
			return;
		}
		String erro = mediator.alterarSeguradoPessoa(seg);
		if (erro != null) {
			mostrarErro(erro);
		} else {
			mostrarInfo("Segurado pessoa alterado com sucesso.");
		}
	}

	private void excluir() {
		String cpf = Campos.somenteDigitos(txtCpf);
		String erro = mediator.validarCpf(cpf);
		if (erro != null) {
			mostrarErro(erro);
			return;
		}
		if (!confirmar("Excluir o segurado pessoa de CPF " + txtCpf.getText() + "?")) {
			return;
		}
		erro = mediator.excluirSeguradoPessoa(cpf);
		if (erro != null) {
			mostrarErro(erro);
		} else {
			mostrarInfo("Segurado pessoa excluído com sucesso.");
			limpar();
		}
	}

	// ------------------------------------------------- formulário <-> objeto

	/** Lê a tela e monta o SeguradoPessoa. Devolve null (após avisar) se data ou renda estiverem ilegíveis. */
	private SeguradoPessoa lerFormulario(BigDecimal bonus) {
		LocalDate dataNascimento;
		double renda;
		try {
			dataNascimento = Campos.lerData(spnDataNascimento);
		} catch (ParseException e) {
			mostrarErro("Data de nascimento inválida. Use o formato dd/mm/aaaa.");
			return null;
		}
		try {
			renda = Campos.lerValor(spnRenda);
		} catch (ParseException e) {
			mostrarErro("Renda inválida.");
			return null;
		}
		return new SeguradoPessoa(Campos.texto(txtNome.getText()), painelEndereco.getEndereco(), dataNascimento, bonus,
				Campos.somenteDigitos(txtCpf), renda);
	}

	private void preencher(SeguradoPessoa seg) {
		Campos.escreverDigitos(txtCpf, seg.getCpf());
		txtNome.setText(seg.getNome());
		if (seg.getDataNacimento() != null) {
			Campos.escreverData(spnDataNascimento, seg.getDataNacimento());
		}
		Campos.escreverValor(spnRenda, seg.getRenda());
		mostrarBonus(seg.getBonus());
		painelEndereco.setEndereco(seg.getEndereco());
	}

	private BigDecimal bonusGravado(String cpf) {
		if (mediator.validarCpf(cpf) == null) {
			SeguradoPessoa gravado = mediator.buscarSeguradoPessoa(cpf);
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
		Campos.escreverDigitos(txtCpf, null);
		txtNome.setText("");
		Campos.escreverData(spnDataNascimento, LocalDate.now());
		Campos.escreverValor(spnRenda, 0.0);
		mostrarBonus(BigDecimal.ZERO);
		painelEndereco.limpar();
		txtCpf.requestFocusInWindow();
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
