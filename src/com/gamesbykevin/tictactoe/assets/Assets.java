package com.gamesbykevin.tictactoe.assets;

import android.graphics.Bitmap;
import android.media.MediaPlayer;

import java.util.HashMap;

/**
 * This class will contain our game assets
 * @author ABRAHAM
 */
public class Assets
{
    //hashmap of images
    private static HashMap<ImageKey, Bitmap> Images = new HashMap<ImageKey, Bitmap>();
    
    //hashmap of audio
    private static HashMap<AudioKey, MediaPlayer> Audio = new HashMap<AudioKey, MediaPlayer>();
    
    /**
     * The different images in our game
     */
    public enum ImageKey
    {
        Player_X, Player_O, 
        Button_NewGame_1_Player, 
        Button_NewGame_2_Player, 
        Button_MoreGames, 
        Button_ExitGame, 
        Button_ResumeGame, 
        Button_Instructions,
        Button_RateGame, 
        Title
    }
    
    /**
     * The key of each sound effect in our game
     */
    public enum AudioKey
    {
        Move, Win, Lose, Tie
    }
    
    public static final void assignImage(final ImageKey key, final Bitmap image)
    {
        if (getImage(key) == null)
            getImages().put(key, image);
    }
    
    public static final void assignAudio(final AudioKey key, final MediaPlayer sound)
    {
        if (getAudio().get(key) == null)
            getAudio().put(key, sound);
    }
    
    public static final Bitmap getImage(final ImageKey key)
    {
        return getImages().get(key);
    }
    
    public static final void playAudio(final AudioKey key)
    {
        if (Audio.get(key) != null)
            Audio.get(key).start();
    }
    
    private static HashMap<AudioKey, MediaPlayer> getAudio()
    {
        if (Audio == null)
            Audio = new HashMap<AudioKey, MediaPlayer>();
        return Audio;
    }
    
    private static HashMap<ImageKey, Bitmap> getImages()
    {
        if (Images == null)
            Images = new HashMap<ImageKey, Bitmap>();
        return Images;
    }
    
    public static void recycle()
    {
        if (Images != null)
        {
            for (Bitmap image : Images.values())
            {
                if (image != null)
                {
                    image.recycle();
                    image = null;
                }
            }
            
            Images.clear();
            Images = null;
        }
        
        if (Audio != null)
        {
            for (MediaPlayer audio : Audio.values())
            {
                if (audio != null)
                {
                    audio.release();
                    audio = null;
                }
            }
            
            Audio.clear();
            Audio = null;
        }
    }
}