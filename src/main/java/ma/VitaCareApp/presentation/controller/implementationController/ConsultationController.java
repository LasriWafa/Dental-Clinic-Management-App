package ma.VitaCareApp.presentation.controller.implementationController;

import ma.VitaCareApp.presentation.controller.api.spesificAPI.IConsultationController;
import ma.VitaCareApp.models.Consultation;
import ma.VitaCareApp.models.enums.TypeConsultation;
import ma.VitaCareApp.presentation.controller.api.IController;
import ma.VitaCareApp.services.implementationService.ConsultationService;
import ma.VitaCareApp.services.exceptions.ServiceException;

import java.util.List;

/**
 * A controller for managing {@link Consultation} entities.
 * Implements the {@link IController} interface and handles CRUD operations.
 */
public class ConsultationController implements IConsultationController {

    private final ConsultationService consultationService;

    /**
     * Constructs a new ConsultationController with the specified ConsultationService.
     *
     * @param consultationService the service used for Consultation data access and business logic
     */
    public ConsultationController(ConsultationService consultationService) {
        this.consultationService = consultationService;
    }

    /**
     * Saves a new Consultation.
     *
     * @param consultation the Consultation to save
     */
    @Override
    public void save(Consultation consultation) {
        try {
            consultationService.save(consultation);
            System.out.println("Consultation saved successfully!");
        } catch (ServiceException e) {
            System.err.println("Error saving Consultation: " + e.getMessage());
        }
    }

    /**
     * Finds a Consultation by its ID.
     *
     * @param id the ID of the Consultation to find
     * @return the found Consultation, or null if no Consultation is found
     */
    @Override
    public Consultation findById(Long id) {
        try {
            return consultationService.findById(id);
        } catch (ServiceException e) {
            System.err.println("Error retrieving Consultation: " + e.getMessage());
            return null;
        }
    }

    /**
     * Updates an existing Consultation.
     *
     * @param consultation the Consultation with updated values
     */
    @Override
    public void update(Consultation consultation) {
        try {
            consultationService.update(consultation);
            System.out.println("Consultation updated successfully!");
        } catch (ServiceException e) {
            System.err.println("Error updating Consultation: " + e.getMessage());
        }
    }

    /**
     * Deletes a Consultation by its ID.
     *
     * @param id the ID of the Consultation to delete
     */
    @Override
    public void deleteById(Long id) {
        try {
            consultationService.deleteById(id);
            System.out.println("Consultation deleted successfully!");
        } catch (ServiceException e) {
            System.err.println("Error deleting Consultation: " + e.getMessage());
        }
    }

    /**
     * Retrieves all Consultations.
     *
     * @return a list of all Consultations
     */
    @Override
    public List<Consultation> findAll() {
        try {
            return consultationService.findAll();
        } catch (ServiceException e) {
            System.err.println("Error retrieving Consultations: " + e.getMessage());
            return List.of(); // Return an empty list in case of error
        }
    }

    /**
     * Retrieves all Consultations of a specific type.
     *
     * @param typeConsultation the type to filter by
     * @return a list of Consultations with the specified type
     */
    public List<Consultation> findAllByType(TypeConsultation typeConsultation) {
        try {
            return consultationService.findAllByType(typeConsultation);
        } catch (ServiceException e) {
            System.err.println("Error retrieving Consultations by TypeConsultation: " + e.getMessage());
            return List.of(); // Return an empty list in case of error
        }
    }

    /**
     * Checks if a Consultation is associated with a specific Ordonnance.
     *
     * @param consultation the Consultation to check
     * @param ordonnanceId the Ordonnance ID to check against
     * @return true if associated, false otherwise
     */
    public boolean isAssociatedWithOrdonnance(Consultation consultation, Long ordonnanceId) {
        return consultationService.isAssociatedWithOrdonnance(consultation, ordonnanceId);
    }

}
