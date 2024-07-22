package net.whgkswo.tesm.conversation;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DecisionStage{
    private List<Decision> contents;
    public DecisionStage(Decision... contents) {
        this.contents = new ArrayList<>(Arrays.asList(contents));
    }
    public List<Decision> getContents() {
        return contents;
    }
}
