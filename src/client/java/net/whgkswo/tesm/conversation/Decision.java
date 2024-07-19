package net.whgkswo.tesm.conversation;

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

    public void setChosen(boolean chosen) {
        isChosen = chosen;
    }
}
