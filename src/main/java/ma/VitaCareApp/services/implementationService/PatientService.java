package ma.VitaCareApp.services.implementationService;

import ma.VitaCareApp.dao.exceptions.DaoException;
import ma.VitaCareApp.dao.implementationDAO.PatientDAO;
import ma.VitaCareApp.models.Patient;
import ma.VitaCareApp.services.api.specificAPI.IPatientService;
import ma.VitaCareApp.services.exceptions.ServiceException;

import java.time.LocalDate;
import java.util.List;

public class PatientService implements IPatientService {

    private final PatientDAO patientDAO;

    // Constructor injection for dependency
    public PatientService(PatientDAO patientDAO) {
        this.patientDAO = patientDAO;
    }



    /**
     * Finds a patient by their CIN (Civil Identification Number).
     *
     * @param cin the CIN of the patient to find
     * @return the found patient
     * @throws ServiceException if the patient is not found or an error occurs while retrieving it
     */
    @Override
    public Patient findByCIN(String cin) throws ServiceException {
        try {
            return patientDAO.findByCIN(cin);
        } catch (DaoException e) {
            throw new ServiceException("Failed to find patient with CIN " + cin + ": " + e.getMessage(), e);
        }
    }

    /**
     * Saves a new entity.
     *
     * @param newPatient the entity to save
     * @return the saved entity
     * @throws ServiceException if an error occurs while saving the entity
     */
    @Override
    public Patient save(Patient newPatient) throws ServiceException {
        try {
            // Add any validation or business logic here
            validatePatient(newPatient);
            return patientDAO.save(newPatient);
        } catch (DaoException e) {
            throw new ServiceException("Failed to save patient: " + e.getMessage(), e);
        }
    }

    /**
     * Finds an entity by its identifier.
     *
     * @param id the identifier of the entity to find
     * @return the found entity
     * @throws ServiceException if the entity is not found or an error occurs while retrieving it
     */
    @Override
    public Patient findById(Long id) throws ServiceException {
        try {
            return patientDAO.findById(id);
        } catch (DaoException e) {
            throw new ServiceException("Failed to find patient with ID " + id + ": " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all entities.
     *
     * @return a list of all entities
     * @throws ServiceException if an error occurs while retrieving the entities
     */
    @Override
    public List<Patient> findAll() throws ServiceException {
        try {
            return patientDAO.findAll();
        } catch (DaoException e) {
            throw new ServiceException("Failed to retrieve all patients: " + e.getMessage(), e);
        }
    }

    /**
     * Updates an existing entity.
     *
     * @param updatedPatient the entity with updated values
     * @throws ServiceException if the entity is not found or an error occurs while updating it
     */
    @Override
    public void update(Patient updatedPatient) throws ServiceException {
        try {
            // Add any validation or business logic here
            validatePatient(updatedPatient);
            patientDAO.update(updatedPatient);
        } catch (DaoException e) {
            throw new ServiceException("Failed to update patient: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes an entity by its identifier.
     *
     * @param id the identifier of the entity to delete
     * @throws ServiceException if the entity is not found or an error occurs while deleting it
     */
    @Override
    public void deleteById(Long id) throws ServiceException {
        try {
            patientDAO.deleteById(id);
        } catch (DaoException e) {
            throw new ServiceException("Failed to delete patient with ID " + id + ": " + e.getMessage(), e);
        }
    }

    /**
     * Deletes an entity.
     *
     * @param patient the entity to delete
     * @throws ServiceException if the entity is not found or an error occurs while deleting it
     */
    @Override
    public void delete(Patient patient) throws ServiceException {
        try {
            patientDAO.delete(patient);
        } catch (DaoException e) {
            throw new ServiceException("Failed to delete patient: " + e.getMessage(), e);
        }
    }

    // Additional method to find patients by a list of IDs
    public List<Patient> findAllById(List<Long> patientIds) throws ServiceException {
        try {
            return patientDAO.findAllById(patientIds);
        } catch (DaoException e) {
            throw new ServiceException("Failed to find patients by IDs: " + e.getMessage(), e);
        }
    }

    // Helper method to validate patient data
    private void validatePatient(Patient patient) throws ServiceException {
        if (patient.getFirstName() == null || patient.getFirstName().isEmpty()) {
            throw new ServiceException("First name is required.");
        }
        if (patient.getLastName() == null || patient.getLastName().isEmpty()) {
            throw new ServiceException("Last name is required.");
        }
        if (patient.getCin() == null || patient.getCin().isEmpty()) {
            throw new ServiceException("CIN is required.");
        }
        if (patient.getBirthDate() == null || patient.getBirthDate().isAfter(LocalDate.now())) {
            throw new ServiceException("Invalid birth date.");
        }
        // Add more validation rules as needed
    }

}
