package hr.chembase.desktop.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBConnection {
	
	public static Connection connection = null;

	/* --------------------------------------------------------------------------- */

	public static boolean setupConnection(String databasePath)
	{
		boolean status = true;
		try
		{
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:" + databasePath);
		}
		catch (Exception ex)
		{
			status = false;
			System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
		}
		return status;
	}

	/* --------------------------------------------------------------------------- */
	
	public static void closeConnection()
	{
		try { if (connection != null) connection.close(); }
		catch (Exception ex) {}
	}
	
	/* --------------------------------------------------------------------------- */
	
	public static boolean constructTables()
	{
		boolean status = true;
		Statement statement = null;
		try
		{
			statement = connection.createStatement();
			statement.execute(SQLStatements.TABLE_CREATE_LOCATIONS_SQL);
			statement.execute(SQLStatements.TABLE_CREATE_CHEMICALS_SQL);			
			statement.execute(SQLStatements.CONSTRUCT_IX_CHEMICAL_NAME_SQL);
			statement.execute(SQLStatements.CONSTRUCT_IX_BRUTTO_FORMULA_SQL);
			statement.execute(SQLStatements.CONSTRUCT_IX_MOLAR_MASS_SQL);
			statement.execute(SQLStatements.CONSTRUCT_IX_STORAGE_LOCATION_SQL);
			statement.execute(SQLStatements.CONSTRUCT_IX_MANUFACTURER_SQL);
			statement.execute(SQLStatements.CONSTRUCT_IX_SUPPLIER_SQL);
		}
		catch (Exception ex)
		{
			status = false;
			System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
		}
		finally
		{
			try { if (statement != null) statement.close(); }
			catch (Exception ex) {}
		}
		
		return status;
	}

	/* --------------------------------------------------------------------------- */

}
