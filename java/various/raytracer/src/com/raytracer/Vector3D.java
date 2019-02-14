package com.raytracer;

public class Vector3D {
    private double x;
    private double y;
    private double z;

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3D(double x, double y) {
        this.x = x;
        this.y = y;
        this.z = 0;
    }

    public Vector3D(double x ) {
        this.x = x;
        this.y = 0;
        this.z = 0;
    }

    public Vector3D() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public void add(Vector3D other) {
        this.x += other.x;
        this.y += other.y;
        this.z += other.z;
    }

    public void substract(Vector3D other) {
        this.x -= other.x;
        this.y -= other.y;
        this.z -= other.z;
    }

    public void scalarMultiplication(double d) {
        this.x *= d;
        this.y *= d;
        this.z *= d;
    }


    public void normalize() {
        double length = Util.scalarProduct(this, this);
        if (Math.abs(length) == 0d) {
            this.x = 0d;
            this.y = 0d;
            this.z = 0d;
        } else {
            this.x /= length;
            this.y /= length;
            this.z /= length;
        }
    }

    public void cross(Vector3D other) {
        this.x = this.y * other.z - this.z * other.y;
        this.y = this.z * other.x - this.x * other.z;
        this.z = this.x * other.y - this.y * other.z;
    }

    public Vector3D add2(Vector3D other) {
        return new Vector3D(this.x + other.x,this.y + other.y, this.z + other.z);
    }

    public Vector3D scalarMultiplication2(double d) {
        return new Vector3D(this.x * d,this.y * d, this.z * d);
    }

    public Vector3D substract2(Vector3D other) {
        return new Vector3D(this.x - other.x,this.y - other.y, this.z - other.z);
    }

    public Vector3D normalize2() {
        double length = Util.scalarProduct(this, this);
        if (Math.abs(length) == 0d) {
            return new Vector3D();
        }

        return new Vector3D(this.x/length, this.y/length, this.z/length);
    }

    public Vector3D cross2(Vector3D other){
        return new Vector3D(
                this.y * other.z - this.z * other.y,
                this.z * other.x - this.x * other.z,
                this.x * other.y - this.y * other.x
        );
    }

    public double product(Vector3D other){
        return this.z * other.z + this.x * other.x + this.y * other.y;
    }

    public double selfProduct(){
        return this.z * this.z + this.x * this.x + this.y * this.y;
    }

    public Vector3D move(double epsilon, Vector3D positionToLight) {
        return this.add2(positionToLight.scalarMultiplication2(epsilon));
    }

    public Vector3D muliply(Vector3D other) {
        return new Vector3D(
                this.x * other.x,
                this.y * other.y,
                this.z * other.z);
    }

    public double lenght() {
        return  Util.scalarProduct(this, this);
    }
}
