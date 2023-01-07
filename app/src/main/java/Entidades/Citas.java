package Entidades;

import java.util.Date;

public class Citas {
    private int Id;
    private String MedicoId;
    private String PacienteId;
    private String EspecialidadId;
    private String fechaAtencion;
    private String HorarioId;
    private String Estado;
    private String Observaciones;
    private String usuarioCreacion;
    private String usuarioModificacion;

    public Citas() {
    }

    public Citas(int id, String medicoId, String pacienteId, String especialidadId, String fechaAtencion, String horarioId, String estado, String observaciones, String usuarioCreacion, String usuarioModificacion) {
        Id = id;
        MedicoId = medicoId;
        PacienteId = pacienteId;
        EspecialidadId = especialidadId;
        this.fechaAtencion = fechaAtencion;
        HorarioId = horarioId;
        Estado = estado;
        Observaciones = observaciones;
        this.usuarioCreacion = usuarioCreacion;
        this.usuarioModificacion = usuarioModificacion;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getMedicoId() {
        return MedicoId;
    }

    public void setMedicoId(String medicoId) {
        MedicoId = medicoId;
    }

    public String getPacienteId() {
        return PacienteId;
    }

    public void setPacienteId(String pacienteId) {
        PacienteId = pacienteId;
    }

    public String getEspecialidadId() {
        return EspecialidadId;
    }

    public void setEspecialidadId(String especialidadId) {
        EspecialidadId = especialidadId;
    }

    public String getFechaAtencion() {
        return fechaAtencion;
    }

    public void setFechaAtencion(String fechaAtencion) {
        this.fechaAtencion = fechaAtencion;
    }

    public String getHorarioId() {
        return HorarioId;
    }

    public void setHorarioId(String horarioId) {
        HorarioId = horarioId;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }

    public String getObservaciones() {
        return Observaciones;
    }

    public void setObservaciones(String observaciones) {
        Observaciones = observaciones;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    @Override
    public String toString() {
        return Id +MedicoId + PacienteId+ EspecialidadId + fechaAtencion + HorarioId +Estado + Observaciones+ usuarioCreacion + usuarioModificacion ;
    }
}
