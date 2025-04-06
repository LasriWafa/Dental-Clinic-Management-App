package ma.VitaCareApp.dao.implementationDAO;

import ma.VitaCareApp.dao.api.specificAPI.IConsultationDao;
import ma.VitaCareApp.dao.exceptions.DaoException;
import ma.VitaCareApp.models.Consultation;
import ma.VitaCareApp.models.InterventionMedcin;
import ma.VitaCareApp.models.Ordonnance;
import ma.VitaCareApp.models.enums.TypeConsultation;

import java.io.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class ConsultationDao implements IConsultationDao {

    private static final String FILE_PATH = "myFileBase/consultations.txt";

    private final OrdonnanceDao ordonnanceDao; // Dependency to fetch Ordonnance objects
    private final InterventionMedcinDao interventionMedcinDao; // Dependency to fetch InterventionMedcin objects

    public ConsultationDao(OrdonnanceDao ordonnanceDao, InterventionMedcinDao interventionMedcinDao) {
        this.ordonnanceDao = ordonnanceDao;
        this.interventionMedcinDao = interventionMedcinDao;
    }

    @Override
    public Consultation save(Consultation consultation) throws DaoException {
        try {
            // Generate the next ID
            Long nextId = generateNextId();
            consultation.setIdConsultation(nextId); // Set the generated ID

            // Append the consultation to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
                writer.write(consultation.toString());
                writer.newLine();
            }
            return consultation;
        } catch (IOException e) {
            throw new DaoException("Failed to save Consultation: " + e.getMessage());
        }
    }

    // Helper method to generate the next ID
    private Long generateNextId() throws DaoException {
        List<Consultation> consultations = findAll();
        if (consultations.isEmpty()) {
            return 1L; // Start with ID 1 if the file is empty
        }
        // Find the maximum ID and increment it by 1
        return consultations.stream()
                .mapToLong(Consultation::getIdConsultation)
                .max()
                .orElse(0L) + 1;
    }

    @Override
    public Consultation findById(Long id) throws DaoException {
        return findAll().stream()
                .filter(consultation -> consultation.getIdConsultation().equals(id))
                .findFirst()
                .orElseThrow(() -> new DaoException("Consultation not found with ID: " + id));
    }

    @Override
    public List<Consultation> findAll() throws DaoException {
        List<Consultation> consultations = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Consultation consultation = parseConsultation(line);
                consultations.add(consultation);
            }
        } catch (IOException e) {
            throw new DaoException("Failed to read Consultations: " + e.getMessage());
        }
        return consultations;
    }

    @Override
    public void update(Consultation updatedConsultation) throws DaoException {
        List<Consultation> consultations = findAll();
        for (int i = 0; i < consultations.size(); i++) {
            if (consultations.get(i).getIdConsultation().equals(updatedConsultation.getIdConsultation())) {
                consultations.set(i, updatedConsultation); // Replace the old Consultation with the updated one
                break;
            }
        }
        saveAll(consultations); // Save the updated list back to the file
    }

    @Override
    public void deleteById(Long id) throws DaoException {
        List<Consultation> consultations = findAll();
        consultations.removeIf(consultation -> consultation.getIdConsultation().equals(id));
        saveAll(consultations); // Save the updated list back to the file
    }

    @Override
    public void delete(Consultation consultation) throws DaoException {
        deleteById(consultation.getIdConsultation());
    }

    // Helper method to save all Consultations to the file
    private void saveAll(List<Consultation> consultations) throws DaoException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Consultation consultation : consultations) {
                writer.write(consultation.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new DaoException("Failed to save Consultations: " + e.getMessage());
        }
    }

    // Helper method to parse a comma-separated string into a Consultation object
    private Consultation parseConsultation(String line) throws DaoException {
        String[] parts = line.split(",");
        Long idConsultation = Long.parseLong(parts[0]);
        TypeConsultation typeConsultation = TypeConsultation.valueOf(parts[1]);
        Long ordonnanceId = Long.parseLong(parts[2]);
        LocalDate dateConsultation = LocalDate.parse(parts[3]);
        List<Long> interventionIds = Stream.of(parts[4].split(";"))
                .map(Long::parseLong)
                .toList();

        // Fetch Ordonnance and Interventions by their IDs
        Ordonnance ordonnance = ordonnanceDao.findById(ordonnanceId);
        List<InterventionMedcin> interventions = interventionIds.stream()
                .map(id -> {
                    try {
                        return interventionMedcinDao.findById(id);
                    } catch (DaoException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        return new Consultation(idConsultation, typeConsultation, ordonnance, dateConsultation, interventions);
    }


}
