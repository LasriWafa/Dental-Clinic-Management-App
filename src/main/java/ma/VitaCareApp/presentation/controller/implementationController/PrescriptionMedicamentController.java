package ma.VitaCareApp.presentation.controller.implementationController;

import ma.VitaCareApp.presentation.controller.api.spesificAPI.IPrescriptionMedicamentController;
import ma.VitaCareApp.models.PrescriptionMedicament;
import ma.VitaCareApp.services.exceptions.ServiceException;
import ma.VitaCareApp.services.implementationService.PrescriptionMedicamentService;
import java.util.List;

/**
 * A controller for managing {@link PrescriptionMedicament} entities.
 * Handles user input, calls the appropriate service methods, and updates the view.
 */
public class PrescriptionMedicamentController implements IPrescriptionMedicamentController {

    private final PrescriptionMedicamentService prescriptionMedicamentService;

    /**
     * Constructs a new PrescriptionMedicamentController with the specified PrescriptionMedicamentService.
     *
     * @param prescriptionMedicamentService the service used for PrescriptionMedicament data access and business logic
     */
    public PrescriptionMedicamentController(PrescriptionMedicamentService prescriptionMedicamentService) {
        this.prescriptionMedicamentService = prescriptionMedicamentService;
    }

    /**
     * Saves a new PrescriptionMedicament.
     *
     * @param prescriptionMedicament the PrescriptionMedicament to save
     */
    @Override
    public void save(PrescriptionMedicament prescriptionMedicament) {
        try {
            prescriptionMedicamentService.save(prescriptionMedicament);
            System.out.println("PrescriptionMedicament saved successfully!");
        } catch (ServiceException e) {
            System.err.println("Error saving PrescriptionMedicament: " + e.getMessage());
        }
    }

    /**
     * Finds a PrescriptionMedicament by its ID.
     *
     * @param id the ID of the PrescriptionMedicament to find
     * @return the found PrescriptionMedicament, or null if no PrescriptionMedicament is found
     */
    @Override
    public PrescriptionMedicament findById(Long id) {
        try {
            return prescriptionMedicamentService.findById(id);
        } catch (ServiceException e) {
            System.err.println("Error retrieving PrescriptionMedicament: " + e.getMessage());
            return null;
        }
    }

    /**
     * Updates an existing PrescriptionMedicament.
     *
     * @param prescriptionMedicament the PrescriptionMedicament with updated values
     */
    @Override
    public void update(PrescriptionMedicament prescriptionMedicament) {
        try {
            prescriptionMedicamentService.update(prescriptionMedicament);
            System.out.println("PrescriptionMedicament updated successfully!");
        } catch (ServiceException e) {
            System.err.println("Error updating PrescriptionMedicament: " + e.getMessage());
        }
    }

    /**
     * Deletes a PrescriptionMedicament by its ID.
     *
     * @param id the ID of the PrescriptionMedicament to delete
     */
    @Override
    public void deleteById(Long id) {
        try {
            prescriptionMedicamentService.deleteById(id);
            System.out.println("PrescriptionMedicament deleted successfully!");
        } catch (ServiceException e) {
            System.err.println("Error deleting PrescriptionMedicament: " + e.getMessage());
        }
    }

    /**
     * Retrieves all PrescriptionMedicaments.
     *
     * @return a list of all PrescriptionMedicaments
     */
    @Override
    public List<PrescriptionMedicament> findAll() {
        try {
            return prescriptionMedicamentService.findAll();
        } catch (ServiceException e) {
            System.err.println("Error retrieving PrescriptionMedicaments: " + e.getMessage());
            return List.of(); // Return an empty list in case of error
        }
    }

    /**
     * Finds PrescriptionMedicaments by a list of IDs.
     *
     * @param prescriptionMedicamentIds the list of IDs to search for
     * @return a list of PrescriptionMedicaments with the specified IDs
     */
    public List<PrescriptionMedicament> findAllById(List<Long> prescriptionMedicamentIds) {
        try {
            return prescriptionMedicamentService.findAllById(prescriptionMedicamentIds);
        } catch (ServiceException e) {
            System.err.println("Error retrieving PrescriptionMedicaments by IDs: " + e.getMessage());
            return List.of(); // Return an empty list in case of error
        }
    }

}
