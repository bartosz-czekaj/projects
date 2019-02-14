package com.raytracer;

import java.awt.*;

public interface Object3D {
    public double interset(Ray ray);
    public int getColor(Vector3D position);
    public void setColor(Color c);
    public Vector3D getNormal(Vector3D position);
}
