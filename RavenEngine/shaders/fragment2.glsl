#version 430

layout(location = 0) out vec3 frag_color;

uniform sampler2D worldColorTexture;
uniform sampler2D worldDepthTexture;
uniform sampler2D waterColorTexture;
uniform sampler2D waterDepthTexture;

uniform sampler2D glowTexture;

uniform vec2 bloomStep;

in vec2 coord;

void main(void) {
	vec3 world_color = texture(worldColorTexture, coord).rgb;
	float world_depth = texture(worldDepthTexture, coord).r;
	vec3 water_color = texture(waterColorTexture, coord).rgb;
	float water_depth = texture(waterDepthTexture, coord).r;

    vec3 color;
	vec3 glow = texture(glowTexture, coord).rgb;

	vec3 bloomGlow = glow / 3.0;

//	for (float i = 1; i < 5.0; i++) {
//		bloomGlow += (texture(glowTexture, coord + bloomStep * i * 2.0).rgb +
//		             texture(glowTexture, coord - bloomStep * i * 2.0).rgb) / (4.0 * i);
//	}

	float glowStrength = min(max(max(bloomGlow.r, bloomGlow.g), bloomGlow.b), 1.0);

    if (water_depth > world_depth) {
        color = world_color;
    } else {
        float diff = min(1.0, (world_depth - water_depth) * 50.0 + .3);
        color = water_color * diff + (world_color * (1.0 - diff));
    }

	frag_color = color;// color * (1.0 - glowStrength) + bloomGlow;
	// frag_color = vec3(water_depth);
}