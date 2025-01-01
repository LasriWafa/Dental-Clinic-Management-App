package ma.VitaCareApp.entities;

public class DossierPatient {

    private Long id;
    private String nomMedcin;
    private Patient idPatient;

    public void setId(Long id) {
        this.id = id;
    }

    public void setNomMedcin(String nomMedcin) {
        this.nomMedcin = nomMedcin;
    }

    public void setIdPatient(Patient idPatient) {
        this.idPatient = idPatient;
    }

    @Override
    public String toString() {
        return "DossierPatient{" +
                "id=" + id +
                ", nomMedcin='" + nomMedcin + '\'' +
                ", idPatient=" + idPatient +
                '}';
    }

    public DossierPatient(Long id, String nomMedcin, Patient idPatient) {
        this.id = id;
        this.nomMedcin = nomMedcin;
        this.idPatient = idPatient;
    }

    public Long getId() {
        return id;
    }

    public String getNomMedcin() {
        return nomMedcin;
    }

    public Patient getIdPatient() {
        return idPatient;
    }
}
