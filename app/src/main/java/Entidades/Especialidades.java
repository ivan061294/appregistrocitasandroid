package Entidades;

public class Especialidades {
    private int Id;
    private String nombre;
    private String descripcion;

    public Especialidades() {
    }

    public Especialidades(int id, String nombre, String descripcion) {
        Id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
