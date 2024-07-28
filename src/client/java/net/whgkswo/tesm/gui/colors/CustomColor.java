package net.whgkswo.tesm.gui.colors;

public class CustomColor{
    private int r;
    private int g;
    private int b;
    private int a;
    public CustomColor(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
        a = 255;
    }

    public CustomColor(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }
    public CustomColor(String hex) throws InvalidHexcodeException {
        int[] rgb = hexToRGB(hex);
        r = rgb[0];
        g = rgb[1];
        b = rgb[2];
    }
    public CustomColor(String hex, int a) throws InvalidHexcodeException {
        int[] rgb = hexToRGB(hex);
        r = rgb[0];
        g = rgb[1];
        b = rgb[2];
        this.a = a;
    }
    public static int[] hexToRGB(String hex) throws InvalidHexcodeException {
        int offset = 0;
        if(hex.startsWith("#")){
            offset = 1;
        }
        if(hex.startsWith("0x")){
            offset = 2;
        }
        if(offset == 0){
            throw new InvalidHexcodeException();
        }
        return new int[]{
                Integer.parseInt(hex.substring(offset, offset + 2), 16),
                Integer.parseInt(hex.substring(offset + 2, offset + 4), 16),
                Integer.parseInt(hex.substring(offset + 4, offset + 8), 16)
        };
    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }

    public int getA() {
        return a;
    }
    public float getFloatR(){
        return (float) r / 255;
    }
    public float getFloatG(){
        return (float) g / 255;
    }
    public float getFloatB(){
        return (float) b / 255;
    }
    public float getFloatA(){
        return (float) a / 255;
    }

    public void setA(int a) {
        this.a = a;
    }
    public String getHexCode(int r, int g, int b){
        return String.format("#%02x%02x%02x", r, g, b);
    }

    public enum ColorsPreset {
        WHITE(new CustomColor(255,255,255)),
        BLACK(new CustomColor(0,0,0)),
        NEUTRAL_GOLD(new CustomColor(170,166,133)),
        RODEO_DUST(new CustomColor(200,160,130))
        ;
        private CustomColor color;

        ColorsPreset(CustomColor color) {
            this.color = color;
        }

        public CustomColor getColor() {
            return color;
        }
    }
}
