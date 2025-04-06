package ma.VitaCareApp.presentation.controller.implementationController;

import ma.VitaCareApp.presentation.controller.api.spesificAPI.IMedicamentController;
import ma.VitaCareApp.models.Medicament;
import ma.VitaCareApp.services.implementationService.MedicamentService;
import ma.VitaCareApp.services.exceptions.ServiceException;

import java.util.List;

/**
 * A controller for managing {@link Medicament} entities.
 * Handles user input, calls the appropriate service methods, and updates the view.
 */
public class MedicamentController implements IMedicamentController {

    private final MedicamentService medicamentService;

    /**
     * Constructs a new MedicamentController with the specified MedicamentService.
     *
     * @param medicamentService the service used for Medicament data access and business logic
     */
    public MedicamentController(MedicamentService medicamentService) {
        this.medicamentService = medicamentService;
    }

    /**
     * Saves a new Medicament.
     *
     * @param medicament the Medicament to save
     */
    @Override
    public void save(Medicament medicament) {
        try {
            medicamentService.save(medicament);
            System.out.println("Medicament saved successfully!");
        } catch (ServiceException e) {
            System.err.println("Error saving Medicament: " + e.getMessage());
        }
    }

    /**
     * Finds a Medicament by its ID.
     *
     * @param id the ID of the Medicament to find
     * @return the found Medicament, or null if no Medicament is found
     */
    @Override
    public Medicament findById(Long id) {
        try {
            return medicamentService.findById(id);
        } catch (ServiceException e) {
            System.err.println("Error retrieving Medicament: " + e.getMessage());
            return null;
        }
    }

    /**
     * Updates an existing Medicament.
     *
     * @param medicament the Medicament with updated values
     */
    @Override
    public void update(Medicament medicament) {
        try {
            medicamentService.update(medicament);
            System.out.println("Medicament updated successfully!");
        } catch (ServiceException e) {
            System.err.println("Error updating Medicament: " + e.getMessage());
        }
    }

    /**
     * Deletes a Medicament by its ID.
     *
     * @param id the ID of the Medicament to delete
     */
    @Override
    public void deleteById(Long id) {
        try {
            medicamentService.deleteById(id);
            System.out.println("Medicament deleted successfully!");
        } catch (ServiceException e) {
            System.err.println("Error deleting Medicament: " + e.getMessage());
        }
    }

    /**
     * Retrieves all Medicaments.
     *
     * @return a list of all Medicaments
     */
    @Override
    public List<Medicament> findAll() {
        try {
            return medicamentService.findAll();
        } catch (ServiceException e) {
            System.err.println("Error retrieving Medicaments: " + e.getMessage());
            return List.of(); // Return an empty list in case of error
        }
    }

}
