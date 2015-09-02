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
    private Button exitGame, newGame1Player, newGame2Player, instructions, moreGames;
    
    //our main screen
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
        }
        
        if (this.newGame2Player == null)
        {
            this.newGame2Player = new Button(Assets.getImage(Assets.ImageKey.Button_NewGame_2_Player));
            this.newGame2Player.setX(180);
            this.newGame2Player.setY(450);
        }
        
        if (this.exitGame == null)
        {
            this.exitGame = new Button(Assets.getImage(Assets.ImageKey.Button_ExitGame));
            this.exitGame.setX(180);
            this.exitGame.setY(600);
        }
        
        if (this.instructions == null)
        {
            this.instructions = new Button(Assets.getImage(Assets.ImageKey.Button_Instructions));
            this.instructions.setX(180);
            this.instructions.setY(725);
        }
        
        if (this.moreGames == null)
        {
            this.moreGames = new Button(Assets.getImage(Assets.ImageKey.Button_MoreGames));
            this.moreGames.setX(180);
            this.moreGames.setY(850);
        }
    }
    
    @Override
    public boolean update(final MotionEvent event, final float x, final float y) throws Exception
    {
        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            if (exitGame.hasBoundary(x, y))
            {
                screen.getPanel().getActivity().finish();
            }
            else if (newGame1Player.hasBoundary(x, y))
            {
                //set the state to running
                screen.setState(MainScreen.State.Running);

                //create the game
                screen.createGame(Game.Mode.SinglePlayer);
            }
            else if (newGame2Player.hasBoundary(x, y))
            {
                //set the state to running
                screen.setState(MainScreen.State.Running);

                //create the game
                screen.createGame(Game.Mode.MultiPlayer);
            }
            else if (moreGames.hasBoundary(x, y))
            {
                //open web page
                screen.getPanel().getActivity().openWebpage(TicTacToe.WEBPAGE_URL);
            }
            else if (instructions.hasBoundary(x, y))
            {
                //open web page
                screen.getPanel().getActivity().openWebpage(TicTacToe.INSTRUCTIONS_URL);
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
        newGame1Player.draw(canvas);
        newGame2Player.draw(canvas);
        exitGame.draw(canvas);
        instructions.draw(canvas);
        moreGames.draw(canvas);
    }
    
    @Override
    public void dispose()
    {
        this.newGame1Player = null;
        this.newGame2Player = null;
        this.exitGame = null;
        this.instructions = null;
        this.moreGames = null;
    }
}