package br.edu.cs.poo.ac.seguro.telas;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import br.edu.cs.poo.ac.seguro.entidades.TipoSinistro;
import br.edu.cs.poo.ac.seguro.excecoes.ExcecaoValidacaoDados;
import br.edu.cs.poo.ac.seguro.mediators.DadosSinistro;
import br.edu.cs.poo.ac.seguro.mediators.SinistroMediator;

/** Tela de inclusão; as regras de negócio ficam no SinistroMediator. */
public class TelaSinistro extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final String TITULO = "Registrar Sinistro";

	private final SinistroMediator mediator = SinistroMediator.getInstancia();
	private final JTextField txtPlaca = new JTextField(10);
	private final JSpinner spnDataHora = Campos.criarSpinnerDataHora();
	private final JTextField txtUsuario = new JTextField(25);
	private final JSpinner spnValor = Campos.criarSpinnerValor();
	private final JComboBox<TipoSinistro> cmbTipo = new JComboBox<>(TipoSinistro.values());

	public TelaSinistro() {
		super(TITULO);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		cmbTipo.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> lista, Object valor, int indice,
					boolean selecionado, boolean comFoco) {
				super.getListCellRendererComponent(lista, valor, indice, selecionado, comFoco);
				if (valor instanceof TipoSinistro tipo) {
					setText(tipo.getNome());
				}
				return this;
			}
		});

		JPanel formulario = new JPanel(new GridBagLayout());
		formulario.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
		adicionarLinha(formulario, 0, "Placa do veículo:", txtPlaca);
		adicionarLinha(formulario, 1, "Data e hora do ocorrido:", spnDataHora);
		adicionarLinha(formulario, 2, "Usuário do registro:", txtUsuario);
		adicionarLinha(formulario, 3, "Valor do sinistro (R$):", spnValor);
		adicionarLinha(formulario, 4, "Tipo de sinistro:", cmbTipo);

		JButton btnRegistrar = new JButton("Registrar sinistro");
		JButton btnLimpar = new JButton("Limpar");
		JButton btnFechar = new JButton("Fechar");
		btnRegistrar.addActionListener(e -> registrar());
		btnLimpar.addActionListener(e -> limpar());
		btnFechar.addActionListener(e -> dispose());
		JPanel botoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 4));
		botoes.add(btnRegistrar);
		botoes.add(btnLimpar);
		botoes.add(btnFechar);
		adicionarLinha(formulario, 5, "", botoes);

		setContentPane(formulario);
		getRootPane().setDefaultButton(btnRegistrar);
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
	}

	private static void adicionarLinha(JPanel painel, int linha, String rotulo, Component campo) {
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 4, 5, 4);
		c.gridy = linha;
		c.gridx = 0;
		c.anchor = GridBagConstraints.LINE_END;
		painel.add(new JLabel(rotulo), c);
		c.gridx = 1;
		c.weightx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		painel.add(campo, c);
	}

	private void registrar() {
		LocalDateTime dataHora;
		double valor;
		try {
			dataHora = Campos.lerDataHora(spnDataHora);
		} catch (ParseException e) {
			mostrarErro("Data e hora inválidas. Use o formato dd/mm/aaaa hh:mm.");
			return;
		}
		try {
			valor = Campos.lerValor(spnValor);
		} catch (ParseException e) {
			mostrarErro("Valor do sinistro inválido.");
			return;
		}

		TipoSinistro tipo = (TipoSinistro) cmbTipo.getSelectedItem();
		DadosSinistro dados = new DadosSinistro(Campos.texto(txtPlaca.getText()).toUpperCase(Locale.ROOT),
				dataHora, Campos.texto(txtUsuario.getText()), valor, tipo == null ? 0 : tipo.getCodigo());
		try {
			String numero = mediator.incluirSinistro(dados, LocalDateTime.now());
			JOptionPane.showMessageDialog(this, "Sinistro registrado com sucesso.\nNúmero: " + numero,
					TITULO, JOptionPane.INFORMATION_MESSAGE);
			limpar();
		} catch (ExcecaoValidacaoDados e) {
			mostrarErro(String.join("\n", e.getMensagens()));
		}
	}

	private void limpar() {
		txtPlaca.setText("");
		spnDataHora.setValue(new Date());
		txtUsuario.setText("");
		Campos.escreverValor(spnValor, 0);
		cmbTipo.setSelectedIndex(0);
		txtPlaca.requestFocusInWindow();
	}

	private void mostrarErro(String mensagem) {
		JOptionPane.showMessageDialog(this, mensagem, TITULO, JOptionPane.ERROR_MESSAGE);
	}
}
