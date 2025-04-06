package ma.VitaCareApp.services.implementationService;

import ma.VitaCareApp.models.Ordonnance;
import ma.VitaCareApp.services.api.specificAPI.IOrdonnanceService;
import ma.VitaCareApp.services.exceptions.ServiceException;

import java.time.LocalDate;
import java.util.List;
import ma.VitaCareApp.dao.implementationDAO.OrdonnanceDao;
import ma.VitaCareApp.dao.exceptions.DaoException;
import ma.VitaCareApp.models.Ordonnance;

/**
 * A service class for managing {@link Ordonnance} entities.
 * Handles business logic, validation, and exception handling for Ordonnance operations.
 */
public class OrdonnanceService implements IOrdonnanceService {

    private final OrdonnanceDao ordonnanceDao;

    /**
     * Constructs a new OrdonnanceService with the specified OrdonnanceDao.
     *
     * @param ordonnanceDao the DAO used for Ordonnance data access
     */
    public OrdonnanceService(OrdonnanceDao ordonnanceDao) {
        this.ordonnanceDao = ordonnanceDao;
    }

    /**
     * Saves a new entity.
     *
     * @param newOrdonnance the entity to save
     * @return the saved entity
     * @throws ServiceException if an error occurs while saving the entity
     */
    @Override
    public Ordonnance save(Ordonnance newOrdonnance) throws ServiceException {
        try {
            // Validate the Ordonnance before saving
            validateOrdonnance(newOrdonnance);
            return ordonnanceDao.save(newOrdonnance);
        } catch (DaoException e) {
            throw new ServiceException("Failed to save Ordonnance: " + e.getMessage(), e);
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
    public Ordonnance findById(Long id) throws ServiceException {
        try {
            return ordonnanceDao.findById(id);
        } catch (DaoException e) {
            throw new ServiceException("Failed to find Ordonnance with ID " + id + ": " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all entities.
     *
     * @return a list of all entities
     * @throws ServiceException if an error occurs while retrieving the entities
     */
    @Override
    public List<Ordonnance> findAll() throws ServiceException {
        try {
            return ordonnanceDao.findAll();
        } catch (DaoException e) {
            throw new ServiceException("Failed to retrieve all Ordonnances: " + e.getMessage(), e);
        }
    }

    /**
     * Updates an existing entity.
     *
     * @param updatedOrdonnance the entity with updated values
     * @throws ServiceException if the entity is not found or an error occurs while updating it
     */
    @Override
    public void update(Ordonnance updatedOrdonnance) throws ServiceException {
        try {
            // Validate the Ordonnance before updating
            validateOrdonnance(updatedOrdonnance);
            ordonnanceDao.update(updatedOrdonnance);
        } catch (DaoException e) {
            throw new ServiceException("Failed to update Ordonnance: " + e.getMessage(), e);
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
            ordonnanceDao.deleteById(id);
        } catch (DaoException e) {
            throw new ServiceException("Failed to delete Ordonnance with ID " + id + ": " + e.getMessage(), e);
        }
    }

    /**
     * Deletes an entity.
     *
     * @param ordonnance the entity to delete
     * @throws ServiceException if the entity is not found or an error occurs while deleting it
     */
    @Override
    public void delete(Ordonnance ordonnance) throws ServiceException {
        try {
            ordonnanceDao.delete(ordonnance);
        } catch (DaoException e) {
            throw new ServiceException("Failed to delete Ordonnance: " + e.getMessage(), e);
        }
    }

    /**
     * Validates an Ordonnance object to ensure it meets business rules.
     *
     * @param ordonnance the Ordonnance to validate
     * @throws ServiceException if the Ordonnance is invalid
     */
    private void validateOrdonnance(Ordonnance ordonnance) throws ServiceException {
        if (ordonnance.getDate() == null || ordonnance.getDate().isAfter(LocalDate.now())) {
            throw new ServiceException("Invalid date for Ordonnance.");
        }
        if (ordonnance.getPrescriptionMedicaments() == null || ordonnance.getPrescriptionMedicaments().isEmpty()) {
            throw new ServiceException("At least one PrescriptionMedicament is required.");
        }
    }
}
