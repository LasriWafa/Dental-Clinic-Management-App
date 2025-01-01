package ma.VitaCareApp.repository;

import ma.VitaCareApp.repository.exceptions.DaoException;

import java.util.List;

public interface ICRUDRepository<T, ID> {

    List<T> findAll() throws DaoException;
    T findById(ID identity) throws DaoException;
    T save(T newElement) throws DaoException;
    void update(T newValuesElement) throws DaoException;
    void delete(T element) throws DaoException;
    void deleteById(ID identity) throws DaoException;

}
