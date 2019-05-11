package com.ruanko.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.ruanko.view.JMainFrame;

public class ExportActionListener implements ActionListener {
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JMainFrame.getMainFrame().exportNews();
	}

}
