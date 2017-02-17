import java.awt.Color;
import java.awt.Graphics;

public class Snake {
static final int SCREEN_SIZE_X=40; // In units of snake sections.
static final int SCREEN_SIZE_Y=30;
final int MAX_SNAKE_LENGTH = 1000;

// While a snake is created with a very large number of snake sections,
// determined by the constant variable MAX_SNAKE_LENGTH, the actual
// apparent length of the snake in the game will be controlled by the
// variable snakeLength. At each time step, the program will only draw
// snakeLength snake sections for each snake.
//
// Each time the snake eats some "food" (a yellow box on the screen),
// the snakeLength for that snake will
// grow by the value of the food, which is printed in the box representing the
// food.

int snakeLength = 5; // Start snakes with length 5.

SnakeSection [] snakeSecs = new SnakeSection[MAX_SNAKE_LENGTH];

// These variables represent the direction the snake is going.
// Each time step, the snake moves in the direction represented by these
// variables. The program does this by adding these values to the previous
// head position of the snake. For example, the snake goes left initially,
// since by adding -1 to the x value (dirX = -1) and adding 0 to the y value
// (dirY=0), the head of the snake moves one square to the left.

int dirX=-1;
int dirY=0;

Color color; // Holds the color of the snake.

public Snake(SnakeSection startPos,int dx,int dy,Color color) {

	// Here, we are creating a large number of snake sections (1000 of them) so
// that we don't have to worry about creating them later.

	for (int i=0; i<MAX_SNAKE_LENGTH; i++)
         snakeSecs[i]=new SnakeSection(0,0);

	// Set the color of the snake based upon the formal parameter.
	this.color=color;
// Here, we create and INITIALIZE the snake sections that are going to be visible
// at the beginning. We give these locations using a starting position, and offsets
// from the start position.
//
// NOTE: Strictly speaking, it is unnecessary to CREATE the snake sections below
// using the new command, since these snake sections have already been created
// in the code just above, and all we really want to do is initialize the coordinates
// of the snake sections. However, in this case, it is easiest to use the
// constructor to initialize the snake sections to the values we want. In order
// to use the constructor, we must call new, and thus create the same snake
// sections again. It is a little bit of wasted effort, but it won't hurt anything.

	for (int i=0; i<snakeLength; i++)
		snakeSecs[i]=new SnakeSection(startPos.x+i*dx,startPos.y+i*dy);}


public boolean contains(int x, int y){
	boolean yesNo = false;
	SnakeSection testSection = new SnakeSection(x,y);
	for (int i = 1 ; i < snakeLength; i++){
		if (snakeSecs[i] == testSection)
			yesNo= true;
								}
return yesNo;
}

public boolean checkBodyPositions(SnakeSection s){
	boolean yesNo = false;
	for (int i=0; i<snakeLength; i++){
		if (s.match(snakeSecs[i])== true){
			yesNo=true;
		}}
	return yesNo;
}

public boolean checkBodyPosition(SnakeSection s) // checks the position of the snakes sections minus the head
{
	boolean yesNo = false;
	for(int i=1; i < snakeLength; i++)
	{
		if(s.match(snakeSecs[i]) == true)
		{
			yesNo=true;
		}
	}
	return yesNo;
}

public void move() {

	for (int i=snakeLength-1; i>0; i--)
	snakeSecs[i]=snakeSecs[i-1];
	int newX=(snakeSecs[1].x + dirX + SCREEN_SIZE_X) % SCREEN_SIZE_X;
	int newY=(snakeSecs[1].y + dirY + SCREEN_SIZE_Y) % SCREEN_SIZE_Y;
	snakeSecs[0]=new SnakeSection(newX,newY);
}
// A snake is painted by drawing a square for each snake section. Each square is 20 by 20 pixels.

public void paint(Graphics g) {
	g.setColor(color);
	
	for (int i=0; i<snakeLength; i++) {
		g.fillRoundRect(snakeSecs[i].x*20, snakeSecs[i].y*20, 20, 20,10,10);
		g.drawRoundRect(snakeSecs[i].x*20,snakeSecs[i].y*20,20,20,10,10);
}
}
}
