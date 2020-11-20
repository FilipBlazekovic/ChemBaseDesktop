package hr.chembase.desktop.exporters;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import hr.chembase.desktop.api.ChemBase;
import hr.chembase.desktop.db.DBConnection;

public class PDFExporter {

    private static final int fontSize = 8;
    private static final int rowsPerPage = 32;
	
	/* ----------------------------------------------------------------------------------------- */
	
	private static final String[] columnTitlesConcise = {

            "ID",
            "Chemical Name",
            "Brutto Formula",
            "Molar Mass",
            "Quantity",
            "Unit",
            "Storage Location",
	};

	private static final int[] fieldWidthsConcise = {
			
			70, 		// ID
			150, 		// Chemical Name
			150, 		// Brutto Formula
			75, 		// Molar Mass
			85, 		// Quantity
			75, 		// Unit
			140 		// Storage Location
	};
	
	/* ----------------------------------------------------------------------------------------- */
	
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

	private static final int[] fieldWidthsFull = {
			
			70, 		// ID
			150, 		// Chemical Name
			140, 		// Brutto Formula
			75, 		// Molar Mass
			85, 		// Quantity
			75, 		// Unit
			150, 		// Storage Location
			65, 		// Manufacturer
			60, 		// Supplier
			60, 		// Date of Entry
			130 		// Additional Info
	};

	/* ----------------------------------------------------------------------------------------- */

	public static boolean performConciseExport(final File outputPath)
	{
		boolean STATUS = true;
		PDDocument document = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try
		{
			document = new PDDocument();

			statement = DBConnection.connection.prepareStatement(ChemBase.getLastPerformedSQL());
			resultSet = statement.executeQuery();
			
			int i = 0;			
			List<String[]> singlePageData = new LinkedList<String[]>();
			
			while (resultSet.next())
			{
				i++;

				String tempID 			   				= String.valueOf(resultSet.getInt(1));
				String tempName 		   				= resultSet.getString(2);
				String tempBruttoFormula   				= resultSet.getString(3);
				String tempMolarMass 	   				= resultSet.getString(4);
				String tempQuantity 	   				= resultSet.getString(5);
				String tempQuantityUnit    				= resultSet.getString(6);
				String tempStorageLocation 				= resultSet.getString(7);
				
				if (tempName == null) 			 		tempName = "";
				if (tempBruttoFormula == null) 	 		tempBruttoFormula = "";
				if (tempMolarMass == null) 		 		tempMolarMass = "";
				if (tempQuantity == null) 		 		tempQuantity = "";
				if (tempQuantityUnit == null)	 		tempQuantityUnit = "";
				if (tempStorageLocation == null) 		tempStorageLocation = "";

				if (tempName.length() > 35) 		 	tempName = (tempName.substring(0, 35) + "...");
				if (tempBruttoFormula.length() > 30) 	tempBruttoFormula = (tempBruttoFormula.substring(0, 30) + "...");
				if (tempQuantity.length() > 20) 		tempQuantity = (tempQuantity.substring(0,20) + "...");
				if (tempStorageLocation.length() > 35) 	tempStorageLocation = (tempStorageLocation.substring(0,35) + "...");
				
				singlePageData.add(new String[] {
						
						tempID,
						tempName,
						tempBruttoFormula,
						tempMolarMass,
						tempQuantity,
						tempQuantityUnit,
						tempStorageLocation
				});

				if (i == rowsPerPage)
				{
					generatePDFPageLandscape(document, null, columnTitlesConcise, singlePageData, fieldWidthsConcise, fontSize, rowsPerPage);
					singlePageData = new LinkedList<String[]>();
					i = 0;
				}
			}

			generatePDFPageLandscape(document, null, columnTitlesConcise, singlePageData, fieldWidthsConcise, fontSize, rowsPerPage);
			document.save(outputPath);
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
				if (document != null) document.close();
                if (!STATUS) 		  outputPath.delete();
			}
			catch (Exception ex) {}
		}

		return STATUS;
	}
    
	/* ----------------------------------------------------------------------------------------- */
	
	public static boolean performFullExport(final File outputPath)
	{
		boolean STATUS = true;
		PDDocument document = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try
		{
			document = new PDDocument();

			statement = DBConnection.connection.prepareStatement(ChemBase.getLastPerformedSQL());
			resultSet = statement.executeQuery();
			
			int i = 0;			
			List<String[]> singlePageData = new LinkedList<String[]>();
			
			while (resultSet.next())
			{
				i++;

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
				if (tempQuantityUnit == null)	 tempQuantityUnit = "";
				if (tempStorageLocation == null) tempStorageLocation = "";
				if (tempManufacturer == null) 	 tempManufacturer = "";
				if (tempSupplier == null) 		 tempSupplier = "";
				if (tempDateOfEntry == null) 	 tempDateOfEntry = "";
				if (tempAdditionalInfo == null)  tempAdditionalInfo = "";
				
				singlePageData.add(new String[] {
						
						tempID,
						tempName,
						tempBruttoFormula,
						tempMolarMass,
						tempQuantity,
						tempQuantityUnit,
						tempStorageLocation,
						tempManufacturer,
						tempSupplier,
						tempDateOfEntry,
						tempAdditionalInfo
				});

				if (i == rowsPerPage)
				{
					generatePDFPageLandscape(document, null, columnTitlesFull, singlePageData, fieldWidthsFull, fontSize, rowsPerPage);
					singlePageData = new LinkedList<String[]>();
					i = 0;
				}
			}

			generatePDFPageLandscape(document, null, columnTitlesFull, singlePageData, fieldWidthsFull, fontSize, rowsPerPage);
			document.save(outputPath);
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
				if (document != null) document.close();
                if (!STATUS) 		  outputPath.delete();
			}
			catch (Exception ex) {}
		}

		return STATUS;
	}

	/* ----------------------------------------------------------------------------------------- */

    private static void generatePDFPageLandscape(PDDocument document,
    											 String[] headerText,
    											 String[] columnTitles,
    											 List<String[]> data,
    											 int[] fieldWidths,
    											 final int fontSize,
    											 final int rowsPerPage) throws IOException
    {
    	PDPage page = new PDPage(new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth()));
    	document.addPage(page);
    	PDPageContentStream contents = new PDPageContentStream(document, page);

    	PDFont font = PDType1Font.TIMES_ROMAN;
    	PDFont fontBold = PDType1Font.TIMES_BOLD;

    	int currentXPosition = 30;
    	int currentYPosition = 550;

    	/* Header */
    	if (headerText != null)
    	{
    		contents.beginText();
    		contents.setFont(fontBold, fontSize);
    		contents.newLineAtOffset(currentXPosition, currentYPosition);

    		int totalYOffset = 0;
    		for (int i = 0; i < headerText.length; i++)
    		{
    			contents.showText(headerText[i]);
    			totalYOffset += (2 * fontSize);
    			contents.newLineAtOffset(0, - (float)(2 * fontSize)); 
    		}
    		currentYPosition -= totalYOffset;
    		currentYPosition -= (2 * fontSize);
    		contents.endText();
    	}

    	/* Titles */
    	contents.beginText();
    	contents.setFont(fontBold, fontSize);
    	contents.newLineAtOffset(currentXPosition, currentYPosition);

    	int totalXOffset = 0;
    	for (int i = 0; i < columnTitles.length; i++)
    	{
    		contents.showText(columnTitles[i]);
    		contents.newLineAtOffset(fieldWidths[i], 0);
    		totalXOffset += fieldWidths[i];
    	}
    	contents.endText();

    	/* Data */
    	currentYPosition = currentYPosition - (int)(2.5 * fontSize);
    	contents.beginText();
    	contents.setFont(font, fontSize);
    	contents.newLineAtOffset(30, currentYPosition);

    	Iterator iterator = data.iterator();
    	while (iterator.hasNext())
    	{
    		final String[] currentLine = (String[]) iterator.next();
    		for (int i = 0; i < currentLine.length; i++)
    		{
    			contents.showText(currentLine[i]);
    			contents.newLineAtOffset(fieldWidths[i], 0);
    		}
    		contents.newLineAtOffset(-totalXOffset, - (float)(2 * fontSize));
    	}

    	contents.endText();
    	contents.close();
    }
}
