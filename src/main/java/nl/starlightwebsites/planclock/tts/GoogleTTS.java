package nl.starlightwebsites.planclock.tts;

import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;
import javazoom.jl.player.Player;
import nl.starlightwebsites.planclock.Main;
import nl.starlightwebsites.planclock.database.DBAudio;
import nl.starlightwebsites.planclock.helpers.GoogleAuthenticate;
import nl.starlightwebsites.planclock.helpers.JavaHelpers;
import nl.starlightwebsites.planclock.tts.interfaces.iTTSExec;
import org.tinylog.Logger;

import javax.print.attribute.standard.Media;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GoogleTTS extends aTTSExec {


    private AudioConfig audioConfig;        // This can always be the same
    private VoiceSelectionParams voice;     // This can always be the same
    private Thread audioThread;             // This is so we can close the thread if needed
    @Override
    public void init(){
        // Select the type of audio file you want returned
        audioConfig = AudioConfig.newBuilder()
                        .setAudioEncoding(AudioEncoding.MP3) // MP3 audio.
                        .build();


        // Build the voice request
       voice = VoiceSelectionParams.newBuilder()
                        .setLanguageCode("en-US") // languageCode = "en_us"
                        .setSsmlGender(SsmlVoiceGender.FEMALE) // ssmlVoiceGender = SsmlVoiceGender.FEMALE
                        .build();
    }

    /**
     * Demonstrates using the Text to Speech client to synthesize text or ssml.
     *
     * @param text the raw text to be synthesized. (e.g., "Hello there!")
     */
    @Override
    public void say(String text) {
        byte[] audioFile = DBAudio.getAudioSample(text);
        if(audioFile != null){
            AudioOutputThread.addArrayToPlay(audioFile, text);
            return;
        }
        // Instantiates a client
        try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create()) {
            // Set the text input to be synthesized
            SynthesisInput input = SynthesisInput.newBuilder().setText(text).build();

            // Perform the text-to-speech request
            SynthesizeSpeechResponse response =
                    textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);

            // Get the audio contents from the response
            byte[] audioContents = response.getAudioContent().toByteArray();
            DBAudio.addAudioFile(audioContents, text);
            AudioOutputThread.addArrayToPlay(audioFile, text);
        } catch(Exception ex) {
            Logger.error(ex, "Failed to create ttsClient");
        }
    }
}