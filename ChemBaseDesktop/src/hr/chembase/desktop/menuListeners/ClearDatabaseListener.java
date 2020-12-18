package hr.chembase.desktop.menuListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import hr.chembase.desktop.api.ChemBase;
import hr.chembase.desktop.db.DBConnection;
import hr.chembase.desktop.db.SQLStatements;
import hr.chembase.desktop.gui.PopupMessage;

public class ClearDatabaseListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e)
    {
        int selectedOption = JOptionPane.showConfirmDialog(ChemBase.getChemBaseRoot(), "Are you sure you want to delete all the entries from the database?", "Chemicals Database", JOptionPane.YES_NO_OPTION);
        if (selectedOption == JOptionPane.YES_OPTION)
        {
            PreparedStatement statement = null;
            boolean status = true;
            try
            {
                statement = DBConnection.connection.prepareStatement(SQLStatements.TABLE_TRUNCATE_CHEMICALS_SQL);
                statement.execute();
                statement.close();
                statement = DBConnection.connection.prepareStatement(SQLStatements.TABLE_TRUNCATE_LOCATIONS_SQL);
                statement.execute();
            }
            catch (SQLException ex) { status = false; }
            finally
            {
                try { if (statement != null) statement.close(); }
                catch (Exception ex) {}
            }
            
            if (status)
            {           
                ChemBase.getTableModel().setRowCount(0);
                ChemBase.getTable().doLayout(); 
                new PopupMessage("Success!");
            }
            else
            {
                new PopupMessage("Something went wrong!");
            }
        }
    }
}
