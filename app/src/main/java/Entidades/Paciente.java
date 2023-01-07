package Entidades;

import java.util.Date;

public class Paciente {
    private int IdPaciente;
    private int Dni;
    private String Nombres;
    private String Apellidos;
    private String Password;
    private String Direccion;
    private String Telefono;
    private int Sexo;
    private Date FechaNacimiento;

    public Paciente() {
    }

    public Paciente(int idPaciente, int dni, String nombres, String apellidos, String password, String direccion, String telefono, int sexo, Date fechaNacimiento) {
        IdPaciente = idPaciente;
        Dni = dni;
        Nombres = nombres;
        Apellidos = apellidos;
        Password = password;
        Direccion = direccion;
        Telefono = telefono;
        Sexo = sexo;
        FechaNacimiento = fechaNacimiento;
    }

    public int getIdPaciente() {
        return IdPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        IdPaciente = idPaciente;
    }

    public int getDni() {
        return Dni;
    }

    public void setDni(int dni) {
        Dni = dni;
    }

    public String getNombres() {
        return Nombres;
    }

    public void setNombres(String nombres) {
        Nombres = nombres;
    }

    public String getApellidos() {
        return Apellidos;
    }

    public void setApellidos(String apellidos) {
        Apellidos = apellidos;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public int getSexo() {
        return Sexo;
    }

    public void setSexo(int sexo) {
        Sexo = sexo;
    }

    public Date getFechaNacimiento() {
        return FechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        FechaNacimiento = fechaNacimiento;
    }

    @Override
    public String toString() {
        return "Paciente{}";
    }
}
