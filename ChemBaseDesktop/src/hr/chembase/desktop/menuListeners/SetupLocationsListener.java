package hr.chembase.desktop.menuListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import hr.chembase.desktop.gui.LocationsPanel;

public class SetupLocationsListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e)
    {
        new LocationsPanel();
    }
}
