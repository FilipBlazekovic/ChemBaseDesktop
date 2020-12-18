package hr.chembase.desktop.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import hr.chembase.desktop.api.ChemBase;

public class PopupMessage extends JDialog {

    private PopupMessage referenceToThis = null;
    
    public PopupMessage(String message)
    {
        referenceToThis = this;
        
        JPanel rootPanel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("<html>" + message + "</html>", SwingConstants.CENTER);
        JButton okButton = new JButton("OK");
        
        okButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                referenceToThis.setVisible(false);
                referenceToThis.dispose();
            }
        });

        okButton.setPreferredSize(new Dimension(30, 30));
        okButton.setMaximumSize(new Dimension(90, 30));     
        rootPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        rootPanel.add(label, BorderLayout.CENTER);
        rootPane.add(new JLabel(), BorderLayout.CENTER);
        rootPanel.add(okButton, BorderLayout.SOUTH);
        
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setTitle("Chemicals Database");
        this.setContentPane(rootPanel);
//      this.pack();
        this.setSize(370,150);
        this.setLocationRelativeTo(ChemBase.getChemBaseRoot());
        this.setModal(true);
        this.setVisible(true);
    }   
}
