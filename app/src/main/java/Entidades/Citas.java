package Entidades;

import java.util.Date;

public class Citas {
    private int Id;
    private int MedicoId;
    private int PacienteId;
    private int EspecialidadId;
    private Date fechaAtencion;
    private int HorarioId;
    private int Estado;
    private String Observaciones;

    public Citas() {
    }

    public Citas(int id, int medicoId, int pacienteId, int especialidadId, Date fechaAtencion, int horarioId, int estado, String observaciones) {
        Id = id;
        MedicoId = medicoId;
        PacienteId = pacienteId;
        EspecialidadId = especialidadId;
        this.fechaAtencion = fechaAtencion;
        HorarioId = horarioId;
        Estado = estado;
        Observaciones = observaciones;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getMedicoId() {
        return MedicoId;
    }

    public void setMedicoId(int medicoId) {
        MedicoId = medicoId;
    }

    public int getPacienteId() {
        return PacienteId;
    }

    public void setPacienteId(int pacienteId) {
        PacienteId = pacienteId;
    }

    public int getEspecialidadId() {
        return EspecialidadId;
    }

    public void setEspecialidadId(int especialidadId) {
        EspecialidadId = especialidadId;
    }

    public Date getFechaAtencion() {
        return fechaAtencion;
    }

    public void setFechaAtencion(Date fechaAtencion) {
        this.fechaAtencion = fechaAtencion;
    }

    public int getHorarioId() {
        return HorarioId;
    }

    public void setHorarioId(int horarioId) {
        HorarioId = horarioId;
    }

    public int getEstado() {
        return Estado;
    }

    public void setEstado(int estado) {
        Estado = estado;
    }

    public String getObservaciones() {
        return Observaciones;
    }

    public void setObservaciones(String observaciones) {
        Observaciones = observaciones;
    }

}
