#version 400

layout(location = 0) out vec4 frag_color;
layout(location = 1) out vec3 frag_id;

uniform sampler2D spriteSheet;
uniform vec3 id;
uniform float z;

in vec2 coords;

void main() {
    vec4 sprite = texture(spriteSheet, coords);
    gl_FragDepth = mix(1, z, sprite.a - coords.y / 1000);

    frag_color = sprite.rgba;
    frag_id = id;
}
