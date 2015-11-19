package com.gamesbykevin.tictactoe;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import android.content.Intent;
import android.net.Uri;

import com.gamesbykevin.tictactoe.panel.GamePanel;

public class TicTacToe extends Activity
{
    //our game panel
    private GamePanel panel;
    
    /**
     * The website address we want to navigate to
     */
    public static final String WEBPAGE_URL = "http://gamesbykevin.com";

    /**
     * The url address where the instructions are
     */
    public static final String INSTRUCTIONS_URL = "http://www.wikihow.com/Play-Tic-Tac-Toe";
    
    /**
     * The url address where the application is
     */
    public static final String APP_URL = "https://play.google.com/store/apps/details?id=com.gamesbykevin.tictactoe";
    
    /**
     * This is called when the activity is first created
     * @param savedInstanceState 
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        //call parent create
        super.onCreate(savedInstanceState);
        
        //turn the title off
        super.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //set the screen to full screen
        super.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //if the panel has not been created
        if (getGamePanel() == null)
        {
            //create the game panel
            setGamePanel(new GamePanel(this));

            //add callback to the game panel to intercept events
            getGamePanel().getHolder().addCallback(getGamePanel());
        }

        //set the content view to our game
        setContentView(getGamePanel());
    }
    
    /**
     * Part of the activity life cycle
     */
    @Override
    public void onStart()
    {
        super.onStart();
    }
    
    /**
     * Part of the activity life cycle
     */
    @Override
    public void onStop()
    {
        super.onStop();
    }
    
    /**
     * Part of the activity life cycle
     */
    @Override
    public void onDestroy()
    {
        //finish the current activity
        super.finish();
        
        if (getGamePanel() != null)
        {
            getGamePanel().dispose();
            setGamePanel(null);
        }
        
        //perform final cleanup
        super.onDestroy();
    }
    
    /**
     * Part of the activity life cycle
     */
    @Override
    public void onPause()
    {
        super.onPause();
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            /*
            case R.id.menuExit:
                super.finish();
                return true;
            
            case R.id.menuReset:
                //getGamePanel().getBoard().reset();
                break;
                
            case R.id.menuWebpage:
                openWebpage();
                break;
            */
        }
        
        return false;
    }
    
    /**
     * Navigate to the desired web page
     * @param url The desired url
     */
    public void openWebpage(final String url)
    {
        //create action view intent
        Intent intent = new Intent(Intent.ACTION_VIEW);
        
        //the content will be the web page
        intent.setData(Uri.parse(url));
        
        //start this new activity
        startActivity(intent);        
    }
    
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    */
    
    /**
     * Get the game panel.
     * @return The object containing our game logic, assets, threads, etc...
     */
    private GamePanel getGamePanel()
    {
        return this.panel;
    }
    
    /**
     * Assign the game panel
     * @param panel The game panel
     */
    private void setGamePanel(final GamePanel panel)
    {
        this.panel = panel;
    }
}