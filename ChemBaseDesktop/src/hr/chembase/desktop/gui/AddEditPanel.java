package hr.chembase.desktop.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import hr.chembase.desktop.api.ChemBase;
import hr.chembase.desktop.db.DBConnection;
import hr.chembase.desktop.db.SQLStatements;

public class AddEditPanel extends JDialog {

	private final String[] unitsString = { "g","kg","ml","l","cm3","dm3", "mol", "mmol" };

	private JLabel nameLabel 						= new JLabel("Name: ");
	private JLabel bruttoFormulaLabel 				= new JLabel("Brutto formula: ");
	private JLabel molarMassLabel 					= new JLabel("Molar mass: ");
	private JLabel quantityLabel 					= new JLabel("Quantity: ");
	private JLabel storageLocationLabel 			= new JLabel("Storage location: ");
	private JLabel manufacturerLabel 				= new JLabel("Manufacturer: ");
	private JLabel supplierLabel 					= new JLabel("Supplier: ");
	private JLabel additionalInfoLabel 				= new JLabel("Additional info: ");
	private JLabel dateOfEntryLabel 				= new JLabel("Date:");

	private JTextField nameField 					= new JTextField(30);
	private JTextField bruttoFormulaField 			= new JTextField(30);
	private JTextField molarMassField 				= new JTextField(30);
	private JTextField quantityAmountField 			= new JTextField(30);
	private JComboBox<String> quantityUnitField 	= new JComboBox<String>(unitsString);
	private JComboBox<String> storageLocationField 	= null;	
	private JTextField manufacturerField 			= new JTextField(30);
	private JTextField supplierField 				= new JTextField(30);
	private JTextField dateOfEntryField 			= new JTextField(30);
	private JTextArea additionalInfoArea 			= new JTextArea(10, 50);

	private JButton addEditButton 					= null;
	private JButton cancelButton 					= null;
	
	private AddEditPanel referenceToThis;

	/* -------------------------------------------------------------------------------- */

	public AddEditPanel(Integer pKey,
						String name,
						String bruttoFormula,
						String molarMass,
						String quantityAmount,
						String quantityUnit,
						String storageLocation,
						String manufacturer,
						String supplier,
						String dateOfEntry,
						String additionalInfo)
	
	{
		referenceToThis = this;
		
		
		// Retrieving list of possible locations from the database
		// -------------------------------------------------------
		List<String> locations = new LinkedList<String>();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try
		{
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

		/* ---------------------------------------------------------------------------- */

		if (locations.isEmpty())
		{
			new PopupMessage("Locations need to be set-up first in the Settings toolbar before new records can be inserted into the database!");

			referenceToThis.setVisible(false);
			referenceToThis.dispose();
		}
		
		/* ---------------------------------------------------------------------------- */
		
		else
		{
			LocalDate date = LocalDate.now();
		
			storageLocationField = new JComboBox<String>((String[]) locations.toArray(new String[0]));	
			nameField.setEditable(true);
			bruttoFormulaField.setEditable(true);
			molarMassField.setEditable(true);
			quantityAmountField.setEditable(true);
			storageLocationField.setEditable(true);
			manufacturerField.setEditable(true);
			supplierField.setEditable(true);
			dateOfEntryField.setEditable(true);
			additionalInfoArea.setEditable(true);

			if (pKey != null)
			{
				nameField.setText(name);
				bruttoFormulaField.setText(bruttoFormula);
				molarMassField.setText(molarMass);
				quantityAmountField.setText(quantityAmount);
				quantityUnitField.setSelectedItem(quantityUnit);
				storageLocationField.setSelectedItem(storageLocation);
				manufacturerField.setText(manufacturer);
				supplierField.setText(supplier);
				dateOfEntryField.setText(dateOfEntry);
				additionalInfoArea.setText(additionalInfo);

				addEditButton = new JButton("Edit Record");
				cancelButton = new JButton("Cancel");
			}
			else
			{
				nameField.setText("");
				bruttoFormulaField.setText("");
				molarMassField.setText("");
				quantityAmountField.setText("");
				quantityUnitField.setSelectedIndex(-1);
				storageLocationField.setSelectedIndex(-1);
				manufacturerField.setText("");
				supplierField.setText("");
				dateOfEntryField.setText(date.toString().replaceAll("-", "/"));
				additionalInfoArea.setText("");

				addEditButton = new JButton("Add Record");
				cancelButton = new JButton("Cancel");
			}

			storageLocationField.setEditable(false);
			dateOfEntryField.setEditable(false);

			JPanel gridPanel = new JPanel(new GridLayout(11, 2, 0, 5));
			gridPanel.add(nameLabel);
			gridPanel.add(nameField);
			gridPanel.add(bruttoFormulaLabel);
			gridPanel.add(bruttoFormulaField);
			gridPanel.add(molarMassLabel);
			gridPanel.add(molarMassField);
			gridPanel.add(quantityLabel);
			gridPanel.add(quantityAmountField);		
			gridPanel.add(new JLabel());
			gridPanel.add(quantityUnitField);
			gridPanel.add(storageLocationLabel);
			gridPanel.add(storageLocationField);
			gridPanel.add(manufacturerLabel);
			gridPanel.add(manufacturerField);
			gridPanel.add(supplierLabel);
			gridPanel.add(supplierField);
			gridPanel.add(dateOfEntryLabel);
			gridPanel.add(dateOfEntryField);
			gridPanel.add(additionalInfoLabel);

			/* ------------------------------------------------------------------------ */

			// Register listeners
			// ------------------
			cancelButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					referenceToThis.setVisible(false);
					referenceToThis.dispose();
				}
			});

			addEditButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					String chemicalName 	= nameField.getText().trim();
					String bruttoFormula 	= bruttoFormulaField.getText().trim().toUpperCase();
					String molarMass 		= molarMassField.getText().trim().replaceAll(",", ".");
					String quantityAmount 	= quantityAmountField.getText().trim().replaceAll(",", ".");
					String manufacturer 	= manufacturerField.getText().trim();
					String supplier 		= supplierField.getText().trim();
					String dateOfEntry 		= dateOfEntryField.getText().trim();
					String additionalInfo 	= additionalInfoArea.getText().trim();
					String quantityUnit 	= (String) quantityUnitField.getSelectedItem();
					String storageLocation 	= (String) storageLocationField.getSelectedItem();
					
					// Validation
					// ----------
					if (chemicalName.length() == 0)
						new PopupMessage("Chemical name must be specified!");		

					else if (quantityAmount.length() == 0)
						new PopupMessage("Quantity must be specified!");

					else if (quantityAmount.length() > 0 && (!quantityAmount.matches("\\d+(\\.\\d{1,8})?")))
						new PopupMessage("Invalid quantity format! Valid format examples: 500, 10.12, 10.1");
					
					else if (quantityUnit == null)
						new PopupMessage("Quantity unit must be selected!");

					else if (!Arrays.asList(unitsString).contains(quantityUnit))
						new PopupMessage("Invalid quantity unit!");
	
					else if (storageLocation == null)
						new PopupMessage("Storage location must be selected!");

					else if (!locations.contains(storageLocation))
						new PopupMessage("Invalid storage location!");
					
					else if (bruttoFormula.length() == 0)
						new PopupMessage("Brutto formula must be specified!");
					
					else if (molarMass.length() == 0)
						new PopupMessage("Molar mass must be specified!");

					else if (bruttoFormula.length() > 0 && (!bruttoFormula.matches("[A-Z0-9]+")))
						new PopupMessage("Invalid brutto formula format! Valid characters are: A-Z, 0-9");

					else if (molarMass != null && molarMass.length() > 0 && (!molarMass.matches("\\d+(\\.\\d{1,8})?")))
						new PopupMessage("Invalid molar mass format! Valid format examples: 64.04, 10, 10.1");
					
					else
					{
						int selectedOption = JOptionPane.showConfirmDialog(ChemBase.getChemBaseRoot(), 
																		   "Are you sure you want to insert the specified record?",
																		   "Chemicals Database",
																		   JOptionPane.YES_NO_OPTION);
						if (selectedOption == JOptionPane.YES_OPTION)
						{
							PreparedStatement statement = null;
							boolean status = true;
							try
							{
								// Edit operation
								// --------------
								if (pKey != null)
								{
									statement = DBConnection.connection.prepareStatement(SQLStatements.EDIT_RECORD_SQL);
									statement.setString(1, chemicalName);
									statement.setString(2, bruttoFormula);
									statement.setString(3, molarMass);
									statement.setDouble(4, Double.valueOf(quantityAmount));
									statement.setString(5, quantityUnit);
									statement.setString(6, storageLocation);
									statement.setString(7, manufacturer);
									statement.setString(8, supplier);
									statement.setString(9, dateOfEntry);
									statement.setString(10, additionalInfo);
									statement.setInt(11, pKey);
									statement.execute();
								}

								// Add operation
								// -------------
								else
								{
									statement = DBConnection.connection.prepareStatement(SQLStatements.ADD_RECORD_SQL);
									statement.setString(1, chemicalName);
									statement.setString(2, bruttoFormula);
									statement.setString(3, molarMass);
									statement.setDouble(4, Double.valueOf(quantityAmount));
									statement.setString(5, quantityUnit);
									statement.setString(6, storageLocation);
									statement.setString(7, manufacturer);
									statement.setString(8, supplier);
									statement.setString(9, dateOfEntry);
									statement.setString(10, additionalInfo);
									statement.execute();
								}							
							}
							catch (Exception ex) { status = false; }
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

							referenceToThis.setVisible(false);
							referenceToThis.dispose();
						}
					}
				}
			});

			/* ---------------------------------------------------------------------------- */
	
			JScrollPane infoPane = new JScrollPane(additionalInfoArea,
												   JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
												   JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

			JPanel buttonsPanel = new JPanel(new GridLayout(1,2));
			buttonsPanel.setBorder(new EmptyBorder(15,0,0,0));
			buttonsPanel.add(addEditButton);
			buttonsPanel.add(cancelButton);
		
			final JPanel rootPane = new JPanel(new BorderLayout(0,0));
			rootPane.setBorder(new EmptyBorder(10, 10, 10, 10));
			rootPane.add(gridPanel, BorderLayout.NORTH);
			rootPane.add(infoPane, BorderLayout.CENTER);
			rootPane.add(buttonsPanel, BorderLayout.SOUTH);

			this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			this.setTitle("Chemicals Database");
			this.setContentPane(rootPane);
			this.setSize(550, 600);
			this.setLocationRelativeTo(ChemBase.getChemBaseRoot());
			this.setModal(true);
			this.setVisible(true);
		}
	}
}
