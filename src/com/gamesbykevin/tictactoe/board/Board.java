package com.gamesbykevin.tictactoe.board;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import com.gamesbykevin.tictactoe.panel.GamePanel;

/**
 * A tic tac toe board
 * @author ABRAHAM
 */
public final class Board 
{
    /**
     * The solution key for the board
     */
    private int[][] board;
    
    public static final int EMPTY = 0;
    public static final int KEY_X = 1;
    public static final int KEY_O = 2;
    
    /**
     * The default dimensions of our tic-tac-toe board
     */
    public static final int DEFAULT_BOARD_DIMENSION = 3;
    
    /**
     * The dimensions for each cell
     */
    public static final int CELL_DIMENSION = 130;
    
    //the dimensions of each cell
    private int cellDimension = CELL_DIMENSION;
    
    //(x, y) coordinates where the board begins
    private int startX, startY;
    
    //assign paint parameters here to draw the board
    private Paint paint;
    
    public Board(final int cols, final int rows)
    {
        //create a new board with the default dimensions
        createBoard(cols, rows);
    }
    
    /**
     * Assign the starting x-coordinate for the first location (0,0)
     * @param startX The x-coordinate
     */
    public final void setX(final int startX)
    {
        this.startX = startX;
    }
    
    /**
     * Assign the starting y-coordinate for the first location (0,0)
     * @param startY The y-coordinate
     */
    public final void setY(final int startY)
    {
        this.startY = startY;
    }
    
    /**
     * Assign the dimension of a single cell
     * @param cellDimension The (width/height) of a single cell
     */
    public void assignCellDimension(final int cellDimension)
    {
        this.cellDimension = cellDimension;
    }
    
    /**
     * Get the dimensions of a single cell
     * @return The (width/height) of a single cell
     */
    public int getCellDimension()
    {
        return this.cellDimension;
    }
    
    /**
     * Create a new board of the specified dimensions
     * @param cols Columns
     * @param rows Rows
     */
    public final void createBoard(final int cols, final int rows)
    {
        //create new board
        this.board = new int[rows][cols];
        
        //assign the default values
        reset();
        
        //assign the cell dimension
        this.assignCellDimension(CELL_DIMENSION);
        
        //position board in the middle
        setX((GamePanel.WIDTH / 2) - (getBoardWidth() / 2));
        setY((GamePanel.HEIGHT / 2) - (getBoardHeight() / 2));
    }
    
    /**
     * Get the overall width of the board
     * @return The overall width of the board
     */
    public int getBoardWidth()
    {
        return getBoard()[0].length * getCellDimension();
    }
    
    /**
     * Get the overall height of the board
     * @return The overall height of the board
     */
    public int getBoardHeight()
    {
        return getBoard().length * getCellDimension();
    }
    
    private int[][] getBoard()
    {
        return this.board;
    }
    
    /**
     * Get the key value at the specified location
     * @param col Column
     * @param row Row
     * @return The key value at the specified location
     */
    private int getKey(final int col, final int row)
    {
        return getBoard()[row][col];
    }
    
    /**
     * Assign a key to the board
     * @param x x-coordinate
     * @param y y-coordinate
     * @param key The key value we want to assign
     * @return true if assigning a key value was successful, false otherwise
     */
    public boolean assignKey(final float x, final float y, final int key)
    {
        for (int col = 0; col < getBoard()[0].length; col++)
        {
            for (int row = 0; row < getBoard().length; row++)
            {
                //calculate the corners to check for collision
                final int x_west = startX + (col * getCellDimension());
                final int x_east = x_west + getCellDimension();
                final int y_north = startY + (row * getCellDimension());
                final int y_south = y_north + getCellDimension();
                
                if (x > x_west && x < x_east && y > y_north && y < y_south)
                {
                    if (getKey(col, row) == EMPTY)
                    {
                        //assign the value
                        assign(col, row, key);
                        
                        //return true since success
                        return true;
                    }
                    else
                    {
                        //return false since occupied
                        return false;
                    }
                }
            }
        }
        
        //no collision found return false
        return false;
    }
    
    /**
     * Assign a value to the specified place on the board
     * @param col Column
     * @param row Row
     * @param key The result (O, X, etc...)
     */
    private void assign(final int col, final int row, final int key)
    {
        getBoard()[row][col] = key;
    }
    
    /**
     * Reset all places in the board to empty
     */
    public void reset()
    {
        for (int col = 0; col < getBoard()[0].length; col++)
        {
            for (int row = 0; row < getBoard().length; row++)
            {
                assign(col, row, EMPTY);
            }
        }
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
        
        for (int col = 0; col < getBoard()[0].length; col++)
        {
            final int x = startX + (col * getCellDimension()) + (getCellDimension() / 2);
            
            for (int row = 0; row < getBoard().length; row++)
            {
                final int y = startY + (row * getCellDimension()) + (getCellDimension() / 2);
                
                switch (getKey(col, row))
                {
                    case KEY_X:
                        canvas.drawBitmap(imageX, x - (imageX.getWidth() / 2), y - (imageX.getHeight() / 2), null);
                        break;
                        
                    case KEY_O:
                        canvas.drawBitmap(imageO, x - (imageO.getWidth() / 2), y - (imageO.getHeight() / 2), null);
                        break;
                        
                    //do nothing here
                    case EMPTY:
                    default:
                        break;
                }
            }
        }
    }
    
    /**
     * Draw the background of the board
     * @param canvas Object we want to render pixel data to
     */
    private void drawBackground(final Canvas canvas)
    {
        //create paint object if it doesn't exist
        if (this.paint == null)
        {
            this.paint = new Paint();
            this.paint.setColor(Color.WHITE);
            this.paint.setStrokeWidth(10f);
        }
        else
        {
            for (int col = 1; col < getBoard()[0].length; col++)
            {
                final int x = startX + (col * getCellDimension());
                canvas.drawLine(x, startY, x, startY + getBoardHeight(), this.paint);
            }

            for (int row = 1; row < getBoard().length; row++)
            {
                final int y = startY + (row * getCellDimension());
                canvas.drawLine(startX, y, startX + getBoardWidth(), y, this.paint);
            }
        }
    }
}