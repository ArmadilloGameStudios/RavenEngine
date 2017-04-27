in float glow;

varying vec3 N, L;
varying vec4 glowColor;

vec3 lightPos = vec3(0, 16, 16);
varying float lightDis;

void main(void) 
{
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
	
	N = normalize(gl_NormalMatrix * gl_Normal);
	vec4 V = gl_ModelViewMatrix * gl_Vertex;
	L = normalize(lightPos - V.xyz);
	
	lightDis = distance(lightPos, V.xyz);
	
	gl_FrontColor = gl_Color; 	
	glowColor = gl_Color * glow;
}