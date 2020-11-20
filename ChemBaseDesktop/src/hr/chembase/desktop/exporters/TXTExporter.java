package hr.chembase.desktop.exporters;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import hr.chembase.desktop.api.ChemBase;
import hr.chembase.desktop.db.DBConnection;

public class TXTExporter {

	private static final String[] columnTitlesConcise = {

            "ID",
            "Chemical Name",
            "Brutto Formula",
            "Molar Mass",
            "Quantity",
            "Unit",
            "Storage Location"
	};

	private static final String[] formatSpecifiersConcise = {
 
            "%-10s",
            "%-35s",
            "%-25s",
            "%-20s",
            "%-20s",
            "%-10s",
            "%-50s"
	};
	
	private static final String[] columnTitlesFull = {

            "ID",
            "Chemical Name",
            "Brutto Formula",
            "Molar Mass",
            "Quantity",
            "Unit",
            "Storage Location",
            "Manufacturer",
            "Supplier",
            "Date of Entry",
            "Additional Info"
	};

	private static final String[] formatSpecifiersFull = {
 
            "%-10s",
            "%-35s",
            "%-25s",
            "%-20s",
            "%-20s",
            "%-10s",
            "%-50s",
            "%-35s",
            "%-35s",
            "%-20s",
            "%-100s"
	};

	/* --------------------------------------------------------------------------------------------- */
	
	public static boolean performConciseExport(final File outputPath)
	{
		boolean STATUS = true;
        BufferedWriter output = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try
		{
			/* Open output file */
            output = new BufferedWriter(new FileWriter(outputPath));
			
			/* Write column titles */
            final StringBuilder titleLineBuilder = new StringBuilder();
            long maxElementIndex = (columnTitlesConcise.length);
            for (int i = 0; i < maxElementIndex; i++) 
            	titleLineBuilder.append(String.format(formatSpecifiersConcise[i], columnTitlesConcise[i]));
            output.write(titleLineBuilder.toString() + "\n");

            /* Write data rows */
            statement = DBConnection.connection.prepareStatement(ChemBase.getLastPerformedSQL());
            resultSet = statement.executeQuery();

			while (resultSet.next())
			{
				String tempID 			   = String.valueOf(resultSet.getInt(1));
				String tempName 		   = resultSet.getString(2);
				String tempBruttoFormula   = resultSet.getString(3);
				String tempMolarMass 	   = resultSet.getString(4);
				String tempQuantity 	   = resultSet.getString(5);
				String tempQuantityUnit    = resultSet.getString(6);
				String tempStorageLocation = resultSet.getString(7);
				
				if (tempName == null) 			 tempName = "";
				if (tempBruttoFormula == null) 	 tempBruttoFormula = "";
				if (tempMolarMass == null) 		 tempMolarMass = "";
				if (tempQuantity == null) 		 tempQuantity = "";
				if (tempQuantityUnit == null) 	 tempQuantityUnit = "";
				if (tempStorageLocation == null) tempStorageLocation = "";

	            final StringBuilder rowBuilder = new StringBuilder();
	            
	            rowBuilder.append(String.format(formatSpecifiersConcise[0], tempID));
	            rowBuilder.append(String.format(formatSpecifiersConcise[1], tempName));
	            rowBuilder.append(String.format(formatSpecifiersConcise[2], tempBruttoFormula));
	            rowBuilder.append(String.format(formatSpecifiersConcise[3], tempMolarMass));
	            rowBuilder.append(String.format(formatSpecifiersConcise[4], tempQuantity));
	            rowBuilder.append(String.format(formatSpecifiersConcise[5], tempQuantityUnit));
	            rowBuilder.append(String.format(formatSpecifiersConcise[6], tempStorageLocation));

	            output.write(rowBuilder.toString() + "\n");
			}
			output.flush();
		}
		catch (Exception ex)
		{
			STATUS = false;
		}
		finally
		{
			try
			{
				if (resultSet != null) resultSet.close();
				if (statement != null) statement.close();	
			}
			catch (Exception ex) {}

			try
			{
				if (output != null) output.close();
                if (!STATUS) 		outputPath.delete();
			}
			catch (Exception ex) {}
		}
		
		return STATUS;
	}

	/* --------------------------------------------------------------------------------------------- */
	
	public static boolean performFullExport(final File outputPath)
	{
		boolean STATUS = true;
        BufferedWriter output = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try
		{
			/* Open output file */
            output = new BufferedWriter(new FileWriter(outputPath));
			
			/* Write column titles */
            final StringBuilder titleLineBuilder = new StringBuilder();
            long maxElementIndex = (columnTitlesFull.length);
            for (int i = 0; i < maxElementIndex; i++) 
            	titleLineBuilder.append(String.format(formatSpecifiersFull[i], columnTitlesFull[i]));
            output.write(titleLineBuilder.toString() + "\n");

            /* Write data rows */			
            statement = DBConnection.connection.prepareStatement(ChemBase.getLastPerformedSQL());
			resultSet = statement.executeQuery();

			while (resultSet.next())
			{
				String tempID 			   = String.valueOf(resultSet.getInt(1));
				String tempName 		   = resultSet.getString(2);
				String tempBruttoFormula   = resultSet.getString(3);
				String tempMolarMass 	   = resultSet.getString(4);
				String tempQuantity 	   = resultSet.getString(5);
				String tempQuantityUnit    = resultSet.getString(6);
				String tempStorageLocation = resultSet.getString(7);
				String tempManufacturer    = resultSet.getString(8);
				String tempSupplier 	   = resultSet.getString(9);
				String tempDateOfEntry 	   = resultSet.getString(10);
				String tempAdditionalInfo  = resultSet.getString(11);
				
				if (tempName == null) 			 tempName = "";
				if (tempBruttoFormula == null) 	 tempBruttoFormula = "";
				if (tempMolarMass == null) 		 tempMolarMass = "";
				if (tempQuantity == null) 		 tempQuantity = "";
				if (tempQuantityUnit == null) 	 tempQuantityUnit = "";
				if (tempStorageLocation == null) tempStorageLocation = "";
				if (tempManufacturer == null) 	 tempManufacturer = "";
				if (tempSupplier == null) 		 tempSupplier = "";
				if (tempDateOfEntry == null) 	 tempDateOfEntry = "";
				if (tempAdditionalInfo == null)  tempAdditionalInfo = "";

	            final StringBuilder rowBuilder = new StringBuilder();
	            
	            rowBuilder.append(String.format(formatSpecifiersFull[0], tempID));
	            rowBuilder.append(String.format(formatSpecifiersFull[1], tempName));
	            rowBuilder.append(String.format(formatSpecifiersFull[2], tempBruttoFormula));
	            rowBuilder.append(String.format(formatSpecifiersFull[3], tempMolarMass));
	            rowBuilder.append(String.format(formatSpecifiersFull[4], tempQuantity));
	            rowBuilder.append(String.format(formatSpecifiersFull[5], tempQuantityUnit));
	            rowBuilder.append(String.format(formatSpecifiersFull[6], tempStorageLocation));
	            rowBuilder.append(String.format(formatSpecifiersFull[7], tempManufacturer));
	            rowBuilder.append(String.format(formatSpecifiersFull[8], tempSupplier));
	            rowBuilder.append(String.format(formatSpecifiersFull[9], tempDateOfEntry));
	            rowBuilder.append(String.format(formatSpecifiersFull[10], tempAdditionalInfo));

	            output.write(rowBuilder.toString() + "\n");
			}
			output.flush();
		}
		catch (Exception ex)
		{
			STATUS = false;
		}
		finally
		{
			try
			{
				if (resultSet != null) resultSet.close();
				if (statement != null) statement.close();	
			}
			catch (Exception ex) {}

			try
			{
				if (output != null) output.close();
				if (!STATUS) 		outputPath.delete();
			}
			catch (Exception ex) {}
		}
		
		return STATUS;
	}

	/* --------------------------------------------------------------------------------------------- */	
}
