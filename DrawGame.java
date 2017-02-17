import java.awt.*;
import javax.swing.*;
import java.awt.event.*; // needed for event handling
import java.awt.Toolkit;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Date;
import java.awt.BorderLayout;
import java.util.Scanner;

public class DrawGame extends JPanel implements KeyListener {
  boolean gameOver=false;         
  
  Toolkit toolkit;
  Timer timer;
  
  final double MIN_TIME_INTERVAL=50;
  int totalTimeSteps=0;
  int millisecs=100; // lowered the starting millisecs
  
  Snake redSnake = new Snake(new SnakeSection(10,10),1,1,Color.red);
  Snake blueSnake = new Snake(new SnakeSection(10,15),1,1,Color.blue);
  Snake orngSnake = new Snake(new SnakeSection(10,20),1,1,Color.orange);// added new snake
  
  int redScore=0;
  int blueScore=0;
  int orngScore=0; // new snake score
  
  int redCrashes=0;
  int blueCrashes=0;
  int orngCrashes=0;// new snake crashes
  
  int foodValue;
  SnakeSection foodPosition;
  
  boolean pause;
    
  public DrawGame() {    
    setFocusable(true);
    
    foodValue = 3;   // Initial food value.
    foodPosition = getNewFoodPosition();
    
    // First thing to do: Start up the periodic task:
    System.out.println("About to start the snake.");
    startSnake(millisecs);   // Argument is number of milliseconds per snake move.
    System.out.println("Snake started.");
 
    setBackground(Color.black);        
    addKeyListener(this);
  }

  public void startSnake(int milliseconds) {
    toolkit = Toolkit.getDefaultToolkit();
    timer = new Timer();
    Date firstTime = new Date();  // Start task now.
    timer.schedule(new AdvanceTheSnakeTask(), firstTime, milliseconds);
  }
    
  public void resetSnakes() {
    millisecs=100;    //when reset milisecs are lowered to 100
    totalTimeSteps=0;    
    redSnake = new Snake(new SnakeSection(10,9),1,0,Color.red);  
    blueSnake = new Snake(new SnakeSection(10,15),1,0,Color.blue);
    orngSnake = new Snake(new SnakeSection(10,20),1,1,Color.orange); // resets the new snake
    timer.cancel();
    startSnake(100); 
  }
  
  /*****************************************************************************************
  * Code for the method getNewFoodPosition() goes here
  * We need to find a position for the food that is not already on one of the snakes.
  * Position is defined by two coordinates: x and y
  * Try different positions until we find one that is not part of a snake (look in the
  * Snake class for method that may help you)
  * Once you have an acceptable position for the food, create a SnakeSection object with
  * these coordinates and return it
  *****************************************************************************************/  
  public SnakeSection getNewFoodPosition(){
   int x=10;
   int y=10;
   do{

 	x =((int)(Math.random()*40));
 	y =((int)(Math.random()*30));
 	}
   while(redSnake.contains(x,y) == true || blueSnake.contains(x,y)== true);
  
   SnakeSection foodPos = new SnakeSection(x,y);
                     
  return foodPos;
  }

  class AdvanceTheSnakeTask extends TimerTask {
    public void run() {
        
      // Put stuff here that should happen during every advance.
      if(!pause)// checks if the game is not paused
      {
      redSnake.move();             
      blueSnake.move();
      orngSnake.move(); // made the new snake move
      }
      // Here, we check to see if the snakes have collided with each other.
      // This is done by checking whether the head of one snake (SnakeSection 0)
      // is equal in position to one of the SnakeSections of the competitor's snake.
      //
      // To make the game a little more interesting, we will define a collision to 
      // be the intersection of a snake's head with another snake's body. In particular,
      // we will NOT count it as a collision if the two snakes' heads move to the same
      // position at the same time. This will allow the snakes to pass through each other!
      // 
      // It is possible for both snakes to "crash" simultaneously, if, during the same
      // time step, each snake hits the other snake's body. In this case, neither player
      // is awarded any points.
 
      boolean redHitsBlue=blueSnake.checkBodyPositions(redSnake.snakeSecs[0]);
      boolean redHitsOrng=orngSnake.checkBodyPositions(redSnake.snakeSecs[0]);// when red hits orange
      boolean blueHitsRed=redSnake.checkBodyPositions(blueSnake.snakeSecs[0]);
      boolean blueHitsOrng=orngSnake.checkBodyPositions(blueSnake.snakeSecs[0]);//when blue hits orange
      boolean orngHitsRed=redSnake.checkBodyPosition(orngSnake.snakeSecs[0]);// when orange hits red
      boolean orngHitsBlue=blueSnake.checkBodyPosition(orngSnake.snakeSecs[0]);// when orange hits blue
      
      boolean blueHitsBlue=blueSnake.checkBodyPosition(blueSnake.snakeSecs[0]);// when blue hits itself
      boolean redHitsRed=redSnake.checkBodyPosition(redSnake.snakeSecs[0]);// when red hits itself
      boolean orngHitsOrng=orngSnake.checkBodyPosition(orngSnake.snakeSecs[0]);// when orange hits itself
      
      
      
      if (redHitsBlue && !blueHitsRed) {          // true if only red crashes.
        blueScore+=blueSnake.snakeLength;
        redCrashes++;
      }
      if (redHitsOrng && !orngHitsRed)         // true if only red crashes.
      {
    	  orngScore+=orngSnake.snakeLength;
    	  redCrashes++;
      }
      if (!redHitsBlue && blueHitsRed) {          // true if only blue crashes.
        redScore+=redSnake.snakeLength;
        blueCrashes++;
      }
      if (!redHitsBlue && orngHitsRed) {          // true if only blue crashes.
          redScore+=redSnake.snakeLength;
          orngCrashes++;
        }
      if (redHitsBlue && blueHitsRed) {           // true if both snakes crash simultaneously.
        blueCrashes++;
        redCrashes++;
      }
      if(redHitsOrng && orngHitsRed)           // true if both snakes crash simultaneously.
      {
    	  orngCrashes++;
          redCrashes++;
      }
      if(blueHitsOrng && orngHitsBlue)         // true if both snakes crash simultaneously.
      {
    	  orngCrashes++;
          blueCrashes++;
      }
      if(redHitsRed && !blueHitsBlue && !orngHitsOrng)    // true if only red hits itself
      {
          redScore-=redSnake.snakeLength;
    	  redCrashes++;
      }
      if(!redHitsRed && blueHitsBlue && !orngHitsOrng)   // true if only blue hits itself
      {
          blueScore-=blueSnake.snakeLength;
    	  blueCrashes++;
      }
      if(!redHitsRed && !blueHitsBlue && orngHitsOrng)   // true if only orange hits itself
      {
    	  orngScore-=orngSnake.snakeLength;
    	  orngCrashes++;
      }
      if (redHitsBlue || redHitsOrng || 
    	  blueHitsRed || blueHitsOrng||
    	  orngHitsRed || orngHitsBlue ||
    		  redHitsRed || blueHitsBlue || orngHitsOrng ) {           // true if ANY snake crashes.
        resetSnakes();//
      }
      if (redCrashes==5 || blueCrashes==5 || orngCrashes==5 ||
    		  redScore==100 || blueScore==100 || orngScore==100) {      // game ends after one player has crashed 5 times.
        gameOver=true;
      }
      
      // Here, we check to see if the snake has eaten the current food.
      // Note that we will only check to see if the head of the snake (SnakeSection 0)
      // is in the same place as the food.
      //
      // Note that if both snakes get to the food simultaneously, they both
      // get to eat it.
      
      boolean newFood=false;
      if (redSnake.snakeSecs[0].match(foodPosition)) {
        redSnake.snakeLength+=foodValue;
        newFood=true;
      }
      if (blueSnake.snakeSecs[0].match(foodPosition)) {
        blueSnake.snakeLength+=foodValue;
        newFood=true;
      }
      if (orngSnake.snakeSecs[0].match(foodPosition)) { // added new snake to be able to eat food
          orngSnake.snakeLength+=foodValue;
          newFood=true;
        }
      if (newFood) {
        foodValue=(int) (Math.random()*8+1);       // Food has value from 1 to 9.
        foodPosition=getNewFoodPosition();
      }
      
      totalTimeSteps++;      
      if (totalTimeSteps%50 == 0) {                 // Update speed every 50 time steps.
        timer.cancel();                             // Cancel previous periodic events.
        if (millisecs>MIN_TIME_INTERVAL) 
          millisecs = (int) (millisecs * .9);       // Reduce current delay by 10%.  
        System.out.print(millisecs+" ");            // Diagnostic.
        startSnake(millisecs);
      }
      repaint();
    }
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);    
    if (gameOver) {
      g.setColor(Color.magenta);
      g.drawString("*********************************",300,200);
      g.drawString("*********** Game over **********",300,220);
      g.drawString("************* Loser *************",300,240);
      g.drawString("Red score: "+redScore+"    Blue score: "+blueScore+" ",300,260);
      g.drawString("********Orange score: "+orngScore+"**********",300,280); // added orange to gameOver
      g.drawString("*********************************",300,300);
      g.drawString("*** Hit any key to quit. ********",300,340);
      
      timer.cancel();          // We must cancel the periodic events scheduled by the timerTask.
    }
    
    // The body of the else paints the screen at each time step of the game.
    // It shows the score of each player, the number of crashes. Then it draws the snakes and
    // the food, with the value of the food in the food box.
    
    else {     
      // Show the score information.
      g.setColor(Color.red);
      g.drawString("Red Snake", 30, 15);
      g.drawString("Score: "+redScore+"  Crashes: "+redCrashes,4,30);
      g.setColor(Color.blue);
      g.drawString("Blue Snake", 680, 15);
      g.drawString("Score: "+blueScore+"  Crashes: "+blueCrashes,654,30);
      g.setColor(Color.orange); 
      g.drawString("Orange Snake", 340, 15); // added an orange GUI
      g.drawString("Score: "+orngScore+" Crashes: "+orngCrashes, 320, 30);
      
      // Draw the snakes.
      redSnake.paint(g);    
      blueSnake.paint(g);
      orngSnake.paint(g); // paints the new snake
      
      // Draw food.
     
      g.setColor(Color.yellow); // changed the color of food to yellow
      g.fillOval(foodPosition.x*20,foodPosition.y*20,20,20); // fills food with the color
      g.drawOval(foodPosition.x*20,foodPosition.y*20,20,20);
      g.setColor(Color.white); // changes the font color o white
      g.drawString(""+foodValue,foodPosition.x*20+5,foodPosition.y*20+15);
    }
  }
  
  public void keyTyped(KeyEvent e) {
    if (gameOver) {
      System.exit(0);
    }
    if (e.getKeyChar()=='a') {              // Red snake left.
      redSnake.dirX=-1;
      redSnake.dirY=0;
    }
    else if (e.getKeyChar()=='d') {         // Red snake right.
      redSnake.dirX=1;
      redSnake.dirY=0;
    }
    else if (e.getKeyChar()=='w') {         // Red snake up.
      redSnake.dirX=0;
      redSnake.dirY=-1;
    }
    else if (e.getKeyChar()=='s') {         // Red snake down.
      redSnake.dirX=0;
      redSnake.dirY=1;
    }
    else if (e.getKeyChar()=='4') {         // Blue snake left.
      blueSnake.dirX=-1;
      blueSnake.dirY=0;
    }
    else if (e.getKeyChar()=='6') {        // Blue snake right.
      blueSnake.dirX=1;
      blueSnake.dirY=0;
    }
    else if (e.getKeyChar()=='8') {         // Blue snake up.
      blueSnake.dirX=0;
      blueSnake.dirY=-1;
    }
    else if (e.getKeyChar()=='5') {         // Blue snake down.
      blueSnake.dirX=0;
      blueSnake.dirY=1;
    }
    else if(e.getKeyChar()=='u'){           // Orange snake up.
    	orngSnake.dirX=0;
    	orngSnake.dirY=-1;
    }
    else if(e.getKeyChar()=='h'){         // Orange snake left.
    	orngSnake.dirX=-1;
    	orngSnake.dirY=0;
    }
    else if(e.getKeyChar()=='j'){         // Orange snake down.
    	orngSnake.dirX=0;
    	orngSnake.dirY=1;
    }
    else if(e.getKeyChar()=='k'){        // Orange snake right.
    	orngSnake.dirX=1;
    	orngSnake.dirY=0;
    }
  }
  

    // Ignore key which is held down.
    public void keyPressed(KeyEvent e) { 
    	if(e.getKeyChar()=='p') // pauses the game
    	{
    		if(pause)
    			pause=false;
    		else
    			pause=true;
    	}
    }

    // Ignore key release events.
    public void keyReleased(KeyEvent e) {
    }

}
