package Entidades;

public class Medicos {
    private int MId;
    private String Nombre;
    private String Apellidos;
    private int DNI;
    private String Direccion;
    private String Correo;
    private String Telefono;
    private String sexo;
    private String numColegiatura;
    private int IdEspecialidad;
    private String fechaNacimiento;

    public Medicos() {
    }

    public Medicos(int MId, String nombre, String apellidos, int DNI, String direccion, String correo, String telefono, String sexo, String numColegiatura, int idEspecialidad, String fechaNacimiento) {
        this.MId = MId;
        Nombre = nombre;
        Apellidos = apellidos;
        this.DNI = DNI;
        Direccion = direccion;
        Correo = correo;
        Telefono = telefono;
        this.sexo = sexo;
        this.numColegiatura = numColegiatura;
        IdEspecialidad = idEspecialidad;
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getMId() {
        return MId;
    }

    public void setMId(int MId) {
        this.MId = MId;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getApellidos() {
        return Apellidos;
    }

    public void setApellidos(String apellidos) {
        Apellidos = apellidos;
    }

    public int getDNI() {
        return DNI;
    }

    public void setDNI(int DNI) {
        this.DNI = DNI;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getNumColegiatura() {
        return numColegiatura;
    }

    public void setNumColegiatura(String numColegiatura) {
        this.numColegiatura = numColegiatura;
    }

    public int getIdEspecialidad() {
        return IdEspecialidad;
    }

    public void setIdEspecialidad(int idEspecialidad) {
        IdEspecialidad = idEspecialidad;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

}
