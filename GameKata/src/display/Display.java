package display;

import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;

public class Display {

	private JFrame frame;
	private Canvas canvas;
	
	private String title;
	private int width;
	private int height;
	
	public Display(String title, int width, int height){
		this.title = title;
		this.width = width;
		this.height = height;
		
		this.createDisplay();
	}
	
	
	private void createDisplay(){
		this.frame = new JFrame(title);
		this.frame.setSize(width, height);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setResizable(false);
		this.frame.setLocationRelativeTo(null);
		this.frame.setVisible(true);
		
		this.canvas = new Canvas();
		this.canvas.setPreferredSize(new Dimension(width, height));
		this.canvas.setMaximumSize(new Dimension(width, height));
		this.canvas.setMinimumSize(new Dimension(width, height));
		this.canvas.setFocusable(false);
		
		this.frame.add(canvas);
		this.frame.pack();
	}
	
	
	public Canvas getCanvas(){
		return this.canvas;
	}
	
	public JFrame getFrame(){
		return this.frame;
	}
}
