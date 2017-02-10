#version 410 

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 texCoord;
layout (location = 2) in vec3 normal;

out vec2 texCoord0;
out vec3 normal0;
out vec3 worldPosition0;

uniform mat4 transform;
uniform mat4 transformProjected;

void main()
{
    gl_Position = transformProjected * vec4(position, 1.0);
    texCoord0 = texCoord;
    vec4 temp =  transform * vec4(normal, 0.0);
    normal0 = temp.xyz;
    worldPosition0 =  (transform * vec4(position, 1.0)).xyz;
}