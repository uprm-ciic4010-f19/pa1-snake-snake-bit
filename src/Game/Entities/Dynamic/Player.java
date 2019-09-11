package Game.Entities.Dynamic;

import Main.Handler;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JOptionPane;

import Game.GameStates.State;

/**
 * Created by AlexVR on 7/2/2018.
 */
public class Player {

    public int lenght;
    public boolean justAte;
    private Handler handler;

    public int xCoord;
    public int yCoord;

    public int moveCounter;
    public int moveSpeedChange = 3; // This variable allows for change in speed. -Jesus
    
    public int scoreNum; // Created variable for score. -Neff

    public String direction;//is your first name one?

    public Player(Handler handler){
        this.handler = handler;
        xCoord = 0;
        yCoord = 0;
        moveCounter = 0;
        direction= "Right";
        justAte = false;
        lenght= 1;

    }

    public void tick(){
        moveCounter++; // Condition is now depending on moveSpeedChange. -Jesus
        if(moveCounter>=moveSpeedChange) {
            checkCollisionAndMove();
            moveCounter=0;
            
        //Figured Backtracking with if statements. -Neff
        }
        if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_UP)){ 
            if(direction != "Down") {
            	 direction="Up";
            }  
        }if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_DOWN)){
            if(direction != "Up") {
           	 direction="Down";
           }
        }if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_LEFT)){
             if(direction != "Right") {
           	 direction="Left";
           }
        }if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_RIGHT)){
            if(direction != "Left") {
           	 direction="Right";
           }
        }if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_N)) {
        	handler.getWorld().body.addLast(new Tail(xCoord, yCoord,handler)); // Tail is  generated at the press of N key. -Jesus
        }if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_EQUALS)) {
        	moveSpeedChange++; // moveSpeedChange increases at the press of = key. -Jesus
        }if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_MINUS)) {
        	moveSpeedChange--; // moveSpeedChange decreases at the press of - key. -Jesus
        }if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_ESCAPE)) {
        	State.setState(handler.getGame().pauseState); //Introduced Escape Key to Pause the game. -Neff
        	
        }
        

    }
    //Make Snake Teleport when hitting a wall. -Neff
    public void checkCollisionAndMove(){
        handler.getWorld().playerLocation[xCoord][yCoord]=false;
        int x = xCoord;
        int y = yCoord;
        
        switch (direction){
            case "Left":
                if(xCoord==0){
                    xCoord = handler.getWorld().GridWidthHeightPixelCount-1;
                }else{
                    xCoord--;
                }
                break;
            case "Right":
                if(xCoord==handler.getWorld().GridWidthHeightPixelCount-1){
                    xCoord = 0;
                }else{
                    xCoord++;
                }
                break;
            case "Up":
                if(yCoord==0){
                	yCoord = handler.getWorld().GridWidthHeightPixelCount-1;
                }else{
                    yCoord--;
                }
                break;
            case "Down":
                if(yCoord==handler.getWorld().GridWidthHeightPixelCount-1){
                	yCoord = 0;
                }else{
                    yCoord++;
                }
                break;
        }
        handler.getWorld().playerLocation[xCoord][yCoord]=true;

        if(handler.getWorld().appleLocation[xCoord][yCoord]){
            Eat();
        }

        if(!handler.getWorld().body.isEmpty()) {
            handler.getWorld().playerLocation[handler.getWorld().body.getLast().x][handler.getWorld().body.getLast().y] = false;
            handler.getWorld().body.removeLast();
            handler.getWorld().body.addFirst(new Tail(x, y,handler));
        }
        
      if(handler.getWorld().body.size() > 0) {
      	for(int i = 0; i < handler.getWorld().body.size(); i++) {
      		if(handler.getWorld().body.get(0) != handler.getWorld().body.get(i)) {
      			if(xCoord == handler.getWorld().body.get(i).x && yCoord == handler.getWorld().body.get(i).y)
      				kill();
      		}
      	}
      }
        
    }

    public void render(Graphics g,Boolean[][] playeLocation){
        Random r = new Random();
        
        //Created Score String in the next three lines. -Neff
        g.setColor(new Color(0, 0, 0));
        g.setFont(new Font("System", Font.PLAIN, 20));
        g.drawString("Score: " + scoreNum , 0, 20);
        
        
        for (int i = 0; i < handler.getWorld().GridWidthHeightPixelCount; i++) {
            for (int j = 0; j < handler.getWorld().GridWidthHeightPixelCount; j++) {
                g.setColor(Color.GREEN);
                //Changed the color of the snake to Green. -Neff

                if(playeLocation[i][j]||handler.getWorld().appleLocation[i][j]){
                    g.fillRect((i*handler.getWorld().GridPixelsize),
                            (j*handler.getWorld().GridPixelsize),
                            handler.getWorld().GridPixelsize,
                            handler.getWorld().GridPixelsize);
                }

            }
        }


    }

    public void Eat() {
        moveSpeedChange += 0 + 1; // 0 is my partner's last Student ID digit. -Jesus
        scoreNum += Math.sqrt(2 * scoreNum + 1); //Added Score Equation. -Neff
        lenght++;
        handler.getWorld().appleLocation[xCoord][yCoord]=false;
        handler.getWorld().appleOnBoard=false;
        Tail tail= null;
        
        switch (direction){
            case "Left":
                if( handler.getWorld().body.isEmpty()){
                    if(this.xCoord!=handler.getWorld().GridWidthHeightPixelCount-1){
                        tail = new Tail(this.xCoord+1,this.yCoord,handler);
                    }else{
                        if(this.yCoord!=0){
                            tail = new Tail(this.xCoord,this.yCoord-1,handler);
                        }else{
                            tail =new Tail(this.xCoord,this.yCoord+1,handler);
                        }
                    }
                }else{
                    if(handler.getWorld().body.getLast().x!=handler.getWorld().GridWidthHeightPixelCount-1){
                        tail=new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler);
                    }else{
                        if(handler.getWorld().body.getLast().y!=0){
                            tail=new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler);
                        }else{
                            tail=new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler);

                        }
                    }

                }
                break;
            case "Right":
                if( handler.getWorld().body.isEmpty()){
                    if(this.xCoord!=0){
                        tail=new Tail(this.xCoord-1,this.yCoord,handler);
                    }else{
                        if(this.yCoord!=0){
                            tail=new Tail(this.xCoord,this.yCoord-1,handler);
                        }else{
                            tail=new Tail(this.xCoord,this.yCoord+1,handler);
                        }
                    }
                }else{
                    if(handler.getWorld().body.getLast().x!=0){
                        tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
                    }else{
                        if(handler.getWorld().body.getLast().y!=0){
                            tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler));
                        }else{
                            tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler));
                        }
                    }

                }
                break;
            case "Up":
                if( handler.getWorld().body.isEmpty()){
                    if(this.yCoord!=handler.getWorld().GridWidthHeightPixelCount-1){
                        tail=(new Tail(this.xCoord,this.yCoord+1,handler));
                    }else{
                        if(this.xCoord!=0){
                            tail=(new Tail(this.xCoord-1,this.yCoord,handler));
                        }else{
                            tail=(new Tail(this.xCoord+1,this.yCoord,handler));
                        }
                    }
                }else{
                    if(handler.getWorld().body.getLast().y!=handler.getWorld().GridWidthHeightPixelCount-1){
                        tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler));
                    }else{
                        if(handler.getWorld().body.getLast().x!=0){
                            tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
                        }else{
                            tail=(new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler));
                        }
                    }

                }
                break;
            case "Down":
                if( handler.getWorld().body.isEmpty()){
                    if(this.yCoord!=0){
                        tail=(new Tail(this.xCoord,this.yCoord-1,handler));
                    }else{
                        if(this.xCoord!=0){
                            tail=(new Tail(this.xCoord-1,this.yCoord,handler));
                        }else{
                            tail=(new Tail(this.xCoord+1,this.yCoord,handler));
                        } System.out.println("Tu biscochito");
                    }
                }else{
                    if(handler.getWorld().body.getLast().y!=0){
                        tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler));
                    }else{
                        if(handler.getWorld().body.getLast().x!=0){
                            tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
                        }else{
                            tail=(new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler));
                        }
                    }

                }
                break;
        }
        handler.getWorld().body.addLast(tail);
        handler.getWorld().playerLocation[tail.x][tail.y] = true;
    }

    public void kill(){
        lenght = 0;
        for (int i = 0; i < handler.getWorld().GridWidthHeightPixelCount; i++) {
            for (int j = 0; j < handler.getWorld().GridWidthHeightPixelCount; j++) {
                handler.getWorld().playerLocation[i][j]=false;
            }
        }
        JOptionPane.showMessageDialog(null, "You made the snake eat itself, so you lost!",
    			"Game Over", JOptionPane.OK_OPTION);
    	State.setState(handler.getGame().menuState);
    }

    public boolean isJustAte() {
        return justAte;
    }

    public void setJustAte(boolean justAte) {
        this.justAte = justAte;
    }
}
