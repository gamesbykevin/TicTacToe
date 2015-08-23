package com.gamesbykevin.tictactoe.board;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.gamesbykevin.androidframework.base.Cell;

import com.gamesbykevin.tictactoe.panel.GamePanel;

/**
 * A tic-tac-toe board
 * @author ABRAHAM
 */
public final class Board 
{
    /**
     * The solution key for the board
     */
    private int[][] board;
    
    public static final int KEY_EMPTY = 0;
    public static final int KEY_X = 1;
    public static final int KEY_O = 2;
    
    //is the game over
    private boolean gameover = false;
    
    /**
     * The default dimensions of our tic-tac-toe board
     */
    public static final int DEFAULT_BOARD_DIMENSION = 3;
    
    /**
     * The dimensions for each cell
     */
    public static final int CELL_DIMENSION = 120;
    
    //(x, y) coordinates where the board begins
    private int startX, startY;
    
    //assign paint parameters here to draw the board etc...
    private Paint backgroundPaint, matchPaint;
    
    //which key won the board
    private int winningKey;
    
    //the locations identifying the match
    private Cell matchStart, matchEnd;
    
    public Board()
    {
        //create a new board with the default dimensions
        createBoard(DEFAULT_BOARD_DIMENSION, DEFAULT_BOARD_DIMENSION);
    }
    
    public void setMatchLocation(final int startCol, final int startRow, final int endCol, final int endRow)
    {
        this.matchStart = new Cell(startCol, startRow);
        this.matchEnd = new Cell(endCol, endRow);
    }
    
    public void setGameover(final boolean gameover)
    {
        this.gameover = gameover;
    }
    
    public boolean hasGameover()
    {
        return this.gameover;
    }
    
    /**
     * Assign the starting x-coordinate for the first location (0,0)
     * @param startX The x-coordinate
     */
    public final void setX(final double startX)
    {
        this.startX = (int)startX;
    }
    
    /**
     * Assign the starting y-coordinate for the first location (0,0)
     * @param startY The y-coordinate
     */
    public final void setY(final double startY)
    {
        this.startY = (int)startY;
    }
    
    /**
     * Create a new board of the specified dimensions
     * @param cols Columns
     * @param rows Rows
     */
    private void createBoard(final int cols, final int rows)
    {
        //create new board
        this.board = new int[rows][cols];
        
        //assign the default values
        reset();
        
        //position board in the middle
        setX((GamePanel.WIDTH * .5) - (getBoardWidth() * .5));
        setY((GamePanel.HEIGHT * .5) - (getBoardHeight() * .75));
    }
    
    /**
     * Get the overall width of the board
     * @return The overall width of the board
     */
    public int getBoardWidth()
    {
        return getBoardKey()[0].length * CELL_DIMENSION;
    }
    
    /**
     * Get the overall height of the board
     * @return The overall height of the board
     */
    public int getBoardHeight()
    {
        return getBoardKey().length * CELL_DIMENSION;
    }
    
    /**
     * Get the board key
     * @return The array containing the keys
     */
    public int[][] getBoardKey()
    {
        return this.board;
    }
    
    /**
     * Get the winning key
     * @return The key that won the board
     */
    public int getWinningKey()
    {
        return this.winningKey;
    }
    
    /**
     * Assign the winning key
     * @param winningKey The key that won the board
     */
    public void setWinningKey(final int winningKey)
    {
        this.winningKey = winningKey;
    }
    
    /**
     * Get the key value at the specified location
     * @param col Column
     * @param row Row
     * @return The key value at the specified location
     */
    public int getKey(final int col, final int row)
    {
        return getBoardKey()[row][col];
    }
    
    /**
     * Assign a key to the board based on the (x,y) coordinate
     * @param x x-coordinate
     * @param y y-coordinate
     * @param key The key value we want to assign
     * @return true if assigning a key value was successful, false otherwise
     */
    public boolean assignKey(final float x, final float y, final int key)
    {
        for (int col = 0; col < getBoardKey()[0].length; col++)
        {
            for (int row = 0; row < getBoardKey().length; row++)
            {
                //calculate the corners to check for collision
                final int x_west = startX + (col * CELL_DIMENSION);
                final int x_east = x_west + CELL_DIMENSION;
                final int y_north = startY + (row * CELL_DIMENSION);
                final int y_south = y_north + CELL_DIMENSION;
                
                if (x > x_west && x < x_east && y > y_north && y < y_south)
                {
                    switch (getKey(col, row))
                    {
                        //we can only make a move where empty
                        case KEY_EMPTY:
                            assignKey(col, row, key);

                            //return true since success
                            return true;
                            
                        //return false since occupied
                        default:
                            return false;
                    }
                }
            }
        }
        
        //no collision found return false
        return false;
    }
    
    /**
     * Do we have a match on the board? (a.k.a. # in a row)
     * @param key The key to check for a match
     * @return true = yes, false = no
     */
    public boolean hasMatch(final int key)
    {
        return BoardHelper.hasMatch(this, key);
    }
    
    /**
     * Assign a value to the specified place on the board
     * @param col Column
     * @param row Row
     * @param key The result (O, X, etc...)
     */
    public void assignKey(final int col, final int row, final int key)
    {
        getBoardKey()[row][col] = key;
        
        //if the board is full set game over
        if (BoardHelper.isFull(this))
            setGameover(true);
    }
    
    /**
     * Reset all places in the board to empty
     */
    public void reset()
    {
        for (int col = 0; col < getBoardKey()[0].length; col++)
        {
            for (int row = 0; row < getBoardKey().length; row++)
            {
                assignKey(col, row, KEY_EMPTY);
            }
        }
        
        //no winning key
        setWinningKey(KEY_EMPTY);
        
        //the game is not over
        setGameover(false);
    }
    
    private int getCellCenterX(final int col)
    {
        return (startX + (col * CELL_DIMENSION) + (CELL_DIMENSION / 2));
    }
    
    private int getCellCenterY(final int row)
    {
        return (startY + (row * CELL_DIMENSION) + (CELL_DIMENSION / 2));
    }
    
    /**
     * Draw the tic-tac-toe board
     * @param canvas
     * @param imageX
     * @param imageO 
     */
    public void draw(final Canvas canvas, final Bitmap imageX, final Bitmap imageO)
    {
        //draw the background
        drawBackground(canvas);
        
        for (int col = 0; col < getBoardKey()[0].length; col++)
        {
            final int x = getCellCenterX(col);
            
            for (int row = 0; row < getBoardKey().length; row++)
            {
                final int y = (getCellCenterY(row));
                
                switch (getKey(col, row))
                {
                    case KEY_X:
                        canvas.drawBitmap(imageX, x - (imageX.getWidth() / 2), y - (imageX.getHeight() / 2), null);
                        break;
                        
                    case KEY_O:
                        canvas.drawBitmap(imageO, x - (imageO.getWidth() / 2), y - (imageO.getHeight() / 2), null);
                        break;
                        
                    //do nothing here
                    case KEY_EMPTY:
                    default:
                        break;
                }
            }
        }
        
        //if the game is over, draw the result
        if (hasGameover())
            drawGameover(canvas);
    }
    
    private void drawGameover(final Canvas canvas)
    {
        switch (getWinningKey())
        {
            //draw the line to highlight the winner
            case KEY_X:
            case KEY_O:
                if (matchStart != null && matchEnd != null)
                {
                    //create paint object if not exists
                    if (matchPaint == null)
                    {
                        this.matchPaint = new Paint();
                        this.matchPaint.setColor(Color.BLUE);
                        this.matchPaint.setStrokeWidth(15f);
                    }

                    //calculate coordinates
                    final int x1 = getCellCenterX((int)matchStart.getCol());
                    final int y1 = getCellCenterY((int)matchStart.getRow());
                    final int x2 = getCellCenterX((int)matchEnd.getCol());
                    final int y2 = getCellCenterY((int)matchEnd.getRow());

                    //draw the match line
                    canvas.drawLine(x1, y1, x2, y2, matchPaint);
                }
                
                break;

            //it was a draw
            default:
                break;
        }
    }
    
    /**
     * Draw the background of the board
     * @param canvas Object we want to render pixel data to
     */
    private void drawBackground(final Canvas canvas)
    {
        //create paint object if it doesn't exist
        if (this.backgroundPaint == null)
        {
            this.backgroundPaint = new Paint();
            this.backgroundPaint.setColor(Color.WHITE);
            this.backgroundPaint.setStrokeWidth(10f);
        }
        else
        {
            for (int col = 1; col < getBoardKey()[0].length; col++)
            {
                final int x = startX + (col * CELL_DIMENSION);
                canvas.drawLine(x, startY, x, startY + getBoardHeight(), this.backgroundPaint);
            }

            for (int row = 1; row < getBoardKey().length; row++)
            {
                final int y = startY + (row * CELL_DIMENSION);
                canvas.drawLine(startX, y, startX + getBoardWidth(), y, this.backgroundPaint);
            }
        }
    }
}