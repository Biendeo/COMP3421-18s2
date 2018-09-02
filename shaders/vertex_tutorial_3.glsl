in vec3 position;
in vec3 normal;

uniform mat4 model_matrix;

uniform mat4 view_matrix;

uniform mat4 proj_matrix;

in vec4 viewPosition;
in vec3 m;

void main() {
    vec4 globalPosition = model_matrix * vec4(position, 1);
    viewPosition = view_matrix * globalPosition;
    gl_Position = viewPosition;

    m = normalize(view_matrix*model_matrix * vec4(normal, 0)).xyz;
}