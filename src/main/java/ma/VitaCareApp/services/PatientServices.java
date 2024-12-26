package ma.VitaCareApp.services;

import ma.VitaCareApp.dao.PatientDAO;
import ma.VitaCareApp.models.Patient;
import java.util.List;

public class PatientServices {

    private final PatientDAO patientDAO = new PatientDAO();

    public List<Patient> getAllPatients() {
        return patientDAO.getAllPatients();
    }

    public void registerPatient(Patient patient) {
        patientDAO.addPatient(patient);
    }

    public void modifyPatient(Patient patient) {
        patientDAO.updatePatient(patient);
    }

    public void removePatient(String id) {
        patientDAO.deletePatient(id);
    }

}
