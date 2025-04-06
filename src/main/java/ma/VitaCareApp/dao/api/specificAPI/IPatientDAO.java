package ma.VitaCareApp.dao.api.specificAPI;

import ma.VitaCareApp.dao.api.IDao;
import ma.VitaCareApp.models.Patient;
import ma.VitaCareApp.dao.exceptions.DaoException;

import java.util.List;

public interface IPatientDAO extends IDao<Patient, Long> {

    /**
     *
     * @param cin : unique identifier
     * @return : the patient with the corresponding cin
     * @throws DaoException : file error
     */
    Patient findByCIN(String cin) throws DaoException;
    List<Patient> findAllById(List<Long> patientIds) throws DaoException;
}
