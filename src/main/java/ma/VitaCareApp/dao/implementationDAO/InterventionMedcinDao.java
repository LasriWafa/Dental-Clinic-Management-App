package ma.VitaCareApp.dao.implementationDAO;

import ma.VitaCareApp.dao.api.specificAPI.IInterventionMedcinDao;
import ma.VitaCareApp.dao.exceptions.DaoException;
import ma.VitaCareApp.models.Acte;
import ma.VitaCareApp.models.InterventionMedcin;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InterventionMedcinDao implements IInterventionMedcinDao {

    // Path to the text file
    private static final String FILE_PATH = "myFileBase/interventionMedcins.txt";
    private final ActeDao acteDAO; // Dependency to fetch Acte objects

    public InterventionMedcinDao(ActeDao acteDAO) {
        this.acteDAO = acteDAO;
    }

    @Override
    public InterventionMedcin save(InterventionMedcin interventionMedcin) throws DaoException {
        try {
            // Auto-generate ID if it is null
            if (interventionMedcin.getIdIntervention() == null) {
                interventionMedcin.setIdIntervention(generateNextId());
            }

            // Write the interventionMedcin to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
                writer.write(interventionMedcinToString(interventionMedcin)); // Use the helper method
                writer.newLine();
            }

            return interventionMedcin;
        } catch (IOException e) {
            throw new DaoException("Failed to save InterventionMedcin: " + e.getMessage());
        }
    }

    // Helper method to generate the next ID
    private Long generateNextId() throws DaoException {
        List<InterventionMedcin> interventionMedcins = findAll();
        if (interventionMedcins.isEmpty()) {
            return 1L; // Start from 1 if no records exist
        }
        // Find the maximum ID and increment it by 1
        return interventionMedcins.stream()
                .mapToLong(InterventionMedcin::getIdIntervention)
                .max()
                .orElse(0L) + 1;
    }

    @Override
    public InterventionMedcin findById(Long id) throws DaoException {
        return findAll().stream()
                .filter(im -> im.getIdIntervention().equals(id))
                .findFirst()
                .orElseThrow(() -> new DaoException("InterventionMedcin not found with ID: " + id));
    }

    @Override
    public List<InterventionMedcin> findAll() throws DaoException {
        List<InterventionMedcin> interventionMedcins = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                InterventionMedcin interventionMedcin = parseInterventionMedcin(line);
                interventionMedcins.add(interventionMedcin);
            }
        } catch (IOException e) {
            throw new DaoException("Failed to read InterventionMedcins: " + e.getMessage());
        }
        return interventionMedcins;
    }

    @Override
    public void update(InterventionMedcin interventionMedcin) throws DaoException {
        List<InterventionMedcin> interventionMedcins = findAll();
        for (int i = 0; i < interventionMedcins.size(); i++) {
            if (interventionMedcins.get(i).getIdIntervention().equals(interventionMedcin.getIdIntervention())) {
                interventionMedcins.set(i, interventionMedcin); // Replace the old InterventionMedcin with the updated one
                break;
            }
        }
        saveAll(interventionMedcins); // Save the updated list back to the file
    }

    @Override
    public void deleteById(Long id) throws DaoException {
        List<InterventionMedcin> interventionMedcins = findAll();
        interventionMedcins.removeIf(im -> im.getIdIntervention().equals(id));
        saveAll(interventionMedcins); // Save the updated list back to the file
    }

    @Override
    public void delete(InterventionMedcin interventionMedcin) throws DaoException {
        deleteById(interventionMedcin.getIdIntervention());
    }

    // Helper method to save all InterventionMedcins to the file
    private void saveAll(List<InterventionMedcin> interventionMedcins) throws DaoException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (InterventionMedcin im : interventionMedcins) {
                writer.write(interventionMedcinToString(im)); // Use the helper method
                writer.newLine();
            }
        } catch (IOException e) {
            throw new DaoException("Failed to save InterventionMedcins: " + e.getMessage());
        }
    }

    // Helper method to convert an InterventionMedcin object to a comma-separated string
    private String interventionMedcinToString(InterventionMedcin interventionMedcin) {
        return String.join(",",
                interventionMedcin.getIdIntervention().toString(),
                interventionMedcin.getNoteMedcin(),
                interventionMedcin.getActe().getIdActe().toString(),
                interventionMedcin.getDent().toString(),
                interventionMedcin.getPrixPatient().toString()
        );
    }

    // Helper method to parse a comma-separated string into an InterventionMedcin object
    private InterventionMedcin parseInterventionMedcin(String line) throws DaoException {
        String[] parts = line.split(",");
        Long idIntervention = Long.parseLong(parts[0]);
        String noteMedcin = parts[1];
        Long acteId = Long.parseLong(parts[2]); // Extract Acte ID
        Long dent = Long.parseLong(parts[3]);
        Double prixPatient = Double.parseDouble(parts[4]);

        // Fetch the Acte object using its ID
        Acte acte = acteDAO.findById(acteId);

        return new InterventionMedcin(idIntervention, noteMedcin, acte, dent, prixPatient);
    }

    public List<InterventionMedcin> findAllById(List<Long> interventionIds) throws DaoException {
        List<InterventionMedcin> allInterventions = findAll(); // Fetch all interventions from the file
        return allInterventions.stream()
                .filter(intervention -> interventionIds.contains(intervention.getIdIntervention())) // Filter by IDs
                .collect(Collectors.toList());
    }
}