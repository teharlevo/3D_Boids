
#version 330 core
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec3 aNormal;
layout (location = 2) in vec2 aUV;

uniform mat4 uModel;
uniform mat4 uProjection;
uniform mat4 uView;

out vec3 fNormal;
out vec2 fUV;
out vec3 fPos;

void main()
{   
    fPos = vec3(uModel * vec4(aPos, 1.0));
    fNormal = mat3(transpose(inverse(uModel))) * aNormal;  
    fUV = aUV;
    gl_Position = uProjection * uView *  uModel * vec4(aPos, 1.0);
}

