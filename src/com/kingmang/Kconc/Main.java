package com.kingmang.Kconc;

import java.io.IOException;

import static com.kingmang.Kconc.Render.load;
import static com.kingmang.Kconc.Screen.initScreen;

public class Main {
    public static void main(String[] args) throws IOException {
        String file = "cube.obj";
        Matrix.setCols(50);
        Matrix.setRows(50);
        Matrix.screenBuffer = new char[2][Matrix.getRows()][Matrix.getCols() + 1];

        initScreen(Matrix.screenBuffer[0]);
        initScreen(Matrix.screenBuffer[1]);
        Render rendeerer = new Render();
        rendeerer.render(load(file));

    }


}
