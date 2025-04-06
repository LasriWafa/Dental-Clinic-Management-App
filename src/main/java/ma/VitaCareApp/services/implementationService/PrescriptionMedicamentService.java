package ma.VitaCareApp.services.implementationService;

import ma.VitaCareApp.dao.api.specificAPI.IPrescriptionMedicamentDao;
import ma.VitaCareApp.dao.exceptions.DaoException;
import ma.VitaCareApp.models.PrescriptionMedicament;

import java.util.List;
import ma.VitaCareApp.dao.implementationDAO.PrescriptionMedicamentDao;
import ma.VitaCareApp.dao.exceptions.DaoException;
import ma.VitaCareApp.models.PrescriptionMedicament;
import ma.VitaCareApp.services.api.specificAPI.IPrescriptionMedicamentService;
import ma.VitaCareApp.services.exceptions.ServiceException;

/**
 * A service class for managing {@link PrescriptionMedicament} entities.
 * Handles business logic, validation, and exception handling for PrescriptionMedicament operations.
 */
public class PrescriptionMedicamentService implements IPrescriptionMedicamentService {

    private final PrescriptionMedicamentDao prescriptionMedicamentDao;

    /**
     * Constructs a new PrescriptionMedicamentService with the specified PrescriptionMedicamentDao.
     *
     * @param prescriptionMedicamentDao the DAO used for PrescriptionMedicament data access
     */
    public PrescriptionMedicamentService(PrescriptionMedicamentDao prescriptionMedicamentDao) {
        this.prescriptionMedicamentDao = prescriptionMedicamentDao;
    }

    /**
     * Finds PrescriptionMedicaments by a list of IDs.
     *
     * @param prescriptionMedicamentIds the list of IDs to search for
     * @return a list of PrescriptionMedicaments with the specified IDs
     * @throws ServiceException if an error occurs while retrieving the PrescriptionMedicaments
     */
    public List<PrescriptionMedicament> findAllById(List<Long> prescriptionMedicamentIds) throws ServiceException {
        try {
            return prescriptionMedicamentDao.findAllById(prescriptionMedicamentIds);
        } catch (DaoException e) {
            throw new ServiceException("Failed to find PrescriptionMedicaments by IDs: " + e.getMessage(), e);
        }
    }

    /**
     * Method to add a certain field to a file
     *
     * @param newPrescriptionMedicament : Name of the field to add
     * @throws ServiceException : If there's a problem with file
     */
    @Override
    public PrescriptionMedicament save(PrescriptionMedicament newPrescriptionMedicament) throws ServiceException {
        try {
            // Validate the PrescriptionMedicament before saving
            validatePrescriptionMedicament(newPrescriptionMedicament);
            return prescriptionMedicamentDao.save(newPrescriptionMedicament);
        } catch (DaoException e) {
            throw new ServiceException("Failed to save PrescriptionMedicament: " + e.getMessage(), e);
        }
    }

    /**
     * Method to find a certain entity by its ID
     *
     * @param id : the identifier of a certain entity
     * @return : return the found entity
     * @throws ServiceException : If there's a problem with file
     */
    @Override
    public PrescriptionMedicament findById(Long id) throws ServiceException {
        try {
            return prescriptionMedicamentDao.findById(id);
        } catch (DaoException e) {
            throw new ServiceException("Failed to find PrescriptionMedicament with ID " + id + ": " + e.getMessage(), e);
        }
    }

    /**
     * Method to retrieve a list of a certain entity
     *
     * @return : List of an entity
     * @throws ServiceException : If there's a problem with file
     */
    @Override
    public List<PrescriptionMedicament> findAll() throws ServiceException {
        try {
            return prescriptionMedicamentDao.findAll();
        } catch (DaoException e) {
            throw new ServiceException("Failed to retrieve all PrescriptionMedicaments: " + e.getMessage(), e);
        }
    }

    /**
     * Method to update the fields of a given entity
     *
     * @param updatedPrescriptionMedicament : the wanted field to update
     * @throws ServiceException : If there's a problem with file
     */
    @Override
    public void update(PrescriptionMedicament updatedPrescriptionMedicament) throws ServiceException {
        try {
            // Validate the PrescriptionMedicament before updating
            validatePrescriptionMedicament(updatedPrescriptionMedicament);
            prescriptionMedicamentDao.update(updatedPrescriptionMedicament);
        } catch (DaoException e) {
            throw new ServiceException("Failed to update PrescriptionMedicament: " + e.getMessage(), e);
        }
    }

    /**
     * Method to delete an entity by its ID
     *
     * @param id : the identifier of the entity to delete
     * @throws ServiceException : If there's a problem with file
     */
    @Override
    public void deleteById(Long id) throws ServiceException {
        try {
            prescriptionMedicamentDao.deleteById(id);
        } catch (DaoException e) {
            throw new ServiceException("Failed to delete PrescriptionMedicament with ID " + id + ": " + e.getMessage(), e);
        }

    }

    /**
     * Method to delete an entity
     *
     * @param prescriptionMedicament : the entity to delete
     * @throws ServiceException : If there's a problem with file
     */
    @Override
    public void delete(PrescriptionMedicament prescriptionMedicament) throws ServiceException {
        try {
            prescriptionMedicamentDao.delete(prescriptionMedicament);
        } catch (DaoException e) {
            throw new ServiceException("Failed to delete PrescriptionMedicament: " + e.getMessage(), e);
        }
    }

    /**
     * Validates a PrescriptionMedicament object to ensure it meets business rules.
     *
     * @param prescriptionMedicament the PrescriptionMedicament to validate
     * @throws ServiceException if the PrescriptionMedicament is invalid
     */
    private void validatePrescriptionMedicament(PrescriptionMedicament prescriptionMedicament) throws ServiceException {
        if (prescriptionMedicament.getUniteMinAPrendre() < 0) {
            throw new ServiceException("UniteMinAPrendre must be a non-negative value.");
        }
        if (prescriptionMedicament.getUniteMaxAPrendre() < prescriptionMedicament.getUniteMinAPrendre()) {
            throw new ServiceException("UniteMaxAPrendre must be greater than or equal to UniteMinAPrendre.");
        }
        if (prescriptionMedicament.getContraintesAlimentaire() == null || prescriptionMedicament.getContraintesAlimentaire().isEmpty()) {
            throw new ServiceException("ContraintesAlimentaire is required.");
        }
        if (prescriptionMedicament.getContraintesTemps() == null || prescriptionMedicament.getContraintesTemps().isEmpty()) {
            throw new ServiceException("ContraintesTemps is required.");
        }
        if (prescriptionMedicament.getMedicamentAPrescrire() == null) {
            throw new ServiceException("Medicament is required.");
        }
    }
}
