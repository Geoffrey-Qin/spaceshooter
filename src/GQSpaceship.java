/*
 * Description: This program is designed to construct a class with spaceships objects that have eight parameters:
 * x and y positions of the object, vertical and horizontal speed, image width and height, life and score;
 * The class have built-in methods to move the objects, generate shots, and display the image.
 * Author: Geoffrey Qin
 * Version: v1.0
 * Date: May 2, 2018
 */

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GQSpaceship{

	//////////////////////////// variables ////////////////////////////

	private Graphics graph;			//enable the graphics
	Image icon;						//enable the image
	int x_pos;						//create a variable to store the x-axis position of the ship
	int y_pos;						//create a variable to store the y-axis position of the ship
	int horizontal_speed;			//create a variable to store the speed of horizontal movement
	int vertical_speed;				//create a variable to store the speed of vertical movement
	int imageWidth;					//create a variable to store the width of the image of ship
	int imageHeight;					//create a variable to store the height of the image of ship
	int life;						//create a variable to store the life parameter of the ship
	int score;						//create a variable to store the score parameter of the ship

	/////////////////////////// constructors ///////////////////////////

	GQSpaceship(int x, int y){		
		x_pos = x;													//store the x and y positions of the ship
		y_pos = y;		
		try{
			icon = ImageIO.read(new File("tempest.png"));			//link the image file to the ship
		}catch(IOException e){
			e.printStackTrace();
		}//end catch 
		imageWidth = icon.getWidth(null);							//store the width of the image
		imageHeight = icon.getHeight(null);							//store the height of the image
	}//end constructor

	//////////////////////////////methods ///////////////////////////

	public int getX() {
		// input: x_postion of the spaceship
		// output: x_position of the spaceship
		return this.x_pos;											//return the x position of the ship
	}//end get X

	public int getY() {
		// input: y_postion of the spaceship
		// output: y_position of the spaceship
		return this.y_pos;											//return the y position of the ship
	}//end get Y

	public void moveX() {
		//input: x_position of the spaceship, horizontal_speed
		//output: the new x_position after movement
		this.x_pos += horizontal_speed;								//move the ship horizontally at the rate of vertical_speed
	}//end moveX

	public void moveY() {
		//input: y_position of the spaceship, vertical_speed
		//output: the new y_position after movement
		this.y_pos += vertical_speed;								//move the ship vertically at the rate of vertical_speed
	}//end moveY

	public GQShots generateShot() {
		//input: x and y positions of the ship
		//output: the bullet object
		GQShots bullet = new GQShots(this.x_pos, this.y_pos);			//create the new shot object 
		return bullet;
	}//end genereateShot

	public void drawPlayer(Graphics g) {
		//input: image, x and y position, imageWidth
		//output: the ship with image
		g.drawImage(icon, (x_pos- (imageWidth/2)), y_pos, null);		//draw the image at the position of the ship
	}//end drawPlayer

}//end class




