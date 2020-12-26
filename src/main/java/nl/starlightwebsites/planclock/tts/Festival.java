package nl.starlightwebsites.planclock.tts;



import nl.starlightwebsites.planclock.tts.interfaces.iTTSExec;
import nl.starlightwebsites.planclock.helpers.ExecHelper;

import java.util.ArrayList;
import java.util.List;

public class Festival extends aTTSExec {
    @Override
    public void say(String textToSay) {
        List<String> result = ExecHelper.StartProgram("festival"," -b '(voice_cmu_us_slt_arctic_hts)' '(SayText \""+textToSay+"\")'", true);
        for(String line : result){
            System.out.println(line);
        }
    }
}
