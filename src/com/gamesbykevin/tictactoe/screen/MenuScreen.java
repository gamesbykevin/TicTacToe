package com.gamesbykevin.tictactoe.screen;

import android.graphics.Canvas;
import android.view.MotionEvent;
import com.gamesbykevin.androidframework.awt.Button;
import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.androidframework.screen.Screen;
import com.gamesbykevin.tictactoe.TicTacToe;
import com.gamesbykevin.tictactoe.assets.Assets;
import com.gamesbykevin.tictactoe.game.Game;

/**
 * Our main menu
 * @author ABRAHAM
 */
public class MenuScreen implements Screen, Disposable
{
    //the buttons in our menu
    private Button exitGame, newGame1Player, newGame2Player, instructions, moreGames, rateGame;
    
    //our main screen reference
    private final MainScreen screen;
    
    public MenuScreen(final MainScreen screen)
    {
        this.screen = screen;
        
        //create our buttons
        if (this.newGame1Player == null)
        {
            this.newGame1Player = new Button(Assets.getImage(Assets.ImageKey.Button_NewGame_1_Player));
            this.newGame1Player.setX(180);
            this.newGame1Player.setY(325);
            this.newGame1Player.updateBounds();
        }
        
        if (this.newGame2Player == null)
        {
            this.newGame2Player = new Button(Assets.getImage(Assets.ImageKey.Button_NewGame_2_Player));
            this.newGame2Player.setX(180);
            this.newGame2Player.setY(450);
            this.newGame2Player.updateBounds();
        }
        
        if (this.exitGame == null)
        {
            this.exitGame = new Button(Assets.getImage(Assets.ImageKey.Button_ExitGame));
            this.exitGame.setX(180);
            this.exitGame.setY(600);
            this.exitGame.updateBounds();
        }
        
        if (this.instructions == null)
        {
            this.instructions = new Button(Assets.getImage(Assets.ImageKey.Button_Instructions));
            this.instructions.setX(180);
            this.instructions.setY(725);
            this.instructions.updateBounds();
        }
        
        if (this.moreGames == null)
        {
            this.moreGames = new Button(Assets.getImage(Assets.ImageKey.Button_MoreGames));
            this.moreGames.setX(180);
            this.moreGames.setY(850);
            this.moreGames.updateBounds();
        }
        
        if (this.rateGame == null)
        {
            this.rateGame = new Button(Assets.getImage(Assets.ImageKey.Button_RateGame));
            this.rateGame.setX(180);
            this.rateGame.setY(975);
            this.rateGame.updateBounds();
        }
    }
    
    public void reset()
    {
        
    }
    
    @Override
    public boolean update(final MotionEvent event, final float x, final float y) throws Exception
    {
        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            if (exitGame.contains(x, y))
            {
                screen.getPanel().getActivity().finish();
            }
            else if (newGame1Player.contains(x, y))
            {
                //set the state to running
                screen.setState(MainScreen.State.Running);

                //create the game
                screen.createGame(Game.Mode.SinglePlayer);
            }
            else if (newGame2Player.contains(x, y))
            {
                //set the state to running
                screen.setState(MainScreen.State.Running);

                //create the game
                screen.createGame(Game.Mode.MultiPlayer);
            }
            else if (moreGames.contains(x, y))
            {
                //open web page
                screen.getPanel().getActivity().openWebpage(TicTacToe.WEBPAGE_URL);
            }
            else if (instructions.contains(x, y))
            {
                //open web page
                screen.getPanel().getActivity().openWebpage(TicTacToe.INSTRUCTIONS_URL);
            }
            else if (rateGame.contains(x, y))
            {
                //open web page
                screen.getPanel().getActivity().openWebpage(TicTacToe.APP_URL);
            }
        }
        
        //return true
        return true;
    }
    
    @Override
    public void update() throws Exception
    {
        
    }
    
    @Override
    public void render(final Canvas canvas) throws Exception
    {
        if (Assets.getImage(Assets.ImageKey.Title) != null)
            canvas.drawBitmap(Assets.getImage(Assets.ImageKey.Title), 70, 150, null);
        
        //draw the menu buttons
        newGame1Player.render(canvas);
        newGame2Player.render(canvas);
        exitGame.render(canvas);
        instructions.render(canvas);
        moreGames.render(canvas);
        rateGame.render(canvas);
    }
    
    @Override
    public void dispose()
    {
        this.newGame1Player = null;
        this.newGame2Player = null;
        this.exitGame = null;
        this.instructions = null;
        this.moreGames = null;
        this.rateGame = null;
    }
}