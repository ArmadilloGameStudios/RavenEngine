#version 410

layout(location = 0) out vec4 frag_color;

uniform sampler2D spriteSheet;
uniform vec4 highlight;

in vec2 texture_coords;
in float depth;

void main() {
    vec4 sprite = texture(spriteSheet, texture_coords);

    if (sprite.a <= 0)
        discard;

    vec3 color = (sprite.xyz * highlight.xyz) *  highlight.w + (sprite.xyz * (1 - highlight.w));

    frag_color = vec4(color, sprite.a);
}