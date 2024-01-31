package com.kingmang.Kconc;

import static com.kingmang.Kconc.Matrix.angle;

public class Rotate {
    static double[][] x = new double[3][3];
    static double[][] y = new double[3][3];
    static double[][] z = new double[3][3];
    static void updateMatrix(){
        x = new double[][]{
                {1, 0, 0},
                {0, 1, 0},
                {0, 0, 1}
        };
        y = new double[][]{
                {Math.cos(angle), 0, Math.sin(angle)},
                {0, 1, 0},
                {-Math.sin(angle), 0, Math.cos(angle)}
        };

        z = new double[][]{
                {1, 0, 0},
                {0, 1, 0},
                {0, 0, 1}
        };
        // Оставляем матрицу z без изменений
    }
}