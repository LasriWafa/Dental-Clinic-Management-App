package ma.VitaCareApp.presentation.controller.implementationController;

import ma.VitaCareApp.presentation.controller.api.spesificAPI.IOrdonnanceController;
import ma.VitaCareApp.models.Ordonnance;
import ma.VitaCareApp.services.implementationService.OrdonnanceService;
import ma.VitaCareApp.services.exceptions.ServiceException;
import java.util.List;

/**
 * A controller for managing {@link Ordonnance} entities.
 * Handles user input, calls the appropriate service methods, and updates the view.
 */
public class OrdonnanceController implements IOrdonnanceController {

    private final OrdonnanceService ordonnanceService;

    /**
     * Constructs a new OrdonnanceController with the specified OrdonnanceService.
     *
     * @param ordonnanceService the service used for Ordonnance data access and business logic
     */
    public OrdonnanceController(OrdonnanceService ordonnanceService) {
        this.ordonnanceService = ordonnanceService;
    }

    /**
     * Saves a new Ordonnance.
     *
     * @param ordonnance the Ordonnance to save
     */
    @Override
    public void save(Ordonnance ordonnance) {
        try {
            ordonnanceService.save(ordonnance);
            System.out.println("Ordonnance saved successfully!");
        } catch (ServiceException e) {
            System.err.println("Error saving Ordonnance: " + e.getMessage());
        }
    }

    /**
     * Finds an Ordonnance by its ID.
     *
     * @param id the ID of the Ordonnance to find
     * @return the found Ordonnance, or null if no Ordonnance is found
     */
    @Override
    public Ordonnance findById(Long id) {
        try {
            return ordonnanceService.findById(id);
        } catch (ServiceException e) {
            System.err.println("Error retrieving Ordonnance: " + e.getMessage());
            return null;
        }
    }

    /**
     * Updates an existing Ordonnance.
     *
     * @param ordonnance the Ordonnance with updated values
     */
    @Override
    public void update(Ordonnance ordonnance) {
        try {
            ordonnanceService.update(ordonnance);
            System.out.println("Ordonnance updated successfully!");
        } catch (ServiceException e) {
            System.err.println("Error updating Ordonnance: " + e.getMessage());
        }
    }

    /**
     * Deletes an Ordonnance by its ID.
     *
     * @param id the ID of the Ordonnance to delete
     */
    @Override
    public void deleteById(Long id) {
        try {
            ordonnanceService.deleteById(id);
            System.out.println("Ordonnance deleted successfully!");
        } catch (ServiceException e) {
            System.err.println("Error deleting Ordonnance: " + e.getMessage());
        }
    }

    /**
     * Retrieves all Ordonnances.
     *
     * @return a list of all Ordonnances
     */
    @Override
    public List<Ordonnance> findAll() {
        try {
            return ordonnanceService.findAll();
        } catch (ServiceException e) {
            System.err.println("Error retrieving Ordonnances: " + e.getMessage());
            return List.of(); // Return an empty list in case of error
        }
    }

}
