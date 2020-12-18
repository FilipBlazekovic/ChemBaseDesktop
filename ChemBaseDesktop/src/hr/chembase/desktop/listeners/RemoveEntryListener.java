package hr.chembase.desktop.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import hr.chembase.desktop.api.ChemBase;
import hr.chembase.desktop.db.DBConnection;
import hr.chembase.desktop.db.SQLStatements;
import hr.chembase.desktop.gui.PopupMessage;

public class RemoveEntryListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Integer pkey = ChemBase.getCurrentPKey();
        if (pkey == -1)
            new PopupMessage("Row must be selected first!");
        else
        {
            int selectedOption = JOptionPane.showConfirmDialog(ChemBase.getChemBaseRoot(), "Are you sure you want to remove the selected record?", "Chemicals Database", JOptionPane.YES_NO_OPTION);
            if (selectedOption == JOptionPane.YES_OPTION)
            {               
                PreparedStatement statement = null;
                boolean status = true;
                try
                {
                    statement = DBConnection.connection.prepareStatement(SQLStatements.DELETE_RECORD_SQL);
                    statement.setInt(1, pkey);
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
                    ChemBase.refresh();
                    new PopupMessage("Success!");
                }
                else
                {
                    new PopupMessage("Something went wrong!");
                }
            }
        }
    }
}
