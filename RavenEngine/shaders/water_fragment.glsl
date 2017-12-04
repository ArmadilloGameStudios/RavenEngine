#version 430
layout(location = 0) out vec4 frag_color;
layout(location = 1) out vec3 frag_glow;
layout(location = 2) out vec3 frag_id;

uniform vec3 id;

in vec3 light, lightGlow, color;

in float water_camera_distance;

in float murk;

void main(void) {

    float c_murk = clamp(murk, 0.0, 1.0);

    frag_color = vec4(color * light * (1.0 - c_murk / 4.0), c_murk);

	frag_glow = lightGlow;
	frag_id = id;
}