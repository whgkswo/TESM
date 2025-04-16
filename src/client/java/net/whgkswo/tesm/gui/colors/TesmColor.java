package net.whgkswo.tesm.gui.colors;

public class TesmColor {
    private int r;
    private int g;
    private int b;
    private double a;

    public static final TesmColor WHITE = new TesmColor(255,255,255);
    public static final TesmColor BLACK = new TesmColor(0,0,0);
    public static final TesmColor CREAM = new TesmColor(250,240,215);
    public static final TesmColor MINT_JULEP = new TesmColor(227,215,180);
    public static final TesmColor NEUTRAL_GOLD = new TesmColor(170,166,133);
    public static final TesmColor RODEO_DUST = new TesmColor(200,160,130);
    public static final TesmColor TRANSPARENT = new TesmColor(0, 0, 0, 0);

    public TesmColor(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
        a = 255;
    }

    public TesmColor(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }
    public TesmColor(String hex){
        int[] rgb = hexToRGB(hex);
        r = rgb[0];
        g = rgb[1];
        b = rgb[2];
    }
    public TesmColor(String hex, int a){
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
        return (int) Math.round(a);
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
    public void addA(int addition){
        a += addition;
        if(a > 255){
            a = 255;
        }
        if(a < 0){
            a = 0;
        }
    }
    public String getHexCode(){
        return String.format("#%02x%02x%02x", r, g, b);
    }
    public int getHexDecimalCode() {
        String hex = String.format("%02x%02x%02x%02x", Math.round(a), r, g, b);
        return (int)Long.parseLong(hex, 16);
    }

    public TesmColor withAlpha(int alpha){
        return new TesmColor(r, g, b, alpha);
    }
}
