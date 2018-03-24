package com.raven.engine.util.math;

public class Quaternion {

    public float w, x, y, z;

    public Quaternion(float w, float x, float y, float z) {
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Matrix4f toMatrix(Matrix4f out) {
        out.m00 = 1f - 2f*y*y - 2f*z*z;
        out.m01 = 2f*x*y - 2f*z*w;
        out.m02 = 2f*x*z + 2f*y*w;
        out.m03 = 0f;

        out.m10 = 2f*x*y + 2f*z*w;
        out.m11 = 1f - 2f*x*x - 2f*z*z;
        out.m12 = 2f*y*z - 2f*x*w;
        out.m13 = 0f;

        out.m20 = 2f*x*z - 2f*y*w;
        out.m21 = 2f*y*z + 2f*x*w;
        out.m22 = 1f - 2f*x*x - 2f*y*y;
        out.m23 = 0f;

        out.m30 = 0f;
        out.m31 = 0f;
        out.m32 = 0f;
        out.m33 = 1f;

        return out;
    }

    @Override
    public String toString() {
        return "" + w + " " + x + " " + y + " " + z;
    }
}
