package ma.VitaCareApp.services.implementationService;

import ma.VitaCareApp.dao.exceptions.DaoException;
import ma.VitaCareApp.dao.implementationDAO.ActeDao;
import ma.VitaCareApp.models.Acte;
import ma.VitaCareApp.models.enums.CategoryActe;
import ma.VitaCareApp.services.api.specificAPI.IActService;
import ma.VitaCareApp.services.exceptions.ServiceException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A service class for managing {@link Acte} entities.
 * Handles business logic, validation, and exception handling for Acte operations.
 */
public class ActeService implements IActService {

    private final ActeDao acteDao;

    /**
     * Constructs a new ActeService with the specified ActeDao.
     *
     * @param acteDao the DAO used for Acte data access
     */
    public ActeService(ActeDao acteDao) {
        this.acteDao = acteDao;
    }

    /**
     * Saves a new Acte entity.
     *
     * @param acte the entity to save
     * @return the saved entity
     * @throws ServiceException if an error occurs while saving the entity
     */
    @Override
    public Acte save(Acte acte) throws ServiceException {
        try {
            // Validate the Acte before saving
            validateActe(acte);
            return acteDao.save(acte);
        } catch (DaoException e) {
            throw new ServiceException("Failed to save Acte: " + e.getMessage(), e);
        }
    }

    /**
     * Finds an Acte entity by its identifier.
     *
     * @param id the identifier of the entity to find
     * @return the found entity
     * @throws ServiceException if the entity is not found or an error occurs while retrieving it
     */
    @Override
    public Acte findById(Long id) throws ServiceException {
        try {
            return acteDao.findById(id);
        } catch (DaoException e) {
            throw new ServiceException("Failed to find Acte with ID " + id + ": " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all Acte entities.
     *
     * @return a list of all entities
     * @throws ServiceException if an error occurs while retrieving the entities
     */
    @Override
    public List<Acte> findAll() throws ServiceException {
        try {
            return acteDao.findAll();
        } catch (DaoException e) {
            throw new ServiceException("Failed to retrieve all Actes: " + e.getMessage(), e);
        }
    }

    /**
     * Updates an existing Acte entity.
     *
     * @param acte the entity with updated values
     * @throws ServiceException if the entity is not found or an error occurs while updating it
     */
    @Override
    public void update(Acte acte) throws ServiceException {
        try {
            // Validate the Acte before updating
            validateActe(acte);
            acteDao.update(acte);
        } catch (DaoException e) {
            throw new ServiceException("Failed to update Acte: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes an Acte entity by its identifier.
     *
     * @param id the identifier of the entity to delete
     * @throws ServiceException if the entity is not found or an error occurs while deleting it
     */
    @Override
    public void deleteById(Long id) throws ServiceException {
        try {
            acteDao.deleteById(id);
        } catch (DaoException e) {
            throw new ServiceException("Failed to delete Acte with ID " + id + ": " + e.getMessage(), e);
        }
    }

    /**
     * Deletes an Acte entity.
     *
     * @param acte the entity to delete
     * @throws ServiceException if the entity is not found or an error occurs while deleting it
     */
    @Override
    public void delete(Acte acte) throws ServiceException {
        try {
            acteDao.delete(acte);
        } catch (DaoException e) {
            throw new ServiceException("Failed to delete Acte: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves a list of Actes by their IDs.
     *
     * @param acteIds the list of identifiers
     * @return a list of Actes
     * @throws ServiceException if an error occurs while retrieving the entities
     */
    public List<Acte> findAllById(List<Long> acteIds) throws ServiceException {
        try {
            return acteDao.findAllById(acteIds);
        } catch (DaoException e) {
            throw new ServiceException("Failed to retrieve Actes by IDs: " + e.getMessage(), e);
        }
    }

    /**
     * Validates an Acte object to ensure it meets business rules.
     *
     * @param acte the Acte to validate
     * @throws ServiceException if the Acte is invalid
     */
    private void validateActe(Acte acte) throws ServiceException {
        if (acte == null) {
            throw new ServiceException("Acte cannot be null.");
        }
        if (acte.getLibeller() == null || acte.getLibeller().isEmpty()) {
            throw new ServiceException("Libeller is required.");
        }
        if (acte.getPrixDeBase() == null || acte.getPrixDeBase() < 0) {
            throw new ServiceException("PrixDeBase must be a non-negative value.");
        }
        if (acte.getCategoryActe() == null) {
            throw new ServiceException("CategoryActe is required.");
        }
    }

    /**
     * Retrieves all Actes of a specific category.
     *
     * @param categoryActe the category to filter by
     * @return a list of Actes with the specified category
     * @throws ServiceException if an error occurs while retrieving the entities
     */
    public List<Acte> findAllByCategory(CategoryActe categoryActe) throws ServiceException {
        try {
            return acteDao.findAll().stream()
                    .filter(acte -> acte.getCategoryActe() == categoryActe)
                    .collect(Collectors.toList());
        } catch (DaoException e) {
            throw new ServiceException("Failed to retrieve Actes by CategoryActe: " + e.getMessage(), e);
        }
    }

    /**
     * Calculates the total cost of a list of Actes.
     *
     * @param actes the list of Actes
     * @return the total cost
     */
    public double calculateTotalCost(List<Acte> actes) {
        return actes.stream()
                .mapToDouble(Acte::getPrixDeBase)
                .sum();
    }
}