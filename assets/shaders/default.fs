#version 330 core

uniform sampler2D uTexture;

in vec3 fNormal;
in vec2 fUV;
in vec3 fPos;

out vec4 color;

void main()
{
    color = texture(uTexture,fUV);
}