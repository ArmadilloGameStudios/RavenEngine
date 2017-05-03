#version 430

layout(location = 0) out vec3 frag_glow;

uniform sampler2D glowTexture; // problem when writing to location=1?

uniform vec2 bloomStep;

in vec2 coord;

void main(void) {
	vec3 glow = texture(glowTexture, coord).rgb;
	vec3 bloomGlow = glow / 3.0;

	for (float i = 1; i < 5.0; i++) {
		bloomGlow += (texture(glowTexture, coord + bloomStep * i * 2.0).rgb +
		             texture(glowTexture, coord - bloomStep * i* 2.0).rgb) / (4.0 * i);
	}

    frag_glow = bloomGlow;
}