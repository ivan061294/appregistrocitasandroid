package Entidades;

import java.io.Serializable;
import java.util.Date;

public class Citas implements Serializable {
    private String cId;
    private String cIdCbo;
    private String MedicoId;
    private String nomMedico;
    private String PacienteId;
    private String EspecialidadId;
    private String nomEspecialidad;
    private String fechaAtencion;
    private String HorarioId;
    private String Estado;
    private String nomHorario;
    private String Observaciones;
    private String usuarioCreacion;
    private String usuarioModificacion;
    private String estado;

    public Citas() {
    }

    public Citas(String cId, String cIdCbo, String medicoId, String nomMedico, String pacienteId, String especialidadId, String nomEspecialidad, String fechaAtencion, String horarioId, String estado, String nomHorario, String observaciones, String usuarioCreacion, String usuarioModificacion, String estado1) {
        this.cId = cId;
        this.cIdCbo = cIdCbo;
        MedicoId = medicoId;
        this.nomMedico = nomMedico;
        PacienteId = pacienteId;
        EspecialidadId = especialidadId;
        this.nomEspecialidad = nomEspecialidad;
        this.fechaAtencion = fechaAtencion;
        HorarioId = horarioId;
        Estado = estado;
        this.nomHorario = nomHorario;
        Observaciones = observaciones;
        this.usuarioCreacion = usuarioCreacion;
        this.usuarioModificacion = usuarioModificacion;
        this.estado = estado1;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getcIdCbo() {
        return cIdCbo;
    }

    public void setcIdCbo(String cIdCbo) {
        this.cIdCbo = cIdCbo;
    }

    public String getMedicoId() {
        return MedicoId;
    }

    public void setMedicoId(String medicoId) {
        MedicoId = medicoId;
    }

    public String getNomMedico() {
        return nomMedico;
    }

    public void setNomMedico(String nomMedico) {
        this.nomMedico = nomMedico;
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

    public String getNomEspecialidad() {
        return nomEspecialidad;
    }

    public void setNomEspecialidad(String nomEspecialidad) {
        this.nomEspecialidad = nomEspecialidad;
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

    public String getNomHorario() {
        return nomHorario;
    }

    public void setNomHorario(String nomHorario) {
        this.nomHorario = nomHorario;
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
        return  cIdCbo ;
    }

    public String toString2() {
        return "Citas{" +
                "cId='" + cId + '\'' +
                ", cIdCbo='" + cIdCbo + '\'' +
                ", MedicoId='" + MedicoId + '\'' +
                ", nomMedico='" + nomMedico + '\'' +
                ", PacienteId='" + PacienteId + '\'' +
                ", EspecialidadId='" + EspecialidadId + '\'' +
                ", nomEspecialidad='" + nomEspecialidad + '\'' +
                ", fechaAtencion='" + fechaAtencion + '\'' +
                ", HorarioId='" + HorarioId + '\'' +
                ", Estado='" + Estado + '\'' +
                ", nomHorario='" + nomHorario + '\'' +
                ", Observaciones='" + Observaciones + '\'' +
                ", usuarioCreacion='" + usuarioCreacion + '\'' +
                ", usuarioModificacion='" + usuarioModificacion + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}
