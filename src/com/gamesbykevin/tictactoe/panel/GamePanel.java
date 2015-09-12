package com.gamesbykevin.tictactoe.panel;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.gamesbykevin.androidframework.resources.Disposable;

import com.gamesbykevin.tictactoe.screen.MainScreen;
import com.gamesbykevin.tictactoe.R;
import com.gamesbykevin.tictactoe.TicTacToe;
import com.gamesbykevin.tictactoe.assets.Assets;
import com.gamesbykevin.tictactoe.thread.MainThread;

/**
 * Game Panel class
 * @author ABRAHAM
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback, Disposable
{
    //default dimensions of window for this game
    public static final int WIDTH = 720;
    public static final int HEIGHT = 1200;
    
    //the reference to our activity
    private final TicTacToe activity;
    
    //the object containing our game screens
    private MainScreen screen;
    
    //our main game thread
    private MainThread thread;
    
    /**
     * Create a new game panel
     * @param activity Our main activity 
     */
    public GamePanel(final TicTacToe activity)
    {
        //call to parent constructor
        super(activity);
        
        //store context
        this.activity = activity;
        
        //make game panel focusable = true so it can handle events
        super.setFocusable(true);
        
        //load game resources
        loadAssets();
    }
    
    @Override
    public void dispose()
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
                
                if (thread != null)
                {
                    //set running false, to stop the infinite loop
                    thread.setRunning(false);

                    //wait for thread to finish
                    thread.join();
                }
                
                //if we made it here, we were successful
                retry = false;
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        
        //make thread null
        this.thread = null;
        
        if (screen != null)
        {
            screen.dispose();
            screen = null;
        }
        
        //recycle asset objects
        Assets.recycle();
    }
    
    /**
     * Get the activity
     * @return The activity reference
     */
    public final TicTacToe getActivity()
    {
        return this.activity;
    }
    
    /**
     * Load game resources, if a resource is already loaded nothing will happen
     */
    private void loadAssets()
    {
        //load images
        Assets.assignImage(Assets.ImageKey.Player_X, BitmapFactory.decodeResource(getResources(), R.drawable.x));
        Assets.assignImage(Assets.ImageKey.Player_O, BitmapFactory.decodeResource(getResources(), R.drawable.o));
        Assets.assignImage(Assets.ImageKey.Button_ExitGame, BitmapFactory.decodeResource(getResources(), R.drawable.exitgame));
        Assets.assignImage(Assets.ImageKey.Button_MoreGames, BitmapFactory.decodeResource(getResources(), R.drawable.moregames));
        Assets.assignImage(Assets.ImageKey.Button_NewGame_1_Player, BitmapFactory.decodeResource(getResources(), R.drawable.newgame1player));
        Assets.assignImage(Assets.ImageKey.Button_NewGame_2_Player, BitmapFactory.decodeResource(getResources(), R.drawable.newgame2player));
        Assets.assignImage(Assets.ImageKey.Button_ResumeGame, BitmapFactory.decodeResource(getResources(), R.drawable.resumegame));
        Assets.assignImage(Assets.ImageKey.Button_Instructions, BitmapFactory.decodeResource(getResources(), R.drawable.instructions));
        Assets.assignImage(Assets.ImageKey.Button_RateGame, BitmapFactory.decodeResource(getResources(), R.drawable.rategame));
        Assets.assignImage(Assets.ImageKey.Title, BitmapFactory.decodeResource(getResources(), R.drawable.title));

        //load audio
        Assets.assignAudio(Assets.AudioKey.Win, MediaPlayer.create(getActivity(), R.raw.sound_win));
        Assets.assignAudio(Assets.AudioKey.Lose, MediaPlayer.create(getActivity(), R.raw.sound_lose));
        Assets.assignAudio(Assets.AudioKey.Move, MediaPlayer.create(getActivity(), R.raw.sound_move));
        Assets.assignAudio(Assets.AudioKey.Tie, MediaPlayer.create(getActivity(), R.raw.sound_tie));
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
            //load game resources
            loadAssets();
            
            //make sure the screen is created first before the thread starts
            if (this.screen == null)
                this.screen = new MainScreen(this);

            //if the thread does not exist, create it
            if (this.thread == null)
                this.thread = new MainThread(getHolder(), this);

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
    public boolean onTouchEvent(final MotionEvent event)
    {
        try
        {
            if (this.screen != null)
            {
                //calculate the coordinate offset
                final float scaleFactorX = (float)WIDTH / getWidth();
                final float scaleFactorY = (float)HEIGHT / getHeight();

                //adjust the coordinates
                final float x = event.getRawX() * scaleFactorX;
                final float y = event.getRawY() * scaleFactorY;

                //update the events
                return this.screen.update(event, x, y);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return super.onTouchEvent(event);
    }
    
    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        //pause the game
        if (screen != null)
            screen.setState(MainScreen.State.Paused);
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
        try
        {
            if (screen != null)
                screen.update();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public void onDraw(Canvas canvas)
    {
        if (canvas != null)
        {
            //store the canvas state
            final int savedState = canvas.save();
            
            try
            {
                //make sure the screen object exists
                if (screen != null)
                    screen.render(canvas);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            
            //restore previous canvas state
            canvas.restoreToCount(savedState);
        }
    }
}