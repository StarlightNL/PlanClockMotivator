package nl.starlightwebsites.planclock.tts;

import nl.starlightwebsites.planclock.tts.interfaces.iTTSExec;

public abstract class aTTSExec implements iTTSExec {
    boolean clipPlaying = false;

    @Override
    public void init() {

    }
}
