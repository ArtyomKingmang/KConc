package com.kingmang.Kconc;

class Rotate {
    private static double[][] x;
    private static double[][] y;
    private static double[][] z;

    public static void update(double[][] x, double[][] y, double[][] z){
        Rotate.x = x;
        Rotate.y = y;
        Rotate.z = z;
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
