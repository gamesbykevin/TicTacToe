package com.gamesbykevin.tictactoe.panel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.gamesbykevin.androidframework.base.Animation;

import com.gamesbykevin.tictactoe.board.Board;
import com.gamesbykevin.tictactoe.R;
import com.gamesbykevin.tictactoe.ai.AI;
import com.gamesbykevin.tictactoe.board.BoardHelper;
import com.gamesbykevin.tictactoe.thread.MainThread;

/**
 * Game Panel class
 * @author ABRAHAM
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback
{
    //default dimensions of window for this game
    public static final int WIDTH = 480;
    public static final int HEIGHT = 800;
    
    //our main game thread
    private final MainThread thread;
    
    //our animations for X and O
    private Animation animationX, animationO;
    
    //the game board
    private Board board;
    
    //track which players turn
    private boolean player1turn = true;
    
    //our object used to draw text
    private Paint infoPaint;
    
    private int winsHuman = 0, winsCpu = 0, ties = 0;
    
    public GamePanel(Context context)
    {
        //call to parent constructor
        super(context);
        
        //create new thread
        this.thread = new MainThread(getHolder(), this);
        
        //make game panel focusable true so it can handle events
        super.setFocusable(true);
    }
    
    public Board getBoard()
    {
        return this.board;
    }
    
    /**
     * Now that the surface has been created we can create our game objects
     * @param holder 
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        try
        {
            //create the animation for the X and O
            if (this.animationO == null)
                this.animationO = new Animation(BitmapFactory.decodeResource(getResources(), R.drawable.o), 0, 0, 93, 124, 1, 1, 1);
            if (this.animationX == null)
                this.animationX = new Animation(BitmapFactory.decodeResource(getResources(), R.drawable.x), 0, 0, 86, 124, 1, 1, 1);
            
            //if the board has not been created yet
            if (getBoard() == null)
                this.board = new Board();
            
            //if the thread hasn't been started yet
            if (!this.thread.isRunning())
            {
                //start the thread
                this.thread.setRunning(true);
                this.thread.start();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public boolean onTouchEvent (MotionEvent event)
    {
        //if not player 1 turn or the game is over, no need to continue
        if (!player1turn || getBoard().hasGameover())
            return super.onTouchEvent(event);
        
        //do nothing on action down
        if (event.getAction() == MotionEvent.ACTION_DOWN)
            return true;
        
        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            //calculate the coordinate offset
            final float scaleFactorX = (float)WIDTH / getWidth();
            final float scaleFactorY = (float)HEIGHT / getHeight();
            
            //adjust the coordinates
            final float x = event.getRawX() * scaleFactorX;
            final float y = event.getRawY() * scaleFactorY;
            
            /**
             * If we are successful assigning the key.<br>
             * 1. Check for match<br>
             * 2. If no match switch turns<br>
             */
            if (getBoard().assignKey(x, y, Board.KEY_X))
            {
                if (getBoard().hasMatch(Board.KEY_X))
                {
                    markWin(Board.KEY_X);
                }
                else
                {
                    this.player1turn = !this.player1turn;
                }
                
                trackWinner();
            }
            
            return true;
        }
        
        return super.onTouchEvent(event);
    }
    
    /**
     * Make sure we continue to keep score
     */
    private void trackWinner()
    {
        if (getBoard().hasGameover())
        {
            if (getBoard().getWinningKey() == Board.KEY_EMPTY)
                this.ties++;
            if (getBoard().getWinningKey() == Board.KEY_O)
                this.winsCpu++;
            if (getBoard().getWinningKey() == Board.KEY_X)
                this.winsHuman++;
        }
    }
    
    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        //it could take several attempts to stop the thread
        boolean retry = true;
        
        //count number of attempts to complete thread
        int count = 0;
        
        while (retry && count <= MainThread.COMPLETE_THREAD_ATTEMPTS)
        {
            try
            {
                //increase count
                count++;
                
                //set running false, to stop the infinite loop
                thread.setRunning(false);
                
                //wait for thread to finish
                thread.join();
                
                //if we made it here, we were successful
                retry = false;
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        
        //also recycle these objects
        this.animationO = null;
        this.animationX = null;
        this.board = null;
    }
    
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        //does anything need to be done here?
    }
    
    /**
     * Update the game state
     */
    public void update()
    {
        //if it is the cpu's turn and the game isn't over
        if (!player1turn && !getBoard().hasGameover())
        {
            //analyze and perform move
            AI.analyze(getBoard(), Board.KEY_O, Board.KEY_X);
            
            //if there is a match mark the win
            if (getBoard().hasMatch(Board.KEY_O))
                markWin(Board.KEY_O);
            
            //switch turns
            this.player1turn = !this.player1turn;
            
            //track winner
            trackWinner();
        }
    }
    
    /**
     * 
     * @param key 
     */
    private void markWin(final int key)
    {
        getBoard().setWinningKey(key);
        getBoard().setGameover(true);
        BoardHelper.markMatch(getBoard(), key);
    }
    
    @Override
    public void onDraw(Canvas canvas)
    {
        //calculate the screen ratio
        final float scaleFactorX = getWidth() / (float)WIDTH;
        final float scaleFactorY = getHeight() / (float)HEIGHT;
        
        if (canvas != null)
        {
            //store the canvas state
            final int savedState = canvas.save();
            
            //fill canvas with black color
            canvas.drawColor(Color.BLACK);
            
            //scale to the screen size
            canvas.scale(scaleFactorX, scaleFactorY);
            
            //render board game elements
            getBoard().draw(canvas, animationX.getImage(), animationO.getImage());
            
            if (this.infoPaint == null)
            {
                this.infoPaint = new Paint();
                this.infoPaint.setColor(Color.WHITE);
                this.infoPaint.setTextSize(36f);
                this.infoPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            }
            
            int x = 175;
            int y = 600;
            
            //if not game over indicate whose turn it is
            if (getBoard().hasGameover())
            {
                //text description
                final String text;
                
                switch (getBoard().getWinningKey())
                {
                    case Board.KEY_O:
                        text = "Cpu wins";
                        break;
                        
                    case Board.KEY_X:
                        text = "Human wins";
                        break;
                        
                    default:
                    case Board.KEY_EMPTY:
                        text = "Tie game";
                        break;
                }
                
                canvas.drawText(text + ": hit 'Menu'", 70, 575, infoPaint);
            }
            
            canvas.drawText("Human: " + this.winsHuman, 70, 635, infoPaint);
            canvas.drawText("Cpu: " + this.winsCpu, 70, 675, infoPaint);
            canvas.drawText("Tie: " + this.ties, 70, 715, infoPaint);
            
            //restore previous canvas state
            canvas.restoreToCount(savedState);
        }
    }
}