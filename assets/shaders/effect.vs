#version 330 core

layout (location = 0) in vec3 aPos;
layout (location = 1) in vec3 aNormal;
layout (location = 2) in vec2 aUV;

out vec3 fNormal;
out vec2 fUV;

void main()
{
    fNormal = aNormal;  
    fUV = aUV;
    gl_Position = vec4(aPos, 1.0);
}

