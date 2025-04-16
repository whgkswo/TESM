package net.whgkswo.tesm.executions;

import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.whgkswo.tesm.general.GlobalVariables;
import net.whgkswo.tesm.message.MessageHelper;

import java.util.concurrent.Callable;

public class ScheduledTask<T> {
    private static final StackTraceElement UNKNOWN_LOCATION = new StackTraceElement("Unknown Class", "Unknown Method", "Unknown File", 0);
    private int countdown;
    private Callable<T> callable;
    private String purpose;

    public ScheduledTask(int countdown, String purpose, Callable<T> callable){
        this.countdown = countdown;
        this.purpose = purpose;
        this.callable = callable;
    }

    public int getCountdown(){
        return countdown;
    }

    public void count(){
        countdown--;
    }

    public void execute(){
        try{
            callable.call();
        } catch (Exception e){
            //String message = getExceptionMessage(e);
            Text text = getExceptionText(e);
            MessageHelper.sendMessage(text);
        }
    }

    public String getExceptionMessage(Exception e){
        StackTraceElement location = e.getStackTrace().length > 0 ? e.getStackTrace()[0] : UNKNOWN_LOCATION;

        return String.format("[%s] 수행 중 %s 발생\n위치: %s, %s(), Line: %d\n[제보하러 가기]",
                purpose, e.getClass().getSimpleName(), location.getFileName(), location.getMethodName(), location.getLineNumber()
                );
    }

    public Text getExceptionText(Exception e) {
        StackTraceElement location = e.getStackTrace().length > 0 ? e.getStackTrace()[0] : UNKNOWN_LOCATION;
        String exceptionName = e.getClass().getSimpleName();

        Style defaultStyle = Style.EMPTY.withColor(Formatting.WHITE);

        // 제목 부분
        MutableText titleText = Text.literal("[" + purpose + "] 수행 중 ");

        // 예외 이름
        titleText.append(Text.literal(exceptionName)
                .setStyle(Style.EMPTY.withColor(Formatting.RED).withBold(true)));

        titleText.append(
                Text.literal(" 발생").setStyle(defaultStyle)
        );

        // 위치 정보
        MutableText locationText = Text.literal("\n위치: ").setStyle(defaultStyle);

        // 파일명
        locationText.append(
                Text.literal(location.getFileName()).setStyle(Style.EMPTY.withColor(Formatting.GOLD).withUnderline(true))
        );

        // 메서드명과 라인 번호
        locationText.append(
                Text.literal(", " + location.getMethodName() + "(), Line: " + location.getLineNumber())
                .setStyle(Style.EMPTY.withColor(Formatting.YELLOW))
        );

        // 제보 링크
        MutableText reportText = Text.literal("\n[제보하러 가기]")
                .setStyle(Style.EMPTY
                        .withColor(Formatting.AQUA)
                        .withBold(true)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, GlobalVariables.GITHUB_ISSUE_URL))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("클릭하여 GitHub 이슈 페이지로 이동")))
                );

        // 모든 텍스트 조합
        MutableText fullText = Text.literal("")
                .append(titleText)
                .append(locationText)
                .append(reportText);

        return fullText;
    }
}
