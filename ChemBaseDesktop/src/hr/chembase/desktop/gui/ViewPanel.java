package hr.chembase.desktop.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import hr.chembase.desktop.api.ChemBase;

public class ViewPanel extends JDialog {

    private JLabel nameLabel                = new JLabel("Name: ");
    private JLabel bruttoFormulaLabel       = new JLabel("Brutto formula: ");
    private JLabel molarMassLabel           = new JLabel("Molar mass: ");
    private JLabel quantityLabel            = new JLabel("Quantity: ");
    private JLabel storageLocationLabel     = new JLabel("Storage location: ");
    private JLabel manufacturerLabel        = new JLabel("Manufacturer: ");
    private JLabel supplierLabel            = new JLabel("Supplier: ");
    private JLabel additionalInfoLabel      = new JLabel("Additional info: ");
    private JLabel dateOfEntryLabel         = new JLabel("Date:");

    private JTextField nameField            = new JTextField(30);
    private JTextField bruttoFormulaField   = new JTextField(30);
    private JTextField molarMassField       = new JTextField(30);
    private JTextField quantityField        = new JTextField(30);
    private JTextField storageLocationField = new JTextField(30);
    private JTextField manufacturerField    = new JTextField(30);
    private JTextField supplierField        = new JTextField(30);
    private JTextField dateOfEntryField     = new JTextField(30);
    private JTextArea additionalInfoArea    = new JTextArea(10, 50);


    public ViewPanel(String name,
                     String bruttoFormula,
                     String molarMass,
                     String quantity,
                     String storageLocation,
                     String manufacturer,
                     String supplier,
                     String dateOfEntry,
                     String additionalInfo)
    {

        nameField.setText(name);
        bruttoFormulaField.setText(bruttoFormula);
        molarMassField.setText(molarMass);
        quantityField.setText(quantity);
        storageLocationField.setText(storageLocation);
        manufacturerField.setText(manufacturer);
        supplierField.setText(supplier);
        dateOfEntryField.setText(dateOfEntry);
        additionalInfoArea.setText(additionalInfo);

        nameField.setEditable(false);
        bruttoFormulaField.setEditable(false);
        molarMassField.setEditable(false);
        quantityField.setEditable(false);
        storageLocationField.setEditable(false);
        manufacturerField.setEditable(false);
        supplierField.setEditable(false);
        dateOfEntryField.setEditable(false);
        additionalInfoArea.setEditable(false);



        JPanel gridPanel = new JPanel(new GridLayout(10, 2, 0, 5));
        gridPanel.add(nameLabel);
        gridPanel.add(nameField);
        gridPanel.add(bruttoFormulaLabel);
        gridPanel.add(bruttoFormulaField);
        gridPanel.add(molarMassLabel);
        gridPanel.add(molarMassField);
        gridPanel.add(quantityLabel);
        gridPanel.add(quantityField);
        gridPanel.add(storageLocationLabel);
        gridPanel.add(storageLocationField);
        gridPanel.add(manufacturerLabel);
        gridPanel.add(manufacturerField);
        gridPanel.add(supplierLabel);
        gridPanel.add(supplierField);
        gridPanel.add(dateOfEntryLabel);
        gridPanel.add(dateOfEntryField);
        gridPanel.add(additionalInfoLabel);

        JScrollPane infoPane = new JScrollPane(additionalInfoArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        JPanel rootPane = new JPanel(new BorderLayout());
        rootPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        rootPane.add(gridPanel, BorderLayout.NORTH);
        rootPane.add(infoPane, BorderLayout.CENTER);

        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setTitle("Chemicals Database");
        this.setContentPane(rootPane);
        this.setSize(550, 550);
        this.setLocationRelativeTo(ChemBase.getChemBaseRoot());
        this.setModal(true);
        this.setVisible(true);
    }
}
