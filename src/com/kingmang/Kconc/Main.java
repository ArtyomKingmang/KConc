package com.kingmang.Kconc;

import java.io.IOException;
import java.util.Scanner;

import static com.kingmang.Kconc.Render.load;
import static com.kingmang.Kconc.Render.render;
import static com.kingmang.Kconc.Screen.initScreen;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);
        String file = scan.nextLine();
        Engine.cols = Integer.parseInt("50");
        Engine.rows = Integer.parseInt("50");
        Engine.screenBuffer = new char[2][Engine.rows][Engine.cols + 1];

        initScreen(Engine.screenBuffer[0]);
        initScreen(Engine.screenBuffer[1]);
        render(load(file));
    }
}
