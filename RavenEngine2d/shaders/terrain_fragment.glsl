#version 400

layout(location = 0) out vec4 frag_color;
layout(location = 1) out vec3 frag_id;

uniform sampler2D spriteSheet;
uniform vec3 id;
uniform float z;
uniform vec4 highlight;

in vec2 coords;

void main() {
    vec4 sprite = texture(spriteSheet, coords);
    gl_FragDepth = mix(1, z, sprite.a - coords.y / 1000);

    vec3 color = mix(sprite.rgb, highlight.rgb, highlight.a);

    frag_color = vec4(color, sprite.a);
    frag_id = id;
}
