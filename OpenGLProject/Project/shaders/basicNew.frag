#version 410 core

in vec3 fPosition;
in vec3 fNormal;
in vec2 fTexCoords;

out vec4 fColor;

//matrices
uniform mat4 modelNew;
uniform mat4 view;
uniform mat3 normalMatrix;
//lighting


in vec4 fragPosEye;
uniform vec3 lightDir;
uniform vec3 lightColor;
uniform vec3 lightPosEye;
uniform vec3 secondlightDir;
uniform vec3 secondlightColor;
// textures
uniform sampler2D diffuseTexture;
uniform sampler2D specularTexture;

//components
vec3 ambient;
vec3 secondambient;
float ambientStrength = 0.2f;
vec3 diffuse;
vec3 seconddifusse;
vec3 specular;
float specularStrength = 0.5f;
float shininess = 32.0f;

float constant = 1.0f;
float linear = 0.0045f;
float quadratic = 0.0075f;


void computeDirLight()
{
    //compute eye space coordinates
    vec4 fPosEye = view * modelNew * vec4(fPosition, 1.0f);
    vec3 normalEye = normalize(normalMatrix * fNormal);

    //normalize light direction
    vec3 lightDirN = vec3(normalize(view * vec4(lightDir, 0.0f)));

    //compute view direction (in eye coordinates, the viewer is situated at the origin
    vec3 viewDir = normalize(- fPosEye.xyz);

    //compute ambient light;
    ambient = ambientStrength * lightColor;

    //compute diffuse light
    diffuse = max(dot(normalEye, lightDirN), 0.0f) * lightColor;

    //compute specular light
    vec3 reflectDir = reflect(-lightDirN, normalEye);
    float specCoeff = pow(max(dot(viewDir, reflectDir), 0.0f), 32);
    specular = specularStrength * specCoeff * lightColor;
}

void computesecondDirLight()
{
    //compute eye space coordinates
    vec4 fPosEye = view * modelNew * vec4(fPosition, 1.0f);
    vec3 normalEye = normalize(normalMatrix * fNormal);

    //normalize light direction
    vec3 lightDirN = vec3(normalize(view * vec4(secondlightDir, 0.0f)));

    //compute view direction (in eye coordinates, the viewer is situated at the origin
    vec3 viewDir = normalize(- fPosEye.xyz);

    //compute ambient light
    secondambient = ambientStrength * secondlightColor;

    //compute diffuse light
    seconddifusse = max(dot(normalEye, lightDirN), 0.0f) * secondlightColor;

    //compute specular light
    vec3 reflectDir = reflect(-lightDirN, normalEye);
    float specCoeff = pow(max(dot(viewDir, reflectDir), 0.0f), 32);
    specular = specularStrength * specCoeff * secondlightColor;
}

void computenewLightSource()
{

	vec3 normalEye = normalize(fNormal);
	//normalize light direction
	vec3 lightDirN = normalize(lightDir);
	//compute view direction
	vec3 viewDirN = normalize(-fragPosEye.xyz);
	//compute half vector
	vec3 halfVector = normalize(lightDirN + viewDirN);
	//compute light direction
	lightDirN = normalize(lightPosEye - fragPosEye.xyz);

	//compute distance to light
	float dist = length(lightPosEye - fragPosEye.xyz);
	//compute attenuation
	float att = 1.0f / (constant + linear * dist + quadratic * (dist * dist));


	//compute ambient light
	secondambient = att * ambientStrength * secondlightColor;
	//compute diffuse light
	seconddifusse = att * max(dot(normalEye, lightDirN), 0.0f) * secondlightColor;
	
	float specCoeff = pow(max(dot(normalEye, halfVector	), 0.0f), shininess);
	specular = att * specularStrength * specCoeff * secondlightColor;



}


void main() 
{
    computeDirLight();
    //computesecondDirLight();

	computenewLightSource();
	 
    //compute final vertex color
    vec3 color = min((ambient + diffuse) * texture(diffuseTexture, fTexCoords).rgb + specular * texture(specularTexture, fTexCoords).rgb, 1.0f);
    vec3 secondcolor = min((secondambient + seconddifusse) * texture(diffuseTexture, fTexCoords).rgb + specular * texture(specularTexture, fTexCoords).rgb, 1.0f);

    fColor = vec4(color + secondcolor, 1.0f);
    
   
}
