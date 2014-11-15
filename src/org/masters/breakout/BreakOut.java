package org.masters.breakout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.masters.breakout.log.Log;

public class BreakOut extends JPanel {

	private final AnimationRunnable animator;
	private final Thread animationThread;
	
	public BreakOut() {
		setLayout(new BorderLayout(3, 3));
		
		BreakOutPanel breakOutPanel = new BreakOutPanel();
		configureBreakOutGameInterface(breakOutPanel);
		
		// create Thread to run animation
		animator = new AnimationRunnable(breakOutPanel);
		animationThread = new Thread(animator, "animation-thread");
		animationThread.setDaemon(true);	

		add(breakOutPanel, BorderLayout.CENTER);
	}
	
	private void configureBreakOutGameInterface(
			BreakOutPanel breakOutPanel) {
		breakOutPanel.setBackground(Color.darkGray);
		breakOutPanel.setForeground(Color.white);
	}
	
	public void startAnimation() {
		if (!animationThread.isAlive())
			animationThread.start();

		animator.startRunning();
	}
	
	public void stopAnimation() {
		animator.stopRunning();
	}

	private static void createAndShowGUI() {
		JFrame frame = new JFrame("BreakOut");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setMinimumSize(new Dimension(300, 300));

		final BreakOut breakOut = new BreakOut();
		
		frame.getContentPane().add(breakOut);

		frame.addWindowListener(new WindowAdapter() {
//			@Override
//			public void windowActivated(WindowEvent arg0) {
//				Log.info("windowActivated");
//				breakOut.startAnimation();
//			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				Log.info("windowClosing");
				breakOut.stopAnimation();
			}
			
			@Override
			public void windowIconified(WindowEvent arg0) {
				Log.info("windowClosing");
				breakOut.stopAnimation();
			}
//			@Override
//			public void windowDeactivated(WindowEvent arg0) {
//				Log.info("windowDeactivated");
//				breakOut.stopAnimation();
//			}

			@Override
			public void windowOpened(WindowEvent arg0) {
				Log.info("windowOpened");
				breakOut.startAnimation();
			}
			
			@Override
			public void windowDeiconified(WindowEvent arg0) {
				Log.info("windowOpened");
				breakOut.startAnimation();
			}
		});
		
		frame.pack();
		frame.setVisible(true);
	}
	

	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				createAndShowGUI();
			}
			
		});
	}

}
