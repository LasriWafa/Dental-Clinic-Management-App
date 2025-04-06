package ma.VitaCareApp.presentation.controller.implementationController;

import ma.VitaCareApp.presentation.controller.api.spesificAPI.IFinancialSituationController;
import ma.VitaCareApp.models.FinancialSituation;
import ma.VitaCareApp.presentation.controller.api.IController;
import ma.VitaCareApp.services.implementationService.FinancialSituationService;
import ma.VitaCareApp.services.exceptions.ServiceException;

import java.util.List;

/**
 * A controller for managing {@link FinancialSituation} entities.
 * Implements the {@link IController} interface and handles CRUD operations.
 */
public class FinancialSituationController implements IFinancialSituationController {

    private final FinancialSituationService financialSituationService;

    /**
     * Constructs a new FinancialSituationController with the specified FinancialSituationService.
     *
     * @param financialSituationService the service used for FinancialSituation data access and business logic
     */
    public FinancialSituationController(FinancialSituationService financialSituationService) {
        this.financialSituationService = financialSituationService;
    }

    /**
     * Saves a new FinancialSituation.
     *
     * @param financialSituation the FinancialSituation to save
     */
    @Override
    public void save(FinancialSituation financialSituation) {
        try {
            financialSituationService.save(financialSituation);
            System.out.println("FinancialSituation saved successfully!");
        } catch (ServiceException e) {
            System.err.println("Error saving FinancialSituation: " + e.getMessage());
        }
    }

    /**
     * Finds a FinancialSituation by its ID.
     *
     * @param id the ID of the FinancialSituation to find
     * @return the found FinancialSituation, or null if no FinancialSituation is found
     */
    @Override
    public FinancialSituation findById(Long id) {
        try {
            return financialSituationService.findById(id);
        } catch (ServiceException e) {
            System.err.println("Error retrieving FinancialSituation: " + e.getMessage());
            return null;
        }
    }

    /**
     * Updates an existing FinancialSituation.
     *
     * @param financialSituation the FinancialSituation with updated values
     */
    @Override
    public void update(FinancialSituation financialSituation) {
        try {
            financialSituationService.update(financialSituation);
            System.out.println("FinancialSituation updated successfully!");
        } catch (ServiceException e) {
            System.err.println("Error updating FinancialSituation: " + e.getMessage());
        }
    }

    /**
     * Deletes a FinancialSituation by its ID.
     *
     * @param id the ID of the FinancialSituation to delete
     */
    @Override
    public void deleteById(Long id) {
        try {
            financialSituationService.deleteById(id);
            System.out.println("FinancialSituation deleted successfully!");
        } catch (ServiceException e) {
            System.err.println("Error deleting FinancialSituation: " + e.getMessage());
        }
    }

    /**
     * Retrieves all FinancialSituations.
     *
     * @return a list of all FinancialSituations
     */
    @Override
    public List<FinancialSituation> findAll() {
        try {
            return financialSituationService.findAll();
        } catch (ServiceException e) {
            System.err.println("Error retrieving FinancialSituations: " + e.getMessage());
            return List.of(); // Return an empty list in case of error
        }
    }

    /**
     * Calculates the total amount paid for a FinancialSituation.
     *
     * @param financialSituation the FinancialSituation to calculate the total paid amount for
     * @return the total amount paid
     */
    public double calculateTotalAmountPaid(FinancialSituation financialSituation) {
        return financialSituationService.calculateTotalAmountPaid(financialSituation);
    }

    /**
     * Calculates the total amount remaining for a FinancialSituation.
     *
     * @param financialSituation the FinancialSituation to calculate the remaining amount for
     * @return the total amount remaining
     */
    public double calculateTotalAmountRemaining(FinancialSituation financialSituation) {
        return financialSituationService.calculateTotalAmountRemaining(financialSituation);
    }

    /**
     * Checks if a FinancialSituation is fully paid.
     *
     * @param financialSituation the FinancialSituation to check
     * @return true if fully paid, false otherwise
     */
    public boolean isFullyPaid(FinancialSituation financialSituation) {
        return financialSituationService.isFullyPaid(financialSituation);
    }

}
