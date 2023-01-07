package Entidades;

public class ClassCboModel {
    public String id;
    public String valor;

    public ClassCboModel() {
    }

    public ClassCboModel(String id, String valor) {
        this.id = id;
        this.valor = valor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
    @Override
    public String toString() {
        return valor;
    }

}
