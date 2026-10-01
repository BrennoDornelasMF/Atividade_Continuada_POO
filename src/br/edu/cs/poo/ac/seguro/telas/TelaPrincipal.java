package br.edu.cs.poo.ac.seguro.telas;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Janela de entrada: um botão para abrir a tela de cada funcionalidade.
 * Execute esta classe para usar o sistema.
 */
public class TelaPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;

	private final JButton btnSeguradoPessoa = new JButton("Segurado Pessoa");
	private final JButton btnSeguradoEmpresa = new JButton("Segurado Empresa");
	private final JButton btnSair = new JButton("Sair");

	public TelaPrincipal() {
		super("Seguradora de Veículos");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		JPanel p = new JPanel(new GridLayout(0, 1, 0, 8));
		p.setBorder(BorderFactory.createEmptyBorder(14, 24, 14, 24));
		p.add(new JLabel("Cadastros", SwingConstants.CENTER));
		p.add(btnSeguradoPessoa);
		p.add(btnSeguradoEmpresa);
		p.add(btnSair);
		setContentPane(p);

		btnSeguradoPessoa.addActionListener(e -> new TelaSeguradoPessoa().setVisible(true));
		btnSeguradoEmpresa.addActionListener(e -> new TelaSeguradoEmpresa().setVisible(true));
		btnSair.addActionListener(e -> dispose());

		pack();
		setLocationRelativeTo(null);
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// segue com o visual padrão do Swing
		}
		SwingUtilities.invokeLater(() -> new TelaPrincipal().setVisible(true));
	}
}
