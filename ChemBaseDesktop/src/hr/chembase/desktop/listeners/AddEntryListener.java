package hr.chembase.desktop.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import hr.chembase.desktop.gui.AddEditPanel;

public class AddEntryListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e)
	{
		new AddEditPanel(null, null, null, null, null, null, null, null, null, null, null);
	}
}

