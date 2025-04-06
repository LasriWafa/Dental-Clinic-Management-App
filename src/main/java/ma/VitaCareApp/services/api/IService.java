package ma.VitaCareApp.services.api;

import ma.VitaCareApp.services.exceptions.ServiceException;
import java.util.List;

/**
 * A generic service interface for performing CRUD (Create, Read, Update, Delete) operations
 * on entities of type {@code T} with an identifier of type {@code ID}.
 *
 * @param <T>  the type of the entity
 * @param <ID> the type of the entity's identifier
 */
public interface IService<T, ID> {

    /**
     * Saves a new entity.
     *
     * @param newElement the entity to save
     * @return the saved entity
     * @throws ServiceException if an error occurs while saving the entity
     */
    T save(T newElement) throws ServiceException;

    /**
     * Finds an entity by its identifier.
     *
     * @param id the identifier of the entity to find
     * @return the found entity
     * @throws ServiceException if the entity is not found or an error occurs while retrieving it
     */
    T findById(ID id) throws ServiceException;

    /**
     * Retrieves all entities.
     *
     * @return a list of all entities
     * @throws ServiceException if an error occurs while retrieving the entities
     */
    List<T> findAll() throws ServiceException;

    /**
     * Updates an existing entity.
     *
     * @param newValuesElement the entity with updated values
     * @throws ServiceException if the entity is not found or an error occurs while updating it
     */
    void update(T newValuesElement) throws ServiceException;

    /**
     * Deletes an entity by its identifier.
     *
     * @param id the identifier of the entity to delete
     * @throws ServiceException if the entity is not found or an error occurs while deleting it
     */
    void deleteById(ID id) throws ServiceException;

    /**
     * Deletes an entity.
     *
     * @param element the entity to delete
     * @throws ServiceException if the entity is not found or an error occurs while deleting it
     */
    void delete(T element) throws ServiceException;
}