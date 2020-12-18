package hr.chembase.desktop.exporters;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import hr.chembase.desktop.api.ChemBase;
import hr.chembase.desktop.db.DBConnection;

public class CSVExporter {

    private static final String[] columnTitlesConcise = {

            "ID",
            "Chemical Name",
            "Brutto Formula",
            "Molar Mass",
            "Quantity",
            "Unit",
            "Storage Location"
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
    
    /* ----------------------------------------------------------------------------------------- */

    public static boolean performConciseExport(final File outputPath)
    {
        boolean STATUS = true;
        BufferedWriter output = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try
        {
            /* Open output file */
            output = new BufferedWriter(new FileWriter(outputPath));
            
            /* Write column titles */
            final StringBuilder titleLineBuilder = new StringBuilder();
            long maxElementIndex = (columnTitlesConcise.length);

            int i;
            for (i = 0; i < (maxElementIndex-1); i++)
                titleLineBuilder.append(columnTitlesConcise[i] + ",");

            titleLineBuilder.append(columnTitlesConcise[i]);
            output.write(titleLineBuilder.toString() + "\n");
            
            
            /* Write data rows */           
            preparedStatement = DBConnection.connection.prepareStatement(ChemBase.getLastPerformedSQL());
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next())
            {
                String tempID              = String.valueOf(resultSet.getInt(1));
                String tempName            = resultSet.getString(2);
                String tempBruttoFormula   = resultSet.getString(3);
                String tempMolarMass       = resultSet.getString(4);
                String tempQuantity        = resultSet.getString(5);
                String tempQuantityUnit    = resultSet.getString(6);
                String tempStorageLocation = resultSet.getString(7);
                
                if (tempName == null)            tempName = "";
                if (tempBruttoFormula == null)   tempBruttoFormula = "";
                if (tempMolarMass == null)       tempMolarMass = "";
                if (tempQuantity == null)        tempQuantity = "";
                if (tempQuantityUnit == null)    tempQuantityUnit = "";
                if (tempStorageLocation == null) tempStorageLocation = "";

                final StringBuilder rowBuilder = new StringBuilder();

                rowBuilder.append(tempID + ",");
                rowBuilder.append(tempName + ",");
                rowBuilder.append(tempBruttoFormula + ",");
                rowBuilder.append(tempMolarMass + ",");
                rowBuilder.append(tempQuantity + ",");
                rowBuilder.append(tempQuantityUnit + ",");
                rowBuilder.append(tempStorageLocation);

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
                if (preparedStatement != null) preparedStatement.close();   
            }
            catch (Exception ex) {}             
            
            try
            {
                if (output != null) output.close();
                if (!STATUS)        outputPath.delete();
            }
            catch (Exception ex) {}
        }

        return STATUS;
    }
    
    /* ----------------------------------------------------------------------------------------- */

    public static boolean performFullExport(final File outputPath)
    {
        boolean STATUS = true;
        BufferedWriter output = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try
        {
            /* Open output file */
            output = new BufferedWriter(new FileWriter(outputPath));
            
            /* Write column titles */
            final StringBuilder titleLineBuilder = new StringBuilder();
            long maxElementIndex = (columnTitlesFull.length);

            int i;
            for (i = 0; i < (maxElementIndex-1); i++)
                titleLineBuilder.append(columnTitlesFull[i] + ",");

            titleLineBuilder.append(columnTitlesFull[i]);
            output.write(titleLineBuilder.toString() + "\n");
            
            
            /* Write data rows */           
            preparedStatement = DBConnection.connection.prepareStatement(ChemBase.getLastPerformedSQL());
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next())
            {
                String tempID              = String.valueOf(resultSet.getInt(1));
                String tempName            = resultSet.getString(2);
                String tempBruttoFormula   = resultSet.getString(3);
                String tempMolarMass       = resultSet.getString(4);
                String tempQuantity        = resultSet.getString(5);
                String tempQuantityUnit    = resultSet.getString(6);
                String tempStorageLocation = resultSet.getString(7);
                String tempManufacturer    = resultSet.getString(8);
                String tempSupplier        = resultSet.getString(9);
                String tempDateOfEntry     = resultSet.getString(10);
                String tempAdditionalInfo  = resultSet.getString(11);
                
                if (tempName == null)            tempName = "";
                if (tempBruttoFormula == null)   tempBruttoFormula = "";
                if (tempMolarMass == null)       tempMolarMass = "";
                if (tempQuantity == null)        tempQuantity = "";
                if (tempQuantityUnit == null)    tempQuantityUnit = "";
                if (tempStorageLocation == null) tempStorageLocation = "";
                if (tempManufacturer == null)    tempManufacturer = "";
                if (tempSupplier == null)        tempSupplier = "";
                if (tempDateOfEntry == null)     tempDateOfEntry = "";
                if (tempAdditionalInfo == null)  tempAdditionalInfo = "";

                final StringBuilder rowBuilder = new StringBuilder();

                rowBuilder.append(tempID + ",");
                rowBuilder.append(tempName + ",");
                rowBuilder.append(tempBruttoFormula + ",");
                rowBuilder.append(tempMolarMass + ",");
                rowBuilder.append(tempQuantity + ",");
                rowBuilder.append(tempQuantityUnit + ",");
                rowBuilder.append(tempStorageLocation + ",");
                rowBuilder.append(tempManufacturer + ",");
                rowBuilder.append(tempSupplier + ",");
                rowBuilder.append(tempDateOfEntry + ",");
                rowBuilder.append(tempAdditionalInfo);

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
                if (preparedStatement != null) preparedStatement.close();   
            }
            catch (Exception ex) {}
              
            try
            {
                if (output != null) output.close();
                if (!STATUS)        outputPath.delete();
            }
            catch (Exception ex) {}
        }

        return STATUS;
    }

    /* ----------------------------------------------------------------------------------------- */

}
