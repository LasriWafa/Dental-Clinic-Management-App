package ma.VitaCareApp.presentation.controller.implementationController;

import ma.VitaCareApp.presentation.controller.api.spesificAPI.IInterventionMedcinController;
import ma.VitaCareApp.models.InterventionMedcin;
import ma.VitaCareApp.presentation.controller.api.IController;
import ma.VitaCareApp.services.implementationService.InterventionMedcinService;
import ma.VitaCareApp.services.exceptions.ServiceException;

import java.util.List;

/**
 * A controller for managing {@link InterventionMedcin} entities.
 * Implements the {@link IController} interface and handles CRUD operations.
 */
public class InterventionMedcinController implements IInterventionMedcinController {

    private final InterventionMedcinService interventionMedcinService;

    /**
     * Constructs a new InterventionMedcinController with the specified InterventionMedcinService.
     *
     * @param interventionMedcinService the service used for InterventionMedcin data access and business logic
     */
    public InterventionMedcinController(InterventionMedcinService interventionMedcinService) {
        this.interventionMedcinService = interventionMedcinService;
    }

    /**
     * Saves a new InterventionMedcin.
     *
     * @param interventionMedcin the InterventionMedcin to save
     */
    @Override
    public void save(InterventionMedcin interventionMedcin) {
        try {
            interventionMedcinService.save(interventionMedcin);
            System.out.println("InterventionMedcin saved successfully!");
        } catch (ServiceException e) {
            System.err.println("Error saving InterventionMedcin: " + e.getMessage());
        }
    }

    /**
     * Finds an InterventionMedcin by its ID.
     *
     * @param id the ID of the InterventionMedcin to find
     * @return the found InterventionMedcin, or null if no InterventionMedcin is found
     */
    @Override
    public InterventionMedcin findById(Long id) {
        try {
            return interventionMedcinService.findById(id);
        } catch (ServiceException e) {
            System.err.println("Error retrieving InterventionMedcin: " + e.getMessage());
            return null;
        }
    }

    /**
     * Updates an existing InterventionMedcin.
     *
     * @param interventionMedcin the InterventionMedcin with updated values
     */
    @Override
    public void update(InterventionMedcin interventionMedcin) {
        try {
            interventionMedcinService.update(interventionMedcin);
            System.out.println("InterventionMedcin updated successfully!");
        } catch (ServiceException e) {
            System.err.println("Error updating InterventionMedcin: " + e.getMessage());
        }
    }

    /**
     * Deletes an InterventionMedcin by its ID.
     *
     * @param id the ID of the InterventionMedcin to delete
     */
    @Override
    public void deleteById(Long id) {
        try {
            interventionMedcinService.deleteById(id);
            System.out.println("InterventionMedcin deleted successfully!");
        } catch (ServiceException e) {
            System.err.println("Error deleting InterventionMedcin: " + e.getMessage());
        }
    }

    /**
     * Retrieves all InterventionMedcins.
     *
     * @return a list of all InterventionMedcins
     */
    @Override
    public List<InterventionMedcin> findAll() {
        try {
            return interventionMedcinService.findAll();
        } catch (ServiceException e) {
            System.err.println("Error retrieving InterventionMedcins: " + e.getMessage());
            return List.of(); // Return an empty list in case of error
        }
    }


}
