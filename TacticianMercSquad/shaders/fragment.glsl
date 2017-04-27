varying vec3 N, L;
varying vec4 glowColor;
varying float lightDis;


void main(void) {
	float NdotL = dot(normalize(N), normalize(L));
	vec4 ambiantLight = gl_Color * .2; 
	
	vec4 spectralLight = gl_Color * vec4(max(0.0, NdotL));

	vec4 light = ambiantLight + spectralLight * min(max(0.0, 10.0 / (lightDis)), .8);

	gl_FragColor =  max(light, glowColor);
}