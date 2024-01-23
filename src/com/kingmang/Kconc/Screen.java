package com.kingmang.Kconc;

import static com.kingmang.Kconc.Matrix.cols;
import static com.kingmang.Kconc.Matrix.screenBuffer;

public class Screen {
    public static void initScreen(char[][] screen) {
        for (int row = 0; row < screen.length; row++) {
            for (int col = 0; col < screen[0].length - 1; col++) {
                screen[row][col] = ' ';
            }
            screen[row][cols] = '\n';
        }
    }

    public static void drawScreen() {
        String screenStr = "";
        for (int row = 0; row < screenBuffer[1].length; row++) {
            for (int col = 0; col < screenBuffer[1][0].length; col++) {
                screenStr += screenBuffer[1][row][col];
            }
        }
        screenStr += "\033[H";
        System.out.print(screenStr);
    }
}
