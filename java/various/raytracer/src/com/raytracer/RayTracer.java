package com.raytracer;

import java.awt.*;

public class RayTracer {

    private final Camera camera = new Camera();

    public void trace() {
        final int WIDTH = Output.getOutput().getCanvasWidth();
        final int HEIGHT = Output.getOutput().getCanvasHeight();

        for(int i = 0; i < WIDTH; ++i){
            for(int j = 0; j <HEIGHT ; ++j){
                double u = camera.getLeft() + (camera.getRight() - camera.getLeft()) * ((double)i + 0.5) / WIDTH;
                double v = camera.getTop() + (camera.getBottom() - camera.getTop()) * ((double)j+0.5) / HEIGHT;

                Vector3D direction = camera.getV().scalarMultiplication2(v)
                        .add2(camera.getW_d_negated())
                        .add2(camera.getU().scalarMultiplication2(u))
                        .normalize2();

                Ray ray = new Ray(camera.getEye(), direction);

                int color = ray.castPrimary(2);
                Output.setPixel(i,j, color );

            }
        }
    }
}
