varying vec2 coord;

void main(void) {
	gl_Position = gl_Vertex;
	
	coord = vec2(gl_MultiTexCoord0);
}