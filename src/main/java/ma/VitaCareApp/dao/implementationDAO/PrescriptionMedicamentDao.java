package ma.VitaCareApp.dao.implementationDAO;

import ma.VitaCareApp.dao.api.specificAPI.IPrescriptionMedicamentDao;
import ma.VitaCareApp.dao.exceptions.DaoException;
import ma.VitaCareApp.models.Medicament;
import ma.VitaCareApp.models.PrescriptionMedicament;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PrescriptionMedicamentDao implements IPrescriptionMedicamentDao {

    // Path to the text file
    private static final String FILE_PATH = "myFileBase/prescriptionMedicaments.txt";

    private final MedicamentDao medicamentDAO; // Dependency to fetch Medicament objects

    public PrescriptionMedicamentDao(MedicamentDao medicamentDAO) {
        this.medicamentDAO = medicamentDAO;
    }

    /**
     * Method to add a new PrescriptionMedicament to the file with auto-incremented ID.
     * @param prescriptionMedicament : The PrescriptionMedicament to add
     * @return : The saved PrescriptionMedicament with the generated ID
     * @throws DaoException : If there's a problem with the file
     */
    @Override
    public PrescriptionMedicament save(PrescriptionMedicament prescriptionMedicament) throws DaoException {
        // Generate the next ID
        Long nextId = generateNextId();
        prescriptionMedicament.setIdPrescription(nextId); // Set the generated ID

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(prescriptionMedicamentToString(prescriptionMedicament)); // Use the helper method
            writer.newLine();
        } catch (IOException e) {
            throw new DaoException("Failed to save PrescriptionMedicament: " + e.getMessage());
        }
        return prescriptionMedicament;
    }

    /**
     * Helper method to generate the next ID by finding the maximum ID in the file and incrementing it by 1.
     * @return : The next available ID
     * @throws DaoException : If there's a problem reading the file
     */
    private Long generateNextId() throws DaoException {
        List<PrescriptionMedicament> prescriptionMedicaments = findAll();
        if (prescriptionMedicaments.isEmpty()) {
            return 1L; // Start with ID 1 if the file is empty
        }
        // Find the maximum ID and increment it by 1
        return prescriptionMedicaments.stream()
                .mapToLong(PrescriptionMedicament::getIdPrescription)
                .max()
                .orElse(0L) + 1;
    }

    /**
     * Method to find a PrescriptionMedicament by its ID.
     * @param id : The identifier of the PrescriptionMedicament
     * @return : The found PrescriptionMedicament
     * @throws DaoException : If there's a problem with the file
     */
    @Override
    public PrescriptionMedicament findById(Long id) throws DaoException {
        return findAll().stream()
                .filter(pm -> pm.getIdPrescription().equals(id))
                .findFirst()
                .orElseThrow(() -> new DaoException("PrescriptionMedicament not found with ID: " + id));
    }

    /**
     * Method to retrieve all PrescriptionMedicaments from the file.
     * @return : List of all PrescriptionMedicaments
     * @throws DaoException : If there's a problem with the file
     */
    @Override
    public List<PrescriptionMedicament> findAll() throws DaoException {
        List<PrescriptionMedicament> prescriptionMedicaments = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                PrescriptionMedicament prescriptionMedicament = parsePrescriptionMedicament(line);
                prescriptionMedicaments.add(prescriptionMedicament);
            }
        } catch (IOException e) {
            throw new DaoException("Failed to read PrescriptionMedicaments: " + e.getMessage());
        }
        return prescriptionMedicaments;
    }

    /**
     * Method to update a PrescriptionMedicament in the file.
     * @param prescriptionMedicament : The PrescriptionMedicament to update
     * @throws DaoException : If there's a problem with the file
     */
    @Override
    public void update(PrescriptionMedicament prescriptionMedicament) throws DaoException {
        List<PrescriptionMedicament> prescriptionMedicaments = findAll();
        for (int i = 0; i < prescriptionMedicaments.size(); i++) {
            if (prescriptionMedicaments.get(i).getIdPrescription().equals(prescriptionMedicament.getIdPrescription())) {
                prescriptionMedicaments.set(i, prescriptionMedicament); // Replace the old PrescriptionMedicament with the updated one
                break;
            }
        }
        saveAll(prescriptionMedicaments); // Save the updated list back to the file
    }

    /**
     * Method to delete a PrescriptionMedicament by its ID.
     * @param id : The identifier of the PrescriptionMedicament to delete
     * @throws DaoException : If there's a problem with the file
     */
    @Override
    public void deleteById(Long id) throws DaoException {
        List<PrescriptionMedicament> prescriptionMedicaments = findAll();
        prescriptionMedicaments.removeIf(pm -> pm.getIdPrescription().equals(id));
        saveAll(prescriptionMedicaments); // Save the updated list back to the file
    }

    /**
     * Method to delete a PrescriptionMedicament.
     * @param prescriptionMedicament : The PrescriptionMedicament to delete
     * @throws DaoException : If there's a problem with the file
     */
    @Override
    public void delete(PrescriptionMedicament prescriptionMedicament) throws DaoException {
        deleteById(prescriptionMedicament.getIdPrescription());
    }

    /**
     * Helper method to save all PrescriptionMedicaments to the file.
     * @param prescriptionMedicaments : The list of PrescriptionMedicaments to save
     * @throws DaoException : If there's a problem with the file
     */
    private void saveAll(List<PrescriptionMedicament> prescriptionMedicaments) throws DaoException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (PrescriptionMedicament pm : prescriptionMedicaments) {
                writer.write(prescriptionMedicamentToString(pm)); // Use the helper method
                writer.newLine();
            }
        } catch (IOException e) {
            throw new DaoException("Failed to save PrescriptionMedicaments: " + e.getMessage());
        }
    }

    /**
     * Helper method to convert a PrescriptionMedicament object to a comma-separated string.
     * @param prescriptionMedicament : The PrescriptionMedicament to convert
     * @return : The comma-separated string representation of the PrescriptionMedicament
     */
    private String prescriptionMedicamentToString(PrescriptionMedicament prescriptionMedicament) {
        return String.join(",",
                prescriptionMedicament.getIdPrescription().toString(),
                String.valueOf(prescriptionMedicament.getUniteMinAPrendre()),
                String.valueOf(prescriptionMedicament.getUniteMaxAPrendre()),
                prescriptionMedicament.getContraintesAlimentaire(),
                prescriptionMedicament.getContraintesTemps(),
                prescriptionMedicament.getMedicamentId().toString() // Store the Medicament ID
        );
    }

    /**
     * Helper method to parse a comma-separated string into a PrescriptionMedicament object.
     * @param line : The comma-separated string to parse
     * @return : The parsed PrescriptionMedicament object
     * @throws DaoException : If there's a problem fetching the associated Medicament
     */
    private PrescriptionMedicament parsePrescriptionMedicament(String line) throws DaoException {
        String[] parts = line.split(",");
        Long idPrescription = Long.parseLong(parts[0]);
        int uniteMinAPrendre = Integer.parseInt(parts[1]);
        int uniteMaxAPrendre = Integer.parseInt(parts[2]);
        String contraintesAlimentaire = parts[3];
        String contraintesTemps = parts[4];
        Long medicamentId = Long.parseLong(parts[5]);

        // Fetch the Medicament object using its ID
        Medicament medicament = medicamentDAO.findById(medicamentId);

        return new PrescriptionMedicament(idPrescription, uniteMinAPrendre, uniteMaxAPrendre, contraintesAlimentaire, contraintesTemps, medicament);
    }

    @Override
    public List<PrescriptionMedicament> findAllById(List<Long> prescriptionMedicamentIds) throws DaoException {
        List<PrescriptionMedicament> allPrescriptionMedicaments = findAll(); // Fetch all PrescriptionMedicaments from the file
        return allPrescriptionMedicaments.stream()
                .filter(pm -> prescriptionMedicamentIds.contains(pm.getIdPrescription())) // Filter by IDs
                .collect(Collectors.toList());
    }
}