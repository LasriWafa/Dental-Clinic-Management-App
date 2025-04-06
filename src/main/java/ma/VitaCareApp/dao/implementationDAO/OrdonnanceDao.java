package ma.VitaCareApp.dao.implementationDAO;

import ma.VitaCareApp.dao.api.specificAPI.IOrdonnanceDao;
import ma.VitaCareApp.dao.exceptions.DaoException;
import ma.VitaCareApp.models.Ordonnance;
import ma.VitaCareApp.models.PrescriptionMedicament;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OrdonnanceDao implements IOrdonnanceDao {

    private static final String FILE_PATH = "myFileBase/ordonnances.txt"; // Path to the text file
    private final PrescriptionMedicamentDao prescriptionMedicamentDAO; // Dependency to fetch PrescriptionMedicament objects

    public OrdonnanceDao(PrescriptionMedicamentDao prescriptionMedicamentDAO) {
        this.prescriptionMedicamentDAO = prescriptionMedicamentDAO;
    }

    /**
     * Method to add a certain field to a file
     * @param ordonnance : Name of the field to add
     * @throws DaoException : If there's a problem with file
     */
    @Override
    public Ordonnance save(Ordonnance ordonnance) throws DaoException {
        try {
            // Generate the next ID if the ordonnance ID is null
            if (ordonnance.getIdOrdonnance() == null) {
                Long nextId = findAll().stream()
                        .mapToLong(Ordonnance::getIdOrdonnance)
                        .max()
                        .orElse(0L) + 1; // Start from 1 if no ordonnances exist
                ordonnance.setIdOrdonnance(nextId);
            }

            // Save the ordonnance to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
                writer.write(ordonnanceToString(ordonnance));
                writer.newLine();
            }
            return ordonnance;
        } catch (IOException e) {
            throw new DaoException("Failed to save Ordonnance: " + e.getMessage());
        }
    }

    /**
     * Method to find a certain entity by its ID
     * @param id : the identifier of a certain entity
     * @return : return the found entity
     * @throws DaoException : If there's a problem with file
     */
    @Override
    public Ordonnance findById(Long id) throws DaoException {
        return findAll().stream()
                .filter(o -> o.getIdOrdonnance().equals(id))
                .findFirst()
                .orElseThrow(() -> new DaoException("Ordonnance not found with ID: " + id));
    }

    /**
     * Method to retrieve a list of a certain entity
     * @return : List of an entity
     * @throws DaoException : If there's a problem with file
     */
    @Override
    public List<Ordonnance> findAll() throws DaoException {
        List<Ordonnance> ordonnances = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Ordonnance ordonnance = parseOrdonnance(line);
                ordonnances.add(ordonnance);
            }
        } catch (IOException e) {
            throw new DaoException("Failed to read Ordonnances: " + e.getMessage());
        }
        return ordonnances;
    }

    /**
     * Method to update the fields of a given entity
     * @param ordonnance : the wanted field to update
     * @throws DaoException : If there's a problem with file
     */
    @Override
    public void update(Ordonnance ordonnance) throws DaoException {
        List<Ordonnance> ordonnances = findAll();
        for (int i = 0; i < ordonnances.size(); i++) {
            if (ordonnances.get(i).getIdOrdonnance().equals(ordonnance.getIdOrdonnance())) {
                ordonnances.set(i, ordonnance); // Replace the old Ordonnance with the updated one
                break;
            }
        }
        saveAll(ordonnances); // Save the updated list back to the file
    }

    /**
     * Method to delete an entity by its ID
     * @param id : the identifier of the entity to delete
     * @throws DaoException : If there's a problem with file
     */
    @Override
    public void deleteById(Long id) throws DaoException {
        List<Ordonnance> ordonnances = findAll();
        ordonnances.removeIf(o -> o.getIdOrdonnance().equals(id));
        saveAll(ordonnances); // Save the updated list back to the file
    }

    /**
     * Method to delete an entity
     * @param ordonnance : the entity to delete
     * @throws DaoException : If there's a problem with file
     */
    @Override
    public void delete(Ordonnance ordonnance) throws DaoException {
        deleteById(ordonnance.getIdOrdonnance());

    }

    // Helper method to save all Ordonnances to the file
    private void saveAll(List<Ordonnance> ordonnances) throws DaoException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Ordonnance o : ordonnances) {
                writer.write(ordonnanceToString(o)); // Use the helper method
                writer.newLine();
            }
        } catch (IOException e) {
            throw new DaoException("Failed to save Ordonnances: " + e.getMessage());
        }
    }

    // Helper method to convert an Ordonnance object to a comma-separated string
    private String ordonnanceToString(Ordonnance ordonnance) {
        return String.join(",",
                ordonnance.getIdOrdonnance().toString(),
                ordonnance.getDate().toString(),
                String.join(";", ordonnance.getPrescriptionMedicamentIds().stream().map(Object::toString).toArray(String[]::new)) // PrescriptionMedicament IDs as semicolon-separated string
        );
    }

    // Helper method to parse a comma-separated string into an Ordonnance object
    private Ordonnance parseOrdonnance(String line) throws DaoException {
        String[] parts = line.split(",");
        Long idOrdonnance = Long.parseLong(parts[0]);
        LocalDate date = LocalDate.parse(parts[1]);
        List<Long> prescriptionMedicamentIds = Stream.of(parts[2].split(";")) // PrescriptionMedicament IDs
                .map(Long::parseLong)
                .collect(Collectors.toList());

        // Fetch the PrescriptionMedicament objects using their IDs
        List<PrescriptionMedicament> prescriptionMedicaments = prescriptionMedicamentDAO.findAllById(prescriptionMedicamentIds);

        return new Ordonnance(idOrdonnance, date, prescriptionMedicaments);
    }

}
