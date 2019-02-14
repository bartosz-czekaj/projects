package com.raytracer;

import java.awt.*;

public class Sphere extends Object3dBase {
    private double radius;
    private Vector3D center;

    public Sphere(double radius, Vector3D center, Material m) {
        super(m);
        this.radius = radius;
        this.center = center;

        m.setObject3DReference(this);
    }

    @Override
    public double interset(Ray ray) {

        final Vector3D origin = ray.getPosition();
        final Vector3D direction = ray.getDirection();

        Vector3D eyeCenter = origin.substract2(center);

        double a = direction.selfProduct();
        double b = 2 * direction.product(eyeCenter);
        double c = eyeCenter.selfProduct() - Math.pow(radius,2);

        Vector3D result = Util.mitternachtsformel(a,b,c);

        switch ((int)Math.round(result.getZ())){
            case 0:
                return Double.MAX_VALUE;
            case 1:
                return result.getX();
            case 2:
                if(result.getX() < 0) {
                    return result.getY() < 0 ? Double.MAX_VALUE : result.getY();
                }
                return result.getY() < 0 ? result.getX() : Math.min(result.getY(),result.getX());
            default:
                return Double.MAX_VALUE;
        }
    }

    @Override
    public Vector3D getNormal(Vector3D position) {
        return position.substract2(center).normalize2();
    }

}
