package ma.VitaCareApp.presentation.controller.implementationController;

import ma.VitaCareApp.presentation.controller.api.spesificAPI.IDossierMedicalController;
import ma.VitaCareApp.models.DossierMedical;
import ma.VitaCareApp.models.enums.StatusPayement;
import ma.VitaCareApp.presentation.controller.api.IController;
import ma.VitaCareApp.services.implementationService.DossierMedicalService;
import ma.VitaCareApp.services.exceptions.ServiceException;

import java.util.List;

/**
 * A controller for managing {@link DossierMedical} entities.
 * Implements the {@link IController} interface and handles CRUD operations.
 */
public class DossierMedicalController implements IDossierMedicalController {

    private final DossierMedicalService dossierMedicalService;

    /**
     * Constructs a new DossierMedicalController with the specified DossierMedicalService.
     *
     * @param dossierMedicalService the service used for DossierMedical data access and business logic
     */
    public DossierMedicalController(DossierMedicalService dossierMedicalService) {
        this.dossierMedicalService = dossierMedicalService;
    }

    /**
     * Saves a new DossierMedical.
     *
     * @param dossierMedical the DossierMedical to save
     */
    @Override
    public void save(DossierMedical dossierMedical) {
        try {
            dossierMedicalService.save(dossierMedical);
            System.out.println("DossierMedical saved successfully!");
        } catch (ServiceException e) {
            System.err.println("Error saving DossierMedical: " + e.getMessage());
        }
    }

    /**
     * Finds a DossierMedical by its ID.
     *
     * @param numeroDossier the ID of the DossierMedical to find
     * @return the found DossierMedical, or null if no DossierMedical is found
     */
    @Override
    public DossierMedical findById(Long numeroDossier) {
        try {
            return dossierMedicalService.findById(numeroDossier);
        } catch (ServiceException e) {
            System.err.println("Error retrieving DossierMedical: " + e.getMessage());
            return null;
        }
    }

    /**
     * Updates an existing DossierMedical.
     *
     * @param dossierMedical the DossierMedical with updated values
     */
    @Override
    public void update(DossierMedical dossierMedical) {
        try {
            dossierMedicalService.update(dossierMedical);
            System.out.println("DossierMedical updated successfully!");
        } catch (ServiceException e) {
            System.err.println("Error updating DossierMedical: " + e.getMessage());
        }
    }

    /**
     * Deletes a DossierMedical by its ID.
     *
     * @param numeroDossier the ID of the DossierMedical to delete
     */
    @Override
    public void deleteById(Long numeroDossier) {
        try {
            dossierMedicalService.deleteById(numeroDossier);
            System.out.println("DossierMedical deleted successfully!");
        } catch (ServiceException e) {
            System.err.println("Error deleting DossierMedical: " + e.getMessage());
        }
    }

    /**
     * Retrieves all DossierMedical entities.
     *
     * @return a list of all DossierMedical entities
     */
    @Override
    public List<DossierMedical> findAll() {
        try {
            return dossierMedicalService.findAll();
        } catch (ServiceException e) {
            System.err.println("Error retrieving DossierMedical: " + e.getMessage());
            return List.of(); // Return an empty list in case of error
        }
    }

    /**
     * Retrieves all DossierMedical entities with a specific payment status.
     *
     * @param statusPayement the payment status to filter by
     * @return a list of DossierMedical entities with the specified payment status
     */
    public List<DossierMedical> findAllByStatusPayement(StatusPayement statusPayement) {
        try {
            return dossierMedicalService.findAllByStatusPayement(statusPayement);
        } catch (ServiceException e) {
            System.err.println("Error retrieving DossierMedical by StatusPayement: " + e.getMessage());
            return List.of(); // Return an empty list in case of error
        }
    }

    /**
     * Checks if a DossierMedical is fully paid.
     *
     * @param dossierMedical the DossierMedical to check
     * @return true if fully paid, false otherwise
     */
    public boolean isFullyPaid(DossierMedical dossierMedical) {
        return dossierMedicalService.isFullyPaid(dossierMedical);
    }


}
