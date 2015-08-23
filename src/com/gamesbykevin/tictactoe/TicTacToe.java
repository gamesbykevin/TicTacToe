package com.gamesbykevin.tictactoe;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.gamesbykevin.tictactoe.panel.GamePanel;
import com.gamesbykevin.tictactoe.R;

public class TicTacToe extends Activity
{
    //our game panel
    private GamePanel panel;
    
    /**
     * This is called when the activity is first created
     * @param savedInstanceState 
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        //turn the title off
        super.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //set the screen to full screen
        super.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //if the panel has not been created
        if (this.panel == null)
        {
            //create the game panel
            panel = new GamePanel(this);

            //add callback to the game panel to intercept events
            panel.getHolder().addCallback(panel);
        }

        //set the content view to our game
        setContentView(panel);
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
            case R.id.menuExit:
                super.finish();
                return true;
                
            case R.id.menuReset:
                this.panel.getBoard().reset();
                break;
        }
        
        return false;
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
}