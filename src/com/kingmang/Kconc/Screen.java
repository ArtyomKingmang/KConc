package com.kingmang.Kconc;

import static com.kingmang.Kconc.Matrix.getCols;
import static com.kingmang.Kconc.Matrix.screenBuffer;

public class Screen {
    public static void initScreen(char[][] screen) {
        for (int row = 0; row < screen.length; row++) {
            for (int col = 0; col < screen[0].length - 1; col++) {
                screen[row][col] = ' ';
            }
            screen[row][getCols()] = '\n';
        }
    }

    public static void drawScreen() {
        StringBuilder buffer = new StringBuilder();
        for (int row = 0; row < screenBuffer[1].length; row++) {
            for (int col = 0; col < screenBuffer[1][0].length; col++) {
                buffer.append(screenBuffer[1][row][col]);
            }
        }
        buffer.append("\033[H");
        System.out.print(buffer.toString());
    }
}
