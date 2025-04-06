package ma.VitaCareApp.presentation.view.commun.buttons;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

@Getter
public class CrudButtonPanel extends JPanel {
    private CreateButton createButton;
    private EditButton editButton;
    private DeleteButton deleteButton;
    private RefreshButton refreshButton;

    public CrudButtonPanel(ActionListener createAction, ActionListener editAction, ActionListener deleteAction, ActionListener refreshAction) {
        setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));

        // Utiliser les nouvelles classes de boutons
        createButton = new CreateButton(createAction);
        editButton = new EditButton(editAction);
        deleteButton = new DeleteButton(deleteAction);
        refreshButton = new RefreshButton(refreshAction);

        // Ajouter les boutons au panneau
        add(createButton);
        add(editButton);
        add(deleteButton);
        add(refreshButton);
    }

}
