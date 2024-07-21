package net.whgkswo.tesm.conversation;


import java.util.List;

public class NormalStage{
    private List<NormalLine> contents;
    private ExecuteAfter executeAfter;
    private String executeTarget;
    public NormalStage(List<NormalLine> contents, ExecuteAfter executeAfter) {
        this.contents = contents;
        this.executeAfter = executeAfter;
    }
    public NormalStage(List<NormalLine> contents, ExecuteAfter executeAfter, String executeTarget) {
        this.contents = contents;
        this.executeAfter = executeAfter;
        this.executeTarget = executeTarget;
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
        EXIT
    }
}
