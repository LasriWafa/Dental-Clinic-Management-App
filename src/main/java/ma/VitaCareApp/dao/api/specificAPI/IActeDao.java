package ma.VitaCareApp.dao.api.specificAPI;

import ma.VitaCareApp.dao.api.IDao;
import ma.VitaCareApp.dao.exceptions.DaoException;
import ma.VitaCareApp.models.Acte;
import ma.VitaCareApp.models.MedicalHistory;

import java.util.List;

public interface IActeDao extends IDao<Acte, Long> {

    List<Acte> findAllById(List<Long> acteIds) throws DaoException;

}
