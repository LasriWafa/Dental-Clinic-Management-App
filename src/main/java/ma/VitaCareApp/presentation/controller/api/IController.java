package ma.VitaCareApp.presentation.controller.api;

import java.util.List;

/**
 * A generic interface for controllers that handle CRUD operations for entities.
 *
 * @param <T>  the type of entity managed by the controller
 * @param <ID> the type of the entity's identifier (e.g., Long, String)
 */
public interface IController<T, ID> {

    /**
     * Saves a new entity.
     *
     * @param entity the entity to save
     */
    void save(T entity);

    /**
     * Finds an entity by its identifier.
     *
     * @param id the identifier of the entity to find
     * @return the found entity, or null if no entity is found
     */
    T findById(ID id);

    /**
     * Updates an existing entity.
     *
     * @param entity the entity with updated values
     */
    void update(T entity);

    /**
     * Deletes an entity by its identifier.
     *
     * @param id the identifier of the entity to delete
     */
    void deleteById(ID id);

    /**
     * Retrieves all entities.
     *
     * @return a list of all entities
     */
    List<T> findAll();
}