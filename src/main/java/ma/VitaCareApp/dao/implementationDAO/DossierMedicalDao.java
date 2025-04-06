package ma.VitaCareApp.dao.implementationDAO;

import lombok.SneakyThrows;
import ma.VitaCareApp.dao.api.specificAPI.IDossierMedicalDao;
import ma.VitaCareApp.dao.exceptions.DaoException;
import ma.VitaCareApp.models.*;
import ma.VitaCareApp.models.enums.StatusPayement;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DossierMedicalDao implements IDossierMedicalDao {

    private static final String FILE_PATH = "myFileBase/dossierMedical.txt";
    private final PatientDAO patientDao; // Dependency to fetch Patient objects
    private final RendezVousDao rdvDao; // Dependency to fetch RendezVous objects
    private final OrdonnanceDao ordonnanceDao; // Dependency to fetch Ordonnance objects
    private final ConsultationDao consultationDao; // Dependency to fetch Consultation objects

    public DossierMedicalDao(PatientDAO patientDao, RendezVousDao rdvDao, OrdonnanceDao ordonnanceDao, ConsultationDao consultationDao) {
        this.patientDao = patientDao;
        this.rdvDao = rdvDao;
        this.ordonnanceDao = ordonnanceDao;
        this.consultationDao = consultationDao;
    }

    /**
     * Method to add a certain field to a file
     * @param dossierMedical : Name of the field to add
     * @throws DaoException : If there's a problem with file
     */
    @Override
    public DossierMedical save(DossierMedical dossierMedical) throws DaoException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            // Auto-generate numeroDossier if not provided
            if (dossierMedical.getNumeroDossier() == null || dossierMedical.getNumeroDossier().isEmpty()) {
                dossierMedical.setNumeroDossier(generateNumeroDossier());
            }
            writer.write(dossierMedical.toString()); // Uses the overridden toString method
            writer.newLine();
        } catch (IOException e) {
            throw new DaoException("Failed to save DossierMedical: " + e.getMessage());
        }
        return dossierMedical;
    }

    private String generateNumeroDossier() {
        // Generate a unique numeroDossier (e.g., using timestamp or a counter)
        return "DOSS" + System.currentTimeMillis();
    }

    /**
     * Method to find a certain entity by its ID
     * @param numeroDossier : the identifier of a certain entity
     * @return : return the found entity
     * @throws DaoException : If there's a problem with file
     */
    @Override
    public DossierMedical findById(Long numeroDossier) throws DaoException {
        return findAll().stream()
                .filter(dossier -> Long.parseLong(dossier.getNumeroDossier()) == numeroDossier)
                .findFirst()
                .orElseThrow(() -> new DaoException("DossierMedical not found with numeroDossier: " + numeroDossier));
    }
    /**
     * Method to retrieve a list of a certain entity
     * @return : List of an entity
     * @throws DaoException : If there's a problem with file
     */
    @Override
    public List<DossierMedical> findAll() throws DaoException {
        List<DossierMedical> dossiers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                DossierMedical dossier = parseDossierMedical(line);
                dossiers.add(dossier);
            }
        } catch (IOException e) {
            throw new DaoException("Failed to read DossierMedical: " + e.getMessage());
        }
        return dossiers;
    }

    /**
     * Method to update the fields of a given entity
     * @param updatedDossier : the wanted field to update
     * @throws DaoException : If there's a problem with file
     */
    @Override
    public void update(DossierMedical updatedDossier) throws DaoException {
        List<DossierMedical> dossiers = findAll();
        for (int i = 0; i < dossiers.size(); i++) {
            if (dossiers.get(i).getNumeroDossier().equals(updatedDossier.getNumeroDossier())) {
                dossiers.set(i, updatedDossier); // Replace the old DossierMedical with the updated one
                break;
            }
        }
        saveAll(dossiers); // Save the updated list back to the file
    }

    /**
     * Method to delete an entity by its ID
     *
     * @param numeroDossier : the identifier of the entity to delete
     * @throws DaoException : If there's a problem with file
     */
    @Override
    public void deleteById(Long numeroDossier) throws DaoException {
        List<DossierMedical> dossiers = findAll();
        dossiers.removeIf(dossier -> Long.parseLong(dossier.getNumeroDossier()) == numeroDossier);
        saveAll(dossiers); // Save the updated list back to the file
    }

    /**
     * Method to delete an entity
     * @param dossierMedical : the entity to delete
     * @throws DaoException : If there's a problem with file
     */
    @Override
    public void delete(DossierMedical dossierMedical) throws DaoException {
        deleteById(Long.valueOf(dossierMedical.getNumeroDossier()));

    }

    // Helper method to save all DossierMedical to the file
    private void saveAll(List<DossierMedical> dossiers) throws DaoException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (DossierMedical dossier : dossiers) {
                writer.write(dossier.toString()); // Uses the overridden toString method
                writer.newLine();
            }
        } catch (IOException e) {
            throw new DaoException("Failed to save DossierMedical: " + e.getMessage());
        }
    }

    // Helper method to parse a comma-separated string into a DossierMedical object

    private DossierMedical parseDossierMedical(String line) throws DaoException {
        String[] parts = line.split(",");
        String numeroDossier = parts[0];
        LocalDate dateCreation = LocalDate.parse(parts[1]);
        StatusPayement statusPayement = StatusPayement.valueOf(parts[2]);
        Long patientId = Long.parseLong(parts[3]);
        List<Long> rdvIds = Arrays.stream(parts[4].split(";"))
                .map(Long::parseLong)
                .toList();
        List<Long> ordonnanceIds = Arrays.stream(parts[5].split(";"))
                .map(Long::parseLong)
                .toList();
        List<Long> consultationIds = Arrays.stream(parts[6].split(";"))
                .map(Long::parseLong)
                .toList();

        // Fetch the full objects using their IDs
        Patient patient = patientDao.findById(patientId);
        List<RendezVous> rdvs = rdvIds.stream()
                .map(id -> {
                    try {
                        return rdvDao.findById(id);
                    } catch (DaoException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
        List<Ordonnance> ordonnances = ordonnanceIds.stream()
                .map(id -> {
                    try {
                        return ordonnanceDao.findById(id);
                    } catch (DaoException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
        List<Consultation> consultations = consultationIds.stream()
                .map(id -> {
                    try {
                        return consultationDao.findById(id);
                    } catch (DaoException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        return new DossierMedical(numeroDossier, dateCreation, statusPayement, patient, rdvs, ordonnances, consultations);
    }
}
