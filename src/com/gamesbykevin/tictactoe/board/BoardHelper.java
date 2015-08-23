package com.gamesbykevin.tictactoe.board;

/**
 * Board Helper methods
 * @author ABRAHAM
 */
public final class BoardHelper 
{
    /**
     * The number of consecutive matches to identify a winner
     */
    public static final int MATCH_COUNT = 3;
    
    /**
     * Check if we have a horizontal match on the board at the specified location
     * @param board The game board
     * @param startCol Start column
     * @param startRow Start Row
     * @param key The matching key
     * @return true if the key matches in consecutive session, false otherwise
     */
    protected static boolean hasMatchHorizontal(final Board board, final int startCol, final int startRow, final int key)
    {
        //check horizontal match
        for (int col = startCol; col < startCol + MATCH_COUNT; col++)
        {
            //if we are out of bounds we don't meet the match criteria
            if (col >= board.getBoardKey()[0].length)
                return false;
            
            //if the key does not match, return false
            if (board.getKey(col, startRow) != key)
                return false;
        }
        
        //there was nothing from preventing a match return true
        return true;
    }
    
    /**
     * Check if we have a vertical match on the board at the specified location
     * @param board The game board
     * @param startCol Start column
     * @param startRow Start Row
     * @param key The matching key
     * @return true if the key matches in consecutive session, false otherwise
     */
    protected static boolean hasMatchVertical(final Board board, final int startCol, final int startRow, final int key)
    {
        //check vertical match
        for (int row = startRow; row < startRow + MATCH_COUNT; row++)
        {
            //if we are out of bounds we don't meet the match criteria
            if (row >= board.getBoardKey().length)
                return false;
            
            //if the key does not match, return false
            if (board.getKey(startCol, row) != key)
                return false;
        }
        
        //there was nothing from preventing a match return true
        return true;
    }
    
    /**
     * Check if we have a diagonal match on the board at the specified location
     * @param board The game board
     * @param startCol Start column
     * @param startRow Start Row
     * @param key The matching key
     * @return true if the key matches in consecutive session, false otherwise
     */
    protected static boolean hasMatchDiagonalSouth(final Board board, final int startCol, final int startRow, final int key)
    {
        int col = startCol;

        for (int row = startRow; row < startRow + MATCH_COUNT; row++)
        {
            //if we are out of bounds we don't meet the match criteria
            if (col >= board.getBoardKey()[0].length)
                return false;

            //if we are out of bounds we don't meet the match criteria
            if (row >= board.getBoardKey().length)
                return false;

            //if the key does not match, return false
            if (board.getKey(col, row) != key)
                return false;
            
            //increase the column
            col++;
        }
        
        //there was nothing from preventing a match return true
        return true;
    }
    
    /**
     * Check if we have a diagonal match on the board at the specified location
     * @param board The game board
     * @param startCol Start column
     * @param startRow Start Row
     * @param key The matching key
     * @return true if the key matches in consecutive session, false otherwise
     */
    protected static boolean hasMatchDiagonalNorth(final Board board, final int startCol, final int startRow, final int key)
    {
        int col = startCol;

        for (int row = startRow; row > startRow - MATCH_COUNT; row--)
        {
            //if we are out of bounds we don't meet the match criteria
            if (col >= board.getBoardKey()[0].length)
                return false;
            
            //if we are out of bounds we don't meet the match criteria
            if (row < 0)
                return false;

            //if the key does not match, return false
            if (board.getKey(col, row) != key)
                return false;
            
            //increase the column
            col++;
        }
        
        //there was nothing from preventing a match return true
        return true;
    }
    
    /**
     * Do we have a match?
     * @param board The board
     * @param key The key we want to check for a match
     * @return true = yes, false = no
     */
    protected static boolean hasMatch(final Board board, final int key)
    {
        //check every position
        for (int col = 0; col < board.getBoardKey()[0].length; col++)
        {
            for (int row = 0; row < board.getBoardKey().length; row++)
            {
                //if any of these match, we have a match
                if (hasMatchHorizontal(board, col, row, key))
                    return true;
                if (hasMatchVertical(board, col, row, key))
                    return true;
                if (hasMatchDiagonalSouth(board, col, row, key))
                    return true;
                if (hasMatchDiagonalNorth(board, col, row, key))
                    return true;
            }
        }
        
        //no match was found
        return false;
    }
    
    /**
     * Mark the start and end point of the match.<br>
     * If there is no match nothing will happen.
     * @param board The board we are playing on
     * @param key The key that has a match
     */
    public static void markMatch(final Board board, final int key)
    {
        //check every position
        for (int col = 0; col < board.getBoardKey()[0].length; col++)
        {
            for (int row = 0; row < board.getBoardKey().length; row++)
            {
                //if any of these match, mark our match
                if (hasMatchHorizontal(board, col, row, key))
                {
                    board.setMatchLocation(col, row, col + MATCH_COUNT - 1, row);
                    return;
                }
                
                if (hasMatchVertical(board, col, row, key))
                {
                    board.setMatchLocation(col, row, col, row + MATCH_COUNT - 1);
                    return;
                }
                
                if (hasMatchDiagonalSouth(board, col, row, key))
                {
                    board.setMatchLocation(col, row, col + MATCH_COUNT - 1, row + MATCH_COUNT - 1);
                    return;
                }
                
                if (hasMatchDiagonalNorth(board, col, row, key))
                {
                    board.setMatchLocation(col, row, col + MATCH_COUNT - 1, row - MATCH_COUNT + 1);
                    return;
                }
            }
        }
    }
    
    /**
     * Is the board full?
     * @param board The board we are playing on
     * @return true if there are no empty spaces, false otherwise
     */
    protected static boolean isFull(final Board board)
    {
        for (int col = 0; col < board.getBoardKey()[0].length; col++)
        {
            for (int row = 0; row < board.getBoardKey().length; row++)
            {
                //if there is an empty space return false
                if (board.getKey(col, row) == Board.KEY_EMPTY)
                    return false;
            }
        }
        
        //this board is full
        return true;
    }
}