package ma.VitaCareApp.services.implementationService;

import ma.VitaCareApp.dao.exceptions.DaoException;
import ma.VitaCareApp.dao.implementationDAO.InterventionMedcinDao;
import ma.VitaCareApp.dao.implementationDAO.MedicalHistoryDao;
import ma.VitaCareApp.models.InterventionMedcin;
import ma.VitaCareApp.services.api.specificAPI.IInterventionMedcinService;
import ma.VitaCareApp.services.exceptions.ServiceException;
import java.util.List;

/**
 * A service class for managing {@link InterventionMedcin} entities.
 * Handles business logic, validation, and exception handling for InterventionMedcin operations.
 */
public class InterventionMedcinService implements IInterventionMedcinService {

    private final InterventionMedcinDao interventionMedcinDao;

    /**
     * Constructs a new InterventionMedcinService with the specified InterventionMedcinDao.
     *
     * @param interventionMedcinDao the DAO used for InterventionMedcin data access
     */
    public InterventionMedcinService(InterventionMedcinDao interventionMedcinDao) {
        this.interventionMedcinDao = interventionMedcinDao;
    }

    /**
     * Saves a new entity.
     *
     * @param newInterventionMedcin the entity to save
     * @return the saved entity
     * @throws ServiceException if an error occurs while saving the entity
     */
    @Override
    public InterventionMedcin save(InterventionMedcin newInterventionMedcin) throws ServiceException {
        try {
            // Validate the InterventionMedcin before saving
            validateInterventionMedcin(newInterventionMedcin);
            return interventionMedcinDao.save(newInterventionMedcin);
        } catch (DaoException e) {
            throw new ServiceException("Failed to save InterventionMedcin: " + e.getMessage(), e);
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
    public InterventionMedcin findById(Long id) throws ServiceException {
        try {
            return interventionMedcinDao.findById(id);
        } catch (DaoException e) {
            throw new ServiceException("Failed to find InterventionMedcin with ID " + id + ": " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all entities.
     *
     * @return a list of all entities
     * @throws ServiceException if an error occurs while retrieving the entities
     */
    @Override
    public List<InterventionMedcin> findAll() throws ServiceException {
        try {
            return interventionMedcinDao.findAll();
        } catch (DaoException e) {
            throw new ServiceException("Failed to retrieve all InterventionMedcins: " + e.getMessage(), e);
        }
    }

    /**
     * Updates an existing entity.
     *
     * @param updatedInterventionMedcin the entity with updated values
     * @throws ServiceException if the entity is not found or an error occurs while updating it
     */
    @Override
    public void update(InterventionMedcin updatedInterventionMedcin) throws ServiceException {
        try {
            // Validate the InterventionMedcin before updating
            validateInterventionMedcin(updatedInterventionMedcin);
            interventionMedcinDao.update(updatedInterventionMedcin);
        } catch (DaoException e) {
            throw new ServiceException("Failed to update InterventionMedcin: " + e.getMessage(), e);
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
            interventionMedcinDao.deleteById(id);
        } catch (DaoException e) {
            throw new ServiceException("Failed to delete InterventionMedcin with ID " + id + ": " + e.getMessage(), e);
        }
    }

    /**
     * Deletes an entity.
     *
     * @param interventionMedcin the entity to delete
     * @throws ServiceException if the entity is not found or an error occurs while deleting it
     */
    @Override
    public void delete(InterventionMedcin interventionMedcin) throws ServiceException {
        try {
            interventionMedcinDao.delete(interventionMedcin);
        } catch (DaoException e) {
            throw new ServiceException("Failed to delete InterventionMedcin: " + e.getMessage(), e);
        }
    }

    /**
     * Validates an InterventionMedcin object to ensure it meets business rules.
     *
     * @param interventionMedcin the InterventionMedcin to validate
     * @throws ServiceException if the InterventionMedcin is invalid
     */
    private void validateInterventionMedcin(InterventionMedcin interventionMedcin) throws ServiceException {
        if (interventionMedcin.getNoteMedcin() == null || interventionMedcin.getNoteMedcin().isEmpty()) {
            throw new ServiceException("NoteMedcin is required.");
        }
        if (interventionMedcin.getActe() == null) {
            throw new ServiceException("Acte is required.");
        }
        if (interventionMedcin.getDent() == null || interventionMedcin.getDent() < 0) {
            throw new ServiceException("Invalid dent value.");
        }
        if (interventionMedcin.getPrixPatient() == null || interventionMedcin.getPrixPatient() < 0) {
            throw new ServiceException("PrixPatient must be a non-negative value.");
        }
    }
}
