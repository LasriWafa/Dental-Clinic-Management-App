package ma.VitaCareApp.dao.api.specificAPI;

import ma.VitaCareApp.dao.api.IDao;
import ma.VitaCareApp.models.RendezVous;

import java.util.List;

public interface IRendezVousDao extends IDao<RendezVous, Long> {
    List<RendezVous> findByStatus(String status);
}
