package ma.VitaCareApp.dao.api.specificAPI;

import ma.VitaCareApp.dao.api.IDao;
import ma.VitaCareApp.dao.exceptions.DaoException;
import ma.VitaCareApp.models.PrescriptionMedicament;

import java.util.List;

public interface IPrescriptionMedicamentDao extends IDao<PrescriptionMedicament, Long> {

     List<PrescriptionMedicament> findAllById(List<Long> prescriptionMedicamentIds) throws DaoException; // Add this method

}
