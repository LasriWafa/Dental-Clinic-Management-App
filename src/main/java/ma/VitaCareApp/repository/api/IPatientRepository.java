package ma.VitaCareApp.repository.api;

import ma.VitaCareApp.entities.Patient;
import ma.VitaCareApp.repository.ICRUDRepository;
import ma.VitaCareApp.repository.exceptions.DaoException;

import java.util.List;

public interface IPatientRepository extends ICRUDRepository<Patient, Long> {

    Patient findByCIN(String cin) throws DaoException;
    List<Patient> findByCINLike(String motCle) throws DaoException;

}
