#version 400

layout(location = 0) out vec4 frag_color;
layout(location = 1) out vec4 frag_normal;
layout(location = 2) out vec4 frag_highlight;
layout(location = 3) out vec3 frag_id;

uniform vec3 id;
uniform vec4 highlight;

in vec3 color, normal;

void main(void) {
    frag_color = vec4(color, 0.0);
    frag_normal = vec4((normalize(normal).xyz) * .5 + .5, 0.0);
    frag_highlight = highlight;
	frag_id = id;
}