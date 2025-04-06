package ma.VitaCareApp.dao.implementationDAO;

import ma.VitaCareApp.dao.api.specificAPI.IFinancialSituationDao;
import ma.VitaCareApp.dao.exceptions.DaoException;
import ma.VitaCareApp.models.DossierMedical;
import ma.VitaCareApp.models.Facture;
import ma.VitaCareApp.models.FinancialSituation;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FinancialSituationDao implements IFinancialSituationDao {

    // Path to the text file
    private static final String FILE_PATH = "myFileBase/financialSituations.txt";

    private final DossierMedicalDao dossierMedicalDAO;
    private final FactureDao factureDAO;
    private final AtomicLong idGenerator = new AtomicLong(1); // Auto-increment ID generator

    public FinancialSituationDao(DossierMedicalDao dossierMedicalDAO, FactureDao factureDAO) {
        this.dossierMedicalDAO = dossierMedicalDAO;
        this.factureDAO = factureDAO;
        initializeIdGenerator(); // Initialize the ID generator
    }

    @Override
    public synchronized FinancialSituation save(FinancialSituation financialSituation) throws DaoException {
        // Validate Facture IDs
        List<Long> factureIds = financialSituation.getFactures().stream()
                .map(Facture::getIdFacture)
                .collect(Collectors.toList());
        List<Facture> factures = factureDAO.findAllById(factureIds);
        if (factures.size() != factureIds.size()) {
            throw new DaoException("One or more Facture IDs do not exist.");
        }

        // Assign an ID if necessary
        if (financialSituation.getIdFinancialSituation() == null) {
            financialSituation.setIdFinancialSituation(idGenerator.getAndIncrement());
        }

        // Save to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(financialSituationToString(financialSituation));
            writer.newLine();
        } catch (IOException e) {
            throw new DaoException("Failed to save FinancialSituation: " + e.getMessage());
        }
        return financialSituation;
    }


    @Override
    public FinancialSituation findById(Long id) throws DaoException {
        return findAll().stream()
                .filter(fs -> fs.getIdFinancialSituation().equals(id))
                .findFirst()
                .orElseThrow(() -> new DaoException("FinancialSituation not found with ID: " + id));
    }

    @Override
    public List<FinancialSituation> findAll() throws DaoException {
        List<FinancialSituation> financialSituations = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    FinancialSituation financialSituation = parseFinancialSituation(line);
                    financialSituations.add(financialSituation);
                } catch (Exception e) {
                    System.err.println("Skipping malformed line: " + line);
                }
            }
        } catch (IOException e) {
            throw new DaoException("Failed to read FinancialSituations: " + e.getMessage());
        }
        return financialSituations;
    }

    @Override
    public synchronized void update(FinancialSituation financialSituation) throws DaoException {
        // Validate Facture IDs
        List<Long> factureIds = financialSituation.getFactures().stream()
                .map(Facture::getIdFacture)
                .collect(Collectors.toList());
        List<Facture> factures = factureDAO.findAllById(factureIds);
        if (factures.size() != factureIds.size()) {
            throw new DaoException("One or more Facture IDs do not exist.");
        }

        // Update the FinancialSituation
        List<FinancialSituation> financialSituations = findAll();
        for (int i = 0; i < financialSituations.size(); i++) {
            if (financialSituations.get(i).getIdFinancialSituation().equals(financialSituation.getIdFinancialSituation())) {
                financialSituations.set(i, financialSituation);
                break;
            }
        }
        saveAll(financialSituations);
    }

    @Override
    public synchronized void deleteById(Long id) throws DaoException {
        List<FinancialSituation> financialSituations = findAll();
        financialSituations.removeIf(fs -> fs.getIdFinancialSituation().equals(id));
        saveAll(financialSituations); // Save the updated list back to the file
    }

    @Override
    public void delete(FinancialSituation financialSituation) throws DaoException {
        deleteById(financialSituation.getIdFinancialSituation());
    }

    // Helper method to save all FinancialSituations to the file
    private void saveAll(List<FinancialSituation> financialSituations) throws DaoException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (FinancialSituation fs : financialSituations) {
                writer.write(financialSituationToString(fs));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new DaoException("Failed to save FinancialSituations: " + e.getMessage());
        }
    }

    // Helper method to convert a FinancialSituation to a comma-separated string
    private String financialSituationToString(FinancialSituation financialSituation) {
        return String.join(",",
                financialSituation.getIdFinancialSituation().toString(),
                financialSituation.getDossierMedical().getNumeroDossier(), // DossierMedical ID
                financialSituation.getFactures().stream()
                        .map(facture -> facture.getIdFacture().toString())
                        .collect(Collectors.joining(";")), // Facture IDs as a semicolon-separated string
                financialSituation.getDateCreation().toString(),
                financialSituation.getMontantGlobaleRestant().toString(),
                financialSituation.getMontantGlobalePaye().toString()
        );
    }

    // Helper method to parse a comma-separated string into a FinancialSituation object
    private FinancialSituation parseFinancialSituation(String line) throws DaoException {
        String[] parts = line.split(",");
        if (parts.length != 6) {
            throw new DaoException("Invalid line format: Expected 6 fields, found " + parts.length + " in line: " + line);
        }

        try {
            Long idFinancialSituation = Long.parseLong(parts[0]);
            Long dossierMedicalId = Long.parseLong(parts[1]); // DossierMedical ID
            List<Long> factureIds = Stream.of(parts[2].split(";")) // Facture IDs
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            LocalDate dateCreation = LocalDate.parse(parts[3]);
            Double montantGlobaleRestant = Double.parseDouble(parts[4]);
            Double montantGlobalePaye = Double.parseDouble(parts[5]);

            // Fetch DossierMedical and Factures from their respective DAOs
            DossierMedical dossierMedical = dossierMedicalDAO.findById(dossierMedicalId);
            List<Facture> factures = factureDAO.findAllById(factureIds);

            return new FinancialSituation(
                    idFinancialSituation,
                    dossierMedical,
                    factures,
                    dateCreation,
                    montantGlobaleRestant,
                    montantGlobalePaye
            );
        } catch (Exception e) {
            throw new DaoException("Failed to parse FinancialSituation: " + e.getMessage() + " in line: " + line);
        }
    }

    // Initialize the ID generator based on the highest ID in the file
    private void initializeIdGenerator() {
        try {
            List<FinancialSituation> financialSituations = findAll();
            if (!financialSituations.isEmpty()) {
                long maxId = financialSituations.stream()
                        .mapToLong(FinancialSituation::getIdFinancialSituation)
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