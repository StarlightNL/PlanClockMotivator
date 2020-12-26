package nl.starlightwebsites.planclock.sst;

import nl.starlightwebsites.planclock.sst.interfaces.iSTT;

public class DeepSpeech implements iSTT {
    private String TFLITE_MODEL_FILENAME = "deepspeech-0.7.0-models.tflite";
    private String SCORER_FILENAME = "deepspeech-0.7.0-models.scorer";
    @Override
    public String listen() {
        return "";
    }
}
