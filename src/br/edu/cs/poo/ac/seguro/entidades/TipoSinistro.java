package br.edu.cs.poo.ac.seguro.entidades;

/*
 * Implementar um enum com as seguintes constantes:
 * 
 * 	COLISAO(1,"Colisão"),
	INCENDIO(2,"Incêndio"),
	FURTO(3, "Furto"),
	ENCHENTE(4, "Enchente"),
	DEPREDACAO(5, "Depredação");
 * 
 * O enum deve ter construtor privado, métodos get públicos para os atributos codigo e nome,
 * e um método público e estático TipoSinistro getTipoSinistro(int codigo), que 
 * retorna o tipo de sinistro correspondente ao código recebido como parâmetro
 */
public enum TipoSinistro{
	COLISAO(1,"Colisão"),
	INCENDIO(2,"Incêndio"),
	FURTO(3, "Furto"),
	ENCHENTE(4, "Enchente"),
	DEPREDACAO(5, "Depredação");
	
	private String nome;
	private int codigo;

	private TipoSinistro(int codigo, String nome) {
		this.codigo = codigo;
		this.nome = nome;
	}
	
	public static TipoSinistro getTipoSinistro(int codigo) {
		for (TipoSinistro tipo : TipoSinistro.values()) {
            if (tipo.getCodigo() == codigo) {
                return tipo;
            }
        }
		return null;
		
	}

	protected String getNome() {
		return nome;
	}

	protected int getCodigo() {
		return codigo;
	}

	
	

	
}


