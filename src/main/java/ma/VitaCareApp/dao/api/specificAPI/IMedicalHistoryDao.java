package ma.VitaCareApp.dao.api.specificAPI;

import ma.VitaCareApp.dao.api.IDao;
import ma.VitaCareApp.dao.exceptions.DaoException;
import ma.VitaCareApp.models.MedicalHistory;
import java.util.List;

public interface IMedicalHistoryDao extends IDao<MedicalHistory, Long> {

    List<MedicalHistory> findAllById(List<Long> medicalHistoryIds) throws DaoException;
}
