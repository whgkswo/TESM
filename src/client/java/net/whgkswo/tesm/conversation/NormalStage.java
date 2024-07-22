package net.whgkswo.tesm.conversation;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NormalStage{
    private List<NormalLine> contents;
    private ExecuteAfter executeAfter;
    private String executeTarget;
    public NormalStage(ExecuteAfter executeAfter, NormalLine... contents) {
        this.executeAfter = executeAfter;
        this.contents = new ArrayList<>(Arrays.asList(contents));
    }
    public NormalStage(ExecuteAfter executeAfter, String executeTarget, NormalLine... contents) {
        this.executeAfter = executeAfter;
        this.executeTarget = executeTarget;
        this.contents = new ArrayList<>(Arrays.asList(contents));
    }

    public List<NormalLine> getContents() {
        return contents;
    }

    public ExecuteAfter getExecuteAfter() {
        return executeAfter;
    }

    public String getExecuteTarget() {
        return executeTarget;
    }

    public enum ExecuteAfter{
        SHOW_DECISIONS,
        JUMP_TO,
        START_QUEST,
        COMPLETE_QUEST,
        CLOSE
    }
}
