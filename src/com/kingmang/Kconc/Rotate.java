package com.kingmang.Kconc;

class Rotate {
    private static double[][] x = new double[3][3];
    private static double[][] y = new double[3][3];
    private static double[][] z = new double[3][3];

    static void updateMatrix(double[][] changedX, double[][] changedY, double[][] changedZ){
        x = changedX;
        y = changedY;
        z = changedZ;
    }

    public static double[][] getX(){
        return x;
    }
    public static double[][] getY(){
        return y;
    }
    public static double[][] getZ(){
        return z;
    }

}
