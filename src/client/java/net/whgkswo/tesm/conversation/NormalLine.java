package net.whgkswo.tesm.conversation;

public class NormalLine extends DialogueLine{
    private boolean revealName;
    public NormalLine(String line) {
        super(line);
    }
    public NormalLine(String line, boolean revealName) {
        super(line);
        this.revealName = revealName;
    }

    public boolean isRevealName() {
        return revealName;
    }
}
