package com.kingmang.Kconc;

import java.util.*;

public class Matrix {
    static int cols;
    static int rows;
    static double angle = 0;
    public static char[][][] screenBuffer;


    public static int clamp(double x, int min, int max) {
        if (x < min) {
            x = min;
        } else if (x > max) {
            x = max;
        }
        return (int) x;
    }

    public static double[][] multiplyMatrices(double[][] matrixA, double[][] matrixB) {
        double[][] finalMatrix = new double[matrixA.length][matrixB[0].length];
        for (int i = 0; i < matrixA.length; i++) {
            for (int j = 0; j < matrixB[0].length; j++) {
                for (int k = 0; k < matrixA[0].length; k++) {
                    finalMatrix[i][j] += matrixA[i][k] * matrixB[k][j];
                }
            }
        }
        return finalMatrix;
    }

    public static double[][] sortVerts(double[][] verts) {
        double[][][] verts3D = new double[verts.length / 4][4][3];
        for (int i = 0; i < verts.length; i += 4) {
            verts3D[i / 4] = new double[][]{verts[i], verts[i + 1], verts[i + 2], verts[i + 3]};
        }
        Arrays.sort(verts3D, new Comparator<double[][]>() {
            @Override
            public int compare(double[][] triangle1, double[][] triangle2) {
                if (((triangle1[0][2] + triangle1[1][2] + triangle1[2][2]) / 3) < ((triangle2[0][2] + triangle2[1][2] + triangle2[2][2]) / 3)) {
                    return 1;
                } else if (((triangle1[0][2] + triangle1[1][2] + triangle1[2][2]) / 3) > ((triangle2[0][2] + triangle2[1][2] + triangle2[2][2]) / 3)) {
                    return -1;
                }
                return 0;
            }
        });

        double[][] finalVerts = new double[verts.length][3];
        for (int i = 0; i < verts3D.length; i++) {
            System.arraycopy(verts3D[i], 0, finalVerts, i * 4, 4);
        }

        return finalVerts;
    }

}
