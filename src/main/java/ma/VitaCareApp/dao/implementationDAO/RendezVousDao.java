package ma.VitaCareApp.dao.implementationDAO;

import ma.VitaCareApp.dao.api.specificAPI.IRendezVousDao;
import ma.VitaCareApp.dao.exceptions.DaoException;
import ma.VitaCareApp.models.RendezVous;
import ma.VitaCareApp.models.enums.TypeRDV;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RendezVousDao implements IRendezVousDao {

    private static final String FILE_PATH = "myFileBase/rendezVous.txt";

    @Override
    public List<RendezVous> findByStatus(String status) {
        return List.of();
    }

    @Override
    public RendezVous save(RendezVous rendezVous) throws DaoException {
        List<RendezVous> rendezVousList = findAll();

        // Generate a new ID (auto-increment)
        Long newId = rendezVousList.isEmpty() ? 1L : rendezVousList.get(rendezVousList.size() - 1).getIdRDV() + 1;
        rendezVous.setIdRDV(newId);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(rendezVousToString(rendezVous));
            writer.newLine();
        } catch (IOException e) {
            throw new DaoException("Failed to save RendezVous: " + e.getMessage());
        }
        return rendezVous;
    }

    @Override
    public RendezVous findById(Long id) throws DaoException {
        return findAll().stream()
                .filter(rdv -> rdv.getIdRDV().equals(id))
                .findFirst()
                .orElseThrow(() -> new DaoException("RendezVous not found with ID: " + id));
    }

    @Override
    public List<RendezVous> findAll() throws DaoException {
        List<RendezVous> rendezVousList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                RendezVous rendezVous = parseRendezVous(line);
                rendezVousList.add(rendezVous);
            }
        } catch (IOException e) {
            throw new DaoException("Failed to read RendezVous: " + e.getMessage());
        }
        return rendezVousList;
    }

    @Override
    public void update(RendezVous updatedRendezVous) throws DaoException {
        List<RendezVous> rendezVousList = findAll();
        for (int i = 0; i < rendezVousList.size(); i++) {
            if (rendezVousList.get(i).getIdRDV().equals(updatedRendezVous.getIdRDV())) {
                rendezVousList.set(i, updatedRendezVous); // Replace the old RendezVous with the updated one
                break;
            }
        }
        saveAll(rendezVousList); // Save the updated list back to the file
    }

    @Override
    public void deleteById(Long id) throws DaoException {
        List<RendezVous> rendezVousList = findAll();
        rendezVousList.removeIf(rdv -> rdv.getIdRDV().equals(id));
        saveAll(rendezVousList); // Save the updated list back to the file
    }

    @Override
    public void delete(RendezVous rendezVous) throws DaoException {
        deleteById(rendezVous.getIdRDV());
    }

    // Helper method to save all RendezVous to the file
    private void saveAll(List<RendezVous> rendezVousList) throws DaoException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (RendezVous rdv : rendezVousList) {
                writer.write(rendezVousToString(rdv));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new DaoException("Failed to save RendezVous: " + e.getMessage());
        }
    }

    // Helper method to convert RendezVous to a string
    private String rendezVousToString(RendezVous rendezVous) {
        return String.join(",",
                rendezVous.getIdRDV().toString(),
                rendezVous.getMotif(),
                rendezVous.getTemps(), // temps is already a String
                rendezVous.getTypeRDV().name(),
                rendezVous.getDateRDV().toString() // Ensure the date format is yyyy-MM-dd
        );
    }

    // Helper method to parse a string into a RendezVous object
    private RendezVous parseRendezVous(String line) {
        String[] parts = line.split(",");
        if (parts.length != 5) {
            throw new IllegalArgumentException("Invalid RendezVous data: " + line);
        }

        try {
            Long idRDV = Long.parseLong(parts[0]);
            String motif = parts[1];
            String temps = parts[2]; // temps is a String
            TypeRDV typeRDV = TypeRDV.valueOf(parts[3]);
            LocalDate dateRDV = LocalDate.parse(parts[4]); // Ensure the date format is yyyy-MM-dd
            return new RendezVous(idRDV, motif, temps, typeRDV, dateRDV);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to parse RendezVous: " + line, e);
        }
    }
}