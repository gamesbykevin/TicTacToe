package com.gamesbykevin.tictactoe.panel;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.gamesbykevin.androidframework.Animation;

import com.gamesbykevin.tictactoe.board.Board;
import com.gamesbykevin.tictactoe.R;
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
    
    public GamePanel(Context context)
    {
        //call to parent constructor
        super(context);
        
        //create new thread
        this.thread = new MainThread(getHolder(), this);
        
        //make game panel focusable true so it can handle events
        super.setFocusable(true);
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
            System.out.println("Surface Created");
            
            //create the animation for the X and O
            if (this.animationO == null)
                this.animationO = new Animation(BitmapFactory.decodeResource(getResources(), R.drawable.o), 0, 0, 93, 124, 1, 1, 1);
            if (this.animationX == null)
                this.animationX = new Animation(BitmapFactory.decodeResource(getResources(), R.drawable.x), 0, 0, 86, 124, 1, 1, 1);
            
            //if the board has not been created yet
            if (this.board == null)
            {
                //create a new game board
                this.board = new Board(3, 3);
            }
            
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
        //if not player 1 turn, no need to continue
        if (!player1turn)
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
            
            this.board.assignKey(x, y, Board.KEY_X);
            
            //if we were successful assigning a key value, switch turns
            //if (this.board.assignKey(event.getX(), event.getY(), Board.KEY_X))
            //    this.player1turn = !this.player1turn;
            
            System.out.println("(" + (event.getRawX() * scaleFactorX) + "," + (event.getRawY() * scaleFactorY) + ")");
            
            return true;
        }
        
        return super.onTouchEvent(event);
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
    
    public void update()
    {
        //if it is the cpu turn
        if (!player1turn)
        {
            
            this.player1turn = !this.player1turn;
        }
    }
    
    @Override
    public void draw(Canvas canvas)
    {
        //calculate the screen ratio
        final float scaleFactorX = getWidth() / (float)WIDTH;
        final float scaleFactorY = getHeight() / (float)HEIGHT;
        
        if (canvas != null)
        {
            //store the canvas state
            final int savedState = canvas.save();
            
            //scale to the screen size
            canvas.scale(scaleFactorX, scaleFactorY);
            
            //render board game elements
            board.draw(canvas, animationX.getImage(), animationO.getImage());
            
            //restore previous canvas state
            canvas.restoreToCount(savedState);
        }
    }
}