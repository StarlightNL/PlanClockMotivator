package nl.starlightwebsites.planclock.helpers;

import org.apache.commons.exec.LogOutputStream;

import java.util.concurrent.CopyOnWriteArrayList;

public class CollectingLogOutputStream extends LogOutputStream {
    private CopyOnWriteArrayList<String> lines = new CopyOnWriteArrayList<String>();
    @Override protected void processLine(String line, int level) {
        lines.add(line);
    }
    public CopyOnWriteArrayList<String> getLines() {
        return lines;
    }
}