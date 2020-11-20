package hr.chembase.desktop.exporters;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;

import hr.chembase.desktop.api.ChemBase;
import hr.chembase.desktop.db.DBConnection;

public class XLSXExporter {

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
        FileOutputStream output = null;
        SXSSFWorkbook workbook = null;

        PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
        
        try
        {
            /* keep 100 rows in memory,
             * exceeding rows will be
             * flushed to disk
             */
            workbook = new SXSSFWorkbook(100);  
            
            
            /* Creating cell style for title columns */
            XSSFFont titlesFont = (XSSFFont) workbook.createFont();
            titlesFont.setBold(true);

            XSSFCellStyle titlesStyle = (XSSFCellStyle) workbook.createCellStyle();
            titlesStyle.setFont(titlesFont);
            titlesStyle.setAlignment(HorizontalAlignment.LEFT);
            titlesStyle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
            titlesStyle.setFillBackgroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
            titlesStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            titlesStyle.setBorderTop(BorderStyle.THIN);
            titlesStyle.setBorderBottom(BorderStyle.THIN);
            titlesStyle.setBorderLeft(BorderStyle.THIN);
            titlesStyle.setBorderRight(BorderStyle.THIN);
//         	titlesStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            titlesStyle.setWrapText(true);

            /* Creating cell style for data columns */
            XSSFCellStyle dataStyle = (XSSFCellStyle) workbook.createCellStyle();
            dataStyle.setAlignment(HorizontalAlignment.LEFT); 
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);
            
            /* _____________________________________________ */
        	
            final SXSSFSheet sheet = workbook.createSheet("ChemBase");
            int rowNumber = 0;

            
            /* Setting title columns */
            /* --------------------- */
            final Row sheetTitleRow = sheet.createRow(rowNumber);
            rowNumber++;
            for (int i = 0; i < columnTitlesConcise.length; i++)
            {
                final Cell cell = sheetTitleRow.createCell(i);
                cell.setCellStyle(titlesStyle);
                cell.setCellValue(columnTitlesConcise[i]);
            }

            
            /* Write data rows */			
			/* --------------- */
            preparedStatement = DBConnection.connection.prepareStatement(ChemBase.getLastPerformedSQL());
			resultSet = preparedStatement.executeQuery();

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
				
                final Row currentRow = sheet.createRow(rowNumber);
                rowNumber++;
                
                final Cell idCell = currentRow.createCell(0);
                final Cell nameCell = currentRow.createCell(1);
                final Cell bruttoFormulaCell = currentRow.createCell(2);
                final Cell molarMassCell = currentRow.createCell(3);
                final Cell quantityCell = currentRow.createCell(4);
                final Cell quantityUnitCell = currentRow.createCell(5);
                final Cell storageLocationCell = currentRow.createCell(6);

                idCell.setCellStyle(dataStyle);
                nameCell.setCellStyle(dataStyle);
                bruttoFormulaCell.setCellStyle(dataStyle);
                molarMassCell.setCellStyle(dataStyle);
                quantityCell.setCellStyle(dataStyle);
                quantityUnitCell.setCellStyle(dataStyle);
                storageLocationCell.setCellStyle(dataStyle);

                idCell.setCellValue(tempID);
                nameCell.setCellValue(tempName);
                bruttoFormulaCell.setCellValue(tempBruttoFormula);
                molarMassCell.setCellValue(tempMolarMass);
                quantityCell.setCellValue(tempQuantity);
                quantityUnitCell.setCellValue(tempQuantityUnit);
                storageLocationCell.setCellValue(tempStorageLocation);
			}
            
            /* Resizing columns */
            sheet.setColumnWidth(0, 20*256);
            sheet.setColumnWidth(1, 35*256);
            sheet.setColumnWidth(2, 25*256);
            sheet.setColumnWidth(3, 20*256);
            sheet.setColumnWidth(4, 20*256);
            sheet.setColumnWidth(5, 10*256);            
            sheet.setColumnWidth(6, 50*256);

            /* Outputing data to file */
            output = new FileOutputStream(outputPath);
            workbook.write(output);
        }
        catch (Exception ex)
        {
        	STATUS = false;
        }
        finally
        {        	
            try { if (resultSet != null) resultSet.close(); }
            catch (Exception ex) {}
          
            try { if (preparedStatement != null) preparedStatement.close(); }
            catch (Exception ex) {}
            
            try { if (output != null) output.close(); }
            catch (Exception ex) {}

            try { if (workbook != null) { workbook.dispose(); workbook.close(); }}
            catch (Exception ex) {}
            
            if (!STATUS)
            {
            	try { outputPath.delete(); } catch (Exception ex) {}
            }
        }

        return STATUS;
	}

	/* ----------------------------------------------------------------------------------------- */
	
	public static boolean performFullExport(final File outputPath)
	{
        boolean STATUS = true;
        FileOutputStream output = null;
        SXSSFWorkbook workbook = null;

        PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
        
        try
        {
            /* keep 100 rows in memory,
             * exceeding rows will be
             * flushed to disk
             */
            workbook = new SXSSFWorkbook(100);

            
            /* Creating cell style for title columns */
            XSSFFont titlesFont = (XSSFFont) workbook.createFont();
            titlesFont.setBold(true);

            XSSFCellStyle titlesStyle = (XSSFCellStyle) workbook.createCellStyle();
            titlesStyle.setFont(titlesFont);
            titlesStyle.setAlignment(HorizontalAlignment.LEFT);
            titlesStyle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
            titlesStyle.setFillBackgroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
            titlesStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            titlesStyle.setBorderTop(BorderStyle.THIN);
            titlesStyle.setBorderBottom(BorderStyle.THIN);
            titlesStyle.setBorderLeft(BorderStyle.THIN);
            titlesStyle.setBorderRight(BorderStyle.THIN);
//         	titlesStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            titlesStyle.setWrapText(true);

            /* Creating cell style for data columns */
            XSSFCellStyle dataStyle = (XSSFCellStyle) workbook.createCellStyle();
            dataStyle.setAlignment(HorizontalAlignment.LEFT); 
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);
            
            /* _____________________________________________ */
        	
            final SXSSFSheet sheet = workbook.createSheet("ChemBase");
            int rowNumber = 0;

            
            /* Setting title columns */
            /* --------------------- */
            final Row sheetTitleRow = sheet.createRow(rowNumber);
            rowNumber++;
            for (int i = 0; i < columnTitlesFull.length; i++)
            {
                final Cell cell = sheetTitleRow.createCell(i);
                cell.setCellStyle(titlesStyle);
                cell.setCellValue(columnTitlesFull[i]);
            }


            /* Write data rows */			
			/* --------------- */
            preparedStatement = DBConnection.connection.prepareStatement(ChemBase.getLastPerformedSQL());
			resultSet = preparedStatement.executeQuery();

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
				
                final Row currentRow = sheet.createRow(rowNumber);
                rowNumber++;

                final Cell idCell = currentRow.createCell(0);
                final Cell nameCell = currentRow.createCell(1);
                final Cell bruttoFormulaCell = currentRow.createCell(2);
                final Cell molarMassCell = currentRow.createCell(3);
                final Cell quantityCell = currentRow.createCell(4);
                final Cell quantityUnitCell = currentRow.createCell(5);
                final Cell storageLocationCell = currentRow.createCell(6);
                final Cell manufacturerCell = currentRow.createCell(7);
                final Cell supplierCell = currentRow.createCell(8);
                final Cell dateOfEntryCell = currentRow.createCell(9);
                final Cell additionalInfoCell = currentRow.createCell(10);

                idCell.setCellStyle(dataStyle);
                nameCell.setCellStyle(dataStyle);
                bruttoFormulaCell.setCellStyle(dataStyle);
                molarMassCell.setCellStyle(dataStyle);
                quantityCell.setCellStyle(dataStyle);
                quantityUnitCell.setCellStyle(dataStyle);
                storageLocationCell.setCellStyle(dataStyle);
                manufacturerCell.setCellStyle(dataStyle);
                supplierCell.setCellStyle(dataStyle);
                dateOfEntryCell.setCellStyle(dataStyle);
                additionalInfoCell.setCellStyle(dataStyle);

                idCell.setCellValue(tempID);
                nameCell.setCellValue(tempName);
                bruttoFormulaCell.setCellValue(tempBruttoFormula);
                molarMassCell.setCellValue(tempMolarMass);
                quantityCell.setCellValue(tempQuantity);
                quantityUnitCell.setCellValue(tempQuantityUnit);
                storageLocationCell.setCellValue(tempStorageLocation);
                manufacturerCell.setCellValue(tempManufacturer);
                supplierCell.setCellValue(tempSupplier);
                dateOfEntryCell.setCellValue(tempDateOfEntry);
                additionalInfoCell.setCellValue(tempAdditionalInfo);
			}
            
            /* Resizing columns */
            sheet.setColumnWidth(0, 20*256);
            sheet.setColumnWidth(1, 35*256);
            sheet.setColumnWidth(2, 25*256);
            sheet.setColumnWidth(3, 20*256);
            sheet.setColumnWidth(4, 20*256);
            sheet.setColumnWidth(5, 10*256);            
            sheet.setColumnWidth(6, 50*256);
            sheet.setColumnWidth(7, 35*256);
            sheet.setColumnWidth(8, 35*256);
            sheet.setColumnWidth(9, 20*256);
            sheet.setColumnWidth(10, 50*256);

            /* Outputing data to file */
            output = new FileOutputStream(outputPath);
            workbook.write(output);
        }
        catch (Exception ex)
        {
        	STATUS = false;
        }
        finally
        {        	
            try { if (resultSet != null) resultSet.close(); }
            catch (Exception ex) {}
          
            try { if (preparedStatement != null) preparedStatement.close(); }
            catch (Exception ex) {}
            
            try { if (output != null) output.close(); }
            catch (Exception ex) {}

            try { if (workbook != null) { workbook.dispose(); workbook.close(); }}
            catch (Exception ex) {}
            
            if (!STATUS)
            {
            	try { outputPath.delete(); } catch (Exception ex) {}
            }
        }

        return STATUS;
	}
	
	/* ----------------------------------------------------------------------------------------- */

}
