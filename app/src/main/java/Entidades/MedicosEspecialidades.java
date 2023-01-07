package Entidades;

public class MedicosEspecialidades {
    private int Id;
    private int MedicoId;
    private String Especialidad;

    public MedicosEspecialidades() {
    }

    public MedicosEspecialidades(int id, int medicoId, String especialidad) {
        Id = id;
        MedicoId = medicoId;
        Especialidad = especialidad;
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

    public String getEspecialidad() {
        return Especialidad;
    }

    public void setEspecialidad(String especialidad) {
        Especialidad = especialidad;
    }
}
