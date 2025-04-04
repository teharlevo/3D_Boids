#version 330 core

uniform sampler2D uTexture;
uniform sampler2D uTextureColorPalte;
uniform int n;

in vec3 fNormal;
in vec2 fUV;

out vec4 color;

void main()
{            
    vec4 baseColor = texture(uTexture,fUV).rgba;
    
    float k = (baseColor.x + baseColor.y + baseColor.z)/3.0;
    k = floor(k * (n - 1) + 0.5)/(n-1);
    if (k >= 1.0){k = 0.99;}
    color = texture(uTextureColorPalte,vec2(k,0)).rgba;
    color.w = 1.0;
}