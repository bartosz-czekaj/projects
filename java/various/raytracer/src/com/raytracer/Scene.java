package com.raytracer;

import java.awt.*;
import java.util.ArrayList;

public class Scene {

    private static Scene scene;
    private ArrayList<Object3D> objects = new ArrayList<>();
    private ArrayList<Light> lights = new ArrayList<>();

    private Scene() {
        objects.add(new Sphere(5, new Vector3D(), new Material(Color.green)));
        objects.add(new Sphere(3, new Vector3D(10, 0, 0), new Material(Color.red)));
        lights.add(new PointLight(new Vector3D(0, 10, 10), new Vector3D(10,10,10)));
        lights.add(new PointLight(new Vector3D(-10, -10, 0), new Vector3D(10,10,10)));
        lights.add(new PointLight(new Vector3D(0, 10, 0), new Vector3D(10,10,10)));
        lights.add(new PointLight(new Vector3D(10, 10, 0), new Vector3D(10,10,10)));

    }

    public ArrayList<Light> getLights() {
        return lights;
    }

    public static Scene getScene() {
        if(scene == null){
            scene = new Scene();
        }
        return scene;
    }

    public ArrayList<Object3D> get3DObjects() {
        return objects;
    }
}
