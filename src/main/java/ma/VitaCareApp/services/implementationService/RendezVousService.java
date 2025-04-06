package ma.VitaCareApp.services.implementationService;

import ma.VitaCareApp.dao.implementationDAO.RendezVousDao;
import ma.VitaCareApp.models.RendezVous;
import ma.VitaCareApp.services.api.specificAPI.IRendezVousService;
import ma.VitaCareApp.services.exceptions.ServiceException;
import ma.VitaCareApp.dao.exceptions.DaoException;
import java.time.LocalDate;
import java.util.List;

public class RendezVousService implements IRendezVousService {

    private final RendezVousDao rendezVousDao;

    public RendezVousService(RendezVousDao rendezVousDao) {
        this.rendezVousDao = rendezVousDao;
    }

    @Override
    public RendezVous save(RendezVous newRendezVous) throws ServiceException {
        try {
            validateRendezVous(newRendezVous);
            return rendezVousDao.save(newRendezVous);
        } catch (DaoException e) {
            throw new ServiceException("Failed to save RendezVous: " + e.getMessage(), e);
        }
    }

    @Override
    public RendezVous findById(Long id) throws ServiceException {
        try {
            return rendezVousDao.findById(id);
        } catch (DaoException e) {
            throw new ServiceException("Failed to find RendezVous with ID " + id + ": " + e.getMessage(), e);
        }
    }

    @Override
    public List<RendezVous> findAll() throws ServiceException {
        try {
            return rendezVousDao.findAll();
        } catch (DaoException e) {
            throw new ServiceException("Failed to retrieve all RendezVous: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(RendezVous updatedRendezVous) throws ServiceException {
        try {
            validateRendezVous(updatedRendezVous);
            rendezVousDao.update(updatedRendezVous);
        } catch (DaoException e) {
            throw new ServiceException("Failed to update RendezVous: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteById(Long id) throws ServiceException {
        try {
            rendezVousDao.deleteById(id);
        } catch (DaoException e) {
            throw new ServiceException("Failed to delete RendezVous with ID " + id + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(RendezVous rendezVous) throws ServiceException {
        try {
            rendezVousDao.delete(rendezVous);
        } catch (DaoException e) {
            throw new ServiceException("Failed to delete RendezVous: " + e.getMessage(), e);
        }
    }

    public List<RendezVous> findByStatus(String status) throws ServiceException {
        return rendezVousDao.findByStatus(status);
    }

    private void validateRendezVous(RendezVous rendezVous) throws ServiceException {
        if (rendezVous.getMotif() == null || rendezVous.getMotif().isEmpty()) {
            throw new ServiceException("Motif is required.");
        }
        if (rendezVous.getTemps() == null || !rendezVous.getTemps().matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")) {
            throw new ServiceException("Invalid time format. Please use HH:mm.");
        }
        if (rendezVous.getTypeRDV() == null) {
            throw new ServiceException("TypeRDV is required.");
        }
        if (rendezVous.getDateRDV() == null) {
            throw new ServiceException("Date RDV is required.");
        }
    }
}