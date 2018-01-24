#version 400

layout(location = 0) in vec3 vertex_pos;

uniform vec2 size = vec2(1920, 1080);
uniform vec2 scale;
uniform float z = 0;
uniform int style = 0; // 0 center,

void main(void) {
    switch (style) {
    case 0:
    default:
        gl_Position = vec4(vertex_pos.xy * scale / size, z, 1.0);
        break;
    }
}