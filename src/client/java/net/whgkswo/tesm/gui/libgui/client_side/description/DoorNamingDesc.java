package net.whgkswo.tesm.gui.libgui.client_side.description;

import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.whgkswo.tesm.gui.libgui.widgets.WTextFieldWithReset;
import net.whgkswo.tesm.networking.payload.data.s2c_req.DoorNaming;

public class DoorNamingDesc extends LightweightGuiDescription {
    public DoorNamingDesc(BlockPos blockPos, String insideName, String outsideName, boolean pushToOutside){
        // 기본 배경 비활성화
        super.setUseDefaultRootBackground(false);

        WBox root = new WBox(Axis.HORIZONTAL);
        setRootPanel(root);
        root.setSize(240, 160);
        root.setInsets(Insets.ROOT_PANEL);
        root.setHorizontalAlignment(HorizontalAlignment.CENTER);
        root.setVerticalAlignment(VerticalAlignment.CENTER);
        root.setBackgroundPainter(BackgroundPainter.createColorful(0x60ffffff));

        // 컨테이너
        WBox innerContainer = new WBox(Axis.VERTICAL);
        innerContainer.setSize(100, 160);
        innerContainer.setHorizontalAlignment(HorizontalAlignment.CENTER);
        innerContainer.setVerticalAlignment(VerticalAlignment.CENTER);
        root.add(innerContainer);

        // 라벨/입력창 추가
        WLabel insideNameLabel = new WLabel(Text.literal("안쪽 공간 이름:"));
        innerContainer.add(insideNameLabel, 120, 1);

        WTextFieldWithReset insideNameField = new WTextFieldWithReset(Axis.HORIZONTAL);
        insideNameField.setText(insideName);
        innerContainer.add(insideNameField);

        WLabel outsideNameLabel = new WLabel(Text.literal("바깥쪽 공간 이름:"));
        innerContainer.add(outsideNameLabel, 120, 1);

        WTextFieldWithReset outsideNameField = new WTextFieldWithReset(Axis.HORIZONTAL);
        outsideNameField.setText(outsideName);
        innerContainer.add(outsideNameField);

        // 버튼들을 가로로 묶기 위한 패널
        WBox buttonBox = new WBox(Axis.HORIZONTAL);
        buttonBox.setSpacing(4); // 버튼 사이 간격
        buttonBox.setSize(160, 20);
        innerContainer.add(buttonBox); // 중앙에 위치하도록 배치

        // 취소 버튼 추가
        WButton cancelButton = new WButton(Text.literal("취소").formatted(Formatting.RED));
        cancelButton.setOnClick(this::close);
        buttonBox.add(cancelButton, 80, 20);

        // 확인 버튼 추가
        WButton confirmButton = new WButton(Text.literal("확인"));
        confirmButton.setAlignment(HorizontalAlignment.CENTER);
        buttonBox.add(confirmButton,80, 20);

        // 서브 컨테이너
        WBox subContainer = new WBox(Axis.VERTICAL);
        subContainer.setSize(50, 160);
        root.add(subContainer);

        // 스위치
        WButton switchButton = new WButton(Text.literal("안-밖 교체"));
        switchButton.setOnClick(() -> onSwitch(insideNameField, outsideNameField));
        subContainer.add(switchButton, 50, 20);

        // 열리는 방향 토글
        WToggleButton toggle = new WToggleButton();
        toggle.setToggle(pushToOutside);
        onToggle(toggle); // 초기화. 순서 주의
        toggle.setOnToggle(state -> onToggle(toggle));
        subContainer.add(toggle);

        confirmButton.setOnClick(() -> onConfirm(blockPos, insideNameField.getTextString(), outsideNameField.getTextString(), toggle.getToggle()));

        root.validate(this);
    }

    private void onToggle(WToggleButton toggle){
        String newState = toggle.getToggle() ? "바깥" : "안쪽";

        toggle.setLabel(Text.literal(newState + "으로 열림"));
    }

    private void onSwitch(WTextFieldWithReset insideNameField, WTextFieldWithReset outsideNameField){
        String outsideName = outsideNameField.getTextString();

        outsideNameField.setText(insideNameField.getTextString());
        insideNameField.setText(outsideName);
    }

    private void onConfirm(BlockPos blockPos, String insideName, String outsideName, boolean pushToOutside){
        ClientPlayNetworking.send(new DoorNaming(blockPos, insideName, outsideName, pushToOutside));
        close();
    }

    private void close(){
        MinecraftClient.getInstance().setScreen(null);
    }
}
