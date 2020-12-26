package nl.starlightwebsites.planclock.tts;

import javazoom.jl.player.Player;
import nl.starlightwebsites.planclock.helpers.JavaHelpers;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.tinylog.Logger;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.Queue;

public class AudioOutputThread {
    public static boolean shouldStop = false;
    private static Thread audioThread = new Thread(AudioOutputThread::audioPlayer, "AudioOutputThread");
    private static Queue<Pair<String, byte[]>> audioQueue = new LinkedList<>();

    public static void addFileToPlay(Path filePath){
        try {
            audioQueue.add(new ImmutablePair<>(filePath.getFileName().toString(), Files.readAllBytes(filePath)));
        } catch (IOException e) {
            Logger.error(e, "Failed to read bytes of audiofile {}", filePath);
        }
    }

    public static void startAudioPlayer(){
        if(audioThread.isAlive()){
            Logger.error("Audio thread is already running");
            return;
        }
        audioThread.start();
    }

    private static void audioPlayer() {
        while (true){
            while(audioQueue.peek() == null){
                if(JavaHelpers.sleep(500)) break;
            }
            Pair<String, byte[]> audio = audioQueue.poll();
            try {
                Player player = new Player(new ByteArrayInputStream(audio.getValue()));
                player.play();
                while (!player.isComplete()) {
                    if (JavaHelpers.sleep(250)) {
                        player.close(); // We got interrupted so we are shutting this down
                    }
                }
            } catch (Exception ex) {
                Logger.error(ex, "Failed to play audio clip", audio.getKey());
            }
        }
    }

    public static void addArrayToPlay(byte[] audioData, String audioName) {
        audioQueue.add(new ImmutablePair<>(audioName, audioData));
    }
}
