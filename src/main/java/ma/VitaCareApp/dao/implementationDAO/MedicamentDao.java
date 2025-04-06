package ma.VitaCareApp.dao.implementationDAO;

import ma.VitaCareApp.dao.api.specificAPI.IMedicamentDao;
import ma.VitaCareApp.dao.exceptions.DaoException;
import ma.VitaCareApp.models.MedicalHistory;
import ma.VitaCareApp.models.Medicament;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MedicamentDao implements IMedicamentDao {

    // Path to the text file
    private static final String FILE_PATH = "myFileBase/medicaments.txt";

    private final MedicalHistoryDao medicalHistoryDAO; // Dependency to fetch MedicalHistory objects

    public MedicamentDao(MedicalHistoryDao medicalHistoryDAO) {
        this.medicalHistoryDAO = medicalHistoryDAO;
    }

    /**
     * Method to add a certain field to a file
     * @param medicament : Name of the field to add
     * @throws DaoException : If there's a problem with file
     */
    @Override
    public Medicament save(Medicament medicament) throws DaoException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(medicamentToString(medicament)); // Use the helper method
            writer.newLine();
        } catch (IOException e) {
            throw new DaoException("Failed to save Medicament: " + e.getMessage());
        }
        return medicament;
    }

    /**
     * Method to find a certain entity by its ID
     * @param id : the identifier of a certain entity
     * @return : return the found entity
     * @throws DaoException : If there's a problem with file
     */
    @Override
    public Medicament findById(Long id) throws DaoException {
        return findAll().stream()
                .filter(m -> m.getIdMedicine().equals(id))
                .findFirst()
                .orElseThrow(() -> new DaoException("Medicament not found with ID: " + id));
    }

    /**
     * Method to retrieve a list of a certain entity
     * @return : List of an entity
     * @throws DaoException : If there's a problem with file
     */
    @Override
    public List<Medicament> findAll() throws DaoException {
        List<Medicament> medicaments = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Medicament medicament = parseMedicament(line);
                medicaments.add(medicament);
            }
        } catch (IOException e) {
            throw new DaoException("Failed to read Medicaments: " + e.getMessage());
        }
        return medicaments;
    }

    /**
     * Method to update the fields of a given entity
     * @param medicament : the wanted field to update
     * @throws DaoException : If there's a problem with file
     */
    @Override
    public void update(Medicament medicament) throws DaoException {
        List<Medicament> medicaments = findAll();
        for (int i = 0; i < medicaments.size(); i++) {
            if (medicaments.get(i).getIdMedicine().equals(medicament.getIdMedicine())) {
                medicaments.set(i, medicament); // Replace the old Medicament with the updated one
                break;
            }
        }
        saveAll(medicaments); // Save the updated list back to the file
    }

    /**
     * Method to delete an entity by its ID
     * @param id : the identifier of the entity to delete
     * @throws DaoException : If there's a problem with file
     */
    @Override
    public void deleteById(Long id) throws DaoException {
        List<Medicament> medicaments = findAll();
        medicaments.removeIf(m -> m.getIdMedicine().equals(id));
        saveAll(medicaments); // Save the updated list back to the file
    }

    /**
     * Method to delete an entity
     * @param medicament : the entity to delete
     * @throws DaoException : If there's a problem with file
     */
    @Override
    public void delete(Medicament medicament) throws DaoException {
        deleteById(medicament.getIdMedicine());
    }

    // Helper method to save all Medicaments to the file
    private void saveAll(List<Medicament> medicaments) throws DaoException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Medicament m : medicaments) {
                writer.write(medicamentToString(m)); // Use the helper method
                writer.newLine();
            }
        } catch (IOException e) {
            throw new DaoException("Failed to save Medicaments: " + e.getMessage());
        }
    }

    // Helper method to convert a Medicament object to a comma-separated string
    private String medicamentToString(Medicament medicament) {
        return String.join(",",
                medicament.getIdMedicine().toString(),
                medicament.getPrice().toString(),
                medicament.getName(),
                medicament.getDescription(),
                String.join(";", medicament.getPrecautionIds().stream().map(Object::toString).toArray(String[]::new)) // Precaution IDs as semicolon-separated string
        );
    }

    // Helper method to parse a comma-separated string into a Medicament object
    private Medicament parseMedicament(String line) throws DaoException {
        String[] parts = line.split(",");
        Long idMedicine = Long.parseLong(parts[0]);
        Double price = Double.parseDouble(parts[1]);
        String name = parts[2];
        String description = parts[3];
        List<Long> precautionIds = Stream.of(parts[4].split(";")) // Precaution IDs
                .map(Long::parseLong)
                .collect(Collectors.toList());

        // Fetch the MedicalHistory objects using their IDs
        List<MedicalHistory> precautions = medicalHistoryDAO.findAllById(precautionIds);

        return new Medicament(idMedicine, price, name, description, precautions);
    }

}
