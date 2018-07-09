#version 400

layout(location = 0) out vec4 frag_color;
layout(location = 1) out vec3 frag_id;

uniform sampler2D spriteSheet;
uniform vec3 id;
uniform float z;
uniform vec4 highlight;

uniform vec2 position;

uniform bool standing;

in vec2 texture_coords;
in float depth;

void main() {
//    vec4 sprite = texture(spriteSheet, texture_coords);
//
//    float d;
//
//    if (sprite.a <= 0) {
//        discard;
//    } else if (standing) {
//        // TODO get world coords and don't use alpha
//        d = position.y;
//    } else {
//        d = position.y - depth;
//    }
//
//    gl_FragDepth = d;
////    frag_color = vec4(vec3(d), 1);
//
//    float part = min(1, dot(sprite.xyz, vec3(.4)));
//
//    vec3 color = mix(sprite.rgb, highlight.xyz, highlight.a * part);
//
//    frag_color = vec4(color, sprite.a);
//    frag_id = id;


    vec4 sprite = texture(spriteSheet, texture_coords);

    if (sprite.a <= 0)
        discard;

    gl_FragDepth = mix(1, z, 1 - texture_coords.y / 1000); // TODO get world coords

    float part = min(1, dot(sprite.xyz, vec3(.4)));

    vec3 color = mix(sprite.rgb, highlight.xyz, highlight.a * part);

    frag_color = vec4(color, sprite.a);
    frag_id = id;
}