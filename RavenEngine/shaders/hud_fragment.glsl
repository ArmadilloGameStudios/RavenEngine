#version 400

layout(location = 0) out vec4 frag_color;

uniform vec4 color;

const uniform bool useText = false;
uniform sampler2D text;

in vec2 texture_coords;

void main(void) {
    if (useText) {
        frag_color = texture(text, texture_coords);
    } else {
        frag_color = color;
    }
}