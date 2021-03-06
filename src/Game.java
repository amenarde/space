/**
 * Exhaust
 * (c) Antonio Menarde
 * @version 1.0, Apr 2016
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;


public class Game implements Runnable {
    
    private static boolean gameLost;
    
    private final JFrame frame;
    
    //game resources
    private final JPanel control_panel;
    private final JLabel status;
    private final JLabel fuel;
    private final JLabel level;
    //instance of game
    private final GameCourt court;
    
    //intro and outro screens
    private final JLabel introScreen;
    private final JLabel outroScreen;
    private final JLabel winScreen;
    
    public Game() {
        gameLost = false;
        frame = new JFrame("Exhaust");
        control_panel = new JPanel();
        status = new JLabel("");
        fuel = new JLabel("");
        level = new JLabel("");
        
        court = new GameCourt(status, level, fuel, this);
        
        BufferedImage intro = null;
        BufferedImage outro = null;
        BufferedImage win = null;
        try {
            intro = ImageIO.read(new File("visual/intro.png"));
            outro = ImageIO.read(new File("visual/end.png"));
            win = ImageIO.read(new File("visual/win.png"));
//            intro = ImageIO.read(this.getClass().getResource("intro.png"));
//            outro = ImageIO.read(this.getClass().getResource("end.png"));
//            win = ImageIO.read(this.getClass().getResource("win.png"));
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
        
        if (intro == null && outro == null) {
            introScreen = new JLabel();
            outroScreen = new JLabel();
            winScreen = new JLabel();
        }
        else {
            introScreen = new JLabel(new ImageIcon(intro));
            outroScreen = new JLabel(new ImageIcon(outro));
            winScreen = new JLabel(new ImageIcon(win));
        }
    }
    
    
    public void run() {
        
		
		frame.setLocation(100, 100);

		//Control Panel
		frame.add(control_panel, BorderLayout.NORTH);
	    
	    //Reset Button
	    final JButton reset = new JButton("PLAY");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (gameLost) {
                    gameLost = false;
                    frame.remove(outroScreen);
                    frame.revalidate();
                    frame.add(court, BorderLayout.CENTER);
                    frame.repaint();
                    //frame.setBackground(Color.BLACK);
                    court.reset();
                    frame.setVisible(true);
                }
                else if (court.getLevelNumber() != 1) {
                    court.playing = true;
                    court.requestFocusInWindow();
                    status.setText("");
                }
                else {
                    frame.remove(introScreen);
                    frame.setVisible(true);
                    court.reset();
                    status.setText("");
                }
            }
        });
        
        //add to Control Panel
	    control_panel.add(fuel);
	    control_panel.add(reset);
	    control_panel.add(status);
	    control_panel.add(level);
		
		frame.add(introScreen, BorderLayout.CENTER);
		// Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        
		// Main playing area
        frame.add(court, BorderLayout.CENTER);
        frame.setBackground(Color.BLACK);

		// Start game
		court.reset();
		court.playing = false;
		status.setText("");
		level.setText("");
		fuel.setText("");
	}
    
    public void lose() {
        court.reset();
        court.playing = false;
        frame.remove(court);
        frame.revalidate();
        frame.add(outroScreen, BorderLayout.CENTER);
        frame.repaint();
        gameLost = true;
        frame.setVisible(true);
    }
    
    public void win() {
        court.reset();
        court.playing = false;
        frame.remove(court);
        frame.revalidate();
        frame.add(winScreen, BorderLayout.CENTER);
        frame.repaint();
        gameLost = true;
        frame.setVisible(true);
    }


	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Game());
	}
}
