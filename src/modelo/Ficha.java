package modelo;

public class Ficha {

    private int    id;
    private int    valor;   
    private String color;   

    //Constructor vacío
    public Ficha() {

    }

    //Constructor completo
    public Ficha(int id, int valor, String color) {
        this.id    = id;
        this.valor = valor;
        this.color = color;
    }

    //Constructor sin id (uso en lógica de juego)
    public Ficha(int valor, String color) {
        this.valor = valor;
        this.color = color;
    }

    //Getters y Setters
    public int getId() {
    	return id;
    	}
    public void setId(int id) {
    	this.id = id;
    	}

    public int getValor() {
    	return valor;
    	}
    
    public void setValor(int valor) {
    	this.valor = valor;
    	}

    public String getColor() {
    	return color;
    	}
    public void setColor(String color) {
    	this.color = color;
    	}

    @Override
    public String toString() {
        return "Ficha [" + color + " | " + valor + " €]";
    }
}
