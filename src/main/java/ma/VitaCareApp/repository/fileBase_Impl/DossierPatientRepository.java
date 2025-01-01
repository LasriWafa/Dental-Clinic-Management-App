package ma.VitaCareApp.repository.fileBase_Impl;

import ma.VitaCareApp.entities.DossierPatient;
import ma.VitaCareApp.entities.Patient;
import ma.VitaCareApp.entities.enums.Sex;
import ma.VitaCareApp.repository.ICRUDRepository;
import ma.VitaCareApp.repository.api.IDossierPatientRepository;
import ma.VitaCareApp.repository.api.IPatientRepository;
import ma.VitaCareApp.repository.exceptions.DaoException;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class DossierPatientRepository implements IDossierPatientRepository {


    private Patient mapToPatient(String fileLine) throws DaoException {

        // File Lines structure : ID-Nom-Prenom-Email-CIN-Sexe-IdDossier
        try {
            StringTokenizer st = new StringTokenizer(fileLine, "-");
            String value = st.nextToken();

            Long id = Long.parseLong(value);
            value = st.nextToken();

            String Nom = (value == "null" ? null : value);
            value = st.nextToken();

            String Prenom = (value == "null" ? null : value);
            value = st.nextToken();

            String email = (value == "null" ? null : value);
            value = st.nextToken();

            String cin = (value == "null" ? null : value);
            value = st.nextToken();

            Sex sexe = (value == "null" ? null : (value.equals("MALE") ? Sex.MALE : Sex.FEMALE));
            value = st.nextToken();



        } catch (NumberFormatException e) {
            throw new DaoException(e.getMessage());
        }
        return null;
    }



    @Override
    public List<DossierPatient> findAll() throws DaoException {
        return List.of();
    }

    @Override
    public DossierPatient findById(Long identity) throws DaoException {
        return null;
    }

    @Override
    public DossierPatient save(DossierPatient newElement) throws DaoException {
        return null;
    }

    @Override
    public void update(DossierPatient newValuesElement) throws DaoException {

    }

    @Override
    public void delete(DossierPatient element) throws DaoException {

    }

    @Override
    public void deleteById(Long identity) throws DaoException {

    }
}
