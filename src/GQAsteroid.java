/*
 * Description: This program is designed to construct a class with asteroid objects that have six parameters:
 * x and y positions of the object, vertical and horizontal speed, and image width and height;
 * The class have built-in methods to move the objects and display the image.
 * Author: Geoffrey Qin
 * Version: v1.0
 * Date: May 4, 2018
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GQAsteroid {

	//////////////////////////// variables ////////////////////////////

	private Graphics graph;			//enable the graphics
	Image rockIcon;					//enable the image
	int x_pos;						//create a variable to store the x-axis position of the rock
	int y_pos;						//create a variable to store the y-axis position of the rock
	int horizontal_speed;			//create a variable to store the speed of horizontal movement
	int vertical_speed;				//create a variable to store the speed of vertical movement
	int imageWidth;					//create a variable to store the width of the image of rock
	int imageHeight;					//create a variable to store the height of the image of rock

	/////////////////////////// constructors //////////////////////////

	GQAsteroid(int x, int y){
		x_pos = x;												//store the x and y position of the rock
		y_pos = y;
		try{
			rockIcon = ImageIO.read(new File("asteroid.png"));	//link the image file to the rock
		}catch(IOException e){
			e.printStackTrace();
		}
		imageWidth = rockIcon.getWidth(null);					//store the width of the image
		imageHeight = rockIcon.getHeight(null);					//store the height of the image

	}//end constructor

	////////////////////////////// methods /////////////////////////////

	public int getX() {
		// input: x_postion of the rock
		// output: x_position of the rock
		return this.x_pos;										//return the x position of the rock
	}//end getX
	
	public int getY() {
		// input: y_postion of the rock
		// output: y_position of the rock
		return this.y_pos;										//return the y position of the rock
	}//end getY
	
	public void moveX() {
		//input: x_position of the rock, horizontal_speed
		//output: the new x_position after movement
		this.x_pos += horizontal_speed;							//move the rock horizontally at the rate of vertical_speed
	}//end moveX

	public void moveY() {
		//input: y_position of the rock, vertical_speed
		//output: the new y_position after movement
		this.y_pos += vertical_speed;							//move the rock vertically at the rate of vertical_speed
	}//end moveY

	public void drawRock(Graphics g) {
		//input: image, x and y position, imageWidth
		//output: the ship with image
		g.drawImage(rockIcon, (x_pos- (imageWidth/2)), y_pos, null);  //draw the image at the position of the rock
	}//end drawRock
	
}//end class
