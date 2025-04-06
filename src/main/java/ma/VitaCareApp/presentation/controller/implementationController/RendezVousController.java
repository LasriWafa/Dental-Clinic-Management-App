package ma.VitaCareApp.presentation.controller.implementationController;

import ma.VitaCareApp.models.RendezVous;
import ma.VitaCareApp.presentation.controller.api.spesificAPI.IRendezVousController;
import ma.VitaCareApp.services.exceptions.ServiceException;
import ma.VitaCareApp.services.implementationService.RendezVousService;

import java.util.List;

public class RendezVousController implements IRendezVousController {

    private final RendezVousService rendezVousService;

    /**
     * Constructs a new RendezVousController with the specified RendezVousService.
     *
     * @param rendezVousService the service used for RendezVous data access and business logic
     */
    public RendezVousController(RendezVousService rendezVousService) {
        this.rendezVousService = rendezVousService;
    }

    /**
     * Saves a new RendezVous.
     *
     * @param rendezVous the RendezVous to save
     */
    @Override
    public void save(RendezVous rendezVous) {
        try {
            rendezVousService.save(rendezVous);
            System.out.println("RendezVous saved successfully!");
        } catch (ServiceException e) {
            System.err.println("Error saving RendezVous: " + e.getMessage());
        }
    }

    /**
     * Finds a RendezVous by its ID.
     *
     * @param id the ID of the RendezVous to find
     * @return the found RendezVous, or null if no RendezVous is found
     */
    @Override
    public RendezVous findById(Long id) {
        try {
            return rendezVousService.findById(id);
        } catch (ServiceException e) {
            System.err.println("Error retrieving RendezVous: " + e.getMessage());
            return null;
        }
    }

    /**
     * Updates an existing RendezVous.
     *
     * @param rendezVous the RendezVous with updated values
     */
    @Override
    public void update(RendezVous rendezVous) {
        try {
            rendezVousService.update(rendezVous);
            System.out.println("RendezVous updated successfully!");
        } catch (ServiceException e) {
            System.err.println("Error updating RendezVous: " + e.getMessage());
        }
    }

    /**
     * Deletes a RendezVous by its ID.
     *
     * @param id the ID of the RendezVous to delete
     */
    @Override
    public void deleteById(Long id) {
        try {
            rendezVousService.deleteById(id);
            System.out.println("RendezVous deleted successfully!");
        } catch (ServiceException e) {
            System.err.println("Error deleting RendezVous: " + e.getMessage());
        }
    }

    /**
     * Retrieves all RendezVous.
     *
     * @return a list of all RendezVous
     */
    @Override
    public List<RendezVous> findAll() {
        try {
            return rendezVousService.findAll();
        } catch (ServiceException e) {
            System.err.println("Error retrieving RendezVous: " + e.getMessage());
            return List.of(); // Return an empty list in case of error
        }
    }

    /**
     * Finds RendezVous by their status.
     *
     * @param status the status to search for
     * @return a list of RendezVous with the specified status
     */
    public List<RendezVous> findByStatus(String status) {
        try {
            return rendezVousService.findByStatus(status);
        } catch (ServiceException e) {
            System.err.println("Error retrieving RendezVous by status: " + e.getMessage());
            return List.of(); // Return an empty list in case of error
        }
    }
}
