package com.raytracer;
import java.awt.*;

public abstract  class Object3dBase implements Object3D {

   private Color color;
    private Material material;

    public Object3dBase(Material material) {
        this.material = material;
    }

    @Override
    public double interset(Ray ray) {
        return 0;
    }

    @Override
    public int getColor(Vector3D position) {
        return material.getRGB(position, 0);
    }

    @Override
    public void setColor(Color c) {
        material.setColor(c);
    }
}
