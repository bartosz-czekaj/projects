package com.raytracer;

import java.awt.*;

public class Material {
    private Object3D reference;

    private Vector3D ambient;
    private Vector3D diffus = new Vector3D(0.7,0.7, 0.7);
    private Vector3D specular = new Vector3D(0.3,0.3, 0.3);

    private double phongExponent = 5;

    private double reflectionIdx = 0.5;

    public Material(Color c){
         this.ambient = new Vector3D(c.getRed()/255, c.getGreen()/255, c.getBlue()/255);
        //this.ambient = new Vector3D(1, 0,0 );
    }


    public int getRGB(Vector3D position, int depth){
        if(reference == null){
            return 0;
        }

        final Camera camera = new Camera();
        Vector3D sum = new Vector3D();

        for(Light light : Scene.getScene().getLights()){
            Vector3D positionToLight = light.getPosition().substract2(position).normalize2();
            Ray shadow = new Ray(position.move(Util.EPSILON, positionToLight), positionToLight);
            boolean shadowed = shadow.castShadow();

            Vector3D ret = ambient.muliply(light.getIntensity(position));

            if(!shadowed){
                Vector3D normal = this.reference.getNormal(position);
                double NL = Math.max(normal.product(positionToLight), 0d);
                ret.add(diffus.muliply(light.getIntensity(position)).scalarMultiplication2(NL));

                Vector3D reflection = normal.scalarMultiplication2(NL*2).substract2(positionToLight).normalize2();

                Vector3D V = camera.getEye().substract2(position).normalize2();

                double RV = Math.max(reflection.product(V), 0);

                ret.add(specular.muliply(light.getIntensity(position)).scalarMultiplication2(Math.pow(RV, phongExponent)));
            }

            double dist = light.getPosition().substract2(position).lenght();
            sum.add(ret.scalarMultiplication2(1/(dist*dist)).scalarMultiplication2(255));
        }

        if(this.reflectionIdx > 0)
        {
            Vector3D normal = this.reference.getNormal(position);
            Vector3D V = camera.getEye().substract2(position).normalize2();
            double NV = Math.max(normal.product(V), 0);
            Vector3D refl = normal.scalarMultiplication2(NV*2).substract2(V).normalize2();
            Ray reflection = new Ray(position.move(Util.EPSILON, refl), refl);
            int res = reflection.castPrimary(depth + 1);
            Color c = new Color(res);
            Vector3D v = new Vector3D(c.getRed(), c.getGreen(), c.getBlue());
            v.scalarMultiplication(reflectionIdx);
            sum.add(v);
        }

        long x = Math.round(Math.max(0,Math.min(sum.getX(), 255)));
        long y = Math.round(Math.max(0,Math.min(sum.getY(), 255)));
        long z = Math.round(Math.max(0,Math.min(sum.getZ(), 255)));

        Color c = new Color((int)x,(int)y,(int)z);
        return c.getRGB();
    }

    public void setObject3DReference(Object3D ref){
        this.reference = ref;
    }

    public void setColor(Color c) {
    }
}
