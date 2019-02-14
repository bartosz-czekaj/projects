package com.raytracer;

import java.awt.*;

public class Ray {
    private Vector3D position;
    private Vector3D direction;


    public Ray(Vector3D position, Vector3D direction) {
        this.position = position;
        this.direction = direction.normalize2();
    }

    public Ray(Vector3D from_point, Vector3D to_point, boolean dummy) {
        this.position = from_point;
        this.direction = to_point.substract2(from_point).normalize2();
    }

    public Vector3D getPosition() {
        return position;
    }

    public Vector3D getDirection() {
        return direction;
    }

    public int castPrimary(int depth) {
        Object3D intersect = null;
        double t = Double.MAX_VALUE - 1;

        for(Object3D obj: Scene.getScene().get3DObjects()) {
            double t2 = obj.interset(this);
            if(t2 > 0 && t2 < t) {
                intersect = obj;
                t = t2;
            }
        }

        if(intersect != null) {
           return intersect.getColor(this.getPosition2(t));
        }

        return Color.BLACK.getRGB();
    }

    public boolean castShadow(){
        double t = Double.MAX_VALUE - 1;

        for(Object3D obj: Scene.getScene().get3DObjects()) {
            double t2 = obj.interset(this);
            if(t2 > 0 && t2 < t) {
                return true;
            }
        }
        return false;
    }

    public Vector3D getPosition2(double t){
        return position.add2(direction.scalarMultiplication2(t));
    }
}
