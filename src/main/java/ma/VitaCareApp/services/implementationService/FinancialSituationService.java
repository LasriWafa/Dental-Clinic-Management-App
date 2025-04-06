package ma.VitaCareApp.services.implementationService;

import ma.VitaCareApp.dao.exceptions.DaoException;
import ma.VitaCareApp.dao.implementationDAO.FinancialSituationDao;
import ma.VitaCareApp.models.FinancialSituation;
import ma.VitaCareApp.services.api.specificAPI.IFinancialSituationService;
import ma.VitaCareApp.services.exceptions.ServiceException;
import java.util.List;

/**
 * A service class for managing {@link FinancialSituation} entities.
 * Handles business logic, validation, and exception handling for FinancialSituation operations.
 */
public class FinancialSituationService implements IFinancialSituationService {

    private final FinancialSituationDao financialSituationDao;

    /**
     * Constructs a new FinancialSituationService with the specified FinancialSituationDao.
     *
     * @param financialSituationDao the DAO used for FinancialSituation data access
     */
    public FinancialSituationService(FinancialSituationDao financialSituationDao) {
        this.financialSituationDao = financialSituationDao;
    }

    /**
     * Saves a new entity.
     *
     * @param financialSituation the entity to save
     * @return the saved entity
     * @throws ServiceException if an error occurs while saving the entity
     */
    @Override
    public FinancialSituation save(FinancialSituation financialSituation) throws ServiceException {
        try {
            // Validate the FinancialSituation before saving
            validateFinancialSituation(financialSituation);
            return financialSituationDao.save(financialSituation);
        } catch (DaoException e) {
            throw new ServiceException("Failed to save FinancialSituation: " + e.getMessage(), e);
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
    public FinancialSituation findById(Long id) throws ServiceException {
        try {
            return financialSituationDao.findById(id);
        } catch (DaoException e) {
            throw new ServiceException("Failed to find FinancialSituation with ID " + id + ": " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all entities.
     *
     * @return a list of all entities
     * @throws ServiceException if an error occurs while retrieving the entities
     */
    @Override
    public List<FinancialSituation> findAll() throws ServiceException {
        try {
            return financialSituationDao.findAll();
        } catch (DaoException e) {
            throw new ServiceException("Failed to retrieve all FinancialSituations: " + e.getMessage(), e);
        }
    }

    /**
     * Updates an existing entity.
     *
     * @param financialSituation the entity with updated values
     * @throws ServiceException if the entity is not found or an error occurs while updating it
     */
    @Override
    public void update(FinancialSituation financialSituation) throws ServiceException {
        try {
            // Validate the FinancialSituation before updating
            validateFinancialSituation(financialSituation);
            financialSituationDao.update(financialSituation);
        } catch (DaoException e) {
            throw new ServiceException("Failed to update FinancialSituation: " + e.getMessage(), e);
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
            financialSituationDao.deleteById(id);
        } catch (DaoException e) {
            throw new ServiceException("Failed to delete FinancialSituation with ID " + id + ": " + e.getMessage(), e);
        }
    }

    /**
     * Deletes an entity.
     *
     * @param financialSituation the entity to delete
     * @throws ServiceException if the entity is not found or an error occurs while deleting it
     */
    @Override
    public void delete(FinancialSituation financialSituation) throws ServiceException {
        try {
            financialSituationDao.delete(financialSituation);
        } catch (DaoException e) {
            throw new ServiceException("Failed to delete FinancialSituation: " + e.getMessage(), e);
        }
    }

    /**
     * Validates a FinancialSituation object to ensure it meets business rules.
     *
     * @param financialSituation the FinancialSituation to validate
     * @throws ServiceException if the FinancialSituation is invalid
     */
    private void validateFinancialSituation(FinancialSituation financialSituation) throws ServiceException {
        if (financialSituation == null) {
            throw new ServiceException("FinancialSituation cannot be null.");
        }
        if (financialSituation.getDossierMedical() == null) {
            throw new ServiceException("DossierMedical is required.");
        }
        if (financialSituation.getFactures() == null || financialSituation.getFactures().isEmpty()) {
            throw new ServiceException("At least one Facture is required.");
        }
        if (financialSituation.getMontantGlobaleRestant() == null || financialSituation.getMontantGlobaleRestant() < 0) {
            throw new ServiceException("MontantGlobaleRestant must be a non-negative value.");
        }
        if (financialSituation.getMontantGlobalePaye() == null || financialSituation.getMontantGlobalePaye() < 0) {
            throw new ServiceException("MontantGlobalePaye must be a non-negative value.");
        }
    }

    /**
     * Calculates the total amount paid for a FinancialSituation.
     *
     * @param financialSituation the FinancialSituation to calculate the total paid amount for
     * @return the total amount paid
     */
    public double calculateTotalAmountPaid(FinancialSituation financialSituation) {
        return financialSituation.getMontantGlobalePaye();
    }

    /**
     * Calculates the total amount remaining for a FinancialSituation.
     *
     * @param financialSituation the FinancialSituation to calculate the remaining amount for
     * @return the total amount remaining
     */
    public double calculateTotalAmountRemaining(FinancialSituation financialSituation) {
        return financialSituation.getMontantGlobaleRestant();
    }

    /**
     * Checks if a FinancialSituation is fully paid.
     *
     * @param financialSituation the FinancialSituation to check
     * @return true if fully paid, false otherwise
     */
    public boolean isFullyPaid(FinancialSituation financialSituation) {
        return financialSituation.getMontantGlobaleRestant() <= 0;
    }
}
