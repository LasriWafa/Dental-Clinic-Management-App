package ma.VitaCareApp.services.api.specificAPI;

import ma.VitaCareApp.models.Patient;
import ma.VitaCareApp.services.api.IService;
import ma.VitaCareApp.services.exceptions.ServiceException;

/**
 * A service interface for performing operations specific to {@link Patient} entities.
 * Extends the generic {@link IService} interface with additional methods for patient management.
 */
public interface IPatientService extends IService<Patient, Long> {

    /**
     * Finds a patient by their CIN (Civil Identification Number).
     *
     * @param cin the CIN of the patient to find
     * @return the found patient
     * @throws ServiceException if the patient is not found or an error occurs while retrieving it
     */
    Patient findByCIN(String cin) throws ServiceException;

}
