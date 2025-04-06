package ma.VitaCareApp.presentation.controller.implementationController;

import ma.VitaCareApp.models.Patient;
import ma.VitaCareApp.presentation.controller.api.spesificAPI.IPatientController;
import ma.VitaCareApp.services.exceptions.ServiceException;
import ma.VitaCareApp.services.implementationService.PatientService;

import java.util.List;

/**
 * A controller for managing {@link Patient} entities.
 * Handles user input, calls the appropriate service methods, and updates the view.
 */
public class PatientController implements IPatientController {

    private final PatientService patientService;

    /**
     * Constructs a new PatientController with the specified PatientService.
     *
     * @param patientService the service used for Patient data access and business logic
     */
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    /**
     * Saves a new entity.
     *
     * @param patient the entity to save
     */
    @Override
    public void save(Patient patient) {
        try {
            patientService.save(patient);
            System.out.println("Patient saved successfully!");
        } catch (ServiceException e) {
            System.err.println("Error saving patient: " + e.getMessage());
        }
    }

    /**
     * Finds an entity by its identifier.
     *
     * @param id the identifier of the entity to find
     * @return the found entity, or null if no entity is found
     */
    @Override
    public Patient findById(Long id) {
        try {
            return patientService.findById(id);
        } catch (ServiceException e) {
            System.err.println("Error retrieving patient: " + e.getMessage());
            return null;
        }
    }

    /**
     * Updates an existing entity.
     *
     * @param patient the entity with updated values
     */
    @Override
    public void update(Patient patient) {
        try {
            patientService.update(patient);
            System.out.println("Patient updated successfully!");
        } catch (ServiceException e) {
            System.err.println("Error updating patient: " + e.getMessage());
        }
    }

    /**
     * Deletes an entity by its identifier.
     *
     * @param id the identifier of the entity to delete
     */
    @Override
    public void deleteById(Long id) {
        try {
            patientService.deleteById(id);
            System.out.println("Patient deleted successfully!");
        } catch (ServiceException e) {
            System.err.println("Error deleting patient: " + e.getMessage());
        }
    }

    /**
     * Retrieves all entities.
     *
     * @return a list of all entities
     */
    @Override
    public List<Patient> findAll() {
        try {
            return patientService.findAll();
        } catch (ServiceException e) {
            System.err.println("Error retrieving patients: " + e.getMessage());
            return List.of(); // Return an empty list in case of error
        }
    }

    /**
     * Finds a patient by their CIN (Civil Identification Number).
     *
     * @param cin the CIN of the patient to find
     * @return the found patient, or null if no patient is found
     */
    public Patient findByCIN(String cin) {
        try {
            return patientService.findByCIN(cin);
        } catch (ServiceException e) {
            System.err.println("Error retrieving patient by CIN: " + e.getMessage());
            return null;
        }
    }

    /**
     * Retrieves a list of patients by their IDs.
     *
     * @param patientIds the list of patient IDs
     * @return a list of patients with the specified IDs
     */
    public List<Patient> findAllById(List<Long> patientIds) {
        try {
            return patientService.findAllById(patientIds);
        } catch (ServiceException e) {
            System.err.println("Error retrieving patients by IDs: " + e.getMessage());
            return List.of(); // Return an empty list in case of error
        }
    }
}
