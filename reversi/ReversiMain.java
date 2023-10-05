package reversi;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ReversiMain
{
	IModel model;
	IView view;
	IController controller;

	ReversiMain()
	{
		// Choose ONE of the models
		model = new SimpleModel();
		
		// Choose ONE of the views
		view = new GUIView();
		
		// Choose one controller
		controller = new ReversiController();
		
		// Don't change the lines below here, which connect things together
		
		// Initialise everything...
		model.initialise(8, 8, view, controller);
		controller.initialise(model, view);
		view.initialise(model, controller);
		
		// Now start the game - set up the board
		controller.startup();
	}
	
	public static void main(String[] args)
	{
		new ReversiMain();
	}
}
