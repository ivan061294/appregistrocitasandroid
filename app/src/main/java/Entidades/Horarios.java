package Entidades;

public class Horarios {
    private int id;
    private int MedicoId;
    private String TipoHorario;

    public Horarios() {
    }

    public Horarios(int id, int medicoId, String tipoHorario) {
        this.id = id;
        MedicoId = medicoId;
        TipoHorario = tipoHorario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMedicoId() {
        return MedicoId;
    }

    public void setMedicoId(int medicoId) {
        MedicoId = medicoId;
    }

    public String getTipoHorario() {
        return TipoHorario;
    }

    public void setTipoHorario(String tipoHorario) {
        TipoHorario = tipoHorario;
    }
}
