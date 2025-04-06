package ma.VitaCareApp.presentation.controller.implementationController;

import ma.VitaCareApp.presentation.controller.api.spesificAPI.IMedicalHistoryController;
import ma.VitaCareApp.presentation.controller.api.spesificAPI.IMedicamentController;
import ma.VitaCareApp.models.MedicalHistory;
import ma.VitaCareApp.presentation.controller.api.IController;
import ma.VitaCareApp.services.implementationService.MedicalHistoryService;
import ma.VitaCareApp.services.exceptions.ServiceException;

import java.util.List;

/**
 * A controller for managing {@link MedicalHistory} entities.
 * Implements the {@link IController} interface and handles CRUD operations.
 */
public class MedicalHistoryController implements IMedicalHistoryController {

    private final MedicalHistoryService medicalHistoryService;

    /**
     * Constructs a new MedicalHistoryController with the specified MedicalHistoryService.
     *
     * @param medicalHistoryService the service used for MedicalHistory data access and business logic
     */
    public MedicalHistoryController(MedicalHistoryService medicalHistoryService) {
        this.medicalHistoryService = medicalHistoryService;
    }

    /**
     * Saves a new MedicalHistory.
     *
     * @param medicalHistory the MedicalHistory to save
     */
    @Override
    public void save(MedicalHistory medicalHistory) {
        try {
            medicalHistoryService.save(medicalHistory);
            System.out.println("MedicalHistory saved successfully!");
        } catch (ServiceException e) {
            System.err.println("Error saving MedicalHistory: " + e.getMessage());
        }
    }

    /**
     * Finds a MedicalHistory by its ID.
     *
     * @param id the ID of the MedicalHistory to find
     * @return the found MedicalHistory, or null if no MedicalHistory is found
     */
    @Override
    public MedicalHistory findById(Long id) {
        try {
            return medicalHistoryService.findById(id);
        } catch (ServiceException e) {
            System.err.println("Error retrieving MedicalHistory: " + e.getMessage());
            return null;
        }
    }

    /**
     * Updates an existing MedicalHistory.
     *
     * @param medicalHistory the MedicalHistory with updated values
     */
    @Override
    public void update(MedicalHistory medicalHistory) {
        try {
            medicalHistoryService.update(medicalHistory);
            System.out.println("MedicalHistory updated successfully!");
        } catch (ServiceException e) {
            System.err.println("Error updating MedicalHistory: " + e.getMessage());
        }
    }

    /**
     * Deletes a MedicalHistory by its ID.
     *
     * @param id the ID of the MedicalHistory to delete
     */
    @Override
    public void deleteById(Long id) {
        try {
            medicalHistoryService.deleteById(id);
            System.out.println("MedicalHistory deleted successfully!");
        } catch (ServiceException e) {
            System.err.println("Error deleting MedicalHistory: " + e.getMessage());
        }
    }

    /**
     * Retrieves all MedicalHistories.
     *
     * @return a list of all MedicalHistories
     */
    @Override
    public List<MedicalHistory> findAll() {
        try {
            return medicalHistoryService.findAll();
        } catch (ServiceException e) {
            System.err.println("Error retrieving MedicalHistories: " + e.getMessage());
            return List.of(); // Return an empty list in case of error
        }
    }

    /**
     * Finds MedicalHistories by a list of IDs.
     *
     * @param medicalHistoryIds the list of IDs to search for
     * @return a list of MedicalHistories with the specified IDs
     */
    public List<MedicalHistory> findAllById(List<Long> medicalHistoryIds) {
        try {
            return medicalHistoryService.findAllById(medicalHistoryIds);
        } catch (ServiceException e) {
            System.err.println("Error retrieving MedicalHistories by IDs: " + e.getMessage());
            return List.of(); // Return an empty list in case of error
        }
    }
}
