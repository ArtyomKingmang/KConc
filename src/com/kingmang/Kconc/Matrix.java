package com.kingmang.Kconc;

import java.util.Arrays;

public class Matrix {
    private static int cols;
    private static int rows;
    private static double angle = 0;
    public static char[][][] screenBuffer;


    public static int clamp(double value, int min, int max) {
        return (int)(Math.min(max, Math.max(value, min)));
    }

    public static double[][] multiplyMatrices(double[][] matrixA, double[][] matrixB) {
        double[][] result = new double[matrixA.length][matrixB[0].length];
        for (int i = 0; i < matrixA.length; i++) {
            for (int j = 0; j < matrixB[0].length; j++) {
                for (int k = 0; k < matrixA[0].length; k++) {
                    result[i][j] += matrixA[i][k] * matrixB[k][j];
                }
            }
        }
        return result;
    }

    public static double[][] sortVerts(double[][] verts) {
        var verts3D = new double[verts.length / 4][4][3];

        for (int i = 0; i < verts.length; i += 4) {
            verts3D[i / 4] = new double[][]{verts[i], verts[i + 1], verts[i + 2], verts[i + 3]};
        }

        Arrays.sort(verts3D, (triangle1, triangle2) -> {
            var firstTriangleAvrege = average(triangle1[0][2], triangle1[1][2], triangle1[2][2]);
            var secondTriangleAvrege = average(triangle2[0][2], triangle2[1][2], triangle2[2][2]);
            return Double.compare(secondTriangleAvrege, firstTriangleAvrege);
        });

        var result = new double[verts.length][3];

        for (int i = 0; i < verts3D.length; i++) {
            System.arraycopy(verts3D[i], 0, result, i * 4, 4);
        }

        return result;
    }

    private static double average(double... values) {
        var optional = Arrays.stream(values).average();
        return optional.isPresent() ? optional.getAsDouble() : 0;
    }

    public static int getCols() {
        return cols;
    }

    public static void setCols(int cols) {
        Matrix.cols = cols;
    }

    public static int getRows() {
        return rows;
    }

    public static void setRows(int rows) {
        Matrix.rows = rows;
    }

    public static double getAngle() {
        return angle;
    }

    public static void setAngle(double angle) {
        Matrix.angle = angle;
    }
}
