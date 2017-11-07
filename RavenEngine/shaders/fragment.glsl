#version 430
layout(location = 0) out vec4 frag_color;
layout(location = 1) out vec3 frag_glow;
layout(location = 2) out vec3 frag_id;

uniform in vec3 id;

in vec3 light, lightGlow, color;

void main(void) {
    frag_color = vec4(color * light, 1.0);
	frag_glow = lightGlow;
	frag_id = id;
}