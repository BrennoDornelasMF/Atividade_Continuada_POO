package br.edu.cs.poo.ac.seguro.telas;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import br.edu.cs.poo.ac.seguro.entidades.Endereco;

/**
 * Painel com os campos de um Endereco. É usado pelas telas de segurado pessoa e
 * de segurado empresa, que têm exatamente o mesmo endereço.
 *
 * O painel só lê e escreve os campos; quem valida é o SeguradoMediator.
 */
public class PainelEndereco extends JPanel {

	private static final long serialVersionUID = 1L;

	private final JTextField txtLogradouro = new JTextField(28);
	private final JTextField txtNumero = new JTextField(8);
	private final JTextField txtComplemento = new JTextField(14);
	private final JFormattedTextField txtCep = Campos.criarCampoComMascara("#####-###");
	private final JTextField txtCidade = new JTextField(16);
	// Listas editáveis: o usuário escolhe na lista ou digita outro valor.
	private final JComboBox<String> cmbEstado = new JComboBox<>(comVazio(Campos.UFS));
	private final JComboBox<String> cmbPais = new JComboBox<>(Campos.PAISES);

	public PainelEndereco() {
		setBorder(new TitledBorder("Endereço"));
		setLayout(new GridBagLayout());
		cmbEstado.setEditable(true);
		cmbPais.setEditable(true);
		cmbEstado.setPrototypeDisplayValue("MMM");
		txtCep.setColumns(8);

		adicionar(0, 0, "Logradouro:", txtLogradouro, 3);
		adicionar(1, 0, "Número:", txtNumero, 1);
		adicionar(1, 2, "Complemento:", txtComplemento, 1);
		adicionar(2, 0, "CEP:", txtCep, 1);
		adicionar(2, 2, "Cidade:", txtCidade, 1);
		adicionar(3, 0, "Estado (UF):", cmbEstado, 1);
		adicionar(3, 2, "País:", cmbPais, 1);
		limpar();
	}

	private static String[] comVazio(String[] itens) {
		String[] r = new String[itens.length + 1];
		r[0] = "";
		System.arraycopy(itens, 0, r, 1, itens.length);
		return r;
	}

	private void adicionar(int linha, int coluna, String rotulo, java.awt.Component campo, int largura) {
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(3, 4, 3, 4);
		c.gridy = linha;
		c.gridx = coluna;
		c.anchor = GridBagConstraints.LINE_END;
		add(new JLabel(rotulo), c);
		c = new GridBagConstraints();
		c.insets = new Insets(3, 4, 3, 4);
		c.gridy = linha;
		c.gridx = coluna + 1;
		c.gridwidth = largura;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = largura > 1 ? GridBagConstraints.HORIZONTAL : GridBagConstraints.NONE;
		c.weightx = largura > 1 ? 1 : 0;
		add(campo, c);
	}

	private static String valor(JComboBox<String> combo) {
		Object item = combo.getEditor().getItem();
		return item == null ? "" : item.toString().trim();
	}

	/** Monta o Endereco com o que está na tela, sem validar nada. */
	public Endereco getEndereco() {
		return new Endereco(Campos.texto(txtLogradouro.getText()), Campos.somenteDigitos(txtCep),
				Campos.textoOuNulo(txtNumero.getText()), Campos.textoOuNulo(txtComplemento.getText()),
				valor(cmbPais), valor(cmbEstado).toUpperCase(), Campos.texto(txtCidade.getText()));
	}

	/** Mostra o endereço na tela; null limpa os campos. */
	public void setEndereco(Endereco e) {
		if (e == null) {
			limpar();
			return;
		}
		txtLogradouro.setText(e.getLogradouro());
		txtNumero.setText(e.getNumero() == null ? "" : e.getNumero());
		txtComplemento.setText(e.getComplemento() == null ? "" : e.getComplemento());
		Campos.escreverDigitos(txtCep, e.getCep());
		txtCidade.setText(e.getCidade());
		cmbEstado.setSelectedItem(e.getEstado());
		cmbPais.setSelectedItem(e.getPais());
	}

	public void limpar() {
		txtLogradouro.setText("");
		txtNumero.setText("");
		txtComplemento.setText("");
		Campos.escreverDigitos(txtCep, null);
		txtCidade.setText("");
		cmbEstado.setSelectedItem("");
		cmbPais.setSelectedItem("Brasil");
	}
}
