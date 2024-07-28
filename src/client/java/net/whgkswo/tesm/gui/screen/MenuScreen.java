package net.whgkswo.tesm.gui.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.whgkswo.tesm.TESMMod;
import net.whgkswo.tesm.general.GeneralUtil;
import net.whgkswo.tesm.gui.Alignment;
import net.whgkswo.tesm.gui.RenderingHelper;
import net.whgkswo.tesm.gui.colors.CustomColor;
import net.whgkswo.tesm.gui.component.LineDirection;
import net.whgkswo.tesm.gui.component.bounds.Boundary;
import net.whgkswo.tesm.gui.component.bounds.LinearBound;
import net.whgkswo.tesm.gui.component.bounds.RectangularBound;
import net.whgkswo.tesm.gui.component.elements.BlankedBox;
import net.whgkswo.tesm.gui.component.elements.StraightLine;
import net.whgkswo.tesm.gui.component.elements.TextBox;
import net.whgkswo.tesm.gui.component.elements.TextBoxWIthBackground;
import net.whgkswo.tesm.gui.screen.templete.CustomScreen;
import org.lwjgl.glfw.GLFW;

public class MenuScreen extends CustomScreen {
    public MenuScreen() {
        super();
    }
    private final Identifier MENU_BACKGROUND = new Identifier(TESMMod.MODID, "textures/gui/menu_background.png");

    @Override
    public void init(){
        super.init();
        GeneralUtil.repeatWithIndex(3, i -> {
            createComponent("testComponent" + i,
                    new TextBoxWIthBackground(
                            new CustomColor(200,160,130),
                            "abc 가나다 123",
                            0xffffff,
                            0.7f,
                            Alignment.LEFT,
                            new RectangularBound(Boundary.BoundType.FIXED,0.1, 0.15 + 0.05 * i, 0.2, 0.04),
                            0.05));
        });
        /*createComponent("testLine",
                new StraightLine(
                        CustomColor.ColorsPreset.RODEO_DUST.getColor(),
                        LineDirection.VERTICAL,
                        new LinearBound(Boundary.BoundType.FIXED, 0.27, 0.25, 0.9, 1)
                        ));
        createComponent("testLine 2",
                new StraightLine(
                        CustomColor.ColorsPreset.RODEO_DUST.getColor(),
                        LineDirection.VERTICAL,
                        new LinearBound(Boundary.BoundType.FIXED, 0.73, 0.25, 0.9, 1)
                ));*/
        createComponent("testBox",
                new BlankedBox(
                        new CustomColor(200, 160, 130),
                        new RectangularBound(Boundary.BoundType.FIXED, 0.25,0.25,0.5,0.5),1
                ));
        createComponent("testTextbox",
                new TextBox(
                        new CustomColor(255,255,255),
                        new RectangularBound(Boundary.BoundType.FLEXIBLE, 0.25, 0.25, 0.5, 0.5),
                        textRenderer,
                        "늑대여왕 전기\n" +
                                "\n" +
                                "카타르 에리파네스 지음\n" +
                                "\n" +
                                "분명하게 악인으로 취급받는 역사상 인물은 많지 않지만, 소위 솔리튜드의 늑대여왕이라고 불리우는 포테마는 확실히 그러한 불명예를 얻었다. 그녀는 임페리얼 왕조에서 세 번째 시대의 67년에 태어나, 그녀의 할아버지이자 인정 많기로 유명한 유리엘 셉팀 2세에게 보여졌다. 그가 이 근엄하고 강한 얼굴의 아기를 보고 속삭이기를, '당장이라도 달려들 듯 한 암늑대 같다'고 하였다.\n" +
                                "\n" +
                                "임페리얼 시티에서의 포테마의 어린 시절은 실로 시작부터 고난이었다. 그녀의 아버지인 펠라기우스 셉팀 왕자와, 어머니인 퀴자라는 그들의 아이들에 애정이 거의 없었다. 포테마가 태어났을 당시 16세였던 장남 안티오커스는 이미 술주정뱅이이자 바람둥이로 제국 내에서 악명이 높았다. 그녀의 남동생인 세피로스와 매그너스는 훨씬 나중에 태어났으므로, 몇년 간 그녀는 궁정의 유일한 어린아이였다.\n" +
                                "\n" +
                                "14세가 되자, 포테마는 많은 구혼자들이 줄을 선 미녀가 되었으나, 북쪽의 왕국인 솔리튜드의 왕 만티아코와의 관계를 결속하기 위해서 그와 결혼했다. 그녀는 처음에 볼모로서 입궁했다고 하지만, 곧 왕비가 되었다. 나이 든 왕 만티아코는 그녀를 사랑했고, 그녀가 원했던 권력을 전부 휘두르도록 해주었는데, 그녀가 바랐던 것은 권력이 전부였다.\n" +
                                "\n" +
                                "유리엘 셉팀 2세가 다음 해에 죽자, 그녀의 아버지는 황제가 되었고, 그의 형편없는 경영 때문에 국고는 격감했다. 펠라기우스 2세는 그들의 지위를 돌려받으려면 돈을 주고 다시 살 것을 강요하며 원로의원회를 해산시켰다. 3E 97년, 많은 유산 끝에 솔리튜드의 여왕은 아들을 낳았고, 조부의 이름을 따 유리엘이라고 이름 지었다. 만티아코는 재빨리 그를 후계자로 지목했지만, 여왕은 그녀의 아이를 위한 훨씬 더 큰 야망을 품고 있었다.\n" +
                                "\n" +
                                "2년 후, 펠라기우스 2세가 사망했고 — 많은 이들은 복수심에 불타는 의원회의 일원에 의해 독살 당했을 거라고 한다 — 그의 아들이자, 포테마의 오빠인 안티오커스가 왕좌를 물려받았다. 48세에 이르러서도, 그의 방탕한 씨앗은 아직도 계속 뿌려지고 있었고, 역사책에서는 그의 재위기간 동안의 궁 안을 묘사하는 것에 거의 외설적일 정도였다. 간음이 아닌 권력에 열성적이었던 포테마는 임페리얼 시티를 방문할 때마다 분개했다.\n" +
                                "\n" +
                                "솔리튜드의 왕 만티아코는 펠라기우스 2세를 따라 봄에 사망했다. 유리엘이 왕좌에 올랐고, 어머니와 함께 통치했다. 의심할 여지없이, 유리엘은 혼자서 통치하는 것이 정당했으며 본인도 그것을 원했을 것이라 여겨지지만, 포테마는 그의 현 위치는 임시적일 뿐이라고 아들을 설득했다. 그는 단순히 왕국이 아니라 제국 전체를 손에 넣으려 했다. 솔리튜드 성에서, 그녀는 스카이림의 다른 왕국에서 온 많은 외교관들과 교제했으며, 불만의 씨앗을 퍼트렸다. 수년 간 그녀의 내빈 목록은 하이 락과 모로윈드의 왕과 왕비들을 포함하기에 이르렀다.\n" +
                                "\n" +
                                "13년 동안, 안티오커스는 탐리엘을 다스렸고, 그의 도덕적 방종에도 불구하고 유능한 지도자임이 증명되었다. 몇몇 역사가들은 포테마가 주문을 걸어 오빠의 목숨을 끊었다는 증거를 제시하지만, 어떻든 간에 증거는 시간의 모래 속으로 사라져 버렸다. 아무튼, 그녀와 그녀의 아들인 유리엘은 안티오커스가 사망했을 당시인 3E 112년에 임페리얼 황궁을 방문했고, 즉시 그의 딸이자 후계자인 킨티라의 통치에 대항했다.\n" +
                                "\n" +
                                "원로의원회에게 했던 포테마의 연설은 연설법을 공부하는 사람에게 도움이 될 만하다.\n" +
                                "\n" +
                                "그녀는 아첨과 겸손으로 말을 꺼내기 시작했다: '가장 위엄 있고 현명한 원로의원회 여러분, 나는 단지 저 지방의 여왕일 뿐이고, 그저 여러분이 이미 숙고하고 계신 것으로 추정되는 것만을 말할 뿐입니다.'\n" +
                                "\n" +
                                "그녀는 결함에도 불구하고 인기있던 통치자였던 지난 황제를 칭찬하며 말을 이어나갔다: '그는 진정한 셉팀가의 사람이었으며 훌륭한 전사였습니다 — 물론 이 의원회와 함께 — 거의 무적이었던 피안도네아의 함대도 파괴했습니다.'\n" +
                                "\n" +
                                "하지만 요점으로 들어가기 전의 시간 낭비는 매우 짧았다: '매그나 여제는 애석하게도 오라버니의 욕정으로 가득한 성질을 누그러뜨리기 위해 아무런 일도 하지 않았습니다. 사실상, 도시 빈민가의 매춘부들도 그녀만큼 많은 잠자리를 하지는 못했을 것입니다. 그녀가 궁의 침실에서의 그녀의 역할에 좀 더 정숙하게 임했다면, 우리는 스스로를 황제의 자손이라 칭하는 멍청하고 약해 빠진 사생아들이 아닌, 진정한 황제의 후계자를 모실 수 있었을 것입니다. 킨티라로 불리는 여자아이는 매그나와 경비대장 사이에서 태어났다고 이미 널리 믿어지고 있습니다. 어쩌면 매그나와 수조를 청소하는 소년의 아이일지도 모릅니다. 우리가 확실히 알 수 있는 것은 없습니다. 셉팀 왕조의 마지막 후손인, 내 아들 유리엘만큼 혈통이 확실한 사람은 없는 것입니다.\n" +
                                "\n" +
                                "포테마의 웅변에도 불구하고, 원로의원회는 킨티라를 여제 킨티라 2세로서 왕좌에 즉위하는 것을 승인했다. 포테마와 유리엘은 분개하여 스카이림으로 돌아왔고 반란을 계획하기 시작했다.\n" +
                                "\n" +
                                "붉은 다이아몬드의 전쟁에 대한 세부 사항은 다른 역사서에 포함되어 있으므로, 여제 킨티라 2세의 포획과 3E 114년에 결국 하이 락에서 처형된 이야기나, 포테마의 아들인 유리엘 3세가 7년 뒤 황제에 즉위한 것에 대해 여기서는 다시 언급하지 않도록 한다. 그녀의 살아남은 남동생인 세피로스와 매그너스는 수년 간 황제와 그 어머니에 대항해 전쟁을 일으켜, 내전으로 제국을 분열시켰다.\n" +
                                "\n" +
                                "유리엘 3세가 3E 127년 당시 삼촌인 세피로스와 해머펠에서 이치닥 전투를 벌일 때, 포테마는 그녀의 다른 남동생, 유리엘의 삼촌 매그너스와 스카이림에서 팔콘스타 전투에 임하고 있었다. 그녀가 아들의 패전과 포획 소식을 들었을 때 그녀는 막 매그너스의 가장 약한 측면을 공격하려던 참이었다. 61세의 늑대 여왕은 분노하여 공격을 직접 지휘했다. 전투는 성공적이었고, 매그너스와 그의 군대는 달아났다. 승리를 축하하는 행사가 한창일 무렵, 포테마는 그녀의 아들인 황제가 임페리얼 시티에서 재판에 부쳐지기도 전에 분노한 군중에 의해서 처형되었다는 소식을 들었다. 그는 이송 도중에 산채로 불에 타 죽었다.\n" +
                                "\n" +
                                "세피로스가 황제 즉위를 선포하자, 포테마의 분노는 극에 달했다. 그녀는 데이드라를 소환해 싸우게 했고, 그녀의 강령술사들은 쓰러진 적군을 언데드 전사로 되살려냈으며, 세피로스 1세의 군대에 맞서 싸우고 또 싸웠다. 그녀의 광기가 커져감에 따라, 그녀의 동맹들은 그녀를 떠나기 시작하여 그녀에게 남은 동료라고는 수년 간 축적한 좀비들과 해골병사들 뿐이었다. 솔리튜드 왕국은 죽음의 땅이 되었다. 고대 늑대 여왕이 썩어가는 해골병사들의 시중을 받으며, 실험체를 겁에 질리게 하는 흡혈귀 장군들과 함께 전쟁 계획을 짜고 있다는 이야기가 전해진다.\n" +
                                "\n" +
                                "포테마는 그녀의 성이 1개월 간 포위당한 뒤 3E 137년에 90세의 나이로 사망했다. 그녀가 살아있을 당시, 그녀는 솔리튜드의 늑대 여왕이었고, 펠라기우스 2세의 딸이자, 만티아코 왕의 아내였으며, 여제 킨티라 2세의 고모, 유리엘 3세 황제의 어머니, 그리고 황제들인 안티오커스와 세피로스의 누나였다. 그녀의 사후 3년째에 세피로스는 사망하였으며 그의 — 그리고 포테마의 — 동생인 매그너스가 왕좌를 차지했다.\n" +
                                "\n" +
                                "그녀의 죽음이 그 악명을 줄여주지는 못했다. 직접적인 증거는 거의 없지만, 일부 신학자들은 그녀의 영혼이 너무나 강하여, 죽은 뒤 데이드라가 되었고 필멸자들의 광적인 야망과 반역을 부추기고 있다고 주장한다. 또한 그녀의 광기가 솔리튜드 성 안에 깊이 스며있어 다음에 통치할 왕을 물들인다고 전해진다. 얄궂게도 그것은 매그너스의 아들이자 그녀의 18살 난 조카인 펠라기우스였다. 전설의 사실성이야 어떻든지 펠라기우스가 3E 145년에 솔리튜드를 떠나 황제 펠라기우스 3세가 되었을 때, 그는 곧 미친 펠라기우스로 알려지게 되었다. 심지어 그가 아버지를 살해했다는 소문도 널리 퍼져 있다.\n" +
                                "\n" +
                                "실로 늑대 여왕이 최후의 승리자이다.",
                        0.6f, 0.04, 0.04, Alignment.LEFT
                ));
    }
    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        RenderingHelper.renderTexture(context, MENU_BACKGROUND, 0.1,0.1,0.8,0.8);
    }
    @Override
    public boolean shouldPause() {
        return false;
    }
    @Override
    public boolean shouldFreezeTicks(){
        return true;
    }
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers){
        if(keyCode == GLFW.GLFW_KEY_TAB || keyCode == GLFW.GLFW_KEY_J){
            this.close();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
