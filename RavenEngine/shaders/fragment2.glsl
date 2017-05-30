#version 430

layout(location = 0) out vec3 frag_color;

uniform sampler2D colorTexture;
uniform sampler2D glowTexture;

uniform vec2 bloomStep;

in vec2 coord;

void main(void) {
	vec3 color = texture(colorTexture, coord).rgb;
	vec3 glow = texture(glowTexture, coord).rgb;

	vec3 bloomGlow = glow / 3.0;

	for (float i = 1; i < 5.0; i++) {
		bloomGlow += (texture(glowTexture, coord + bloomStep * i * 2.0).rgb +
		             texture(glowTexture, coord - bloomStep * i * 2.0).rgb) / (4.0 * i);
	}

	float glowStrength = min(max(max(bloomGlow.r, bloomGlow.g), bloomGlow.b), 1.0);
	
	frag_color = color * (1.0 - glowStrength) + bloomGlow;
	frag_color = color;
}