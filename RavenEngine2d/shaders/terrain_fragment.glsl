#version 400

layout(location = 0) out vec4 frag_color;
layout(location = 1) out vec3 frag_id;

uniform sampler2D spriteSheet;
uniform vec3 id;

in vec2 coords;

void main() {
    vec4 sprite = texture(spriteSheet, coords);
    gl_FragDepth = 1.0 - 1.0 * sprite.a;

    frag_color = sprite.rgba;
    frag_id = id;
}
