#version 410

layout(location = 0) out vec4 frag_color;
layout(location = 1) out vec3 frag_id;

uniform sampler2D spriteSheet;
uniform vec3 highlight_id;

in vec2 texture_coords;

void main() {
    vec4 sprite = texture(spriteSheet, texture_coords);

    if (sprite.rgb == vec3(0)) {
        discard;
    }

    frag_color = vec4(sprite.rgb, 1);
    frag_id = sprite.rgb;

    gl_FragDepth = 1;
}