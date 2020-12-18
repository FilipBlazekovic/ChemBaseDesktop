package hr.chembase.desktop.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import hr.chembase.desktop.api.ChemBase;
import hr.chembase.desktop.db.DBConnection;
import hr.chembase.desktop.db.SQLStatements;

public class LocationsPanel extends JDialog {

    private JList<String> locationsList    = null;
    private JTextField locationField       = null;
    private JButton addButton              = null;
    private JButton deleteButton           = null;
    private LocationsPanel referenceToThis = null;
    
    /* ---------------------------------------------------------------------------------------- */
    
    public LocationsPanel()
    {   

        referenceToThis = this;

        
        // Retrieving list of possible locations from the database
        // -------------------------------------------------------
        List<String> locations = new LinkedList<String>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try
        {
            DBConnection.connection.setAutoCommit(false);
            statement = DBConnection.connection.prepareStatement(SQLStatements.GET_LOCATIONS_SQL);
            resultSet = statement.executeQuery();

            while (resultSet.next())
            {
                locations.add(resultSet.getString(1));
            }
        }
        catch (Exception ex) {}
        finally
        {
            try
            {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
            }
            catch (Exception ex) {}
        }
        
        /* --------------------------------------------------------------------------------------- */

        addButton     = new JButton("ADD");
        deleteButton  = new JButton("DELETE");
        locationField = new JTextField(30);
        locationsList = new JList<String>(locations.toArray(new String[0]));

        /* --------------------------------------------------------------------------------------- */

        locationsList.addListSelectionListener(new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                String selectedLocation = locationsList.getSelectedValue();
                locationField.setText(selectedLocation);
            }
        });
        
        /* --------------------------------------------------------------------------------------- */

        addButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                final String currentLocation = locationField.getText().trim();
                if (currentLocation == null || currentLocation.length() == 0)
                    new PopupMessage("Enter location first!");
                else if (locations.contains(currentLocation))
                    new PopupMessage("Entered location already exists in the database!");
                else
                {
                    boolean status = true;
                    PreparedStatement statement = null;
                    try
                    {                       
                        statement = DBConnection.connection.prepareStatement(SQLStatements.INSERT_LOCATION_SQL);
                        statement.setString(1, currentLocation);
                        statement.execute();
                    }
                    catch (Exception ex) { status = false; }
                    finally
                    {
                        try { if (statement != null) statement.close(); }
                        catch (Exception ex) {}
                    }
                    
                    if (status)
                    {
                        locations.add(currentLocation);
                        locationsList.setListData(locations.toArray(new String[0]));
                        new PopupMessage("Success!");
                    }
                    else
                    {
                        new PopupMessage("Something went wrong!");
                    }
                }
            }
        });
        
        /* --------------------------------------------------------------------------------------- */

        deleteButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String selectedLocation = locationsList.getSelectedValue();
                if (selectedLocation == null || selectedLocation.trim().length() == 0)
                    new PopupMessage("Location must be selected first!");
                else
                {
                    boolean status              = true;
                    boolean recordsExist        = false;
                    PreparedStatement statement = null;
                    ResultSet resultSet         = null;

                    try
                    {
                        /* Check if there are any records in the chemicals
                         * table that contain the specified location
                         */
                        statement = DBConnection.connection.prepareStatement(SQLStatements.CHECK_IF_RECORDS_EXIST_FOR_LOCATION_SQL);
                        statement.setString(1, selectedLocation);
                        resultSet = statement.executeQuery();
                        
                        if (resultSet.next())
                            recordsExist = true;
                    }
                    catch (Exception ex) { status = false; }
                    finally
                    {
                        try
                        {
                            if (resultSet != null) resultSet.close();
                            if (statement != null) statement.close();
                        }
                        catch (Exception ex) {}
                    }
                    

                    if (!status)
                        new PopupMessage("Something went wrong!");
                    else if (recordsExist)
                        new PopupMessage("There are chemicals currently stored in the selected location. Delete those records first then you can delete this location.");
                    else
                    {
                        try
                        {
                            statement = DBConnection.connection.prepareStatement(SQLStatements.DELETE_LOCATION_SQL);
                            statement.setString(1, selectedLocation);
                            statement.execute();
                        }
                        catch (Exception ex) { status = false; }
                        finally
                        {
                            try { if (statement != null) statement.close(); }
                            catch (Exception ex) {}
                        }
                        
                        if (status)
                        {
                            locations.remove(selectedLocation);
                            locationsList.setListData(locations.toArray(new String[0]));
                            new PopupMessage("Success!");
                        }
                        else
                        {
                            new PopupMessage("Something went wrong!");
                        }
                    }
                }
            }
        });
        
        /* --------------------------------------------------------------------------------------- */

        JButton commitButton = new JButton("Save changes");
        JButton rollbackButton = new JButton("Cancel changes");

        
        commitButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try { DBConnection.connection.commit(); }
                catch (SQLException ex) {}

                referenceToThis.setVisible(false);
                referenceToThis.dispose();
            }
        });
        
        rollbackButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try { DBConnection.connection.rollback(); }
                catch (SQLException ex) {}

                referenceToThis.setVisible(false);
                referenceToThis.dispose();
            }
        });

        this.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosed(WindowEvent e)
            {
                try
                {
                    DBConnection.connection.rollback();
                    DBConnection.connection.setAutoCommit(true);
                }
                catch (SQLException ex) {}
            }
        });

        /* --------------------------------------------------------------------------------------- */

        locationsList.setVisibleRowCount(10);
        locationsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane locationsPane = new JScrollPane(locationsList,
                                                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        JPanel topPanel = new JPanel(new GridLayout(2,1));
        topPanel.add(new JLabel("Enter location:"));
        topPanel.add(locationField);

        JPanel bottomPanel = new JPanel(new GridLayout(2,1));
        bottomPanel.add(addButton);
        bottomPanel.add(deleteButton);
        
        JPanel contentPanel = new JPanel(new BorderLayout(0,2));
        contentPanel.add(topPanel, BorderLayout.NORTH);
        contentPanel.add(locationsPane, BorderLayout.CENTER);
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);
        Border innerBorder = new EmptyBorder(10,10,10,10);
        Border outerBorder = BorderFactory.createSoftBevelBorder(BevelBorder.LOWERED);
        contentPanel.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));       
        
        JPanel commitRollbackButtonsPanel = new JPanel(new GridLayout(1,2));
        commitRollbackButtonsPanel.setBorder(new EmptyBorder(20,0,0,0));
        commitRollbackButtonsPanel.add(commitButton);
        commitRollbackButtonsPanel.add(rollbackButton);

        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBorder(new EmptyBorder(10,10,10,10));
        rootPanel.add(contentPanel, BorderLayout.CENTER);
        rootPanel.add(commitRollbackButtonsPanel, BorderLayout.SOUTH);
        
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setTitle("Chemicals Database");
        this.setContentPane(rootPanel);
        this.pack();
        this.setLocationRelativeTo(ChemBase.getChemBaseRoot());
        this.setModal(true);
        this.setVisible(true);
    }
}
