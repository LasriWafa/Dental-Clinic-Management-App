package ma.VitaCareApp.dao.implementationDAO;

import ma.VitaCareApp.dao.api.specificAPI.IMedicalHistoryDao;
import ma.VitaCareApp.dao.exceptions.DaoException;
import ma.VitaCareApp.models.MedicalHistory;
import ma.VitaCareApp.models.Patient;
import ma.VitaCareApp.models.enums.CategoryMedicalHistory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MedicalHistoryDao implements IMedicalHistoryDao {

    // Path to the text file
    private static final String FILE_PATH = "myFileBase/medicalHistories.txt";

    private final PatientDAO patientDAO; // Dependency to fetch Patient objects

    public MedicalHistoryDao(PatientDAO patientDAO) {
        this.patientDAO = patientDAO;
    }

    /**
     * Method to add a certain field to a file
     * @param medicalHistory : Name of the field to add
     * @throws DaoException : If there's a problem with file
     */
    @Override
    public MedicalHistory save(MedicalHistory medicalHistory) throws DaoException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(medicalHistoryToString(medicalHistory)); // Use the helper method
            writer.newLine();
        } catch (IOException e) {
            throw new DaoException("Failed to save MedicalHistory: " + e.getMessage());
        }
        return medicalHistory;
    }

    /**
     * Method to find a certain entity by its ID
     * @param id : the identifier of a certain entity
     * @return : return the found entity
     * @throws DaoException : If there's a problem with file
     */
    @Override
    public MedicalHistory findById(Long id) throws DaoException {
        return findAll().stream()
                .filter(mh -> mh.getIdAntecedant().equals(id))
                .findFirst()
                .orElseThrow(() -> new DaoException("MedicalHistory not found with ID: " + id));
    }

    /**
     * Method to retrieve a list of a certain entity
     * @return : List of an entity
     * @throws DaoException : If there's a problem with file
     */
    @Override
    public List<MedicalHistory> findAll() throws DaoException {
        List<MedicalHistory> medicalHistories = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                MedicalHistory medicalHistory = parseMedicalHistory(line);
                medicalHistories.add(medicalHistory);
            }
        } catch (IOException e) {
            throw new DaoException("Failed to read MedicalHistories: " + e.getMessage());
        }
        return medicalHistories;
    }

    /**
     * Method to update the fields of a given entity
     * @param medicalHistory : the wanted field to update
     * @throws DaoException : If there's a problem with file
     */
    @Override
    public void update(MedicalHistory medicalHistory) throws DaoException {
        List<MedicalHistory> medicalHistories = findAll();
        for (int i = 0; i < medicalHistories.size(); i++) {
            if (medicalHistories.get(i).getIdAntecedant().equals(medicalHistory.getIdAntecedant())) {
                medicalHistories.set(i, medicalHistory); // Replace the old MedicalHistory with the updated one
                break;
            }
        }
        saveAll(medicalHistories); // Save the updated list back to the file
    }

    /**
     * Method to delete an entity by its ID
     * @param id : the identifier of the entity to delete
     * @throws DaoException : If there's a problem with file
     */
    @Override
    public void deleteById(Long id) throws DaoException {
        List<MedicalHistory> medicalHistories = findAll();
        medicalHistories.removeIf(mh -> mh.getIdAntecedant().equals(id));
        saveAll(medicalHistories); // Save the updated list back to the file
    }

    /**
     * Method to delete an entity
     * @param medicalHistory : the entity to delete
     * @throws DaoException : If there's a problem with file
     */
    @Override
    public void delete(MedicalHistory medicalHistory) throws DaoException {
        deleteById(medicalHistory.getIdAntecedant());
    }

    // Helper method to save all MedicalHistories to the file
    private void saveAll(List<MedicalHistory> medicalHistories) throws DaoException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (MedicalHistory mh : medicalHistories) {
                writer.write(medicalHistoryToString(mh)); // Use the helper method
                writer.newLine();
            }
        } catch (IOException e) {
            throw new DaoException("Failed to save MedicalHistories: " + e.getMessage());
        }
    }

    // Helper method to convert a MedicalHistory object to a comma-separated string
    private String medicalHistoryToString(MedicalHistory medicalHistory) {
        return String.join(",",
                medicalHistory.getIdAntecedant().toString(),
                String.join(";", medicalHistory.getPatientsWithMedicalHistory().stream().map(Object::toString).toArray(String[]::new)), // Patient IDs as semicolon-separated string
                medicalHistory.getLibelle(),
                medicalHistory.getCategory().name() // Store the enum name
        );
    }

    // Helper method to parse a comma-separated string into a MedicalHistory object
    private MedicalHistory parseMedicalHistory(String line) throws DaoException {
        String[] parts = line.split(",");
        Long idAntecedant = Long.parseLong(parts[0]);
        List<Long> patientIds = Stream.of(parts[1].split(";")) // Patient IDs
                .map(Long::parseLong)
                .collect(Collectors.toList());
        String libelle = parts[2];
        CategoryMedicalHistory category = CategoryMedicalHistory.valueOf(parts[3]); // Parse the enum

        // Fetch the Patient objects using their IDs
        List<Patient> patients = patientDAO.findAllById(patientIds);

        return new MedicalHistory(idAntecedant, patients, libelle, category);
    }

    /**
     * @param medicalHistoryIds : identifiers of medical Histories
     * @return : list of medical histories
     * @throws DaoException : error handling
     */
    @Override
    public List<MedicalHistory> findAllById(List<Long> medicalHistoryIds) throws DaoException {
        List<MedicalHistory> allMedicalHistories = findAll(); // Fetch all MedicalHistories from the file
        return allMedicalHistories.stream()
                .filter(mh -> medicalHistoryIds.contains(mh.getIdAntecedant())) // Filter by IDs
                .collect(Collectors.toList());
    }
}
