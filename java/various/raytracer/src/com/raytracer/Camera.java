package com.raytracer;

public class Camera {

    private int left = -(Output.getOutput().getCanvasWidth()/2);
    private int right = left * (-1);
    private int top = (Output.getOutput().getCanvasHeight()/2);
    private int bottom = top * (-1);
    private double distance = top/Math.tan(Math.PI/4)/2;

    private Vector3D UP = new Vector3D(0, 1, 0);
    private Vector3D eye = new Vector3D(0, 0, -10);
    private Vector3D Z = new Vector3D(0, 0, 0);

    private Vector3D W = eye.substract2(Z).normalize2();
    private Vector3D U = UP.cross2(W).normalize2();
    private Vector3D V = W.cross2(U).normalize2();

    private Vector3D w_d_negated = W.scalarMultiplication2(distance *(-1));

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getBottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    public Vector3D getUP() {
        return UP;
    }

    public void setUP(Vector3D UP) {
        this.UP = UP;
    }

    public Vector3D getEye() {
        return eye;
    }

    public void setEye(Vector3D eye) {
        this.eye = eye;
    }

    public Vector3D getZ() {
        return Z;
    }

    public void setZ(Vector3D z) {
        Z = z;
    }

    public Vector3D getW() {
        return W;
    }

    public void setW(Vector3D w) {
        W = w;
    }

    public Vector3D getU() {
        return U;
    }

    public void setU(Vector3D u) {
        U = u;
    }

    public Vector3D getV() {
        return V;
    }

    public void setV(Vector3D v) {
        V = v;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Vector3D getW_d_negated() {
        return w_d_negated;
    }

    public void setW_d_negated(Vector3D w_d_negated) {
        this.w_d_negated = w_d_negated;
    }




    public Camera () {
        //W.normalize();
        //U.normalize();
        //V.normalize();
    }
}
