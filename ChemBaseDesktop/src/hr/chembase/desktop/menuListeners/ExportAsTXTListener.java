package hr.chembase.desktop.menuListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;

import hr.chembase.desktop.api.ChemBase;
import hr.chembase.desktop.exporters.TXTExporter;
import hr.chembase.desktop.gui.PopupMessage;

public class ExportAsTXTListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e)
    {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select output location");   
        int userSelection = fileChooser.showSaveDialog(ChemBase.getChemBaseRoot());
        
        if (userSelection == JFileChooser.APPROVE_OPTION)
        {
            File outputFile = fileChooser.getSelectedFile();

            boolean status;
            if (ChemBase.fullExportON())
                status = TXTExporter.performFullExport(outputFile);
            else
                status = TXTExporter.performConciseExport(outputFile);

            if (status)
                new PopupMessage("Success!");
            else
                new PopupMessage("Something went wrong!");
        }
    }
}
