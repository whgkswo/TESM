package net.whgkswo.tesm.gui.libgui.widgets;

import io.github.cottonmc.cotton.gui.widget.WBox;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WTextField;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class WTextFieldWithReset extends WBox {
    private final WTextField textField;
    private final WButton resetButton;

    public WTextFieldWithReset(Axis axis){
        super(axis);
        this.setSpacing(4);
        textField = new WTextField();
        textField.setSize(100, textField.getHeight());
        textField.setMaxLength(100);
        resetButton = new WButton(Text.literal("✕").formatted(Formatting.RED));
        resetButton.setSize(20, textField.getHeight());
        resetButton.setOnClick(() -> textField.setText(""));
        this.add(textField, textField.getWidth(), textField.getHeight());
        this.add(resetButton, resetButton.getWidth(), resetButton.getHeight());
    }

    public WTextField getTextField(){
        return textField;
    }

    public WButton getResetButton(){
        return resetButton;
    }

    public String getTextString(){
        return textField.getText();
    }

    public void setText(String str){
        textField.setText(str);
    }
}
