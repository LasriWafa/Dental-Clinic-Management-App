package ma.VitaCareApp.dao.implementationDAO;

import ma.VitaCareApp.dao.api.IDao;
import ma.VitaCareApp.models.*;
import ma.VitaCareApp.models.enums.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PatientDao implements IDao<Patient, Long> {

    private static final String FILE_PATH = "resources/data/patients.txt";

    public PatientDao(Path filePath) {}
    public PatientDao() {}

    public List<String> readFile() {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {  System.out.println(e.getMessage()); }
        return lines;
    }

    public Patient stringToPatient(String line) {
        String[] fields = line.split("\\|");

        // Fields inherited from Person
        long id = Long.parseLong(fields[0].trim());
        String phone = fields[1].trim();
        String email = fields[2].trim();
        Sex sex = Sex.valueOf(fields[3].trim());
        String address = fields[4].trim();
        String birthDate = fields[5].trim();
        String firstName = fields[6].trim();
        String lastName = fields[7].trim();
        String cin = fields[8].trim();
        String profilePicture = fields[9].trim();

        // Fields specific to Patient
        BloodType bloodType = BloodType.valueOf(fields[10].trim());
        LocalDate birthDay = LocalDate.parse(fields[11].trim());
        Insurance insurance = Insurance.valueOf(fields[12].trim());
        String profession = fields[13].trim();

        // Assume medicalHistory and dossierMedical are empty for now
        List<MedicalHistory> medicalHistory = new ArrayList<>();
        DossierMedical dossierMedical = new DossierMedical();

        // Create and return the Patient object
        return new Patient(id, phone, email, sex, address, birthDate, firstName, lastName, cin, profilePicture,
                bloodType, birthDay, insurance, medicalHistory, profession, dossierMedical);
    }

    public String patientToString(Patient patient) {
        StringBuilder sb = new StringBuilder();

        // Fields inherited from Person
        sb.append(patient.getId()).append("|");
        sb.append(patient.getPhone()).append("|");
        sb.append(patient.getEmail()).append("|");
        sb.append(patient.getSex()).append("|");
        sb.append(patient.getAddress()).append("|");
        sb.append(patient.getBirthDate()).append("|");
        sb.append(patient.getFirstName()).append("|");
        sb.append(patient.getLastName()).append("|");
        sb.append(patient.getCin()).append("|");
        sb.append(patient.getProfilePicture()).append("|");

        // Fields specific to Patient
        sb.append(patient.getBloodType()).append("|");
        sb.append(patient.getBirthDay()).append("|");
        sb.append(patient.getInsurance()).append("|");
        sb.append(patient.getProfession()).append("|");

        // Simplified placeholders for complex fields
        // TODO: Serialize medicalHistory and dossierMedical if needed
        sb.append("[]").append("|"); // Placeholder for medicalHistory
        sb.append("{}"); // Placeholder for dossierMedical

        return sb.toString();
    }


    // ------------------------------------ Test Methods ------------------------------------------------------



    public static void main(String[] args) throws URISyntaxException {

        // Get the file
        ClassLoader classLoader = PatientDao.class.getClassLoader();
        Path filePath = Paths.get(classLoader.getResource("data/patients.txt").toURI());

        // Read the file
        try {
            List<String> lines = Files.readAllLines(filePath);
            lines.forEach(System.out::println);
        } catch (IOException e) { System.out.println(e.getMessage()); }

        //--------------------------------------------- Test Add Method ----------------------------------
        PatientDao patientDao = new PatientDao();

        // Create a test patient
        Patient testPatient = new Patient(
                1L, "123456789", "test@example.com", Sex.FEMALE, "123 Street, City",
                "1990-01-01", "Jane", "Doe", "CIN123456", "profile.jpg",
                BloodType.A_POSITIVE, LocalDate.of(1990, 1, 1), Insurance.CNSS,
                new ArrayList<>(), "Software Engineer", new DossierMedical()
        );

        // Add the test patient
        patientDao.add(testPatient);

    }





    // ------------------------------------ Implemented Methods ------------------------------------------------------

    @Override
    public void add(Patient patient) {
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            // Get the file path
            Path filePath = Paths.get(classLoader.getResource("data/patients.txt").toURI());

            // Convert patient to a string
            String patientString = patientToString(patient);

            // Write to the file in append mode
            Files.write(filePath, (patientString + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
            System.out.println("Patient added successfully!");

        } catch (IOException | URISyntaxException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }


    @Override
    public Optional<Patient> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public List<Patient> findAll() {
        return List.of();
    }

    @Override
    public void update(Long aLong, Patient entity) {

    }

    @Override
    public void delete(Long aLong) {

    }
}
