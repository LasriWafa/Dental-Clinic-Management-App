package ma.VitaCareApp.dao.api;

import java.util.List;
import java.util.Optional;

public interface IDao<T, ID> {

    // Adds a new entity
    void add(T entity) ;

    // Retrieves an entity by its ID
    Optional<T> findById(ID id) ;

    // Retrieves all entities
    List<T> findAll() ;

    // Updates an existing entity by its ID
    void update(ID id, T entity) ;

    // Deletes an entity by its ID
    void delete(ID id) ;

}
