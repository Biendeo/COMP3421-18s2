out vec4 outputColor;

uniform vec4 input_color;

uniform mat4 view_matrix;

uniform vec3 lightPos;
uniform vec3 lightIntensity;
uniform vec3 ambientIntensity;

uniform vec3 ambientCoeff;
uniform vec3 diffuseCoeff;

in vec4 viewPosition;
in vec3 m;

void main()
{
    vec3 s = normalize(view_matrix*vec4(lightPos,1) - viewPosition).xyz;
    // There's obviously errors here; fix it in the tutorial!
    float ambient = ambientIntensity*ambientCoeff;
    float diffuse = max(lightIntensity*diffuseCoeff*dot(m,s), 0.0);

    float intensity = ambient + diffuse;

    outputColor = vec4(intensity,1)*input_color;
}