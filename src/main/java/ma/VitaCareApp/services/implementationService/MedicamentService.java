package ma.VitaCareApp.services.implementationService;

import ma.VitaCareApp.dao.api.specificAPI.IMedicamentDao;
import ma.VitaCareApp.dao.exceptions.DaoException;
import ma.VitaCareApp.dao.implementationDAO.MedicamentDao;
import ma.VitaCareApp.models.Medicament;
import ma.VitaCareApp.services.api.specificAPI.IMedicamentService;
import ma.VitaCareApp.services.exceptions.ServiceException;

import java.util.List;

/**
 * A service class for managing {@link Medicament} entities.
 * Handles business logic, validation, and exception handling for Medicament operations.
 */
public class MedicamentService implements IMedicamentService {

    private final MedicamentDao medicamentDao;

    /**
     * Constructs a new MedicamentService with the specified MedicamentDao.
     *
     * @param medicamentDao the DAO used for Medicament data access
     */
    public MedicamentService(MedicamentDao medicamentDao) {
        this.medicamentDao = medicamentDao;
    }

    /**
     * Method to add a certain field to a file
     *
     * @param newMedicament : Name of the field to add
     * @throws ServiceException : If there's a problem with file
     */
    @Override
    public Medicament save(Medicament newMedicament) throws ServiceException {
        try {
            // Validate the Medicament before saving
            validateMedicament(newMedicament);
            return medicamentDao.save(newMedicament);
        } catch (DaoException e) {
            throw new ServiceException("Failed to save Medicament: " + e.getMessage(), e);
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
    public Medicament findById(Long id) throws ServiceException {
        try {
            return medicamentDao.findById(id);
        } catch (DaoException e) {
            throw new ServiceException("Failed to find Medicament with ID " + id + ": " + e.getMessage(), e);
        }
    }

    /**
     * Method to retrieve a list of a certain entity
     *
     * @return : List of an entity
     * @throws ServiceException : If there's a problem with file
     */
    @Override
    public List<Medicament> findAll() throws ServiceException {
        try {
            return medicamentDao.findAll();
        } catch (DaoException e) {
            throw new ServiceException("Failed to retrieve all Medicaments: " + e.getMessage(), e);
        }
    }

    /**
     * Method to update the fields of a given entity
     *
     * @param updatedMedicament : the wanted field to update
     * @throws ServiceException : If there's a problem with file
     */
    @Override
    public void update(Medicament updatedMedicament) throws ServiceException {
        try {
            // Validate the Medicament before updating
            validateMedicament(updatedMedicament);
            medicamentDao.update(updatedMedicament);
        } catch (DaoException e) {
            throw new ServiceException("Failed to update Medicament: " + e.getMessage(), e);
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
            medicamentDao.deleteById(id);
        } catch (DaoException e) {
            throw new ServiceException("Failed to delete Medicament with ID " + id + ": " + e.getMessage(), e);
        }
    }

    /**
     * Method to delete an entity
     *
     * @param medicament : the entity to delete
     * @throws ServiceException : If there's a problem with file
     */
    @Override
    public void delete(Medicament medicament) throws ServiceException {
        try {
            medicamentDao.delete(medicament);
        } catch (DaoException e) {
            throw new ServiceException("Failed to delete Medicament: " + e.getMessage(), e);
        }
    }

    /**
     * Validates a Medicament object to ensure it meets business rules.
     *
     * @param medicament the Medicament to validate
     * @throws ServiceException if the Medicament is invalid
     */
    private void validateMedicament(Medicament medicament) throws ServiceException {
        if (medicament.getName() == null || medicament.getName().isEmpty()) {
            throw new ServiceException("Name is required.");
        }
        if (medicament.getDescription() == null || medicament.getDescription().isEmpty()) {
            throw new ServiceException("Description is required.");
        }
        if (medicament.getPrice() < 0) {
            throw new ServiceException("Price must be a non-negative value.");
        }
        if (medicament.getPrecautions() == null || medicament.getPrecautions().isEmpty()) {
            throw new ServiceException("At least one precaution is required.");
        }
    }
}
