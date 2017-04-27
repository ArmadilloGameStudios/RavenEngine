package com.crookedbird.engine.graphics3d;

public class GLMatrix {

	// public static float[] ortho(float left, float right, float bottom,
	// float top, float zNear, float zFar) {
	//
	// Matrix4f m = new Matrix4f();
	//
	// m.m00 = 2 / (right - left);
	// m.m11 = 2 / (top - bottom);
	// m.m22 = -2 / (zFar - zNear);
	// m.m30 = -(right + left) / (right - left);
	// m.m31 = -(top + bottom) / (top - bottom);
	// m.m32 = -(zFar + zNear) / (zFar - zNear);
	//
	// return m;
	// }
	//
	// public static float[] frustum(float left, float right, float bottom,
	// float top, float zNear, float zFar) {
	//
	// Matrix4f m = new Matrix4f();
	//
	// m.m00 = (2 * zNear) / (right - left);
	// m.m11 = (2 * zNear) / (top - bottom);
	// m.m20 = (right + left) / (right - left);
	// m.m21 = (top + bottom) / (top - bottom);
	// m.m22 = -(zFar + zNear) / (zFar - zNear);
	// m.m23 = -1;
	// m.m32 = -(2 * zFar * zNear) / (zFar - zNear);
	//
	// return m;
	// }

	public static float[] perspective(float fovy, float aspect, float zNear,
			float zFar) {

		float range = (float) Math.tan(Math.toRadians(fovy / 2)) * zNear;
		float left = -range * aspect;
		float right = range * aspect;
		float bottom = -range;
		float top = range;

		float[] m = new float[] { (2 * zNear) / (right - left), 0, 0, 0, 0,
				(2 * zNear) / (top - bottom), 0, 0, 0, 0,
				-(zFar + zNear) / (zFar - zNear), -1, 0, 0,
				-(2 * zFar * zNear) / (zFar - zNear), 0, };

		return m;
	}

	// public static Matrix4f lookAt(Vector3f eye, Vector3f center, Vector3f up)
	// {
	//
	// Vector3f forward = Vector3f.sub(center, eye, null);
	// forward.normalise(forward);
	//
	// Vector3f u = up.normalise(null);
	//
	// Vector3f side = Vector3f.cross(forward, u, null);
	// side.normalise(side);
	//
	// Vector3f.cross(side, forward, u);
	//
	// Matrix4f m = new Matrix4f();
	//
	// m.m00 = side.x;
	// m.m10 = side.y;
	// m.m20 = side.z;
	//
	// m.m01 = u.x;
	// m.m11 = u.y;
	// m.m21 = u.z;
	//
	// m.m02 = -forward.x;
	// m.m12 = -forward.y;
	// m.m22 = -forward.z;
	//
	// m.translate(new Vector3f(-eye.x, -eye.y, -eye.z));
	//
	// return m;
	// }
}
