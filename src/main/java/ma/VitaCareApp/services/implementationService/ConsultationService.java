package ma.VitaCareApp.services.implementationService;

import ma.VitaCareApp.dao.exceptions.DaoException;
import ma.VitaCareApp.dao.implementationDAO.ConsultationDao;
import ma.VitaCareApp.models.Consultation;
import ma.VitaCareApp.models.enums.TypeConsultation;
import ma.VitaCareApp.services.api.specificAPI.IConsultationService;
import ma.VitaCareApp.services.exceptions.ServiceException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A service class for managing {@link Consultation} entities.
 * Handles business logic, validation, and exception handling for Consultation operations.
 */
public class ConsultationService implements IConsultationService {

    private final ConsultationDao consultationDao;

    /**
     * Constructs a new ConsultationService with the specified ConsultationDao.
     *
     * @param consultationDao the DAO used for Consultation data access
     */
    public ConsultationService(ConsultationDao consultationDao) {
        this.consultationDao = consultationDao;
    }

    /**
     * Saves a new Consultation entity.
     *
     * @param consultation the entity to save
     * @return the saved entity
     * @throws ServiceException if an error occurs while saving the entity
     */
    @Override
    public Consultation save(Consultation consultation) throws ServiceException {
        try {
            // Validate the Consultation before saving
            validateConsultation(consultation);
            return consultationDao.save(consultation);
        } catch (DaoException e) {
            throw new ServiceException("Failed to save Consultation: " + e.getMessage(), e);
        }
    }

    /**
     * Finds a Consultation entity by its identifier.
     *
     * @param id the identifier of the entity to find
     * @return the found entity
     * @throws ServiceException if the entity is not found or an error occurs while retrieving it
     */
    @Override
    public Consultation findById(Long id) throws ServiceException {
        try {
            return consultationDao.findById(id);
        } catch (DaoException e) {
            throw new ServiceException("Failed to find Consultation with ID " + id + ": " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all Consultation entities.
     *
     * @return a list of all entities
     * @throws ServiceException if an error occurs while retrieving the entities
     */
    @Override
    public List<Consultation> findAll() throws ServiceException {
        try {
            return consultationDao.findAll();
        } catch (DaoException e) {
            throw new ServiceException("Failed to retrieve all Consultations: " + e.getMessage(), e);
        }
    }

    /**
     * Updates an existing Consultation entity.
     *
     * @param consultation the entity with updated values
     * @throws ServiceException if the entity is not found or an error occurs while updating it
     */
    @Override
    public void update(Consultation consultation) throws ServiceException {
        try {
            // Validate the Consultation before updating
            validateConsultation(consultation);
            consultationDao.update(consultation);
        } catch (DaoException e) {
            throw new ServiceException("Failed to update Consultation: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes a Consultation entity by its identifier.
     *
     * @param id the identifier of the entity to delete
     * @throws ServiceException if the entity is not found or an error occurs while deleting it
     */
    @Override
    public void deleteById(Long id) throws ServiceException {
        try {
            consultationDao.deleteById(id);
        } catch (DaoException e) {
            throw new ServiceException("Failed to delete Consultation with ID " + id + ": " + e.getMessage(), e);
        }
    }

    /**
     * Deletes a Consultation entity.
     *
     * @param consultation the entity to delete
     * @throws ServiceException if the entity is not found or an error occurs while deleting it
     */
    @Override
    public void delete(Consultation consultation) throws ServiceException {
        try {
            consultationDao.delete(consultation);
        } catch (DaoException e) {
            throw new ServiceException("Failed to delete Consultation: " + e.getMessage(), e);
        }
    }

    /**
     * Validates a Consultation object to ensure it meets business rules.
     *
     * @param consultation the Consultation to validate
     * @throws ServiceException if the Consultation is invalid
     */
    private void validateConsultation(Consultation consultation) throws ServiceException {
        if (consultation == null) {
            throw new ServiceException("Consultation cannot be null.");
        }
        if (consultation.getTypeConsultation() == null) {
            throw new ServiceException("TypeConsultation is required.");
        }
        if (consultation.getOrdonnance() == null) {
            throw new ServiceException("Ordonnance is required.");
        }
        if (consultation.getDateConsultation() == null || consultation.getDateConsultation().isAfter(LocalDate.now())) {
            throw new ServiceException("Invalid DateConsultation.");
        }
        if (consultation.getInterventions() == null || consultation.getInterventions().isEmpty()) {
            throw new ServiceException("At least one Intervention is required.");
        }
    }

    /**
     * Retrieves all Consultations of a specific type.
     *
     * @param typeConsultation the type to filter by
     * @return a list of Consultations with the specified type
     * @throws ServiceException if an error occurs while retrieving the entities
     */
    public List<Consultation> findAllByType(TypeConsultation typeConsultation) throws ServiceException {
        try {
            return consultationDao.findAll().stream()
                    .filter(consultation -> consultation.getTypeConsultation() == typeConsultation)
                    .collect(Collectors.toList());
        } catch (DaoException e) {
            throw new ServiceException("Failed to retrieve Consultations by TypeConsultation: " + e.getMessage(), e);
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
        return consultation.getOrdonnance().getIdOrdonnance().equals(ordonnanceId);
    }
}