package modelo;

public class Carta {

	private int id;
	private String valor; // 'A','2','3',...'J','Q','K'
	private String palo; // 'Corazones','Diamantes','Treboles','Picas'
	private int puntos; // valor numérico en Blackjack

	// Constructor vacío
	public Carta() {
		
	}

	// Constructor completo
	public Carta(int id, String valor, String palo, int puntos) {
		
		this.id = id;
		this.valor = valor;
		this.palo = palo;
		this.puntos = puntos;
		
	}

	// Getters y Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getPalo() {
		return palo;
	}

	public void setPalo(String palo) {
		this.palo = palo;
	}

	public int getPuntos() {
		return puntos;
	}

	public void setPuntos(int puntos) {
		this.puntos = puntos;
	}	
	
	@Override
	public String toString() {
	    return  "\n┌─────────┐\n" +
	            String.format("│ %-2s %-2s   │\n", valor, palo) +
	            "│         │\n" +
	            "│         │\n" +
	            String.format("│    %s    │\n", palo) +
	            "│         │\n" +
	            "│         │\n" +
	            String.format("│    %-2s %-2s│\n", palo, valor) +
	            "└─────────┘";
	}
	
}
