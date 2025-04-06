package ma.VitaCareApp.presentation.controller.implementationController;

import ma.VitaCareApp.presentation.controller.api.spesificAPI.IActeController;
import ma.VitaCareApp.models.Acte;
import ma.VitaCareApp.models.enums.CategoryActe;
import ma.VitaCareApp.presentation.controller.api.IController;
import ma.VitaCareApp.services.implementationService.ActeService;
import ma.VitaCareApp.services.exceptions.ServiceException;

import java.util.List;

/**
 * A controller for managing {@link Acte} entities.
 * Implements the {@link IController} interface and handles CRUD operations.
 */
public class ActeController implements IActeController {



    private final ActeService acteService;

    /**
     * Constructs a new ActeController with the specified ActeService.
     *
     * @param acteService the service used for Acte data access and business logic
     */
    public ActeController(ActeService acteService) {
        this.acteService = acteService;
    }

    /**
     * Saves a new Acte.
     *
     * @param acte the Acte to save
     */
    @Override
    public void save(Acte acte) {
        try {
            acteService.save(acte);
            System.out.println("Acte saved successfully!");
        } catch (ServiceException e) {
            System.err.println("Error saving Acte: " + e.getMessage());
        }
    }

    /**
     * Finds an Acte by its ID.
     *
     * @param id the ID of the Acte to find
     * @return the found Acte, or null if no Acte is found
     */
    @Override
    public Acte findById(Long id) {
        try {
            return acteService.findById(id);
        } catch (ServiceException e) {
            System.err.println("Error retrieving Acte: " + e.getMessage());
            return null;
        }
    }

    /**
     * Updates an existing Acte.
     *
     * @param acte the Acte with updated values
     */
    @Override
    public void update(Acte acte) {
        try {
            acteService.update(acte);
            System.out.println("Acte updated successfully!");
        } catch (ServiceException e) {
            System.err.println("Error updating Acte: " + e.getMessage());
        }
    }

    /**
     * Deletes an Acte by its ID.
     *
     * @param id the ID of the Acte to delete
     */
    @Override
    public void deleteById(Long id) {
        try {
            acteService.deleteById(id);
            System.out.println("Acte deleted successfully!");
        } catch (ServiceException e) {
            System.err.println("Error deleting Acte: " + e.getMessage());
        }
    }

    /**
     * Retrieves all Actes.
     *
     * @return a list of all Actes
     */
    @Override
    public List<Acte> findAll() {
        try {
            return acteService.findAll();
        } catch (ServiceException e) {
            System.err.println("Error retrieving Actes: " + e.getMessage());
            return List.of(); // Return an empty list in case of error
        }
    }

    /**
     * Retrieves all Actes of a specific category.
     *
     * @param categoryActe the category to filter by
     * @return a list of Actes with the specified category
     */
    public List<Acte> findAllByCategory(CategoryActe categoryActe) {
        try {
            return acteService.findAllByCategory(categoryActe);
        } catch (ServiceException e) {
            System.err.println("Error retrieving Actes by CategoryActe: " + e.getMessage());
            return List.of(); // Return an empty list in case of error
        }
    }

    /**
     * Calculates the total cost of a list of Actes.
     *
     * @param actes the list of Actes
     * @return the total cost
     */
    public double calculateTotalCost(List<Acte> actes) {
        return acteService.calculateTotalCost(actes);
    }

}
