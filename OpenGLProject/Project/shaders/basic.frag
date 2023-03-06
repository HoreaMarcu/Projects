#version 410 core

in vec3 fPosition;
in vec3 fNormal;
in vec2 fTexCoords;
in vec4 fragPosLightSpace;

out vec4 fColor;

//matrices
uniform mat4 model;
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
uniform sampler2D shadowMap;


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
	vec3 cameraPosEye = vec3(0.0f);
    //compute eye space coordinates
    vec4 fPosEye = view * model * vec4(fPosition, 1.0f);
    //vec3 normalEye = normalize(normalMatrix * fNormal);
	vec3 normalEye = normalize(fNormal);

    //normalize light direction
    //vec3 lightDirN = vec3(normalize(view * vec4(lightDir, 0.0f)));
	vec3 lightDirN = normalize(lightDir);

    //compute view direction (in eye coordinates, the viewer is situated at the origin
    vec3 viewDir = normalize(cameraPosEye - fPosEye.xyz);

    //compute ambient light;
    ambient = ambientStrength * lightColor;

    //compute diffuse light
    diffuse = max(dot(normalEye, lightDirN), 0.0f) * lightColor;

    //compute specular light
    vec3 reflectDir = reflect(-lightDirN, normalEye);
    float specCoeff = pow(max(dot(viewDir, reflectDir), 0.0f), 32.0f);
    specular = specularStrength * specCoeff * lightColor;
}

void computesecondDirLight()
{
    //compute eye space coordinates
    vec4 fPosEye = view * model * vec4(fPosition, 1.0f);
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

	vec3 cameraPosEye = vec3(0.0f);

	vec3 normalEye = normalize(fNormal);
	//normalize light direction
	vec3 lightDirN = normalize(lightDir);
	//compute view direction
	vec3 viewDirN = normalize(cameraPosEye - fragPosEye.xyz);
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
	
	vec3 reflection = reflect(-lightDirN, normalEye);
	float specCoeff = pow(max(dot(viewDirN, reflection	), 0.0f), shininess);
	
	
	specular = att * specularStrength * specCoeff * secondlightColor;



}

float computeShadow()
{

	//perform perspective divide
	vec3 normalizedCoords= fragPosLightSpace.xyz / fragPosLightSpace.w;

	//tranform from [-1,1] range to [0,1] range
	normalizedCoords = normalizedCoords * 0.5 + 0.5;

	//get closest depth value from lights perspective
	float closestDepth = texture(shadowMap, normalizedCoords.xy).r;

	//get depth of current fragment from lights perspective
	float currentDepth = normalizedCoords.z;

	//if the current fragments depth is greater than the value in the depth map, the current fragment is in shadow 
	//else it is illuminated
	//float shadow = currentDepth > closestDepth ? 1.0 : 0.0;
	float bias = 0.005f;
	float shadow = currentDepth - bias > closestDepth ? 1.0 : 0.0;
	if (normalizedCoords.z > 1.0f)
		return 0.0f;
	return shadow;
	
	


}

float computeFog()
{

 vec4 fragmentPosEyeSpace = view * model * vec4(fPosition, 1.0f);
 float fogDensity = 0.05f;
 float fragmentDistance = length(fragmentPosEyeSpace);
 float fogFactor = exp(-pow(fragmentDistance * fogDensity, 2));

 return clamp(fogFactor, 0.0f, 1.0f);
}



void main() 
{
    computeDirLight();
    //computesecondDirLight();

	computenewLightSource();
	
	ambient *= texture(diffuseTexture, fTexCoords).rgb;
	diffuse *= texture(diffuseTexture, fTexCoords).rgb;
	specular *= texture(specularTexture, fTexCoords).rgb;
    
    float shadow = computeShadow();
	
	
	
	
	
    //compute final vertex color
    //vec3 color = min((ambient + (1.0f - shadow) * diffuse) * texture(diffuseTexture, fTexCoords).rgb + specular * texture(specularTexture, fTexCoords).rgb, 1.0f);
    vec3 color = min((ambient + (1.0f - shadow) * diffuse) + (1.0f - shadow) * specular * texture(specularTexture, fTexCoords).rgb, 1.0f);
    //vec3 secondcolor = min((secondambient + (1.0f - shadow) * seconddifusse) + (1.0f - shadow) * specular * texture(specularTexture, fTexCoords).rgb, 1.0f);
    vec3 secondcolor = min((secondambient + seconddifusse) * texture(diffuseTexture, fTexCoords).rgb + specular * texture(specularTexture, fTexCoords).rgb, 1.0f);

	
	
	
	vec4 fcolorAux = vec4(color + secondcolor, 1.0f);
	
	float fogFactor = computeFog();
	vec4 fogColor = vec4(0.5f, 0.5f, 0.5f, 1.0f);
	fColor = mix(fogColor, fcolorAux, fogFactor);
	
	
    fColor = fogColor * (1 - fogFactor) + fcolorAux * fogFactor;

    
   
}
