package ma.VitaCareApp.dao.api;

import ma.VitaCareApp.dao.exceptions.DaoException;

import java.util.List;
import java.util.Optional;

public interface IDao<T, ID> {


    /**
     * Method to add a certain field to a file
     * @param newElement : Name of the field to add
     * @throws DaoException : If there's a problem with file
     */
    T save(T newElement) throws DaoException;


    /**
     * Method to find a certain entity by its ID
     * @param id : the identifier of a certain entity
     * @return : return the found entity
     * @throws DaoException : If there's a problem with file
     */
    T findById(ID id) throws DaoException;



    /**
     * Method to retrieve a list of a certain entity
     * @return : List of an entity
     * @throws DaoException : If there's a problem with file
     */
    List<T> findAll() throws DaoException;


    /**
     * Method to update the fields of a given entity
     * @param newValuesElement : the wanted field to update
     * @throws DaoException : If there's a problem with file
     */
    void update(T newValuesElement) throws DaoException;


    /**
     * Method to delete an entity by its ID
     * @param id : the identifier of the entity to delete
     * @throws DaoException : If there's a problem with file
     */
    void deleteById(ID id) throws DaoException;


    /**
     * Method to delete an entity
     * @param element : the entity to delete
     * @throws DaoException : If there's a problem with file
     */
    void delete(T element) throws DaoException;

}
