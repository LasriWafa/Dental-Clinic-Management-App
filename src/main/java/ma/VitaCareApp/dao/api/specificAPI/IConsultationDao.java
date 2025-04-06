package ma.VitaCareApp.dao.api.specificAPI;

import ma.VitaCareApp.dao.api.IDao;
import ma.VitaCareApp.dao.exceptions.DaoException;
import ma.VitaCareApp.dao.implementationDAO.ConsultationDao;
import ma.VitaCareApp.models.Consultation;

public interface IConsultationDao extends IDao<Consultation, Long> {
}
