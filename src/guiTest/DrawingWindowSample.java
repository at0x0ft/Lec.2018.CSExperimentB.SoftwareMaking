import processing.core.*;

public class DrawingWindowSample extends PApplet {
    // ウィンドウサイズの指定 (w * h)
    public void settings() {
        size(400, 300);
    }

    // 背景色を黒に設定 (0~255).
    public void setup() {
        background(0);
    }

    // 描画
    public void draw() {
        // 描画する線の色の指定.
        stroke(255, 255, 0);
        // 描画する線の幅の指定.
        strokeWeight(3);

        // マウスが押されたら
        if (mousePressed) {
            // 微小区間において直線を描画 (連続時間でマウスを動かすと曲線っぽく見える.)
            line(mouseX, mouseY, pmouseX, pmouseY);
        }
    }

    // MainWindowの呼び出し
    public static void main(String args[]) {
        PApplet.main("DrawingWindowSample");
    }
}