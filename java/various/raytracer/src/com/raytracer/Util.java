package com.raytracer;

public class Util {
    public static final double EPSILON = 0.00004;

    public static double scalarProduct(Vector3D a, Vector3D b){
        return Math.sqrt(a.getX()* b.getX() + a.getY()* b.getY() + a.getZ()* b.getZ());
    }


    public static Vector3D mitternachtsformel(double a, double b, double c) {
        if(a == 0d){
            return new Vector3D();
        }

        double discrimamnt = Math.pow(b, 2) - (4*a*c);
        if(discrimamnt < 0d) {
            return new Vector3D();
        }

        if(discrimamnt == 0d){
            return new Vector3D(-b/(2*a), 0, 1);
        }

        double rightPart = Math.sqrt(discrimamnt);
        return new Vector3D((-b + rightPart)/(2*a), (-b - rightPart)/(2*a), 2);

    }

}
