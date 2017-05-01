uniform sampler2D colorTexture;
uniform sampler2D glowTexture;
uniform sampler2D idTexture;

varying vec2 coord;

void main(void) {
	vec3 color = texture(colorTexture, coord).rgb;
	vec3 glow = texture(glowTexture, coord).rgb;
	float glowStrength = max(max(glow.r, glow.g), glow.b);
	
	gl_FragColor = vec4(color * (1.0 - glowStrength) + glow, 1.0);
}