package com.gamesbykevin.tictactoe.game;

import android.view.MotionEvent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.gamesbykevin.androidframework.anim.Animation;
import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.tictactoe.ai.AI;
import com.gamesbykevin.tictactoe.assets.Assets;
import com.gamesbykevin.tictactoe.board.Board;
import com.gamesbykevin.tictactoe.board.BoardHelper;
import com.gamesbykevin.tictactoe.screen.MainScreen;

/**
 * The main game logic will happen here
 * @author ABRAHAM
 */
public class Game implements Disposable
{
    //the game board
    private Board board;
    
    //track which players turn
    private boolean player1turn = true;
    
    //keep score
    private int player1wins = 0, player2wins = 0, ties = 0;
    
    //our animations for X and O
    private Animation animationX, animationO;
    
    //object to draw text
    private Paint paint;
    
    //our main screen object reference
    private final MainScreen screen;
    
    /**
     * The mode of gameplay
     */
    public enum Mode
    {
        SinglePlayer, MultiPlayer
    }
    
    //store the mode of gameplay
    private Mode mode;
    
    public Game(final MainScreen screen)
    {
        //our main screen object reference
        this.screen = screen;
        
        //setup objects
        //if the board has not been created yet
        this.board = new Board();
        
        //create the animation for the X and O
        this.animationO = new Animation(Assets.getImage(Assets.ImageKey.Player_O), 0, 0, 156, 228, 1, 1, 1);
        this.animationX = new Animation(Assets.getImage(Assets.ImageKey.Player_X), 0, 0, 142, 228, 1, 1, 1);
        
        //create the text paint object
        this.paint = new Paint();
        this.paint.setColor(Color.WHITE);
        this.paint.setTextSize(48f);
        this.paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
    }
    
    public Mode getMode()
    {
        return this.mode;
    }
    
    public void setMode(final Mode mode)
    {
        //if the mode has changed, reset win counters
        if (getMode() != mode)
        {
            this.player1wins = 0;
            this.player2wins = 0;
            this.ties = 0;
        }
        
        this.mode = mode;
    }
    
    /**
     * Update the game based on the motion event
     * @param event Motion Event
     * @param x (x-coordinate)
     * @param y (y-coordinate)
     */
    public void updateMotionEvent(final MotionEvent event, final float x, final float y)
    {
        //if the game is over no way we can continue
        if (getBoard().hasGameover())
            return;
        
        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            //this will determine if a move was made
            boolean result = false;
            
            if (mode == Mode.SinglePlayer)
            {
                if (player1turn)
                {
                    //get the result of the move
                    result = getBoard().assignKey(x, y, Board.KEY_X);
                }
            }
            else
            {
                if (player1turn)
                {
                    //get the result of the move
                    result = getBoard().assignKey(x, y, Board.KEY_X);
                }
                else
                {
                    //get the result of the move
                    result = getBoard().assignKey(x, y, Board.KEY_O);
                }
            }
            
            //if move was made
            if (result)
            {
                //switch turns
                this.player1turn = !this.player1turn;

                //play sound effect
                Assets.playAudio(Assets.AudioKey.Move);

                //check for a winner
                trackWinner();
            }
        }
    }
    
    public void update()
    {
        //if the game is over no way we can continue
        if (getBoard().hasGameover())
            return;
        
        //only check the ai if playing single player
        if (mode == Mode.SinglePlayer)
        {
            //also make sure it is the ai's turn
            if (!player1turn)
            {
                //analyze and perform move
                AI.analyze(getBoard(), Board.KEY_O, Board.KEY_X);

                //switch turns
                this.player1turn = !this.player1turn;

                //track winner
                trackWinner();
            }
        }
    }
    
    /**
     * Check if there is a winner
     * @param key The key to check for winning match
     */
    private void checkWin(final int key)
    {
        //if there is a match, we have a win
        if (getBoard().hasMatch(key))
        {
            getBoard().setWinningKey(key);
            getBoard().setGameover(true);
            BoardHelper.markMatch(getBoard(), key);
        }
    }
    
    /**
     * Make sure we continue to keep score
     */
    private void trackWinner()
    {
        //see if either won
        checkWin(Board.KEY_X);
        checkWin(Board.KEY_O);
        
        if (getBoard().hasGameover())
        {
            switch (getBoard().getWinningKey())
            {
                case Board.KEY_EMPTY:
                    this.ties++;
                    Assets.playAudio(Assets.AudioKey.Tie);
                    break;
                    
                case Board.KEY_O:
                    this.player2wins++;
                    Assets.playAudio(Assets.AudioKey.Lose);
                    break;
                    
                case Board.KEY_X:
                    this.player1wins++;
                    Assets.playAudio(Assets.AudioKey.Win);
                    break;
            }
            
            //change the state if the game is over
            screen.setState(MainScreen.State.Ready);
        }
    }
    
    public final Board getBoard()
    {
        return this.board;
    }
    
    @Override
    public void dispose()
    {
        //recycle these objects
        this.animationO = null;
        this.animationX = null;
        this.board = null;
    }
    
    public void render(final Canvas canvas)
    {
        //render board game elements
        getBoard().draw(canvas, animationX.getImage(), animationO.getImage());
        
        //the text coordinates
        final int fontHeight = 55;
        int startX = 70;
        int startY = getBoard().getY() + getBoard().getBoardHeight() + fontHeight;

        //if not game over indicate whose turn it is
        if (getBoard().hasGameover())
        {
            //text description
            final String text;

            switch (getBoard().getWinningKey())
            {
                case Board.KEY_O:
                    text = "Player 2 Wins";
                    break;

                case Board.KEY_X:
                    text = "Player 1 Wins";
                    break;

                default:
                case Board.KEY_EMPTY:
                    text = "Tie game";
                    break;
            }

            startY += fontHeight;
            canvas.drawText(text, startX, startY, paint);
        }
        else
        {
            startY += fontHeight;
            
            if (getBoard().hasGameover())
            {
                canvas.drawText("Score Board", startX, startY, paint);
            }
            else 
            {
                canvas.drawText((player1turn) ? "Player 1's Turn - X" : "Player 2's Turn - O", startX, startY, paint);
            }
        }

        startY += fontHeight;
        canvas.drawText("Player 1 Wins (Hum): " + player1wins, startX, startY, paint);
        startY += fontHeight;
        canvas.drawText((mode == Mode.SinglePlayer) ? "Player 2 Wins (Cpu): " + player2wins : "Player 2 Wins (Hum): " + player2wins, startX, startY, paint);
        startY += fontHeight;
        canvas.drawText("Tie Games: " + ties, startX, startY, paint);
    }
}