package ma.VitaCareApp.dao.implementationDAO;

import ma.VitaCareApp.dao.api.specificAPI.IActeDao;
import ma.VitaCareApp.dao.exceptions.DaoException;
import ma.VitaCareApp.models.Acte;
import ma.VitaCareApp.models.enums.CategoryActe;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ActeDao implements IActeDao {

    // Path to the text file
    private static final String FILE_PATH = "myFileBase/actes.txt";

    /**
     * Method to add a certain field to a file
     * @param acte : Name of the field to add
     * @throws DaoException : If there's a problem with file
     */
    @Override
    public Acte save(Acte acte) throws DaoException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(acteToString(acte)); // Convert Acte to a string
            writer.newLine();
        } catch (IOException e) {
            throw new DaoException("Failed to save Acte: " + e.getMessage());
        }
        return acte;
    }

    /**
     * Method to find a certain entity by its ID
     * @param id : the identifier of a certain entity
     * @return : return the found entity
     * @throws DaoException : If there's a problem with file
     */
    @Override
    public Acte findById(Long id) throws DaoException {
        return findAll().stream()
                .filter(acte -> acte.getIdActe().equals(id))
                .findFirst()
                .orElseThrow(() -> new DaoException("Acte not found with ID: " + id));
    }

    /**
     * Method to retrieve a list of a certain entity
     * @return : List of an entity
     * @throws DaoException : If there's a problem with file
     */
    @Override
    public List<Acte> findAll() throws DaoException {
        List<Acte> actes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Acte acte = parseActe(line);
                actes.add(acte);
            }
        } catch (IOException e) {
            throw new DaoException("Failed to read Actes: " + e.getMessage());
        }
        return actes;
    }

    /**
     * Method to update the fields of a given entity
     * @param updatedActe : the wanted field to update
     * @throws DaoException : If there's a problem with file
     */
    @Override
    public void update(Acte updatedActe) throws DaoException {
        List<Acte> actes = findAll();
        for (int i = 0; i < actes.size(); i++) {
            if (actes.get(i).getIdActe().equals(updatedActe.getIdActe())) {
                actes.set(i, updatedActe); // Replace the old Acte with the updated one
                break;
            }
        }
        saveAll(actes); // Save the updated list back to the file
    }

    /**
     * Method to delete an entity by its ID
     * @param id : the identifier of the entity to delete
     * @throws DaoException : If there's a problem with file
     */
    @Override
    public void deleteById(Long id) throws DaoException {
        List<Acte> actes = findAll();
        actes.removeIf(acte -> acte.getIdActe().equals(id));
        saveAll(actes); // Save the updated list back to the file
    }

    /**
     * Method to delete an entity
     * @param acte : the entity to delete
     * @throws DaoException : If there's a problem with file
     */
    @Override
    public void delete(Acte acte) throws DaoException {
        deleteById(acte.getIdActe());

    }

    // Helper method to save all Actes to the file
    private void saveAll(List<Acte> actes) throws DaoException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Acte acte : actes) {
                writer.write(acteToString(acte)); // Convert Acte to a string
                writer.newLine();
            }
        } catch (IOException e) {
            throw new DaoException("Failed to save Actes: " + e.getMessage());
        }
    }

    // Helper method to convert an Acte object to a comma-separated string
    private String acteToString(Acte acte) {
        return String.join(",",
                acte.getIdActe().toString(),
                acte.getLibeller(),
                acte.getPrixDeBase().toString(),
                acte.getCategoryActe().name() // Store the enum name
        );
    }

    // Helper method to parse a comma-separated string into an Acte object
    private Acte parseActe(String line) {
        String[] parts = line.split(",");
        Long idActe = Long.parseLong(parts[0]);
        String libeller = parts[1];
        Double prixDeBase = Double.parseDouble(parts[2]);
        CategoryActe categoryActe = CategoryActe.valueOf(parts[3]); // Parse the enum
        return new Acte(idActe, libeller, prixDeBase, categoryActe);
    }

    @Override
    public List<Acte> findAllById(List<Long> acteIds) throws DaoException {
        List<Acte> allActes = findAll(); // Fetch all Actes from the file
        return allActes.stream()
                .filter(acte -> acteIds.contains(acte.getIdActe())) // Filter by IDs
                .collect(Collectors.toList());
    }
}
