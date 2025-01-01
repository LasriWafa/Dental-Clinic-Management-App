package ma.VitaCareApp.entities;

import ma.VitaCareApp.entities.enums.Sex;

public class Patient {

    private long id;
    private String email;
    private Sex sex;
    private String firstName;
    private String lastName;
    private String cin;
    private DossierPatient dossierPatient;

    public Patient(long id, String email, Sex sex, String firstName, String lastName, String cin, DossierPatient dossierPatient) {
        this.id = id;
        this.email = email;
        this.sex = sex;
        this.firstName = firstName;
        this.lastName = lastName;
        this.cin = cin;
        this.dossierPatient = dossierPatient;
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Sex getSex() {
        return sex;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCin() {
        return cin;
    }

    public DossierPatient getDossierPatient() {
        return dossierPatient;
    }
}
