package ma.VitaCareApp.presentation.controller.implementationController;

import ma.VitaCareApp.presentation.controller.api.spesificAPI.IFactureController;
import ma.VitaCareApp.models.Facture;
import ma.VitaCareApp.models.enums.TypePayement;
import ma.VitaCareApp.presentation.controller.api.IController;
import ma.VitaCareApp.services.implementationService.FactureService;
import ma.VitaCareApp.services.exceptions.ServiceException;

import java.util.List;

/**
 * A controller for managing {@link Facture} entities.
 * Implements the {@link IController} interface and handles CRUD operations.
 */
public class FactureController implements IFactureController {

    private final FactureService factureService;

    /**
     * Constructs a new FactureController with the specified FactureService.
     *
     * @param factureService the service used for Facture data access and business logic
     */
    public FactureController(FactureService factureService) {
        this.factureService = factureService;
    }

    /**
     * Saves a new Facture.
     *
     * @param facture the Facture to save
     */
    @Override
    public void save(Facture facture) {
        try {
            factureService.save(facture);
            System.out.println("Facture saved successfully!");
        } catch (ServiceException e) {
            System.err.println("Error saving Facture: " + e.getMessage());
        }
    }

    /**
     * Finds a Facture by its ID.
     *
     * @param id the ID of the Facture to find
     * @return the found Facture, or null if no Facture is found
     */
    @Override
    public Facture findById(Long id) {
        try {
            return factureService.findById(id);
        } catch (ServiceException e) {
            System.err.println("Error retrieving Facture: " + e.getMessage());
            return null;
        }
    }

    /**
     * Updates an existing Facture.
     *
     * @param facture the Facture with updated values
     */
    @Override
    public void update(Facture facture) {
        try {
            factureService.update(facture);
            System.out.println("Facture updated successfully!");
        } catch (ServiceException e) {
            System.err.println("Error updating Facture: " + e.getMessage());
        }
    }

    /**
     * Deletes a Facture by its ID.
     *
     * @param id the ID of the Facture to delete
     */
    @Override
    public void deleteById(Long id) {
        try {
            factureService.deleteById(id);
            System.out.println("Facture deleted successfully!");
        } catch (ServiceException e) {
            System.err.println("Error deleting Facture: " + e.getMessage());
        }
    }

    /**
     * Retrieves all Factures.
     *
     * @return a list of all Factures
     */
    @Override
    public List<Facture> findAll() {
        try {
            return factureService.findAll();
        } catch (ServiceException e) {
            System.err.println("Error retrieving Factures: " + e.getMessage());
            return List.of(); // Return an empty list in case of error
        }
    }

    /**
     * Retrieves a list of Factures by their IDs.
     *
     * @param factureIds the list of identifiers
     * @return a list of Factures
     */
    public List<Facture> findAllById(List<Long> factureIds) {
        try {
            return factureService.findAllById(factureIds);
        } catch (ServiceException e) {
            System.err.println("Error retrieving Factures by IDs: " + e.getMessage());
            return List.of(); // Return an empty list in case of error
        }
    }

    /**
     * Calculates the total amount paid for a Facture.
     *
     * @param facture the Facture to calculate the total paid amount for
     * @return the total amount paid
     */
    public double calculateTotalAmountPaid(Facture facture) {
        return factureService.calculateTotalAmountPaid(facture);
    }

    /**
     * Calculates the total amount remaining for a Facture.
     *
     * @param facture the Facture to calculate the remaining amount for
     * @return the total amount remaining
     */
    public double calculateTotalAmountRemaining(Facture facture) {
        return factureService.calculateTotalAmountRemaining(facture);
    }

    /**
     * Checks if a Facture is fully paid.
     *
     * @param facture the Facture to check
     * @return true if fully paid, false otherwise
     */
    public boolean isFullyPaid(Facture facture) {
        return factureService.isFullyPaid(facture);
    }

    /**
     * Retrieves all Factures of a specific payment type.
     *
     * @param typePayement the payment type to filter by
     * @return a list of Factures with the specified payment type
     */
    public List<Facture> findAllByTypePayement(TypePayement typePayement) {
        try {
            return factureService.findAllByTypePayement(typePayement);
        } catch (ServiceException e) {
            System.err.println("Error retrieving Factures by TypePayement: " + e.getMessage());
            return List.of(); // Return an empty list in case of error
        }
    }

}
