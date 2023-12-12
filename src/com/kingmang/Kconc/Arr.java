package com.kingmang.Kconc;

public class Arr {
    public static double[] toArray1D(double[][] array2D) {
        double[] array1D = new double[array2D.length];
        for (int i = 0; i < array1D.length; i++) {
            array1D[i] = array2D[i][0];
        }
        return array1D;
    }

    public static double[][] toArray2D(double[] array1D) {
        double[][] array2D = new double[array1D.length][1];
        for (int i = 0; i < array2D.length; i++) {
            array2D[i][0] = array1D[i];
        }
        return array2D;
    }
}
