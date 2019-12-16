/*
 * Description: This program is designed to construct a class with shots objects that have six parameters:
 * x and y positions of the object, vertical and horizontal speed, and image width and height;
 * The class have built-in methods to move the objects and display the image.
 * Author: Geoffrey Qin
 * Version: v1.0
 * Date: May 2, 2018
 */

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GQShots{

	//////////////////////////// variables /////////////////////////////

	private Graphics graph;			//enable the graphics
	Image icon;						//enable the image
	private int x_pos;				//create a variable to store the x position of the bullet
	private int y_pos;				//create a variable to store the y position of the bullet
	int horizontal_speed;			//create a variable to store the speed of horizontal movement
	int vertical_speed;				//create a variable to store the speed of vertical movement
	int imageWidth;					//create a variable to store the width of the image of ship
	int imageHeight;					//create a variable to store the height of the image of ship

	/////////////////////////// constructors ///////////////////////////

	GQShots(int x, int y){
		this.x_pos = x;				//store the x and y position of the bullet
		this.y_pos = y;
		try{
			icon = ImageIO.read(new File("laser.png"));			//link the image file to the bullet
		}catch(IOException e){
			e.printStackTrace();
		}//end catch 
		imageWidth = icon.getWidth(null);							//store the width of the image
		imageHeight = icon.getHeight(null);							//store the height of the image
	}//end constructor

	/////////////////////////////// Methods //////////////////////////// 

	public int getX() {
		// input: x_postion of the bullet
		// output: x_position of the bullet
		return this.x_pos;											//return the x position of the bullet
	}//end getX

	public int getY() {
		// input: y_postion of the bullet
		// output: y_position of the bullet
		return this.y_pos;											//return the y position of the bullet
	}//end getY

	public GQShots moveShot() {
		//input: y_position of the spaceship, vertical_speed
		//output: the new y_position after movement
		this.y_pos += vertical_speed;								//move the bullet vertically at the rate of vertical_speed			
		return this;
	}//end moveShot

	public void drawShot(Graphics g){
		//input: image, x and y position, imageWidth
		//output: the ship with image
		g.drawImage(icon, (x_pos- (imageWidth/2)), y_pos, null);		//draw the image at the position of the bullet
	}//end drawShot

}//end class
