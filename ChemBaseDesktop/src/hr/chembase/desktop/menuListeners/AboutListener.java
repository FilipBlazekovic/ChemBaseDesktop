package hr.chembase.desktop.menuListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import hr.chembase.desktop.gui.PopupMessage;

public class AboutListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e)
	{
		new PopupMessage("https://github.com/FilipBlazekovic/ChemBaseDesktop");
	}
}