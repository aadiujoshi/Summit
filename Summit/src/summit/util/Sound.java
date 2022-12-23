package summit.util;

import java.io.File;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

import summit.Main;

public class Sound {
    //-------------- Preloaded sounds -------------------------------------

    public static final Sound DUNGEON_SOUNDS = new Sound(Main.path + "resources/sound/ambience/dungeons_ambience.wav");
    public static final Sound WALKING_HARD = new Sound(Main.path + "resources/sound/sfx/footsteps_hard.wav");

    //-----------------------------------------------------------------
    
    private File wav;    
    private volatile boolean playing;

    public Sound(String path) {
        wav = new File(path);
    }
    
    public static void stopAll(){
        DUNGEON_SOUNDS.stop();
        WALKING_HARD.stop();
    }

    public void stop(){
        playing = false;
    }

    public boolean playing(){
        return playing;
    }

    public void play() {
        if(playing)
            return;
        
        playing = true;

        Thread s = new Thread(() -> {
            try{
                final Clip clip = (Clip)AudioSystem.getLine(new Line.Info(Clip.class));

                clip.open(AudioSystem.getAudioInputStream(wav));
                clip.start();

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
                

                //check if manually stopped sound
                while(playing){}

                clip.close();

                playing = false;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
        s.setPriority(Thread.MIN_PRIORITY);
        s.setDaemon(true);
        s.start();
    }
}
