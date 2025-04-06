package ma.VitaCareApp.services.implementationService;

import ma.VitaCareApp.dao.exceptions.DaoException;
import ma.VitaCareApp.dao.implementationDAO.DossierMedicalDao;
import ma.VitaCareApp.models.DossierMedical;
import ma.VitaCareApp.models.enums.StatusPayement;
import ma.VitaCareApp.services.api.specificAPI.IDossierMedicalService;
import ma.VitaCareApp.services.exceptions.ServiceException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A service class for managing {@link DossierMedical} entities.
 * Handles business logic, validation, and exception handling for DossierMedical operations.
 */
public class DossierMedicalService implements IDossierMedicalService {

    private final DossierMedicalDao dossierMedicalDao;

    /**
     * Constructs a new DossierMedicalService with the specified DossierMedicalDao.
     *
     * @param dossierMedicalDao the DAO used for DossierMedical data access
     */
    public DossierMedicalService(DossierMedicalDao dossierMedicalDao) {
        this.dossierMedicalDao = dossierMedicalDao;
    }

    /**
     * Saves a new DossierMedical entity.
     *
     * @param dossierMedical the entity to save
     * @return the saved entity
     * @throws ServiceException if an error occurs while saving the entity
     */
    @Override
    public DossierMedical save(DossierMedical dossierMedical) throws ServiceException {
        try {
            // Validate the DossierMedical before saving
            validateDossierMedical(dossierMedical);
            return dossierMedicalDao.save(dossierMedical);
        } catch (DaoException e) {
            throw new ServiceException("Failed to save DossierMedical: " + e.getMessage(), e);
        }
    }

    /**
     * Finds a DossierMedical entity by its identifier.
     *
     * @param numeroDossier the identifier of the entity to find
     * @return the found entity
     * @throws ServiceException if the entity is not found or an error occurs while retrieving it
     */
    @Override
    public DossierMedical findById(Long numeroDossier) throws ServiceException {
        try {
            return dossierMedicalDao.findById(numeroDossier);
        } catch (DaoException e) {
            throw new ServiceException("Failed to find DossierMedical with numeroDossier " + numeroDossier + ": " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all DossierMedical entities.
     *
     * @return a list of all entities
     * @throws ServiceException if an error occurs while retrieving the entities
     */
    @Override
    public List<DossierMedical> findAll() throws ServiceException {
        try {
            return dossierMedicalDao.findAll();
        } catch (DaoException e) {
            throw new ServiceException("Failed to retrieve all DossierMedical: " + e.getMessage(), e);
        }
    }

    /**
     * Updates an existing DossierMedical entity.
     *
     * @param dossierMedical the entity with updated values
     * @throws ServiceException if the entity is not found or an error occurs while updating it
     */
    @Override
    public void update(DossierMedical dossierMedical) throws ServiceException {
        try {
            // Validate the DossierMedical before updating
            validateDossierMedical(dossierMedical);
            dossierMedicalDao.update(dossierMedical);
        } catch (DaoException e) {
            throw new ServiceException("Failed to update DossierMedical: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes a DossierMedical entity by its identifier.
     *
     * @param numeroDossier the identifier of the entity to delete
     * @throws ServiceException if the entity is not found or an error occurs while deleting it
     */
    @Override
    public void deleteById(Long numeroDossier) throws ServiceException {
        try {
            dossierMedicalDao.deleteById(numeroDossier);
        } catch (DaoException e) {
            throw new ServiceException("Failed to delete DossierMedical with numeroDossier " + numeroDossier + ": " + e.getMessage(), e);
        }
    }

    /**
     * Deletes a DossierMedical entity.
     *
     * @param dossierMedical the entity to delete
     * @throws ServiceException if the entity is not found or an error occurs while deleting it
     */
    @Override
    public void delete(DossierMedical dossierMedical) throws ServiceException {
        try {
            dossierMedicalDao.delete(dossierMedical);
        } catch (DaoException e) {
            throw new ServiceException("Failed to delete DossierMedical: " + e.getMessage(), e);
        }
    }

    /**
     * Validates a DossierMedical object to ensure it meets business rules.
     *
     * @param dossierMedical the DossierMedical to validate
     * @throws ServiceException if the DossierMedical is invalid
     */
    private void validateDossierMedical(DossierMedical dossierMedical) throws ServiceException {
        if (dossierMedical == null) {
            throw new ServiceException("DossierMedical cannot be null.");
        }
        if (dossierMedical.getNumeroDossier() == null || dossierMedical.getNumeroDossier().isEmpty()) {
            throw new ServiceException("NumeroDossier is required.");
        }
        if (dossierMedical.getDateCreation() == null || dossierMedical.getDateCreation().isAfter(LocalDate.now())) {
            throw new ServiceException("Invalid DateCreation.");
        }
        if (dossierMedical.getStatusPayement() == null) {
            throw new ServiceException("StatusPayement is required.");
        }
        if (dossierMedical.getPatient() == null) {
            throw new ServiceException("Patient is required.");
        }
        if (dossierMedical.getRdvs() == null || dossierMedical.getRdvs().isEmpty()) {
            throw new ServiceException("At least one RendezVous is required.");
        }
        if (dossierMedical.getOrdonnances() == null || dossierMedical.getOrdonnances().isEmpty()) {
            throw new ServiceException("At least one Ordonnance is required.");
        }
        if (dossierMedical.getConsultations() == null || dossierMedical.getConsultations().isEmpty()) {
            throw new ServiceException("At least one Consultation is required.");
        }
    }

    /**
     * Retrieves all DossierMedical entities with a specific payment status.
     *
     * @param statusPayement the payment status to filter by
     * @return a list of DossierMedical entities with the specified payment status
     * @throws ServiceException if an error occurs while retrieving the entities
     */
    public List<DossierMedical> findAllByStatusPayement(StatusPayement statusPayement) throws ServiceException {
        try {
            return dossierMedicalDao.findAll().stream()
                    .filter(dossier -> dossier.getStatusPayement() == statusPayement)
                    .collect(Collectors.toList());
        } catch (DaoException e) {
            throw new ServiceException("Failed to retrieve DossierMedical by StatusPayement: " + e.getMessage(), e);
        }
    }

    /**
     * Checks if a DossierMedical is fully paid.
     *
     * @param dossierMedical the DossierMedical to check
     * @return true if fully paid, false otherwise
     */
    public boolean isFullyPaid(DossierMedical dossierMedical) {
        return dossierMedical.getStatusPayement() == StatusPayement.PAYE;
    }
}