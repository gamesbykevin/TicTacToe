package com.gamesbykevin.tictactoe.ai;

import com.gamesbykevin.tictactoe.board.Board;

import java.util.Random;

/**
 * Our artificial intelligence
 * @author ABRAHAM
 */
public class AI 
{
    /**
     * Our random object used to make random decisions
     */
    public static final Random RANDOM = new Random();
    
    public static void analyze(final Board board, final int key, final int opponentKey)
    {
        //get the size of the board
        final int cols = board.getBoardKey()[0].length;
        final int rows = board.getBoardKey().length;
        
        //first start analyzing to see if we have a win
        for (int col = 0; col < cols; col++)
        {
            for (int row = 0; row < rows; row++)
            {
                //only check empty places
                if (board.getKey(col, row) != Board.KEY_EMPTY)
                    continue;
                
                //assign our key
                board.assignKey(col, row, key);
                
                //if we made a match we are done
                if (board.hasMatch(key))
                    return;
                
                //since no luck, make this empty again
                board.assignKey(col, row, Board.KEY_EMPTY);
            }
        }
        
        //second check if the opponent has a chance to match
        for (int col = 0; col < cols; col++)
        {
            for (int row = 0; row < rows; row++)
            {
                //only check empty places
                if (board.getKey(col, row) != Board.KEY_EMPTY)
                    continue;
                
                //assign the opponent key
                board.assignKey(col, row, opponentKey);
                
                /**
                 * If the opponent can match here, we have to block it
                 */
                if (board.hasMatch(opponentKey))
                {
                    //make our move here
                    board.assignKey(col, row, key);
                    return;
                }
                
                //since no luck, make this empty again
                board.assignKey(col, row, Board.KEY_EMPTY);
            }
        }
        
        //finally if no success, make a random move
        while (true)
        {
            final int col = RANDOM.nextInt(cols);
            final int row = RANDOM.nextInt(rows);
            
            //if this is an empty space we can move here
            if (board.getKey(col, row) == Board.KEY_EMPTY)
            {
                board.assignKey(col, row, key);
                break;
            }
        }
    }
}