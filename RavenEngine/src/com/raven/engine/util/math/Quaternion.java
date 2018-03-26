package com.raven.engine.util.math;

public class Quaternion {

    public float w, x, y, z;

    public static Quaternion tempQuat = new Quaternion();

    public static Quaternion lerp(Quaternion a, Quaternion b, float alpah, Quaternion out) {
        return lerp(a, b, alpah, true, out);
    }

    public static Quaternion lerp(Quaternion a, Quaternion b, float alpah, boolean normalize, Quaternion out) {

        if (normalize) {
            tempQuat.w = a.w * (1f - alpah) + b.w * alpah;
            tempQuat.x = a.x * (1f - alpah) + b.x * alpah;
            tempQuat.y = a.y * (1f - alpah) + b.y * alpah;
            tempQuat.z = a.z * (1f - alpah) + b.z * alpah;

            tempQuat.normalize(out);
        } else {
            out.w = a.w * (1f - alpah) + b.w * alpah;
            out.x = a.x * (1f - alpah) + b.x * alpah;
            out.y = a.y * (1f - alpah) + b.y * alpah;
            out.z = a.z * (1f - alpah) + b.z * alpah;
        }

        return out;
    }

    public Quaternion() {
        this(1,0,0,0);
    }

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

    public Quaternion scale(float s, Quaternion out) {
        out.w = this.w * s;
        out.x = this.x * s;
        out.y = this.y * s;
        out.z = this.z * s;

        return out;
    }

    public Quaternion normalize(Quaternion out) {
        this.scale(1 / this.length(), out);

        return out;
    }

    public float length2() {
        return w*w + x*x + y*y + z*z;
    }

    public float length() {
        return (float)Math.sqrt(length2());
    }

    @Override
    public String toString() {
        return "" + w + " " + x + " " + y + " " + z;
    }
}
