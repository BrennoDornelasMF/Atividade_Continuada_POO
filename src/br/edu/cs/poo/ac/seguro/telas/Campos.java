package br.edu.cs.poo.ac.seguro.telas;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.MaskFormatter;

/**
 * Fábricas de controles Swing escolhidos pela natureza do dado:
 * documentos e CEP usam campo com máscara, datas usam JSpinner de data
 * e valores em dinheiro usam JSpinner numérico.
 */
public final class Campos {

	public static final String[] UFS = { "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG",
			"PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO" };

	public static final String[] PAISES = { "Brasil", "Argentina", "Chile", "Estados Unidos", "Paraguai", "Portugal",
			"Uruguai" };

	private Campos() {
	}

	/** Campo com máscara (ex.: "###.###.###-##"). O valor guardado só tem os dígitos. */
	public static JFormattedTextField criarCampoComMascara(String mascara) {
		try {
			MaskFormatter formatador = new MaskFormatter(mascara);
			formatador.setPlaceholderCharacter('_');
			formatador.setValueContainsLiteralCharacters(false);
			JFormattedTextField campo = new JFormattedTextField(formatador);
			// Se o usuário digitar só parte do número, o texto fica na tela em vez de
			// ser descartado ao sair do campo; assim o mediator pode reclamar do tamanho.
			campo.setFocusLostBehavior(JFormattedTextField.PERSIST);
			return campo;
		} catch (ParseException e) {
			throw new IllegalArgumentException("Máscara inválida: " + mascara, e);
		}
	}

	/** Devolve só os dígitos digitados num campo com máscara ("" se estiver vazio). */
	public static String somenteDigitos(JFormattedTextField campo) {
		return campo.getText().replaceAll("\\D", "");
	}

	/** Escreve dígitos num campo com máscara; null ou vazio limpa o campo. */
	public static void escreverDigitos(JFormattedTextField campo, String digitos) {
		if (digitos == null || digitos.isEmpty()) {
			campo.setValue(null);
		} else {
			campo.setValue(digitos);
		}
	}

	/** Seletor de data no formato dd/MM/aaaa, iniciado com a data de hoje. */
	public static JSpinner criarSpinnerData() {
		JSpinner spinner = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH));
		JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "dd/MM/yyyy");
		editor.getTextField().setColumns(9);
		spinner.setEditor(editor);
		return spinner;
	}

	/** Lê a data, aproveitando o que foi digitado à mão. Lança ParseException se o texto for inválido. */
	public static LocalDate lerData(JSpinner spinner) throws ParseException {
		spinner.commitEdit();
		Date data = (Date) spinner.getValue();
		return data.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public static void escreverData(JSpinner spinner, LocalDate data) {
		spinner.setValue(Date.from(data.atStartOfDay(ZoneId.systemDefault()).toInstant()));
	}

	/** Seletor de valor em dinheiro (duas casas decimais, a partir de zero). */
	public static JSpinner criarSpinnerValor() {
		JSpinner spinner = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 1.0E12, 100.0));
		JSpinner.NumberEditor editor = new JSpinner.NumberEditor(spinner, "#,##0.00");
		editor.getTextField().setColumns(12);
		spinner.setEditor(editor);
		return spinner;
	}

	/** Lê o valor, aproveitando o que foi digitado à mão. Lança ParseException se o texto for inválido. */
	public static double lerValor(JSpinner spinner) throws ParseException {
		spinner.commitEdit();
		return ((Number) spinner.getValue()).doubleValue();
	}

	public static void escreverValor(JSpinner spinner, double valor) {
		spinner.setValue(valor);
	}

	/** Texto sem espaços nas pontas; null vira "". */
	public static String texto(String s) {
		return s == null ? "" : s.trim();
	}

	/** Para campos opcionais: texto em branco vira null. */
	public static String textoOuNulo(String s) {
		String t = texto(s);
		return t.isEmpty() ? null : t;
	}
}
