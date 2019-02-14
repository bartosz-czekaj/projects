package com.raytracer;

public class PointLight implements Light {
    private Vector3D position;
    private Vector3D intesity;

    public PointLight(Vector3D position, Vector3D intesity) {
        this.position = position;
        this.intesity = intesity;
    }

    @Override
    public Vector3D getPosition() {
        return position;
    }

    @Override
    public Vector3D getIntensity(Vector3D fromPosition) {
        return intesity;
    }
}
