package com.codegym.games.game2048;

import com.codegym.engine.cell.*;
import java.util.Arrays;

public class Game2048 extends Game{
    private static final int SIDE = 4;
    private int[][] gameField = new int[SIDE][SIDE];
    int x;
    int y;
    private boolean isGameStopped = false;
    private int score;
        
   @Override
    public void initialize(){
       setScreenSize(SIDE, SIDE);
       createGame();
       drawScene();       
    }

   private void createGame(){
       gameField = new int [SIDE][SIDE];
       createNewNumber();
       createNewNumber();
   }
   
    private void drawScene(){
        for (int i = 0; i < SIDE; i++){
            for (int j = 0; j < SIDE; j++ ){
                setCellColoredNumber(i, j, gameField[j][i]);
            }
        }
    }
   
    private void createNewNumber(){
        if(getMaxTileValue()== 2048){
            win();
        }
       
        int x = getRandomNumber(SIDE);
        int y = getRandomNumber(SIDE);
        int chance = getRandomNumber(10);
      
        if (chance == 9){
          gameField[x][y] = 4;
        } else {
          gameField[x][y]= 2;
        }          
    }
    
    private Color getColorByValue(int n){
        Color color = null;
        switch(n){
            case 0:
                color = Color.WHITE;
                break;
            case 2:
                color = Color.YELLOW;
                break;
            case 4:
                color = Color.PURPLE;
                break;
                
            case 8:
                color = Color.BLUE;
                break;

            case 16:
                color = Color.CYAN;
                break;

            case 32:
                color = Color.GREEN;
                break;

            case 64:
                color = Color.LIGHTGRAY;
                break;

            case 128:
                color = Color.ORANGE;
                break;

            case 256:
                color = Color.CORAL;
                break;

            case 512:
                color = Color.RED;
                break;
            case 1024:
                color = Color.PINK;
                break;

            case 2048:
                color = Color.MAGENTA;
                break;
            
            default:
                color = Color.WHITE;
                break;
        }
    return color;
    }
    
    private void setCellColoredNumber(int x, int y, int value) {         
        Color color = getColorByValue(value);         
        if (value > 0) {             
            setCellValueEx(x, y, color, Integer.toString(value));         
        } else {             
            setCellValueEx(x,y, color, "");         
        }     
    }
   
    private boolean compressRow(int[]row){
        int[]tempRow = row.clone();
        boolean isChanged = false;
        for (int i = 0; i < row.length; i++) {
        for (int j = 0; j < row.length-1; j++) {
            if (row[j] == 0) { 
                int temp = row[j]; 
                row[j] = row[j+1]; 
                row[j+1] = temp; 
            } 
        }
    }

       if(!Arrays.equals(row,tempRow)){
        isChanged = true;
       }
    return isChanged;      
    }
   
    private boolean mergeRow(int[] row){
        boolean moved = false;
        for(int i = 0; i < row.length-1; i++){
            if ((row[i] == row[i + 1])&& (row[i]!= 0)){
                row[i] = 2*row[i];
                row[i+1] = 0;
                moved = true;
                score += row[i];
                setScore(score);
            }
        }
    return false;
   }
   
    @Override
    public void onKeyPress(Key key){
        if(isGameStopped){           
           if(key == Key.SPACE){
               isGameStopped = false;
              score = 0;
              setScore(score);
               createGame();
               drawScene();
           }
        } else if(canUserMove() == false){
           gameOver();
        } else {
            if( key == Key.LEFT) {
                drawScene();
                moveLeft();
            } else if(key == Key.RIGHT) {
                drawScene();
                moveRight();
            }
            else if(key == Key.UP) {           
                drawScene();
                moveUp();
            } else if(key == Key.DOWN){
                drawScene();
                moveDown();
            }
        }
    }
    
    private void rotateClockwise(){       
        for (int i = 0; i < SIDE / 2; i++){
            for (int j = i; j < SIDE - i - 1; j++){  
                int temp = gameField[i][j];                 
                gameField[i][j] = gameField[SIDE - 1 - j][i];                 
                gameField[SIDE - 1 - j][i] = gameField[SIDE - 1 - i][SIDE - 1 - j]; 
                gameField[SIDE - 1 - i] [SIDE - 1 - j] = gameField[j][SIDE - 1 - i];                 
                gameField[j][SIDE - 1 - i] = temp;             
            } 
        }  
    }
        
    private void moveLeft(){
        boolean compress;
        boolean merge;
        boolean compress2;
        
        int move = 0;
        for(int i = 0; i < SIDE; i++){
            compress = compressRow(gameField[i]);
            merge = mergeRow(gameField[i]);
            compress2 = compressRow(gameField[i]);
            if(compress || merge || compress2){
                move++;
            }
        }
        
        if(move != 0){
            createNewNumber();
        }
    }
    private void moveRight(){
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();        
    }

    private void moveUp(){
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();        
    }

    private void moveDown(){
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();        
    }
    
    private int getMaxTileValue() {
        int max = 0;
        for (int i = 0 ; i < SIDE; i++){
            for (int j = 0 ; j<SIDE; j++){
                if (gameField[i][j] > max){
                    max = gameField[i][j];
                }
            }
        }
         return max;
    }
    
    private void win(){
       isGameStopped = true;
       showMessageDialog(Color.BLUE, "You win!", Color.ORANGE,50);
    }
    private boolean canUserMove(){
        for(int i = 0; i < SIDE ; i++){
            for(int j = 0; j  < SIDE ; j++){
                if( gameField[i][j] == 0){
                    return true;
                }
            }
        }

        for(int i = 0; i<SIDE ;i++){
            for(int j = 0; j  < SIDE-1 ; j++){
                if(gameField[i][j] == gameField[i][j+1]){
                    return true;
                }
            }
        }

        for(int i = 0; i<SIDE-1 ;i++){
            for(int j = 0; j  < SIDE ; j++){
                if(gameField[i][j] == gameField[i+1][j]){
                    return true;
                }
            }
        }
        return false;
    }
    
    private void gameOver(){
        isGameStopped = true;
        showMessageDialog(Color.RED, "You did not win", Color.BLACK, 20);
    }
}