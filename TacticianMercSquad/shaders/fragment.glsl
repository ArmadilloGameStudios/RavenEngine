#version 430
layout(location = 0) out vec3 frag_color;
layout(location = 1) out vec3 frag_glow;
layout(location = 2) out vec3 frag_id;

in vec3 light, lightGlow, color;

void main(void) {
	float glowStrength = max(max(lightGlow.r, lightGlow.g), lightGlow.b);

	frag_color = light * color;
	frag_glow = lightGlow;
}