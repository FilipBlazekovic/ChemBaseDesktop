package hr.chembase.desktop.api;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import hr.chembase.desktop.db.DBConnection;
import hr.chembase.desktop.db.SQLStatements;
import hr.chembase.desktop.gui.PopupMessage;
import hr.chembase.desktop.listeners.AddEntryListener;
import hr.chembase.desktop.listeners.EditEntryListener;
import hr.chembase.desktop.listeners.RemoveEntryListener;
import hr.chembase.desktop.listeners.SearchListener;
import hr.chembase.desktop.listeners.ViewEntryListener;
import hr.chembase.desktop.menuListeners.AboutListener;
import hr.chembase.desktop.menuListeners.ClearDatabaseListener;
import hr.chembase.desktop.menuListeners.ExitListener;
import hr.chembase.desktop.menuListeners.ExportAsCSVListener;
import hr.chembase.desktop.menuListeners.ExportAsPDFListener;
import hr.chembase.desktop.menuListeners.ExportAsTXTListener;
import hr.chembase.desktop.menuListeners.ExportAsXLSXListener;
import hr.chembase.desktop.menuListeners.SetupLocationsListener;

/* _________________________________________________________________________________________________ */

public class ChemBase extends JFrame {

    private static Insets margin        = new Insets(5, 0, 5, 0);
    private static Insets marginLow     = new Insets(5, 0, 10, 0);
    private static Insets marginHigh    = new Insets(10, 0, 5, 0);

    private static boolean REGULAR_EXPRESSIONS_ON = true;
    private static boolean FULL_EXPORT_ON         = true;

    private static String[] tableColumnNames = {

            "ID",
            "Chemical Name",
            "Brutto Formula",
            "Molar Mass",
            "Quantity",
            "Unit",
            "Storage Location"
    };

    private static JTable table;
    private static DefaultTableModel tableModel = new DefaultTableModel(null, tableColumnNames);

    private static ChemBase chemBase;

    private static JTextField searchField;
    private static JButton searchButton;

    private static JRadioButton nameRadioButton;
    private static JRadioButton bruttoFormulaRadioButton;
    private static JRadioButton molarMassRadioButton;
    private static JRadioButton storageLocationRadioButton;
    private static JRadioButton manufacturerRadioButton;
    private static JRadioButton supplierRadioButton;
    
    private static String lastPerformedSQL = SQLStatements.INITIAL_SQL;

    /* _____________________________________________________________________________________________ */
    /* _____________________________________________________________________________________________ */
    /*                                                                                               */
    /*                                         MAIN METHOD                                           */
    /* _____________________________________________________________________________________________ */
    /* _____________________________________________________________________________________________ */

    public static void main(String[] args)
    {

        // Read database path from args
        // ----------------------------
        String databasePath = "chembase.db";
        if (args.length >= 1)
            databasePath = args[0];


        // Checking if database file exists, if not create a database and construct it's tables
        // ------------------------------------------------------------------------------------
        boolean constructTables = true;
        if (new File(databasePath).exists() && new File(databasePath).isFile())
            constructTables = false;

        
        // Establish database connection
        // -----------------------------
        boolean status = DBConnection.setupConnection(databasePath);
        if (status == false)
        {
            System.err.println("[ERROR] Could not establish a database connection!");
            System.exit(1); 
        }


        // Construct tables if necessary
        // -----------------------------
        if (constructTables)
        {
            status = DBConnection.constructTables();
            if (status == false)
            {
                System.err.println("[ERROR] Could not construct database tables!");
                System.exit(1); 
            }
        }


        // Setting the Nimbus Look & Feel
        // ------------------------------
        try
        {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
            {
                if ("Nimbus".equals(info.getName()))
                {
                    UIManager.put("nimbusBase",                 new Color(100, 100, 100));
                    UIManager.put("nimbusLightBackground",      new Color(112, 128, 108));
                    UIManager.put("control",                    new Color(100, 100, 100));
                    UIManager.put("info",                       new Color(100, 100, 100));
                    UIManager.put("nimbusAlertYellow",          new Color(248, 187,   0));
                    UIManager.put("nimbusDisabledText",         new Color(128, 128, 128));
                    UIManager.put("nimbusFocus",                new Color(112, 128, 108)); 
                    UIManager.put("nimbusGreen",                new Color(176, 179,  50));
                    UIManager.put("nimbusInfoBlue",             new Color( 66, 139, 221));
                    UIManager.put("nimbusOrange",               new Color(191,  98,   4));
                    UIManager.put("nimbusRed",                  new Color(169,  46,  34));
                    UIManager.put("nimbusSelectedText",         new Color(255, 255, 255));
                    UIManager.put("text",                       new Color(0, 0, 0));
                    UIManager.put("nimbusSelectionBackground",  new Color(64, 64, 64));
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch (Exception ex) {}


        // Constructing the GUI
        // --------------------
        chemBase = new ChemBase();
    }

    /* _____________________________________________________________________________________________ */
    /* _____________________________________________________________________________________________ */
    /*                                                                                               */
    /*                                        CONSTRUCTOR                                            */
    /* _____________________________________________________________________________________________ */
    /* _____________________________________________________________________________________________ */
    
    private ChemBase()
    {
        
        // Constructing buttons
        // --------------------
        searchField  = new JTextField(40);
        searchButton = new JButton("SEARCH");

        searchField.setMargin(margin);
        searchButton.setMargin(margin);

        JButton viewEntryButton     = new JButton("VIEW ENTRY");
        JButton addEntryButton      = new JButton("ADD ENTRY");
        JButton changeEntryButton   = new JButton("EDIT ENTRY");
        JButton removeEntryButton   = new JButton("REMOVE ENTRY");      

        JLabel searchOptionsLabel = new JLabel("Search options: ");
        Border border = new EmptyBorder(10,3,20,0);
        searchOptionsLabel.setBorder(border);
        Font f = searchOptionsLabel.getFont();
        searchOptionsLabel.setFont(f.deriveFont(f.getStyle() ^ Font.BOLD));
        
        nameRadioButton             = new JRadioButton("Search by name");
        bruttoFormulaRadioButton    = new JRadioButton("Search by brutto formula");
        molarMassRadioButton        = new JRadioButton("Search by molar mass");
        storageLocationRadioButton  = new JRadioButton("Search by storage location");
        manufacturerRadioButton     = new JRadioButton("Search by manufacturer");
        supplierRadioButton         = new JRadioButton("Search by supplier");

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(nameRadioButton);
        buttonGroup.add(bruttoFormulaRadioButton);
        buttonGroup.add(molarMassRadioButton);
        buttonGroup.add(storageLocationRadioButton);
        buttonGroup.add(manufacturerRadioButton);
        buttonGroup.add(supplierRadioButton);
        nameRadioButton.setSelected(true);
        
        /* ----------------------------------------------------------------- */

        // Constructing the menu & it's elements
        // -------------------------------------
        JMenuItem exportAsTXT   = new JMenuItem("Export as TXT");
        JMenuItem exportAsCSV   = new JMenuItem("Export as CSV");
        JMenuItem exportAsXLSX  = new JMenuItem("Export as XLSX");
        JMenuItem exportAsPDF   = new JMenuItem("Export as PDF");
        JMenuItem clearDatabase = new JMenuItem("Clear database");      
        JMenuItem about         = new JMenuItem("About");
        JMenuItem exit          = new JMenuItem("Exit");

        exportAsTXT.setMargin(margin);
        exportAsCSV.setMargin(margin);
        exportAsXLSX.setMargin(margin);
        exportAsPDF.setMargin(margin);
        clearDatabase.setMargin(margin);
        about.setMargin(margin);
        exit.setMargin(margin);
        
        exportAsTXT.addActionListener(new ExportAsTXTListener());
        exportAsCSV.addActionListener(new ExportAsCSVListener());
        exportAsXLSX.addActionListener(new ExportAsXLSXListener());
        exportAsPDF.addActionListener(new ExportAsPDFListener());
        clearDatabase.addActionListener(new ClearDatabaseListener());
        about.addActionListener(new AboutListener());
        exit.addActionListener(new ExitListener());

        JMenuItem setupLocations = new JMenuItem("Setup locations");
        JCheckBoxMenuItem searchWithRegexOnOff = new JCheckBoxMenuItem("Search with regex ON/OFF");
        JCheckBoxMenuItem exportFullDataOnOff = new JCheckBoxMenuItem("Export full data ON/OFF");
        
        searchWithRegexOnOff.setSelected(true);
        searchWithRegexOnOff.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {               
                if (searchWithRegexOnOff.isSelected())
                    REGULAR_EXPRESSIONS_ON = true;
                else
                    REGULAR_EXPRESSIONS_ON = false;
            }
        });
        
        exportFullDataOnOff.setSelected(true);
        exportFullDataOnOff.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {               
                if (exportFullDataOnOff.isSelected())
                    FULL_EXPORT_ON = true;
                else
                    FULL_EXPORT_ON = false;
            }
        });
        
        setupLocations.setMargin(marginLow);
        searchWithRegexOnOff.setMargin(marginHigh);
        exportFullDataOnOff.setMargin(margin);

        setupLocations.addActionListener(new SetupLocationsListener());
        
        JMenu fileMenu = new JMenu("File");
        fileMenu.add(exportAsTXT);
        fileMenu.add(exportAsCSV);
        fileMenu.add(exportAsXLSX);
        fileMenu.add(exportAsPDF);
        fileMenu.add(new JSeparator());
        fileMenu.add(clearDatabase);
        fileMenu.add(new JSeparator());
        fileMenu.add(about);
        fileMenu.add(new JSeparator());
        fileMenu.add(exit);
        
        JMenu settingsMenu = new JMenu("Settings");
        settingsMenu.add(setupLocations);
        settingsMenu.add(new JSeparator());
        settingsMenu.add(searchWithRegexOnOff);
        settingsMenu.add(exportFullDataOnOff);
        
        JMenuBar menuBar = new JMenuBar();      
        menuBar.add(fileMenu);
        menuBar.add(settingsMenu);
        
        menuBar.setMargin(margin);
        
        /* ----------------------------------------------------------------- */

        // Constructing panels
        // -------------------
        JPanel mainPanel          = new JPanel(new BorderLayout());
        JPanel subPanel           = new JPanel(new BorderLayout());
        JPanel searchPanel        = new JPanel(new FlowLayout());
        JPanel searchOptionsPanel = new JPanel(new GridLayout(7,1));
        JPanel buttonsPanel       = new JPanel(new GridLayout(4, 1));

        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        searchOptionsPanel.add(searchOptionsLabel);
        searchOptionsPanel.add(nameRadioButton);
        searchOptionsPanel.add(bruttoFormulaRadioButton);
        searchOptionsPanel.add(molarMassRadioButton);
        searchOptionsPanel.add(storageLocationRadioButton);
        searchOptionsPanel.add(manufacturerRadioButton);
        searchOptionsPanel.add(supplierRadioButton);
    
        buttonsPanel.add(viewEntryButton);
        buttonsPanel.add(addEntryButton);
        buttonsPanel.add(changeEntryButton);
        buttonsPanel.add(removeEntryButton);
        
        subPanel.add(searchOptionsPanel, BorderLayout.WEST);
        subPanel.add(buttonsPanel, BorderLayout.EAST);

        /* ----------------------------------------------------------------- */

        // Table construction
        // ------------------
        table = new JTable(tableModel);

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(cellRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(cellRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(cellRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(cellRenderer);
        table.getColumnModel().getColumn(5).setCellRenderer(cellRenderer);

        TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
        JLabel headerLabel = (JLabel) headerRenderer;
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        table.setRowHeight(30);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowSelectionAllowed(true);

        JScrollPane tablePanel = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(subPanel, BorderLayout.CENTER);
        mainPanel.add(tablePanel, BorderLayout.SOUTH);

        /* ----------------------------------------------------------------- */

        // Fill table with test values retrieved from the database
        // -------------------------------------------------------
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try
        {
            statement = DBConnection.connection.prepareStatement(SQLStatements.INITIAL_SQL);
            resultSet = statement.executeQuery();
            tableModel.setRowCount(0);
            
            while (resultSet.next())
            {
                final Integer tempID             = resultSet.getInt(1);
                final String tempName            = resultSet.getString(2);
                final String tempBruttoFormula   = resultSet.getString(3);
                final String tempMolarMass       = resultSet.getString(4);
                final String tempQuantityAmount  = resultSet.getString(5);
                final String tempQuantityUnit    = resultSet.getString(6);
                final String tempStorageLocation = resultSet.getString(7);

                tableModel.addRow(new Object[] {tempID, tempName, tempBruttoFormula, tempMolarMass, tempQuantityAmount, tempQuantityUnit, tempStorageLocation});
            }
            table.doLayout();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            System.exit(1);             
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
        
        /* ----------------------------------------------------------------- */
        
        // Create and register listeners
        // -----------------------------
        SearchListener searchListener = new SearchListener();
        searchField.addActionListener(searchListener);
        searchButton.addActionListener(searchListener);

        viewEntryButton.addActionListener(new ViewEntryListener());
        addEntryButton.addActionListener(new AddEntryListener());
        changeEntryButton.addActionListener(new EditEntryListener());
        removeEntryButton.addActionListener(new RemoveEntryListener());

        /* ----------------------------------------------------------------- */

        // Finishing-Up
        // ------------
        this.setJMenuBar(menuBar);
        this.add(mainPanel);
        this.setTitle("ChemBase");
        this.setSize(1200, 750);
        this.validate();
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
    }
    
    /* _____________________________________________________________________________________________ */
    /* _____________________________________________________________________________________________ */
    /*                                                                                               */
    /*                                      HELPER METHODS                                           */
    /* _____________________________________________________________________________________________ */
    /* _____________________________________________________________________________________________ */
    
    public static ChemBase getChemBaseRoot()        { return chemBase; }    
    public static JTable getTable()                 { return table; }
    public static DefaultTableModel getTableModel() { return tableModel; }
    
    /* --------------------------------------------------------------------------- */

    public static Integer getCurrentPKey()
    {   
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1)
            return -1;
        
        return (Integer) table.getValueAt(selectedRow, 0);  
    }

    /* --------------------------------------------------------------------------- */

    public static int getSelectedSearchOption()
    {
        if (nameRadioButton.isSelected())
            return ChemBaseConstants.SEARCH_BY_NAME;
        else if (bruttoFormulaRadioButton.isSelected())
            return ChemBaseConstants.SEARCH_BY_BRUTTO_FORMULA;
        else if (molarMassRadioButton.isSelected())
            return ChemBaseConstants.SEARCH_BY_MOLAR_MASS;
        else if (storageLocationRadioButton.isSelected())
            return ChemBaseConstants.SEARCH_BY_STORAGE_LOCATION;
        else if (manufacturerRadioButton.isSelected())
            return ChemBaseConstants.SEARCH_BY_MANUFACTURER;
        else
            return ChemBaseConstants.SEARCH_BY_SUPPLIER;
    }

    /* --------------------------------------------------------------------------- */
    
    public static String getSearchText()
    {
        String searchText = searchField.getText();
        if (searchText == null)
            searchText = "";
        
        return (searchText.trim());
    }
    
    /* --------------------------------------------------------------------------- */

    public static boolean regularExpressionsInSearchON()
    {
        return REGULAR_EXPRESSIONS_ON;
    }
    
    public static boolean fullExportON()
    {
        return FULL_EXPORT_ON;
    }
    
    /* --------------------------------------------------------------------------- */

    public static void setLastPerformedSQL(String sql)
    {
        lastPerformedSQL = sql;
    }
    
    public static String getLastPerformedSQL()
    {
        return lastPerformedSQL;
    }

    /* --------------------------------------------------------------------------- */
    
    public static void refresh()
    {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try
        {   
            statement = DBConnection.connection.prepareStatement(lastPerformedSQL);
            resultSet = statement.executeQuery();

            ChemBase.getTableModel().setRowCount(0);
            while (resultSet.next())
            {
                final Integer tempID             = resultSet.getInt(1);
                final String tempName            = resultSet.getString(2);
                final String tempBruttoFormula   = resultSet.getString(3);
                final String tempMolarMass       = resultSet.getString(4);
                final String tempQuantityAmount  = resultSet.getString(5);
                final String tempQuantityUnit    = resultSet.getString(6);
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

    /* --------------------------------------------------------------------------- */

}
