package ma.VitaCareApp.repository.fileBase_Impl;

import ma.VitaCareApp.entities.Patient;
import ma.VitaCareApp.entities.DossierPatient;
import ma.VitaCareApp.entities.enums.Sex;
import ma.VitaCareApp.repository.api.IDossierPatientRepository;
import ma.VitaCareApp.repository.api.IPatientRepository;
import ma.VitaCareApp.repository.exceptions.DaoException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class PatientRepository implements IPatientRepository {

    // -----------------------------------------------------------------------------------------------------------

    private IDossierPatientRepository DossierPatientDao;
    private static final File patientTable = new File("myFileBase/patient.txt");

    // -----------------------------------------------------------------------------------------------------------

    @Override
    public Patient findByCIN(String cin) throws DaoException {
        return null;
    }

    @Override
    public List<Patient> findByCINLike(String motCle) throws DaoException {
        return List.of();
    }

    // ----------------------------------------- Find All Patients -------------------------------------------------

    @Override
    public List<Patient> findAll() throws DaoException {

        List<Patient> patients = new ArrayList<>();

        try {
            // List of lines
            List<String> lines = Files.readAllLines(patientTable.toPath());
            // Remove the header column
            lines.removeFirst();

            for(String line : lines) {
                patients.add(mapToPatient(line));
            }
            return patients;

        } catch (IOException e) {
            throw new DaoException(e.getMessage());
        }

    }

    // --------------------------------------- Convert From line to Patient -----------------------------------------

     private Patient mapToPatient(String fileLine) throws DaoException {

        // File Lines structure : ID-Nom-Prenom-Email-CIN-Sexe-IdDossier
         try {
             StringTokenizer st = new StringTokenizer(fileLine, "-");
             String value = st.nextToken();

             Long id = Long.parseLong(value);
                value = st.nextToken();

             String Nom = (value == "null" ? null : value);
                value = st.nextToken();

             String Prenom = (value == "null" ? null : value);
             value = st.nextToken();

             String email = (value == "null" ? null : value);
                value = st.nextToken();

             String cin = (value == "null" ? null : value);
                value = st.nextToken();

             Sex sexe = (value == "null" ? null : (value.equals("MALE") ? Sex.MALE : Sex.FEMALE));
                value = st.nextToken();

             DossierPatient dossier = (value == "null" ? null : DossierPatientDao.findById(Long.parseLong(value)));
                value = st.nextToken();

         } catch (NumberFormatException e) {
             throw new DaoException(e.getMessage());
             }
         return null;
     }

    // -----------------------------------------------------------------------------------------------------------

    private String mapToLine(Patient patient) {

        Long id = patient.getId();
        String nom = patient.getFirstName();
        String prenom = patient.getLastName();
        String email = patient.getEmail();
        String cin = patient.getCin();
        Sex sexe = patient.getSex();
        DossierPatient dossier = patient.getDossierPatient();

        String result = id + "-" + nom + "-" + prenom + "-" + email + "-" + cin + "-" +
                (sexe == null ? "null" : sexe.toString()) + "-" +
                dossier.getId();

        return result;
    }


    @Override
    public Patient findById(Long identity) throws DaoException {
        return null;
    }

    // Add patient to file with auto id incrementation
    @Override
    public Patient save(Patient newElement) throws DaoException {
        return null;
    }

    @Override
    public void update(Patient newValuesElement) throws DaoException {

    }

    @Override
    public void delete(Patient element) throws DaoException {

    }

    @Override
    public void deleteById(Long identity) throws DaoException {

    }

}
