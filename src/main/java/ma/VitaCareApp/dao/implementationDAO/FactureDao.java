package ma.VitaCareApp.dao.implementationDAO;

import ma.VitaCareApp.dao.api.specificAPI.IFactureDao;
import ma.VitaCareApp.dao.exceptions.DaoException;
import ma.VitaCareApp.models.Consultation;
import ma.VitaCareApp.models.Facture;
import ma.VitaCareApp.models.enums.TypePayement;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class FactureDao implements IFactureDao {

    // Path to the text file
    private static final String FILE_PATH = "myFileBase/facture.txt";

    private final ConsultationDao consultationDAO; // Dependency to fetch Consultation objects
    private final AtomicLong idGenerator = new AtomicLong(1); // Auto-increment ID generator

    public FactureDao(ConsultationDao consultationDAO) {
        this.consultationDAO = consultationDAO;
        initializeIdGenerator(); // Initialize the ID generator
    }

    /**
     * Saves a Facture entity to the file.
     *
     * @param facture the entity to save
     * @return the saved entity
     * @throws DaoException if there's a problem with file
     */
    @Override
    public synchronized Facture save(Facture facture) throws DaoException {
        if (facture.getIdFacture() == null) {
            facture.setIdFacture(idGenerator.getAndIncrement()); // Assign the next ID
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(facture.toString()); // Use the toString method for serialization
            writer.newLine(); // Ensure each Facture is written on a new line
        } catch (IOException e) {
            throw new DaoException("Failed to save Facture: " + e.getMessage());
        }
        return facture;
    }

    /**
     * Finds a Facture entity by its ID.
     *
     * @param id the identifier of the entity to find
     * @return the found entity
     * @throws DaoException if the entity is not found or there's a problem with file
     */
    @Override
    public Facture findById(Long id) throws DaoException {
        return findAll().stream()
                .filter(f -> f.getIdFacture().equals(id))
                .findFirst()
                .orElseThrow(() -> new DaoException("Facture not found with ID: " + id));
    }

    /**
     * Retrieves all Facture entities from the file.
     *
     * @return a list of all entities
     * @throws DaoException if there's a problem with file
     */
    @Override
    public List<Facture> findAll() throws DaoException {
        List<Facture> factures = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    Facture facture = parseFacture(line);
                    factures.add(facture);
                } catch (Exception e) {
                    // Skip malformed lines and log the error
                    System.err.println("Skipping malformed line: " + line);
                }
            }
        } catch (IOException e) {
            throw new DaoException("Failed to read Factures: " + e.getMessage());
        }
        return factures;
    }

    /**
     * Updates an existing Facture entity in the file.
     *
     * @param facture the entity with updated values
     * @throws DaoException if the entity is not found or there's a problem with file
     */
    @Override
    public synchronized void update(Facture facture) throws DaoException {
        List<Facture> factures = findAll();
        for (int i = 0; i < factures.size(); i++) {
            if (factures.get(i).getIdFacture().equals(facture.getIdFacture())) {
                factures.set(i, facture); // Replace the old Facture with the updated one
                break;
            }
        }
        saveAll(factures); // Save the updated list back to the file
    }

    /**
     * Deletes a Facture entity by its ID.
     *
     * @param id the identifier of the entity to delete
     * @throws DaoException if the entity is not found or there's a problem with file
     */
    @Override
    public synchronized void deleteById(Long id) throws DaoException {
        List<Facture> factures = findAll();
        factures.removeIf(f -> f.getIdFacture().equals(id));
        saveAll(factures); // Save the updated list back to the file
    }

    /**
     * Deletes a Facture entity.
     *
     * @param facture the entity to delete
     * @throws DaoException if the entity is not found or there's a problem with file
     */
    @Override
    public void delete(Facture facture) throws DaoException {
        deleteById(facture.getIdFacture());
    }

    /**
     * Retrieves a list of Factures by their IDs.
     *
     * @param factureIds the list of identifiers
     * @return a list of Factures
     * @throws DaoException if there's a problem with file
     */
    @Override
    public List<Facture> findAllById(List<Long> factureIds) throws DaoException {
        List<Facture> allFactures = findAll(); // Fetch all Factures from the file
        return allFactures.stream()
                .filter(facture -> factureIds.contains(facture.getIdFacture())) // Filter by IDs
                .collect(Collectors.toList());
    }

    // Helper method to save all Factures to the file
    private synchronized void saveAll(List<Facture> factures) throws DaoException {
        // Create a backup of the existing file
        File file = new File(FILE_PATH);
        File backupFile = new File(FILE_PATH + ".bak");
        if (file.exists()) {
            file.renameTo(backupFile);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Facture f : factures) {
                writer.write(f.toString()); // Use the toString method for serialization
                writer.newLine();
            }
            // Delete the backup file after successful write
            if (backupFile.exists()) {
                backupFile.delete();
            }
        } catch (IOException e) {
            // Restore the backup file if an error occurs
            if (backupFile.exists()) {
                backupFile.renameTo(file);
            }
            throw new DaoException("Failed to save Factures: " + e.getMessage());
        }
    }

    // Helper method to parse a comma-separated string into a Facture object
    private Facture parseFacture(String line) throws DaoException {
        String[] parts = line.split(",");
        if (parts.length != 7) {
            throw new DaoException("Invalid line format: Expected 7 fields, found " + parts.length + " in line: " + line);
        }

        try {
            Long idFacture = Long.parseLong(parts[0]);
            Double montantTotale = Double.parseDouble(parts[1]);
            Double montantRestant = Double.parseDouble(parts[2]);
            Double montantPaye = Double.parseDouble(parts[3]);
            Long consultationId = Long.parseLong(parts[4]); // Extract Consultation ID
            TypePayement typePayement = TypePayement.valueOf(parts[5]); // Parse the enum
            LocalDate dateFacturation = LocalDate.parse(parts[6]);

            // Fetch the Consultation object using its ID
            Consultation consultation = consultationDAO.findById(consultationId);

            // Create and return the Facture object
            return new Facture(idFacture, montantTotale, montantPaye, consultation, typePayement, dateFacturation);
        } catch (Exception e) {
            throw new DaoException("Failed to parse Facture: " + e.getMessage() + " in line: " + line);
        }
    }

    // Initialize the ID generator based on the highest ID in the file
    private void initializeIdGenerator() {
        try {
            List<Facture> factures = findAll();
            if (!factures.isEmpty()) {
                long maxId = factures.stream()
                        .mapToLong(Facture::getIdFacture)
                        .max()
                        .orElse(0);
                idGenerator.set(maxId + 1); // Set the next ID
            }
        } catch (DaoException e) {
            // If there's an error, start from ID 1
            idGenerator.set(1);
        }
    }
}