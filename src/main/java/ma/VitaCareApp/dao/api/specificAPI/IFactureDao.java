package ma.VitaCareApp.dao.api.specificAPI;

import ma.VitaCareApp.dao.api.IDao;
import ma.VitaCareApp.dao.exceptions.DaoException;
import ma.VitaCareApp.models.Facture;

import java.util.List;

public interface IFactureDao extends IDao<Facture, Long> {

    List<Facture> findAllById(List<Long> factureIds) throws DaoException;

}
