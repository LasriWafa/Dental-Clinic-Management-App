package ma.VitaCareApp.services.implementationService;

import ma.VitaCareApp.dao.exceptions.DaoException;
import ma.VitaCareApp.dao.implementationDAO.FactureDao;
import ma.VitaCareApp.models.Facture;
import ma.VitaCareApp.models.enums.TypePayement;
import ma.VitaCareApp.services.api.specificAPI.IFactureService;
import ma.VitaCareApp.services.exceptions.ServiceException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A service class for managing {@link Facture} entities.
 * Handles business logic, validation, and exception handling for Facture operations.
 */
public class FactureService implements IFactureService {

    private final FactureDao factureDao;

    /**
     * Constructs a new FactureService with the specified FactureDao.
     *
     * @param factureDao the DAO used for Facture data access
     */
    public FactureService(FactureDao factureDao) {
        this.factureDao = factureDao;
    }

    /**
     * Saves a new Facture entity.
     *
     * @param facture the entity to save
     * @return the saved entity
     * @throws ServiceException if an error occurs while saving the entity
     */
    @Override
    public Facture save(Facture facture) throws ServiceException {
        try {
            // Validate the Facture before saving
            validateFacture(facture);
            return factureDao.save(facture);
        } catch (DaoException e) {
            throw new ServiceException("Failed to save Facture: " + e.getMessage(), e);
        }
    }

    /**
     * Finds a Facture entity by its identifier.
     *
     * @param id the identifier of the entity to find
     * @return the found entity
     * @throws ServiceException if the entity is not found or an error occurs while retrieving it
     */
    @Override
    public Facture findById(Long id) throws ServiceException {
        try {
            return factureDao.findById(id);
        } catch (DaoException e) {
            throw new ServiceException("Failed to find Facture with ID " + id + ": " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all Facture entities.
     *
     * @return a list of all entities
     * @throws ServiceException if an error occurs while retrieving the entities
     */
    @Override
    public List<Facture> findAll() throws ServiceException {
        try {
            return factureDao.findAll();
        } catch (DaoException e) {
            throw new ServiceException("Failed to retrieve all Factures: " + e.getMessage(), e);
        }
    }

    /**
     * Updates an existing Facture entity.
     *
     * @param facture the entity with updated values
     * @throws ServiceException if the entity is not found or an error occurs while updating it
     */
    @Override
    public void update(Facture facture) throws ServiceException {
        try {
            // Validate the Facture before updating
            validateFacture(facture);
            factureDao.update(facture);
        } catch (DaoException e) {
            throw new ServiceException("Failed to update Facture: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes a Facture entity by its identifier.
     *
     * @param id the identifier of the entity to delete
     * @throws ServiceException if the entity is not found or an error occurs while deleting it
     */
    @Override
    public void deleteById(Long id) throws ServiceException {
        try {
            factureDao.deleteById(id);
        } catch (DaoException e) {
            throw new ServiceException("Failed to delete Facture with ID " + id + ": " + e.getMessage(), e);
        }
    }

    /**
     * Deletes a Facture entity.
     *
     * @param facture the entity to delete
     * @throws ServiceException if the entity is not found or an error occurs while deleting it
     */
    @Override
    public void delete(Facture facture) throws ServiceException {
        try {
            factureDao.delete(facture);
        } catch (DaoException e) {
            throw new ServiceException("Failed to delete Facture: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves a list of Factures by their IDs.
     *
     * @param factureIds the list of identifiers
     * @return a list of Factures
     * @throws ServiceException if an error occurs while retrieving the entities
     */
    public List<Facture> findAllById(List<Long> factureIds) throws ServiceException {
        try {
            return factureDao.findAllById(factureIds);
        } catch (DaoException e) {
            throw new ServiceException("Failed to retrieve Factures by IDs: " + e.getMessage(), e);
        }
    }

    /**
     * Validates a Facture object to ensure it meets business rules.
     *
     * @param facture the Facture to validate
     * @throws ServiceException if the Facture is invalid
     */
    private void validateFacture(Facture facture) throws ServiceException {
        if (facture == null) {
            throw new ServiceException("Facture cannot be null.");
        }
        if (facture.getMontantTotale() == null || facture.getMontantTotale() < 0) {
            throw new ServiceException("MontantTotale must be a non-negative value.");
        }
        if (facture.getMontantRestant() == null || facture.getMontantRestant() < 0) {
            throw new ServiceException("MontantRestant must be a non-negative value.");
        }
        if (facture.getMontantPaye() == null || facture.getMontantPaye() < 0) {
            throw new ServiceException("MontantPaye must be a non-negative value.");
        }
        if (facture.getConsultation() == null) {
            throw new ServiceException("Consultation is required.");
        }
        if (facture.getTypePayement() == null) {
            throw new ServiceException("TypePayement is required.");
        }
        if (facture.getDateFacturation() == null || facture.getDateFacturation().isAfter(LocalDate.now())) {
            throw new ServiceException("Invalid DateFacturation.");
        }
    }

    /**
     * Calculates the total amount paid for a Facture.
     *
     * @param facture the Facture to calculate the total paid amount for
     * @return the total amount paid
     */
    public double calculateTotalAmountPaid(Facture facture) {
        return facture.getMontantPaye();
    }

    /**
     * Calculates the total amount remaining for a Facture.
     *
     * @param facture the Facture to calculate the remaining amount for
     * @return the total amount remaining
     */
    public double calculateTotalAmountRemaining(Facture facture) {
        return facture.getMontantRestant();
    }

    /**
     * Checks if a Facture is fully paid.
     *
     * @param facture the Facture to check
     * @return true if fully paid, false otherwise
     */
    public boolean isFullyPaid(Facture facture) {
        return facture.getMontantRestant() <= 0;
    }

    /**
     * Retrieves all Factures of a specific payment type.
     *
     * @param typePayement the payment type to filter by
     * @return a list of Factures with the specified payment type
     * @throws ServiceException if an error occurs while retrieving the entities
     */
    public List<Facture> findAllByTypePayement(TypePayement typePayement) throws ServiceException {
        try {
            return factureDao.findAll().stream()
                    .filter(f -> f.getTypePayement() == typePayement)
                    .collect(Collectors.toList());
        } catch (DaoException e) {
            throw new ServiceException("Failed to retrieve Factures by TypePayement: " + e.getMessage(), e);
        }
    }
}