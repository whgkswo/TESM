package net.whgkswo.tesm.conversation;


import java.util.List;

public class DecisionStage{
    private List<Decision> contents;
    public DecisionStage( List<Decision> contents) {
        this.contents = contents;
    }
    public List<Decision> getContents() {
        return contents;
    }
}
