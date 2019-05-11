package com.ruanko.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.ruanko.model.Channel;
import com.ruanko.view.JMainFrame;

public class ReadActionListener implements ActionListener {

	private Channel channel;

	public ReadActionListener(Channel channel) {
		this.channel = channel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JMainFrame mainFrame = JMainFrame.getMainFrame();
		mainFrame.readNews(channel);
	}

}
