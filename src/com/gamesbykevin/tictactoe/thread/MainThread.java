package com.gamesbykevin.tictactoe.thread;

import android.graphics.Canvas;
import android.view.SurfaceHolder;
import com.gamesbykevin.androidframework.Animation;
import static java.lang.Thread.sleep;

import com.gamesbykevin.tictactoe.panel.GamePanel;

/**
 * Our main thread containing the game loop
 * @author ABRAHAM
 */
public class MainThread extends Thread
{
    //the assigned fps for this game
    private static final int FPS = 30;
    
    //our game panel
    private final GamePanel panel;
    
    //area where game play is rendered
    private final SurfaceHolder holder;
    
    //is the thread running
    private boolean running;
    
    //our canvas to render image(s)
    public static Canvas canvas;
    
    /**
     * When the game is terminated and recycling variables, this is the maximum number of attempts to stop the thread
     */
    public static final int COMPLETE_THREAD_ATTEMPTS = 1000;
    
    public MainThread(SurfaceHolder holder, GamePanel panel)
    {
        super();
        
        //assign the necessary references
        this.holder = holder;
        this.panel = panel;
    }
    
    @Override
    public void run()
    {
        long totalTime = 0;
        
        //the 
        int frames = 0;
        
        //the expected amount of time per each update
        final long targetTime = (Animation.MILLISECONDS_PER_SECOND / FPS);
        
        //continue to loop while the thread is running
        while (running)
        {
            //get the start time of this update
            final long startTime = System.nanoTime();
            
            //assign the canvas null
            canvas = null;
            
            try 
            {
                //attempt to lock the canvas to edit the pixels of the surface
                canvas = holder.lockCanvas();
                
                //make sure no other threads are accessing the holder
                synchronized (holder)
                {
                    //update our game panel
                    this.panel.update();
                    
                    //if the canvas object exists, render
                    if (canvas != null)
                        this.panel.draw(canvas);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally 
            {
                //remove the lock (if possible)
                if (canvas != null)
                {
                    try
                    {
                        //render the pixels on the canvas to the screen
                        holder.unlockCanvasAndPost(canvas);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            
            //calculate the number of milliseconds elapsed
            final long timeMillis = (System.nanoTime() - startTime) / Animation.NANO_SECONDS_PER_MILLISECOND;
            
            //determine the amount of time to sleep
            final long waitTime = targetTime - timeMillis;
            
            try
            {
                //only sleep if the waitTime has a value
                if (waitTime > 0)
                    sleep(waitTime);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            
            //calculate the total time passed
            totalTime += System.nanoTime() - startTime;
            
            //increase the frame count
            frames++;
            
            //if the frame count = the assigned fps
            if (frames == FPS)
            {
                //calculate the average fps
                final double fpsAverage = (double)Animation.MILLISECONDS_PER_SECOND / ((double)(totalTime / frames) / Animation.NANO_SECONDS_PER_MILLISECOND);
                
                //reset these values
                frames = 0;
                totalTime = 0;
                
                //display the average
                System.out.println("Average FPS " + fpsAverage);
            }
        }
    }
    
    /**
     * Assign the running value
     * @param running Is the thread running? true = yes, false = no
     */
    public void setRunning(final boolean running)
    {
        this.running = running;
    }
    
    public boolean isRunning()
    {
        return this.running;
    }
}