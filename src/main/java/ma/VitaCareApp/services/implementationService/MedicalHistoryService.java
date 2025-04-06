package ma.VitaCareApp.services.implementationService;

import ma.VitaCareApp.dao.api.specificAPI.IMedicalHistoryDao;
import ma.VitaCareApp.dao.exceptions.DaoException;
import ma.VitaCareApp.dao.implementationDAO.MedicalHistoryDao;
import ma.VitaCareApp.models.MedicalHistory;
import ma.VitaCareApp.services.api.specificAPI.IMedicalHistoryService;
import ma.VitaCareApp.services.exceptions.ServiceException;

import java.util.List;

/**
 * A service class for managing {@link MedicalHistory} entities.
 * Handles business logic, validation, and exception handling for MedicalHistory operations.
 */
public class MedicalHistoryService implements IMedicalHistoryService {

    private final MedicalHistoryDao medicalHistoryDao;

    /**
     * Constructs a new MedicalHistoryService with the specified MedicalHistoryDao.
     *
     * @param medicalHistoryDao the DAO used for MedicalHistory data access
     */
    public MedicalHistoryService(MedicalHistoryDao medicalHistoryDao) {
        this.medicalHistoryDao = medicalHistoryDao;
    }

    /**
     * Finds MedicalHistories by a list of IDs.
     *
     * @param medicalHistoryIds the list of IDs to search for
     * @return a list of MedicalHistories with the specified IDs
     * @throws ServiceException if an error occurs while retrieving the MedicalHistories
     */
    public List<MedicalHistory> findAllById(List<Long> medicalHistoryIds) throws ServiceException {
        try {
            return medicalHistoryDao.findAllById(medicalHistoryIds);
        } catch (DaoException e) {
            throw new ServiceException("Failed to find MedicalHistories by IDs: " + e.getMessage(), e);
        }
    }

    /**
     * Method to add a certain field to a file
     *
     * @param newMedicalHistory : Name of the field to add
     */
    @Override
    public MedicalHistory save(MedicalHistory newMedicalHistory) throws ServiceException{
        try {
            // Validate the MedicalHistory before saving
            validateMedicalHistory(newMedicalHistory);
            return medicalHistoryDao.save(newMedicalHistory);
        } catch (DaoException e) {
            throw new ServiceException("Failed to save MedicalHistory: " + e.getMessage(), e);
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
    public MedicalHistory findById(Long id) throws ServiceException {
        try {
            return medicalHistoryDao.findById(id);
        } catch (DaoException e) {
            throw new ServiceException("Failed to find MedicalHistory with ID " + id + ": " + e.getMessage(), e);
        }
    }

    /**
     * Method to retrieve a list of a certain entity
     *
     * @return : List of an entity
     * @throws ServiceException : If there's a problem with file
     */
    @Override
    public List<MedicalHistory> findAll() throws ServiceException {
        try {
            return medicalHistoryDao.findAll();
        } catch (DaoException e) {
            throw new ServiceException("Failed to retrieve all MedicalHistories: " + e.getMessage(), e);
        }
    }

    /**
     * Method to update the fields of a given entity
     *
     * @param updatedMedicalHistory : the wanted field to update
     * @throws ServiceException : If there's a problem with file
     */
    @Override
    public void update(MedicalHistory updatedMedicalHistory) throws ServiceException {
        try {
            // Validate the MedicalHistory before updating
            validateMedicalHistory(updatedMedicalHistory);
            medicalHistoryDao.update(updatedMedicalHistory);
        } catch (DaoException e) {
            throw new ServiceException("Failed to update MedicalHistory: " + e.getMessage(), e);
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
            medicalHistoryDao.deleteById(id);
        } catch (DaoException e) {
            throw new ServiceException("Failed to delete MedicalHistory with ID " + id + ": " + e.getMessage(), e);
        }
    }

    /**
     * Method to delete an entity
     *
     * @param medicalHistory : the entity to delete
     * @throws ServiceException : If there's a problem with file
     */
    @Override
    public void delete(MedicalHistory medicalHistory) throws ServiceException {
        try {
            medicalHistoryDao.delete(medicalHistory);
        } catch (DaoException e) {
            throw new ServiceException("Failed to delete MedicalHistory: " + e.getMessage(), e);
        }
    }

    /**
     * Validates a MedicalHistory object to ensure it meets business rules.
     *
     * @param medicalHistory the MedicalHistory to validate
     * @throws ServiceException if the MedicalHistory is invalid
     */
    private void validateMedicalHistory(MedicalHistory medicalHistory) throws ServiceException {
        if (medicalHistory.getLibelle() == null || medicalHistory.getLibelle().isEmpty()) {
            throw new ServiceException("Libelle is required.");
        }
        if (medicalHistory.getCategory() == null) {
            throw new ServiceException("Category is required.");
        }
        if (medicalHistory.getPatientsWithMedicalHistory() == null || medicalHistory.getPatientsWithMedicalHistory().isEmpty()) {
            throw new ServiceException("At least one patient is required.");
        }
    }
}
