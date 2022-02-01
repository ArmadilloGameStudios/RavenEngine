#version 410

layout(location = 0) out vec4 frag_color;
layout(location = 1) out vec3 frag_id;

uniform vec3 id;
uniform float z;
uniform vec4 highlight;

in vec2 texture_coords;

void main() {

    gl_FragDepth = z;

    frag_color = highlight;

    if (id != vec3(0))
        frag_id = id;
    else
        frag_id = vec3(1);
}