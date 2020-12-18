package hr.chembase.desktop.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import hr.chembase.desktop.api.ChemBase;
import hr.chembase.desktop.db.DBConnection;
import hr.chembase.desktop.db.SQLStatements;
import hr.chembase.desktop.gui.PopupMessage;
import hr.chembase.desktop.gui.ViewPanel;

public class ViewEntryListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Integer pkey = ChemBase.getCurrentPKey();
        if (pkey == -1)
            new PopupMessage("Row must be selected first!");
        else
        {
            PreparedStatement statement = null;
            ResultSet resultSet = null;
            try
            {
                String chemicalName     = null;
                String bruttoFormula    = null;
                String molarMass        = null;
                String quantityAmount   = null;
                String quantityUnit     = null;
                String storageLocation  = null;
                String manufacturer     = null;
                String supplier         = null;
                String dateOfEntry      = null;
                String additionalInfo   = null;

                statement = DBConnection.connection.prepareStatement(SQLStatements.VIEW_RECORD_SQL);
                statement.setInt(1, pkey);
                resultSet = statement.executeQuery();
                
                if (resultSet.next())
                {
                    chemicalName    = resultSet.getString(1);
                    bruttoFormula   = resultSet.getString(2);
                    molarMass       = resultSet.getString(3);
                    quantityAmount  = resultSet.getString(4);
                    quantityUnit    = resultSet.getString(5);
                    storageLocation = resultSet.getString(6);
                    manufacturer    = resultSet.getString(7);
                    supplier        = resultSet.getString(8);
                    dateOfEntry     = resultSet.getString(9);
                    additionalInfo  = resultSet.getString(10);
                }
                
                new ViewPanel(chemicalName,
                              bruttoFormula,
                              molarMass,
                              quantityAmount + " " + quantityUnit,
                              storageLocation,
                              manufacturer,
                              supplier,
                              dateOfEntry,
                              additionalInfo);
            }
            catch (SQLException ex)
            {
                new PopupMessage("Something went wrong!");
            }
            finally
            {
                try
                {
                    if (resultSet != null) resultSet.close();
                    if (statement != null) statement.close();
                }
                catch (Exception ex) {}
            }
        }
    }
}
