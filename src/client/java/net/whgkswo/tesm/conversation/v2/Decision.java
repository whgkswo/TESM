package net.whgkswo.tesm.conversation.v2;

public class Decision {
    private String content;
    private boolean isChosen;

    public Decision(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public boolean isChosen() {
        return isChosen;
    }
}
