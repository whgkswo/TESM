package net.whgkswo.tesm.conversation;

public class Decision extends DialogueLine{
    private boolean isChosen;

    public Decision(String line) {
        super(line);
    }

    public boolean isChosen() {
        return isChosen;
    }

    public void setChosen(boolean chosen) {
        isChosen = chosen;
    }
}
