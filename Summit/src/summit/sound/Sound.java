package summit.sound;

import javax.sound.sampled.*;

import summit.Main;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

//  STOLEN CODE !!!!!!!!!

public class Sound {
    //-------------- Preloaded sounds -------------------------------------

    public static final Sound DUNGEON_SOUNDS = new Sound(Main.path + "sound/ambience/dungeons_ambience.wav");

    //-----------------------------------------------------------------
    
    private File wav;    
    private volatile boolean playing;

    public Sound(String path) {
        wav = new File(path);
    }
    
    public void stop(){
        playing = false;
    }

    public void play() {
        if(playing)
            return;
        
        playing = true;

        new Thread(() -> {
            try{
                final Clip clip = (Clip)AudioSystem.getLine(new Line.Info(Clip.class));

                clip.addLineListener(new LineListener()
                {
                    @Override
                    public void update(LineEvent event)
                    {
                        if (event.getType() == LineEvent.Type.STOP){
                            playing = false;
                            clip.close();
                        }
                    }
                });

                clip.open(AudioSystem.getAudioInputStream(wav));
                clip.start();

                //check if manually stopped sound
                while(playing){}

                clip.close();

                playing = false;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
