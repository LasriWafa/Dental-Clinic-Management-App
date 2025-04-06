package ma.VitaCareApp.dao.implementationDAO;

import ma.VitaCareApp.dao.api.specificAPI.IPatientDAO;
import ma.VitaCareApp.dao.exceptions.DaoException;
import ma.VitaCareApp.models.Patient;
import ma.VitaCareApp.models.enums.BloodType;
import ma.VitaCareApp.models.enums.Mutuelle;
import ma.VitaCareApp.models.enums.Sex;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PatientDAO implements IPatientDAO {

    // Path to the text file
    private static final String FILE_PATH = "myFileBase/patients.txt";

    /**
     * @param cin : unique identifier
     * @return : the patient with the corresponding cin
     * @throws DaoException : file error
     */
    @Override
    public Patient findByCIN(String cin) throws DaoException {
        return findAll().stream()
                .filter(patient -> patient.getCin().equals(cin))
                .findFirst()
                .orElseThrow(() -> new DaoException("Patient not found with CIN: " + cin));
    }

    /**
     * Method to add a certain field to a file
     * @param newPatient : Name of the field to add
     * @throws DaoException : If there's a problem with file
     */
    @Override
    public Patient save(Patient newPatient) throws DaoException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            // Generate a unique ID for the new patient
            Long newId = generateNewId();
            newPatient.setId(newId);

            // Convert the patient to a comma-separated line and write to the file
            writer.write(newPatient.toString());
            writer.newLine();
        } catch (IOException e) {
            throw new DaoException("Failed to save patient: " + e.getMessage());
        }
        return newPatient;
    }

    // Helper method to generate a unique ID
    private Long generateNewId() throws DaoException {
        List<Patient> patients = findAll();
        if (patients.isEmpty()) {
            return 1L; // Start with ID 1 if no patients exist
        }
        // Find the maximum ID and increment it
        return patients.stream()
                .mapToLong(Patient::getId)
                .max()
                .orElse(0L) + 1;
    }

    /**
     * Method to find a certain entity by its ID
     * @param id : the identifier of a certain entity
     * @return : return the found entity
     * @throws DaoException : If there's a problem with file
     */
    @Override
    public Patient findById(Long id) throws DaoException {
        return findAll().stream()
                .filter(patient -> patient.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new DaoException("Patient not found with ID: " + id));
    }

    /**
     * Method to retrieve a list of a certain entity
     * @return : List of an entity
     * @throws DaoException : If there's a problem with file
     */
    @Override
    public List<Patient> findAll() throws DaoException {
        List<Patient> patients = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Parse the comma-separated line into a Patient object
                Patient patient = parsePatient(line);
                patients.add(patient);
            }
        } catch (IOException e) {
            throw new DaoException("Failed to read patients: " + e.getMessage());
        }
        return patients;
    }

    /**
     * Method to update the fields of a given entity
     * @param updatedPatient : the wanted field to update
     * @throws DaoException : If there's a problem with file
     */
    @Override
    public void update(Patient updatedPatient) throws DaoException {
        List<Patient> patients = findAll();
        for (int i = 0; i < patients.size(); i++) {
            if (patients.get(i).getId().equals(updatedPatient.getId())) {
                patients.set(i, updatedPatient); // Replace the old patient with the updated one
                break;
            }
        }
        saveAll(patients); // Save the updated list back to the file
    }

    /**
     * Method to delete an entity by its ID
     * @param id : the identifier of the entity to delete
     * @throws DaoException : If there's a problem with file
     */
    @Override
    public void deleteById(Long id) throws DaoException {
        List<Patient> patients = findAll();
        patients.removeIf(patient -> patient.getId().equals(id));
        saveAll(patients); // Save the updated list back to the file
    }

    /**
     * Method to delete an entity
     * @param patient : the entity to delete
     * @throws DaoException : If there's a problem with file
     */
    @Override
    public void delete(Patient patient) throws DaoException {
        deleteById(patient.getId());
    }

    // Helper method to save all patients to the file
    private void saveAll(List<Patient> patients) throws DaoException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Patient patient : patients) {
                // Convert each patient to a comma-separated line and write to the file
                writer.write(patientToLine(patient));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new DaoException("Failed to save patients: " + e.getMessage());
        }
    }

    // Helper method to convert a Patient object to a comma-separated line
    private String patientToLine(Patient patient) {
        return String.join(",",
                patient.getId().toString(),
                patient.getPhone(),
                patient.getEmail(),
                patient.getSex().name(),
                patient.getAddress(),
                patient.getBirthDate().toString(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getCin(),
                patient.getBloodType().name(),
                patient.getMutuelle().name(),
                patient.getProfession()
        );
    }

    // Helper method to parse a comma-separated line into a Patient object
    private Patient parsePatient(String line) {
        String[] parts = line.split(",");
        Long id = Long.parseLong(parts[0]);
        String phone = parts[1];
        String email = parts[2];
        Sex sex = Sex.valueOf(parts[3]);
        String address = parts[4];
        LocalDate birthDate = LocalDate.parse(parts[5]);
        String firstName = parts[6];
        String lastName = parts[7];
        String cin = parts[8];
        BloodType bloodType = BloodType.valueOf(parts[9]);
        Mutuelle mutuelle = Mutuelle.valueOf(parts[10]);
        String profession = parts[11];

        return new Patient(id, phone, email, sex, address, birthDate, firstName, lastName, cin, bloodType, mutuelle, profession);
    }

    @Override
    public List<Patient> findAllById(List<Long> patientIds) throws DaoException {
        List<Patient> allPatients = findAll(); // Fetch all patients from the file
        return allPatients.stream()
                .filter(patient -> patientIds.contains(patient.getId())) // Filter by IDs
                .collect(Collectors.toList());
    }

}
