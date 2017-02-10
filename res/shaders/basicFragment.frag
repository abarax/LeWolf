#version 410

in vec2 texCoord0;

uniform vec3 colour;
uniform  sampler2D sampler;

out vec4 frag_color;


void main () {
    frag_color = texture(sampler, texCoord0.xy) * vec4(colour, 1);
}