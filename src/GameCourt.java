/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

import java.awt.*;
import java.awt.event.*;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.*;


/**
 * GameCourt
 * 
 * This class holds the primary game logic for how different objects interact
 * with one another. Take time to understand how the timer interacts with the
 * different methods and how it repaints the GUI on every tick().
 * 
 */
@SuppressWarnings("serial")
public class GameCourt extends JPanel {

	// the state of the game logic
	private SpaceShip ship;
	private Set<Celestial> planets;
	private Set<SpaceObject> stations;
	private int fuelLeft;
	private int levelNumber;

	public boolean playing = false; // whether the game is running
	private JLabel status; // Current status text (i.e. Running...)
	private JLabel level;
	private JLabel fuel;

	// Game constants
	public static final int COURT_WIDTH = 900;
	public static final int COURT_HEIGHT = 600;
	public static final int INTERVAL = 35; // Update interval for timer, in milliseconds

	public GameCourt(JLabel status, JLabel level, JLabel fuel) {
	    super();
        this.setOpaque(true);
        this.setBackground(Color.BLACK);
	    
	    setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		Timer timer = new Timer(INTERVAL, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tick();
			}
		});
		timer.start(); 
		setFocusable(true);

		int effect = 2;
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_LEFT && fuelLeft > 0) {
				    ship.force(-effect, 0); fuelLeft--; }
				else if (e.getKeyCode() == KeyEvent.VK_RIGHT && fuelLeft > 0) {
				    ship.force(effect, 0); fuelLeft--; }
				else if (e.getKeyCode() == KeyEvent.VK_DOWN && fuelLeft > 0) {
				    ship.force(0, -effect); fuelLeft--; }
				else if (e.getKeyCode() == KeyEvent.VK_UP && fuelLeft > 0) {
				    ship.force(0, effect); fuelLeft--; }
			}

			public void keyReleased(KeyEvent e) {
				//
			}
		});

		this.status = status;
		this.level = level;
		this.fuel = fuel;
	}

	private void buildLevel() {
	    
	    planets = new TreeSet<>();
	    stations = new TreeSet<>();
	            
	    switch (levelNumber) {
	    case 1: ship = new SpaceShip(100, 100);
                planets.add(new Celestial(650, 350, "gasgiant.png", true));
                stations.add(new SpaceObject(725, 425, "station.png"));
                fuelLeft = 1;
                fuel.setText("Fuel: " + fuelLeft);
                level.setText("Level1");
        break;
	    case 2: ship = new SpaceShip(800, 100);
                planets.add(new Celestial(200, 200, "earthlike.png", true));
                planets.add(new Celestial(300, 300, "earthlike.png", true));
                planets.add(new Celestial(800, 300, "earthlike.png", true));
                planets.add(new Celestial(650, 250, "balloffire.png", false));
                planets.add(new Celestial(650, 350, "rocky.png", true));
                planets.add(new Celestial(650, 350, "gasgiant.png", true));
                stations.add(new SpaceObject(800, 500, "station.png"));
                fuelLeft = 3;
                fuel.setText("Fuel: " + fuelLeft);
                level.setText("Level2");
        break;
        default: playing = false;
                 status.setText("You Win!");
	    }
	}
	
	
	public void reset() {

		levelNumber = 1;
		buildLevel();
	    
	    playing = true;
		status.setText("Running...");

		requestFocusInWindow(); // Make sure that this component has the keyboard focus
	}

	/**
	 * This method is called every time the timer defined in the constructor
	 * triggers.
	 */
	void tick() {
		if (playing) {
			
		    for (Celestial c : planets) {
		        gravity(ship, c);
		    }
		    
			ship.move();
			
			repaint();
			
			for (Celestial c : planets) {
			    if (ship.intersects(c)) {
	                playing = false;
	                status.setText("You lose!");
	            }
            }
			
			for (SpaceObject s : stations) {
                if (ship.intersects(s)) {
                    levelNumber++;
                    buildLevel();
                    repaint();
                }
            }
			
			fuel.setText("Fuel: " + fuelLeft);
		}
	}
	
	public static void gravity (SpaceShip body, Celestial planet) {
	    
	    int xDistance = Math.abs(body.getCenterX() - planet.getCenterX());
	    int yDistance = Math.abs(body.getCenterY() - planet.getCenterY());
	    int distance = (int)(Math.sqrt(Math.pow(xDistance, 2) +
	                                   Math.pow(yDistance, 2)));
	    double force = ((double)planet.getGravity() / Math.pow(distance, 2));
	    double xForce = force * (xDistance) / (xDistance + yDistance);
	    double yForce = force * (yDistance) / (xDistance + yDistance);
	    
	    if (body.getCenterX() - planet.getCenterX() >= 0) {
	        xForce = -xForce;
	    }
	    if (body.getCenterY() - planet.getCenterY() >= 0) {
            yForce = -yForce;
        }
	    
	    body.force(xForce, yForce);
	}
	
//TODO: Solve Wall Problem


	@Override
	public void paintComponent(Graphics g) {
	    super.paintComponent(g);
		ship.draw(g);
		for (Celestial c : planets) {
		    c.draw(g); 
		}
		for (SpaceObject s : stations) {
            s.draw(g); 
        }
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(COURT_WIDTH, COURT_HEIGHT);
	}
}
