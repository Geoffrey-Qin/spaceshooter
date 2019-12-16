/*
 * Description: This program is designed to create an applet where the user can control the players using
 * keyboards to shot bullets to destroy asteroids and enemy ships to score points. 
 * Unique Feature Highlight: 
 * 1). Different falling speed for individual asteroid
 * 2). Player ship can move in all directions instead of x-axis movement only
 * 3). Enemy ship image the player's moves and it fires at both ends, so it will be always be a threat to the player no matter it is at the top or bottom 
 * 4). Enemy ship random comes out from the top or the bottom of the screen each time it is regenerated 
 * 5). Extra life will be rewarded every 150 points (10 points per destroyed asteroid)
 * Author: Geoffrey Qin
 * Version: v1.0
 * Date: May 3, 2018
 * 							
 */
import java.awt.*;
import javax.swing.*;
import java.applet.*;
import java.awt.*;

//public class Main extends JApplet
public class GQMainApplet extends Applet implements Runnable{
	public GQMainApplet() {
	}
	
	///////////////////////////////// variables //////////////////////////////////
	
	static Thread th;								//enable the timer
	private GQSpaceship player;						//declare a spaceShip object called "player"
	private GQEnemyShip enemy;						//declare a enemyShip object called "enemy"
	private GQAsteroid[] asteroid;					//declare an array of asteroid objects
	private GQShots [][] shots;						//declare a 2D array of shots objects called "shots"

	private Rectangle playerRect;					//declare a rectangle called "playerRect"
	private Rectangle enemyRect;						//declare a rectangle called "enemyRect"
	private Rectangle[][] shotRect;					//declare a 2D array of rectangle called "shotRect"
	private Rectangle[] asteroidRect;				//declare a array of asteroidRect called "asteroidRect"

	//////////////////////////////// speed constants /////////////////////////////
	
	private final int shotSpeed = -3;				//create the constant for shotSpeed and set it to -3(moving up)
	private final int playerLeftSpeed = -2;			//create the constants for players' movement speed and set them to 2
	private final int playerRightSpeed = 2;
	private final int playerUpSpeed = -2;
	private final int playerDownSpeed = 2;

	private final int PLAYERCOLUMN = 0;				//create the index for the player column in the array
	private final int WINDOW_HEIGHT = 500;			//create the constant to store the height of the window
	private final int WINDOW_WIDTH = 700;			//create the constant to store the width of the window
	private final int ASTEROID_AMOUNT = 10;			//create the constant to store the number of asteroid 
	private final int INITIAL_LIFE = 3;				//create the constant to store the initial life value

	private int BOTTOM_BOUNDARY = WINDOW_HEIGHT - 50;	//create the constants of the boundaries of the window
	private int RIGHT_BOUNDARY  = WINDOW_WIDTH;
	private final int TOP_BOUNDARY = 0;
	private final int LEFT_BOUNDARY = 0;

	/////////////////////////////////// move flags ///////////////////////////////

	private boolean playerMoveLeft;					//declare the booleans for player's movement	
	private boolean playerMoveRight;
	private boolean playerMoveUp;
	private boolean playerMoveDown;
	private boolean reward;							//declare a boolean for reward

	private int enemyIndex = 1;						//create the index of the enemy column
	// double buffering
	private Image dbImage;							//enable the image
	private Graphics dbg;							//enable the graphics

	public void init()  {
		
		setSize(WINDOW_WIDTH,WINDOW_HEIGHT);			//create the window with the set height and width
		setBackground (Color.black);					//set the background to black
		player = new GQSpaceship(WINDOW_WIDTH/2, 280);//create the player object in the middle of the window
		playerRect = new Rectangle(player.getX() - (player.imageWidth/2), player.getY(), player.imageWidth,player.imageHeight); 	//attach the rectangle to the player
		player.life = INITIAL_LIFE;					//reset the player life to INITIAL_LIFE
		player.score = 0;							//reset the score to 0
		reward = false;								//reset the reward to false
		enemy = new GQEnemyShip ((int)(WINDOW_WIDTH * Math.random()), (int)(Math.round(Math.random()) * WINDOW_HEIGHT));		//create the enemy ship at a random position at the top or bottom of the window
		enemyRect = new Rectangle(enemy.getX() - (enemy.imageWidth/2), enemy.getY(), enemy.imageWidth,enemy.imageHeight);		//attach the rectangle to the enemy

		asteroid = new GQAsteroid[ASTEROID_AMOUNT];	//create a array of asteroid
		asteroidRect = new Rectangle[ASTEROID_AMOUNT];//attach the rectangles to the asteroid

		shots = new GQShots[5][10];  				//create a array of shots
		shotRect = new Rectangle[5][10];				//attach the rectangles to the shots
		for(int i=0; i< asteroid.length; i++) {		//keep looping until the program reaches the end of asteroid array
			asteroid[i] = new GQAsteroid((int)(WINDOW_WIDTH * Math.random()), 0);			//random generate the asteroid on the top of the window							
			asteroidRect[i] = new Rectangle(asteroid[i].getX() - (asteroid[i].imageWidth/2), asteroid[i].getY(), asteroid[i].imageWidth,asteroid[i].imageHeight);		//attach the rectangle to the asteroid
			asteroid[i].vertical_speed = 1 + (int)(Math.random() *4);			//randomly assign the falling speed for each asteroid
		}//end for loop
	}//end init


	public void start ()  {
		th = new Thread(this);
		th.start ();
	}

	public void stop()  {
		th.stop();
	}

	public void destroy()  {
		th.stop();
	}


	public void run ()  {

		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

		while (true)  {

			if(player.life > 0) {						//run the app only if the player life is more than 0
			
				if(player.score % 150 == 0 && player.score != 0 && reward == false) {		//check if the score is factorable by 150 and the player has not be rewarded yet
					reward = true;						//mark that the extra life has been rewarded
					player.life++;						//add one to player's life
				}
				for(int i = 0; i< shots[PLAYERCOLUMN].length; i++) {	//keep looping until the program reaches the end of shots array
					//  Loop through the array of shots
					//If shot is not null
					//then move shot
					if(shots[enemyIndex][i] != null) {    					//run if the shots is not null
						// test if shot Y coordinate is below 0 
						// then remove shot from array of shots

						shots[enemyIndex][i].vertical_speed = shotSpeed;		//set the speed of the falling shots for the enemy
						shots[enemyIndex][i].moveShot();						//move the enemy shots
						shotRect[enemyIndex][i] = new Rectangle(shots[enemyIndex][i].getX(), shots[enemyIndex][i].getY(), shots[enemyIndex][i].imageWidth,shots[enemyIndex][i].imageHeight);	//create the rectangle of the shots
						if(shots[enemyIndex][i].getY() > getHeight()) {		//check if the bullet falls under the bottom of the window
							shots[enemyIndex][i] = null;						//remove the shots and rectangles
							shotRect[enemyIndex][i] = null;
						}else {												//if the shots are still in the window
							shotRect[enemyIndex][i].x = shots[enemyIndex][i].getX();	//update the x and y positions of the shots
							shotRect[enemyIndex][i].y = shots[enemyIndex][i].getY();
						}//end else if(shots[enemyIndex][i].getY() > getHeight())
					}//end if(shots[enemyIndex][i] != null)
					
					if(shots[enemyIndex + 1][i] != null) {   				 //run if the shots is not null	
						// test if shot Y coordinate is below 0 
						// then remove shot from array of shots
						shots[enemyIndex + 1][i].vertical_speed = -shotSpeed;	 //set the speed of the rising shots for the enemy
						shots[enemyIndex + 1][i].moveShot();					 //move the enemy shots
						shotRect[enemyIndex + 1][i] = new Rectangle(shots[enemyIndex + 1][i].getX(), shots[enemyIndex + 1][i].getY(), shots[enemyIndex + 1][i].imageWidth,shots[enemyIndex + 1][i].imageHeight);		//create the rectangle of the shots
						if(shots[enemyIndex + 1][i].getY() < 0) {		    	//check if the bullet flies over the top of the window
							shots[enemyIndex + 1][i] = null;					//remove the shots and rectangles
							shotRect[enemyIndex + 1][i] = null;
						}else {												//if the shots are still in the window
							shotRect[enemyIndex + 1][i].x = shots[enemyIndex + 1][i].getX();		//update the x and y positions of the shots
							shotRect[enemyIndex + 1][i].y = shots[enemyIndex + 1][i].getY();
						}//end else if (shots[enemyIndex + 1][i].getY() < 0)
					}//end if(shots[enemyIndex + 1][i] != null)

					if(shots[PLAYERCOLUMN][i] != null) {   					 //run if the shots is not null
						// test if shot Y coordinate is below 0 
						// then remove shot from array of shots
						shots[PLAYERCOLUMN][i].vertical_speed = shotSpeed;	//set the speed of the rising shots for the player
						shots[PLAYERCOLUMN][i].moveShot();					//move the player shots
						shotRect[PLAYERCOLUMN][i] = new Rectangle(shots[PLAYERCOLUMN][i].getX(), shots[PLAYERCOLUMN][i].getY() -50, shots[PLAYERCOLUMN][i].imageWidth,shots[PLAYERCOLUMN][i].imageHeight);		//create the rectangle of the shots
						if(shots[PLAYERCOLUMN][i].getY() < 0) {				//check if the bullet flies over the top of the window
							shots[PLAYERCOLUMN][i] = null;					//remove the shots and rectangles
							shotRect[PLAYERCOLUMN][i] = null;
						}//end if(shots[PLAYERCOLUMN][i].getY() < 0) 
						
						// test for collisions with enemies (Best way is to use the Rectangle object  in order to detect collision Check the moodle )
						playerRect.x = player.getX() - (player.imageWidth/2);	 //update the x and y positions of the player
						playerRect.y = player.getY();
						enemyRect.x = enemy.getX() - (enemy.imageWidth/2);	//update the x and y positions of the enemy
						enemyRect.y = enemy.getY();
						for(int row = 0; row< shotRect.length; row++) {		//keep looping until the program reaches the end of shotRect array
							for(int column = 0; column < shotRect[row].length; column++) {
								if(shotRect[row][column] != null) {			//run if the shotRect is not null
									if(playerRect.intersects(shotRect[row][column]) == true ) {	//check if the player is hit by the shots
										shots[row][column] = null;			//remove the shots
										shotRect[row][column] = null;
										player.life --;						//reduce one player life
									}//end if (playerRect.intersects(shotRect[row][column]) == true )
									
									if(enemyRect != null && shotRect[PLAYERCOLUMN][column] != null) { //run if enemyRect and shotRect is not null
										if(enemyRect.intersects(shotRect[PLAYERCOLUMN][column]) == true) {	//check if the enemy is hit by the shots
											enemy = new GQEnemyShip ((int)(WINDOW_WIDTH * Math.random()), (int)(Math.round(Math.random()) * WINDOW_HEIGHT));	//regenerate the enemy randomly at the top or bottom of the window
											enemyRect = new Rectangle(enemy.getX() - (enemy.imageWidth/2), enemy.getY(), enemy.imageWidth,enemy.imageHeight);	//attach the rectangle to the enemy
										}//end if (enemyRect.intersects(shotRect[PLAYERCOLUMN][column]) == true)
									}//end if(enemyRect != null && shotRect[PLAYERCOLUMN][column] != null) 
								}//end if (shotRect[row][column] != null)
							}//end for (int column = 0; column < shotRect[row].length; column++) 
						}//end for (int row = 0; row< shotRect.length; row++)
						
					}//end if (shots[PLAYERCOLUMN][i] != null)
				}//end for (int i = 0; i< shots[PLAYERCOLUMN].length; i++)

				// move player
				if(playerMoveLeft == true) {
					//if playerMoveLeft is true key is pressed
					//Then move left
					player.horizontal_speed = playerLeftSpeed;		//move the player left at the rate of horizontal speed
					enemy.horizontal_speed = 10* playerRightSpeed;	//move the enemy reversely at 10 times of player
					player.moveX();
					enemy.moveX();
				}//end if (playerMoveLeft == true)
				
				if(playerMoveRight == true) {
					//if playerMoveRight is true key is pressed
					//move right
					player.horizontal_speed = playerRightSpeed;		//move the player right at the rate of horizontal speed
					enemy.horizontal_speed = 10* playerLeftSpeed;	//move the enemy reversely at 10 times of player
					player.moveX();
					enemy.moveX();
				}//end if(playerMoveRight == true)
				
				if(playerMoveUp == true) {
					//if playerMoveUp is true key is pressed
					//Then move up
					player.vertical_speed = playerUpSpeed;			//move the player up at the rate of vertical speed
					enemy.vertical_speed = playerDownSpeed;			//move the enemy reversely
					player.moveY();
					enemy.moveY();
				}//end if (playerMoveUp == true)
				
				if(playerMoveDown == true) {
					//if playerMoveDown is true key is pressed
					//move down
					player.vertical_speed = playerDownSpeed;			//move the player down at the rate of vertical speed
					enemy.vertical_speed = playerUpSpeed;			//move the enemy reversely
					player.moveY();
					enemy.moveY();
				}//end if (playerMoveDown == true)

				//////////////////////////////// Player Boundary Control ////////////////////////////////

				if(player.getY() >= BOTTOM_BOUNDARY) {			//check if the player falls under the bottom boundary of the window
					player.y_pos = BOTTOM_BOUNDARY;				//keep the player above the bottom boundary
				}
				if(player.getY() <= TOP_BOUNDARY) {				//check if the player flies over the top boundary of the window
					player.y_pos = TOP_BOUNDARY;					//keep the player below the top boundary
				}
				if(player.getX() >= RIGHT_BOUNDARY) {			//check if the player flies over the right boundary of the window
					player.x_pos = RIGHT_BOUNDARY;				//keep the player in the boundary
				}
				if(player.getX() <= LEFT_BOUNDARY) {				//check if the player flies over the left boundary of the window
					player.x_pos = LEFT_BOUNDARY;				//keep the player in the boundary
				}

				///////////////////////////////// Enemy Boundary Control ////////////////////////////////

				if(enemy.getY() >= BOTTOM_BOUNDARY) {			//check if the enemy falls under the bottom boundary of the window
					enemy.y_pos = BOTTOM_BOUNDARY;				//keep the enemy above the bottom boundary
				}
				if(enemy.getY() <= TOP_BOUNDARY) {				//check if the enemy flies over the top boundary of the window
					enemy.y_pos = TOP_BOUNDARY;					//keep the enemy below the top boundary
				}
				if(enemy.getX() >= RIGHT_BOUNDARY) {				//check if the enemy flies over the right boundary of the window
					enemy.x_pos = RIGHT_BOUNDARY;				//keep the enemy in the boundary
				}
				if(enemy.getX() <= LEFT_BOUNDARY) {				//check if the enemy flies over the left boundary of the window
					enemy.x_pos = LEFT_BOUNDARY;					//keep the enemy in the boundary
				}
				
				for(int i=0; i< asteroid.length; i++) {			//keep looping until the program reaches the end of asteroid array
					if(asteroid[i] != null) {					//run if the asteroid is not null
						asteroid[i].moveY();						//move the asteroid vertically
						playerRect.x = player.getX() - (player.imageWidth/2);		//update the positions of playerRect
						playerRect.y = player.getY();
						asteroidRect[i].x = asteroid[i].getX() - (asteroid[i].imageWidth/2);	//update the positions of asteroidRect
						asteroidRect[i].y = asteroid[i].getY();
						if(asteroid[i].getY() >= WINDOW_HEIGHT) {	//check if the asteroid fall below the bottom of the window
							asteroid[i] = new GQAsteroid((int)(WINDOW_WIDTH * Math.random()), 0);		//recreate the asteroid on the top of the window
							asteroidRect[i] = new Rectangle(asteroid[i].getX() - (asteroid[i].imageWidth/2), asteroid[i].getY(), asteroid[i].imageWidth,asteroid[i].imageHeight);	//create the rectangle of the asteroid
							asteroid[i].vertical_speed = 1 + (int)(Math.random() *4);		//randomly assign the falling speed of the asteroid
						}//end if (asteroid[i].getY() >= WINDOW_HEIGHT)
						
						if(playerRect.intersects(asteroidRect[i]) == true) {		//check if the player collides with the asteroid
							asteroid[i] = null;					//remove the asteroid and rectangle
							asteroidRect[i] = null;
							asteroid[i] = new GQAsteroid((int)(WINDOW_WIDTH * Math.random()), 0);		//recreate the asteroid on the top of the window
							asteroidRect[i] = new Rectangle(asteroid[i].getX() - (asteroid[i].imageWidth/2), asteroid[i].getY(), asteroid[i].imageWidth,asteroid[i].imageHeight);	//create the rectangle of the asteroid
							asteroid[i].vertical_speed = 1 + (int)(Math.random() *4);		//randomly assign the falling speed of the asteroid
							player.life --;						//reduce one on the player's life
						}//end if(playerRect.intersects(asteroidRect[i]) == true)

					}//end if (asteroid[i] != null)
				}//end for (int i=0; i< asteroid.length; i++)

				for(int row = 0; row< shots.length; row++) {		//keep looping until the program reaches the end of shots array
					for(int column = 0; column < shots[row].length; column ++) {
						
						if(shotRect[row][column] != null) {			//check if shotRect is not null
							for(int i=0; i < asteroid.length; i++) {	//keep looping until the program reaches the end of asteroid array
								if(shotRect[row][column] != null) {	//check if shotRect is not null
									if(asteroidRect[i].intersects(shotRect[row][column]) == true) {	//check if the asteroid is hit by shots
										asteroid[i] = null;			//remove the asteroid and rectangle
										asteroidRect[i] = null;
										asteroid[i] = new GQAsteroid((int)(WINDOW_WIDTH * Math.random()), 0);		//recreate the asteroid randomly at the top of window
										asteroidRect[i] = new Rectangle(asteroid[i].getX() - (asteroid[i].imageWidth/2), asteroid[i].getY(), asteroid[i].imageWidth,asteroid[i].imageHeight);	//create the rectangle of the asteroid
										asteroid[i].vertical_speed = 1 + (int)(Math.random() *4);		//randomly assign the falling speed of asteroid
										shots[row][column] = null;	//remove the shots
										shotRect[row][column] = null;
										player.score = player.score + 10;	//score 10 points for the player
										reward = false;				//reset the reward to false
									}//end if (asteroidRect[i].intersects(shotRect[row][column]) == true)
								}//end if (shotRect[row][column] != null)
							}//end for(int i=0; i < asteroid.length; i++)
						}//end if (shotRect[row][column] != null)
						
					}//end for(int column = 0; column < shots[row].length; column ++)
				}//end for(int row = 0; row< shots.length; row++)


				// repaint applet
				repaint();

				try {
					Thread.sleep(10);

				}
				catch (InterruptedException ex){
					// do nothing
				}

				Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
			}else {		//run when the player died
				
				if  (JOptionPane.showConfirmDialog(null, "Do you want to start again?",  "You Lost", JOptionPane.YES_NO_OPTION ) == JOptionPane.YES_OPTION){	//display the lost message
					playerMoveLeft = false;		//reset the booleans
					playerMoveRight = false;
					playerMoveUp = false;
					playerMoveDown = false;
					reward = false;
					init();						//reinitialize the game
				}else {
					System.exit(0);		//exit the program
				}

			}//end else if(player.life > 0)
		}//end while
	}//end method run

	public boolean keyDown(Event e, int key){
		if(key == Event.LEFT) {						//check if the left arrow is pressed down
			playerMoveLeft = true; 					//set playerMoveLeft = true;
		}
		else if(key == Event.RIGHT){					//check if the right arrow is pressed down
			playerMoveRight = true;					//set playerMoveRight to true
		}
		else if(key == Event.UP){					//check if the up arrow is pressed down
			playerMoveUp = true;						//set playerMoveUp to true
		}
		else if(key == Event.DOWN){					//check if the Down arrow is pressed down
			playerMoveDown = true;					//set playerMoveDown to true
		}
		else if(key == 32){							//check if the space key is pressed down
			// generate new shot and add it to shots array
			for(int i=0; i<shots[PLAYERCOLUMN].length; i++){			//keep looping until the program reaches the end of shots array
				if(shots[PLAYERCOLUMN][i] == null){					//check if shots is not null
					shots[PLAYERCOLUMN][i] = player.generateShot();	//generate the player's shots
					enemy.bullet_x = enemy.getX();					//set the enemy bullet positions to enemy current positions
					enemy.bullet_y = enemy.getY();
					shots[enemyIndex][i] = enemy.generateShot();		//generate the rising bullets for enemy
					enemy.bullet_x = enemy.getX();					//set the enemy bullet position to the bottom end of the enemy image
					enemy.bullet_y = (enemy.getY() + enemy.imageHeight);
					shots[enemyIndex +1][i] = enemy.generateShot();	//generate the falling bullets for enemy
					break;
				}//end if (shots[PLAYERCOLUMN][i] == null)
			}//end for(int i=0; i<shots[PLAYERCOLUMN].length; i++) 
		}//end else if (key == 32)
		return true;
	}//end method keyDown

	public boolean keyUp(Event e, int key)  {
		if(key == Event.LEFT)    {					//check if the left arrow was pressed
			playerMoveLeft = false;					//set the playerMoveLeft to false
		}    else if(key == Event.RIGHT)   {			//check if the right arrow was pressed
			playerMoveRight = false;					//set the playerMoveRight to false
		}    else if(key == Event.UP)   	  {			//check if the up arrow was pressed
			playerMoveUp = false;					//set the playerMoveUp to false
		}    else if(key == Event.DOWN)    {			//check if the down arrow was pressed
			playerMoveDown = false;					//set the playerMoveDown to false
		}//end else if (key == Event.DOWN)
		return true;
	}//end keyUp


	public void update (Graphics g)  {
		if (dbImage == null)    {
			dbImage = createImage (this.getSize().width, this.getSize().height);
			dbg = dbImage.getGraphics ();
		}

		dbg.setColor (getBackground ());
		dbg.fillRect (0, 0, this.getSize().width, this.getSize().height);

		dbg.setColor (getForeground());
		paint (dbg);

		g.drawImage (dbImage, 0, 0, this);
	}



	public void paint (Graphics g)  {
		// draw player/spacecraft
		player.drawPlayer(g);						//draw the player
		enemy.drawPlayer(g);							//draw the enemy
		for(int i=0; i< asteroid.length; i++) {		//keep looping until the program reaches the end of asteroid array
			if(asteroid[i] != null)					//check if asteroid is not null
				asteroid[i].drawRock(g);				//draw asteroid
		}//end for(int i=0; i< asteroid.length; i++)
		// draw shots
		for(int i = 0; i < shots.length; i ++) {		//keep looping until the program reaches the end of shots array
			if(shots[PLAYERCOLUMN][i] != null){		//check if the shots is not null
				shots[PLAYERCOLUMN][i].drawShot(g);	//draw the shots
			}
			if(shots[enemyIndex][i] != null){		//check if the shots is not null
				shots[enemyIndex][i].drawShot(g);	//draw the shots
			}
			if(shots[enemyIndex +1][i] != null){		//check if the shots is not null
				shots[enemyIndex +1][i].drawShot(g);	//draw the shots
			}
			//remember it’s an array
			g.setColor(Color.white);					//set the graphics color to white
			g.setFont(new Font(g.getFont().toString(),0,20));		//set the fonts of the graphics
			g.drawString("Lives: " + player.life, this.getWidth()-200, 40);	//display the lives of player on the top right corner of the window
			g.drawString("Score: " + player.score, 20, 40);					//display the score of player on the top left corner of the window
			if(reward == true) {						//check if the reward is true
				g.drawString("Extra Life Rewarded!", 20, WINDOW_HEIGHT - 40);	//display reward message
			}//end if (reward == true)
		}//for (int i = 0; i < shots.length; i ++)
	}//end method paint


	/**
	 * Returns information about this applet. 
	 * An applet should override this method to return a String containing 
	 * information about the author, version, and copyright of the JApplet.
	 *
	 * @return a String representation of information about this JApplet
	 */
	public String getAppletInfo() {
		// provide information about the applet
		return "Title:   \nAuthor:   \nA simple applet example description. ";
	}//end get AppletInfo


	/**
	 * Returns parameter information about this JApplet. 
	 * Returns information about the parameters than are understood by this JApplet.
	 * An applet should override this method to return an array of Strings 
	 * describing these parameters. 
	 * Each element of the array should be a set of three Strings containing 
	 * the name, the type, and a description.
	 *
	 * @return a String[] representation of parameter information about this JApplet
	 */
	public String[][] getParameterInfo() {
		// provide parameter information about the applet
		String paramInfo[][] = {
				{"firstParameter",    "1-10",    "description of first parameter"},
				{"status", "boolean", "description of second parameter"},
				{"images",   "url",     "description of third parameter"}
		};
		return paramInfo;
	}//end get ParameterInfo
}//end class

