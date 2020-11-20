package hr.chembase.desktop.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import hr.chembase.desktop.api.ChemBase;
import hr.chembase.desktop.api.ChemBaseConstants;
import hr.chembase.desktop.db.DBConnection;
import hr.chembase.desktop.db.SQLStatements;
import hr.chembase.desktop.gui.PopupMessage;

public class SearchListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e)
	{
		String sql = SQLStatements.CORE_SEARCH_SQL;
		String searchText = ChemBase.getSearchText();

		switch (ChemBase.getSelectedSearchOption())
		{
			case ChemBaseConstants.SEARCH_BY_NAME:
				if (ChemBase.regularExpressionsInSearchON())
					sql += (" WHERE x.chemical_name LIKE '%" + searchText + "%'");
				else
					sql += (" WHERE x.chemical_name = " + "'" + searchText + "'");
				break;

			case ChemBaseConstants.SEARCH_BY_BRUTTO_FORMULA:
				searchText = searchText.toUpperCase();
				if (ChemBase.regularExpressionsInSearchON())
					sql += (" WHERE brutto_formula LIKE '%" + searchText + "%'");
				else
					sql += (" WHERE brutto_formula = " + "'" + searchText + "'");
				break;
				
			case ChemBaseConstants.SEARCH_BY_MOLAR_MASS:
				searchText = searchText.replaceAll(",", ".");
				if (ChemBase.regularExpressionsInSearchON())
					sql += (" WHERE molar_mass LIKE '%" + searchText + "%'");
				else
					sql += (" WHERE molar_mass =  " + "'" + searchText + "'");
				break;

			case ChemBaseConstants.SEARCH_BY_STORAGE_LOCATION:
				if (ChemBase.regularExpressionsInSearchON())
					sql += (" WHERE storage_location IN (SELECT id FROM locations WHERE location LIKE '%" + searchText + "%')");
				else
					sql += (" WHERE storage_location = (SELECT id FROM locations WHERE location = " + "'" + searchText + "')");
				break;

			case ChemBaseConstants.SEARCH_BY_MANUFACTURER:
				if (ChemBase.regularExpressionsInSearchON())
					sql += (" WHERE manufacturer LIKE '%" + searchText + "%'");
				else
					sql += (" WHERE manufacturer = " + "'" + searchText + "'");
				break;
				
			case ChemBaseConstants.SEARCH_BY_SUPPLIER:
				if (ChemBase.regularExpressionsInSearchON())
					sql += (" WHERE supplier LIKE '%" + searchText + "%'");
				else
					sql += (" WHERE supplier = " + "'" + searchText + "'");
				break;
		}
			
		/* ------------------------------------------------------------------------------------ */
			
		sql += " ORDER BY x.id";

		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try
		{	
			statement = DBConnection.connection.prepareStatement(sql);
			resultSet = statement.executeQuery();

			ChemBase.getTableModel().setRowCount(0);
			while (resultSet.next())
			{
				final Integer tempID 			 = resultSet.getInt(1);
				final String tempName 			 = resultSet.getString(2);
				final String tempBruttoFormula   = resultSet.getString(3);
				final String tempMolarMass 		 = resultSet.getString(4);
				final String tempQuantityAmount  = resultSet.getString(5);
				final String tempQuantityUnit 	 = resultSet.getString(6);
				final String tempStorageLocation = resultSet.getString(7);
					
				ChemBase.getTableModel().addRow(new Object[]{tempID,
															 tempName,
															 tempBruttoFormula,
															 tempMolarMass,
															 tempQuantityAmount,
															 tempQuantityUnit,
															 tempStorageLocation});
			}

			ChemBase.getTable().doLayout();
			ChemBase.setLastPerformedSQL(sql);
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
