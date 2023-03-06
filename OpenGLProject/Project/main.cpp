#define GLEW_STATIC
#include <GL/glew.h>
#include <GLFW/glfw3.h>

#include <glm/glm.hpp> //core glm functionality
#include <glm/gtc/matrix_transform.hpp> //glm extension for generating common transformation matrices
#include <glm/gtc/matrix_inverse.hpp> //glm extension for computing inverse matrices
#include <glm/gtc/type_ptr.hpp> //glm extension for accessing the internal data structure of glm types

#include "Window.h"
#include "Shader.hpp"
#include "Camera.hpp"
#include "Model3D.hpp"
#include "SkyBox.hpp"


#include <iostream>



// window
gps::Window myWindow;
gps::SkyBox mySkyBox;


// matrices
glm::mat4 model;
glm::mat4 modelNew;
glm::mat4 modelNew2;

glm::mat4 view;
glm::mat4 projection;
glm::mat3 normalMatrix;
gps::Model3D lightCube;


// light parameters
glm::vec3 lightDir;
glm::vec3 lightColor;

glm::vec3 lightPos;

glm::vec3 secondlightDir;
glm::vec3 secondlightColor;

const unsigned int SHADOW_WIDTH = 2048;
const unsigned int SHADOW_HEIGHT = 2048;

// shader uniform locations
GLint modelLoc;
GLint modelLocNew;
GLint modelLocNew2;
GLint viewLoc;
GLint projectionLoc;
GLint normalMatrixLoc;
GLint lightDirLoc;
GLint lightPosLoc;
GLint lightColorLoc;
GLint secondlightDirLoc;
GLint secondlightColorLoc;
glm::mat4 lightRotation;

// camera
gps::Camera myCamera(
    glm::vec3(0.0f, 0.0f, 3.0f),
    glm::vec3(0.0f, 0.0f, -10.0f),
    glm::vec3(0.0f, 1.0f, 0.0f));

GLfloat cameraSpeed = 0.1f;

GLboolean pressedKeys[1024];

// models
gps::Model3D teapot;
gps::Model3D ground;
gps::Model3D ground2;
gps::Model3D ground3;
gps::Model3D ground4;
gps::Model3D ground5;
gps::Model3D ground6;
gps::Model3D ground7;
gps::Model3D ground8;
gps::Model3D ground9;
gps::Model3D cup[21];
gps::Model3D plant[21];
gps::Model3D dice[21];
gps::Model3D balloon[21];


GLfloat angle;
GLfloat angle1 = 0.0f;
GLfloat lightAngle;


// shaders
gps::Shader myCustomShader;
gps::Shader myBasicShader;
gps::Shader myBasicShaderNew;
gps::Shader lightShader;
gps::Shader depthMapShader;
gps::Shader skyboxShader;

GLuint shadowMapFBO;
GLuint depthMapTexture;
std::vector<const GLchar*> skyBoxFaces;




GLenum glCheckError_(const char *file, int line)
{
	GLenum errorCode;
	while ((errorCode = glGetError()) != GL_NO_ERROR) {
		std::string error;
		switch (errorCode) {
            case GL_INVALID_ENUM:
                error = "INVALID_ENUM";
                break;
            case GL_INVALID_VALUE:
                error = "INVALID_VALUE";
                break;
            case GL_INVALID_OPERATION:
                error = "INVALID_OPERATION";
                break;
            case GL_STACK_OVERFLOW:
                error = "STACK_OVERFLOW";
                break;
            case GL_STACK_UNDERFLOW:
                error = "STACK_UNDERFLOW";
                break;
            case GL_OUT_OF_MEMORY:
                error = "OUT_OF_MEMORY";
                break;
            case GL_INVALID_FRAMEBUFFER_OPERATION:
                error = "INVALID_FRAMEBUFFER_OPERATION";
                break;
        }
		std::cout << error << " | " << file << " (" << line << ")" << std::endl;
	}
	return errorCode;
}
#define glCheckError() glCheckError_(__FILE__, __LINE__)

void windowResizeCallback(GLFWwindow* window, int width, int height) {
	fprintf(stdout, "Window resized! New width: %d , and height: %d\n", width, height);
	//TODO
}

void keyboardCallback(GLFWwindow* window, int key, int scancode, int action, int mode) {
	if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
        glfwSetWindowShouldClose(window, GL_TRUE);
    }

	if (key >= 0 && key < 1024) {
        if (action == GLFW_PRESS) {
            pressedKeys[key] = true;
        } else if (action == GLFW_RELEASE) {
            pressedKeys[key] = false;
        }
    }
}

double xMousePos = 0;
double yMousePos = 0;
float posx = 0.0f;
float posy = 0.0f;
float posz = 3.0f;

void mouseCallback(GLFWwindow* window, double xpos, double ypos) {
    //TODO

    glfwGetCursorPos(window, &xpos, &ypos);
    xMousePos = xpos;
    yMousePos = ypos;
    view = glm::lookAt(glm::vec3(posx, posy, posz), glm::vec3(posx + (float)xMousePos / 700, posy - (float)yMousePos / 700, posz - 1), glm::vec3(0.0f, 1.0f, 0.0f));

    myBasicShader.useShaderProgram();
    glUniformMatrix4fv(viewLoc, 1, GL_FALSE, glm::value_ptr(view));
    // compute normal matrix for teapot

    normalMatrix = glm::mat3(glm::inverseTranspose(view * model));
}



void processMovement() {

    if (pressedKeys[GLFW_KEY_M]) {
        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
    }
    if (pressedKeys[GLFW_KEY_N]) {
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
    }
    if (pressedKeys[GLFW_KEY_B]) {
        glPolygonMode(GL_FRONT_AND_BACK, GL_POINT);
    }


    if (pressedKeys[GLFW_KEY_J]) {
        lightAngle -= 1.0f;
    }

    if (pressedKeys[GLFW_KEY_L]) {
        lightAngle += 1.0f;
    }


	if (pressedKeys[GLFW_KEY_D]) {
		//myCamera.move(gps::MOVE_FORWARD, cameraSpeed);
		//update view matrix
        view = myCamera.getViewMatrix();
        view = glm::lookAt(glm::vec3(posx, posy, posz), glm::vec3(posx + (float)xMousePos / 1000, posy - (float)yMousePos / 1000, posz - 1), glm::vec3(0.0f, 1.0f, 0.0f));
        myBasicShader.useShaderProgram();
        glUniformMatrix4fv(viewLoc, 1, GL_FALSE, glm::value_ptr(view));
        // compute normal matrix for teapot
        

        model = glm::translate(model, glm::vec3(cameraSpeed, 0, 0));
        normalMatrix = glm::mat3(glm::inverseTranspose(view * model));
    }

	if (pressedKeys[GLFW_KEY_A]) {
		//myCamera.move(gps::MOVE_BACKWARD, cameraSpeed);
        //update view matrix
        view = myCamera.getViewMatrix();
        view = glm::lookAt(glm::vec3(posx, posy, posz), glm::vec3(posx + (float)xMousePos / 1000, posy - (float)yMousePos / 1000, posz - 1), glm::vec3(0.0f, 1.0f, 0.0f));
        myBasicShader.useShaderProgram();
        glUniformMatrix4fv(viewLoc, 1, GL_FALSE, glm::value_ptr(view));
        // compute normal matrix for teapot
        

        model = glm::translate(model, glm::vec3(-cameraSpeed, 0, 0));
        normalMatrix = glm::mat3(glm::inverseTranspose(view * model));
	}

	if (pressedKeys[GLFW_KEY_S]) {
		//myCamera.move(gps::MOVE_LEFT, cameraSpeed);
        //update view matrix
        view = myCamera.getViewMatrix();
        view = glm::lookAt(glm::vec3(posx, posy, posz), glm::vec3(posx + (float)xMousePos / 1000, posy - (float)yMousePos / 1000, posz - 1), glm::vec3(0.0f, 1.0f, 0.0f));
        myBasicShader.useShaderProgram();
        glUniformMatrix4fv(viewLoc, 1, GL_FALSE, glm::value_ptr(view));
        // compute normal matrix for teapot
        

        model = glm::translate(model, glm::vec3(0, 0, -cameraSpeed));
        normalMatrix = glm::mat3(glm::inverseTranspose(view * model));
	}

	if (pressedKeys[GLFW_KEY_W]) {
		//myCamera.move(gps::MOVE_RIGHT, cameraSpeed);
        //update view matrix
        view = myCamera.getViewMatrix();
        view = glm::lookAt(glm::vec3(posx, posy, posz), glm::vec3(posx + (float)xMousePos / 1000, posy - (float)yMousePos / 1000, posz - 1), glm::vec3(0.0f, 1.0f, 0.0f));
        myBasicShader.useShaderProgram();
        glUniformMatrix4fv(viewLoc, 1, GL_FALSE, glm::value_ptr(view));
        // compute normal matrix for teapot
        
        model = glm::translate(model, glm::vec3(0, 0, cameraSpeed));
        normalMatrix = glm::mat3(glm::inverseTranspose(view * model));
	}

    if (pressedKeys[GLFW_KEY_Q]) {
        angle = -0.5f;
        // update model matrix for teapot
        glm::mat4 aux = model;
        model = glm::rotate(aux, glm::radians(angle), glm::vec3(0, 1, 0));
        // update normal matrix for teapot
        normalMatrix = glm::mat3(glm::inverseTranspose(view*model));
    }

    if (pressedKeys[GLFW_KEY_E]) {
        angle = 0.5f;
        // update model matrix for teapot
        glm::mat4 aux = model;
        model = glm::rotate(aux, glm::radians(angle), glm::vec3(0, 1, 0));
        // update normal matrix for teapot
        normalMatrix = glm::mat3(glm::inverseTranspose(view*model));
    }

    if (pressedKeys[GLFW_KEY_R]) {
        
        model = glm::scale(model, glm::vec3(1.05f, 1.05f, 1.05f));

        // update normal matrix for teapot
        normalMatrix = glm::mat3(glm::inverseTranspose(view * model));
    }

    if (pressedKeys[GLFW_KEY_T]) {

        model = glm::scale(model, glm::vec3(0.95f, 0.95f, 0.95f));

        // update normal matrix for teapot
        normalMatrix = glm::mat3(glm::inverseTranspose(view * model));
    }

    if (pressedKeys[GLFW_KEY_I]) {

        myCamera.move(gps::MOVE_RIGHT, cameraSpeed);
        //update view matrix
        view = myCamera.getViewMatrix();


        posz += cameraSpeed;
        view = glm::lookAt(glm::vec3(posx, posy, posz), glm::vec3(posx + (float)xMousePos / 1000, posy - (float)yMousePos / 1000, posz - 1), glm::vec3(0.0f, 1.0f, 0.0f));
        myBasicShader.useShaderProgram();
        glUniformMatrix4fv(viewLoc, 1, GL_FALSE, glm::value_ptr(view));
        // compute normal matrix for teapot
        
        normalMatrix = glm::mat3(glm::inverseTranspose(view * model));
    }

    if (pressedKeys[GLFW_KEY_8]) {

        myCamera.move(gps::MOVE_RIGHT, cameraSpeed);
        //update view matrix
        view = myCamera.getViewMatrix();

        posz -= cameraSpeed;
        view = glm::lookAt(glm::vec3(posx, posy, posz), glm::vec3(posx + (float)xMousePos / 1000, posy - (float)yMousePos  / 1000, posz - 1), glm::vec3(0.0f, 1.0f, 0.0f));
        myBasicShader.useShaderProgram();
        glUniformMatrix4fv(viewLoc, 1, GL_FALSE, glm::value_ptr(view));
        // compute normal matrix for teapot

        normalMatrix = glm::mat3(glm::inverseTranspose(view * model));
    }
    if (pressedKeys[GLFW_KEY_U]) {

        myCamera.move(gps::MOVE_RIGHT, cameraSpeed);
        //update view matrix
        view = myCamera.getViewMatrix();

        posx -= cameraSpeed;
        view = glm::lookAt(glm::vec3(posx, posy, posz), glm::vec3(posx + (float)xMousePos / 1000, posy - (float)yMousePos / 1000, posz - 1), glm::vec3(0.0f, 1.0f, 0.0f));
        myBasicShader.useShaderProgram();
        glUniformMatrix4fv(viewLoc, 1, GL_FALSE, glm::value_ptr(view));
        // compute normal matrix for teapot

        normalMatrix = glm::mat3(glm::inverseTranspose(view * model));
    }
    if (pressedKeys[GLFW_KEY_O]) {

        myCamera.move(gps::MOVE_RIGHT, cameraSpeed);
        //update view matrix
        view = myCamera.getViewMatrix();

        posx += cameraSpeed;
        view = glm::lookAt(glm::vec3(posx, posy, posz), glm::vec3(posx + (float)xMousePos / 1000, posy - (float)yMousePos / 1000, posz - 1), glm::vec3(0.0f, 1.0f, 0.0f));
        myBasicShader.useShaderProgram();
        glUniformMatrix4fv(viewLoc, 1, GL_FALSE, glm::value_ptr(view));
        // compute normal matrix for teapot

        normalMatrix = glm::mat3(glm::inverseTranspose(view * model));
    }
    if (pressedKeys[GLFW_KEY_X]) {
        myBasicShader.useShaderProgram();
        secondlightColor = glm::vec3(0.0f, 0.0f, 0.0f); //light color
        secondlightColorLoc = glGetUniformLocation(myBasicShader.shaderProgram, "secondlightColor");
        // send light color to shader
        glUniform3fv(secondlightColorLoc, 1, glm::value_ptr(secondlightColor));
    }
    if (pressedKeys[GLFW_KEY_Z]) {
        myBasicShader.useShaderProgram();
        secondlightColor = glm::vec3(1.0f, 1.0f, 0.0f); //light color
        secondlightColorLoc = glGetUniformLocation(myBasicShader.shaderProgram, "secondlightColor");
        // send light color to shader
        glUniform3fv(secondlightColorLoc, 1, glm::value_ptr(secondlightColor));
    }


}

void initOpenGLWindow() {
    myWindow.Create(1024, 768, "OpenGL Project Core");
}

void setWindowCallbacks() {
	glfwSetWindowSizeCallback(myWindow.getWindow(), windowResizeCallback);
    glfwSetKeyCallback(myWindow.getWindow(), keyboardCallback);
    glfwSetCursorPosCallback(myWindow.getWindow(), mouseCallback);
}

void initOpenGLState() {
	glClearColor(0.7f, 0.7f, 0.7f, 1.0f);
	glViewport(0, 0, myWindow.getWindowDimensions().width, myWindow.getWindowDimensions().height);
    glEnable(GL_FRAMEBUFFER_SRGB);
	glEnable(GL_DEPTH_TEST); // enable depth-testing
	glDepthFunc(GL_LESS); // depth-testing interprets a smaller value as "closer"
	glEnable(GL_CULL_FACE); // cull face
	glCullFace(GL_BACK); // cull back face
	glFrontFace(GL_CCW); // GL_CCW for counter clock-wise
}

void initModels() {
    teapot.LoadModel("models/teapot/teapot20segUT.obj");
    lightCube.LoadModel("objects/cube/cube.obj");
    ground.LoadModel("objects/ground/ground.obj");
    ground2.LoadModel("objects/ground/ground.obj");
    ground3.LoadModel("objects/ground/ground.obj");
    ground4.LoadModel("objects/ground/ground.obj");
    ground5.LoadModel("objects/ground/ground.obj");
    ground6.LoadModel("objects/ground/ground.obj");
    ground7.LoadModel("objects/ground/ground.obj");
    ground8.LoadModel("objects/ground/ground.obj");
    ground9.LoadModel("objects/ground/ground.obj");
    for (int i = 0; i < 21; i++) {
        cup[i].LoadModel("models/Coffee cup/obj/coffeecup.obj");
        plant[i].LoadModel("models/6y992g5owvi8-marijuanna/marijuanna.obj");
        dice[i].LoadModel("models/h85ezc5nhxj4-Dice/Dice/Formats/dice.obj");
        balloon[i].LoadModel("models/pwgo6lkvoe0w-Hot_Air_Balloon_Iridesium/Hot Air Balloon Iridesium/Air_Balloon.obj");
    }
    
    
}

void initShaders() {
    myBasicShaderNew.loadShader(
        "shaders/basicNew.vert",
        "shaders/basicNew.frag");

	myBasicShader.loadShader(
        "shaders/basic.vert",
        "shaders/basic.frag");

    

    lightShader.loadShader("shaders/lightCube.vert", "shaders/lightCube.frag");
    lightShader.useShaderProgram();

    depthMapShader.loadShader("shaders/depthMapShader.vert", "shaders/depthMapShader.frag");
    depthMapShader.useShaderProgram();

    myCustomShader.loadShader("shaders/shaderStart.vert", "shaders/shaderStart.frag");
    myCustomShader.useShaderProgram();



    mySkyBox.Load(skyBoxFaces);
    skyboxShader.loadShader("shaders/skyboxShader.vert", "shaders/skyboxShader.frag");
    skyboxShader.useShaderProgram();

    view = myCamera.getViewMatrix();
    glUniformMatrix4fv(glGetUniformLocation(skyboxShader.shaderProgram, "view"), 1, GL_FALSE, glm::value_ptr(view));

    projection = glm::perspective(glm::radians(45.0f), (float)myWindow.getWindowDimensions().width / (float)myWindow.getWindowDimensions().height, 0.1f, 1000.0f);
    glUniformMatrix4fv(glGetUniformLocation(skyboxShader.shaderProgram, "projection"), 1, GL_FALSE, glm::value_ptr(projection));
}

void initUniforms() {
	myBasicShader.useShaderProgram();

    // create model matrix for teapot
    model = glm::rotate(glm::mat4(1.0f), glm::radians(angle), glm::vec3(0.0f, 1.0f, 0.0f));
	modelLoc = glGetUniformLocation(myBasicShader.shaderProgram, "model");

    myBasicShaderNew.useShaderProgram();
    modelNew = glm::rotate(glm::mat4(1.0f), glm::radians(angle), glm::vec3(0.0f, 1.0f, 0.0f));
    modelLocNew = glGetUniformLocation(myBasicShaderNew.shaderProgram, "modelNew");


    //depthMapShader.useShaderProgram();
    myBasicShader.useShaderProgram();
    modelNew2 = glm::rotate(glm::mat4(1.0f), glm::radians(angle), glm::vec3(0.0f, 1.0f, 0.0f));
    modelLocNew2 = glGetUniformLocation(myBasicShader.shaderProgram, "modelNew2");


    myBasicShader.useShaderProgram();
	// get view matrix for current camera
	view = myCamera.getViewMatrix();
	viewLoc = glGetUniformLocation(myBasicShader.shaderProgram, "view");
	// send view matrix to shader
    glUniformMatrix4fv(viewLoc, 1, GL_FALSE, glm::value_ptr(view));

    // compute normal matrix for teapot
    normalMatrix = glm::mat3(glm::inverseTranspose(view*model));
	normalMatrixLoc = glGetUniformLocation(myBasicShader.shaderProgram, "normalMatrix");

	// create projection matrix
	projection = glm::perspective(glm::radians(45.0f),
                               (float)myWindow.getWindowDimensions().width / (float)myWindow.getWindowDimensions().height,
                               0.1f, 20.0f);
	projectionLoc = glGetUniformLocation(myBasicShader.shaderProgram, "projection");
	// send projection matrix to shader
	glUniformMatrix4fv(projectionLoc, 1, GL_FALSE, glm::value_ptr(projection));	


    
	//set the light direction (direction towards the light)
	lightDir = glm::vec3(0.0f, 1.0f, 1.0f);
    lightRotation = glm::rotate(glm::mat4(1.0f), glm::radians(lightAngle), glm::vec3(0.0f, 1.0f, 0.0f));
	lightDirLoc = glGetUniformLocation(myBasicShader.shaderProgram, "lightDir");
	// send light dir to shader
	//glUniform3fv(lightDirLoc, 1, glm::value_ptr(lightDir));
    glUniform3fv(lightDirLoc, 1, glm::value_ptr(glm::inverseTranspose(glm::mat3(view * lightRotation)) * lightDir));


    //set the light direction (direction towards the light)
    //secondlightDir = glm::vec3(-1.0f, 0.0f, 0.0f);
    //secondlightDirLoc = glGetUniformLocation(myBasicShader.shaderProgram, "secondlightDir");
    // send light dir to shader
    //glUniform3fv(secondlightDirLoc, 1, glm::value_ptr(secondlightDir));


    lightPos = glm::vec3(2.0f, 5.0f, 1.0f);
    lightPosLoc = glGetUniformLocation(myBasicShader.shaderProgram, "lightPosEye");

    glUniform3fv(lightPosLoc, 1, glm::value_ptr(glm::mat3(view) * lightPos));

	//set light color
	lightColor = glm::vec3(1.0f, 1.0f, 1.0f); //light color
	lightColorLoc = glGetUniformLocation(myBasicShader.shaderProgram, "lightColor");
	// send light color to shader
	glUniform3fv(lightColorLoc, 1, glm::value_ptr(lightColor));

    //set light color
    secondlightColor = glm::vec3(1.0f, 1.0f, 0.0f); //light color
    secondlightColorLoc = glGetUniformLocation(myBasicShader.shaderProgram, "secondlightColor");
    // send light color to shader
    glUniform3fv(secondlightColorLoc, 1, glm::value_ptr(secondlightColor));


    //lightShader.useShaderProgram();
    //glUniformMatrix4fv(glGetUniformLocation(lightShader.shaderProgram, "projection"), 1, GL_FALSE, glm::value_ptr(projection));

}

bool createSkyBox() {
    GLuint textureID;
    glGenTextures(1, &textureID);
    glBindTexture(GL_TEXTURE_CUBE_MAP, textureID);

    
    skyBoxFaces.push_back("textures/skybox/right.tga");
    skyBoxFaces.push_back("textures/skybox/left.tga");
    skyBoxFaces.push_back("textures/skybox/top.tga");
    skyBoxFaces.push_back("textures/skybox/bottom.tga");
    skyBoxFaces.push_back("textures/skybox/back.tga");
    skyBoxFaces.push_back("textures/skybox/front.tga");
    int width, height, n;

    for (GLuint i = 0; i < skyBoxFaces.size(); i++)
    {
        unsigned char *image = stbi_load(skyBoxFaces[i], &width, &height, &n, 0);
        if (!image) {
            fprintf(stderr, "ERROR: could not load %s\n", skyBoxFaces[i]);
            return false;
        }
        glTexImage2D(
            GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0,
            GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, image);
    }
    return true;

}


glm::mat4 computeLightSpaceTrMatrix() {
    //TODO - Return the light-space transformation matrix
    //return glm::mat4(1.0f);
    //glm::mat4 lightView = glm::lookAt(lightDir, glm::vec3(0.0f), glm::vec3(0.0f, 1.0f, 0.0f)); 

    //glm::mat4 lightView = glm::lookAt(glm::mat3(lightRotation) * lightDir, glm::vec3(0.0f), glm::vec3(0.0f, 1.0f, 0.0f));
    
    //glm::mat4 lightView = glm::lookAt(glm::mat3(lightRotation) * glm::vec3(posx, posy, posz), glm::vec3(0.0f), glm::vec3(0.0f, 1.0f, 0.0f));
    glm::mat4 lightView = glm::lookAt(glm::mat3(lightRotation) * lightDir + glm::vec3(posx, posy, posz - 2), glm::vec3(posx, posy, posz - 2), glm::vec3(0.0f, 1.0f, 0.0f));
    //glm::mat4 cameraView = myCamera.getViewMatrix();
    
    
    //const GLfloat near_plane = 0.1f, far_plane = 5.0f;
    //glm::mat4 lightProjection = glm::ortho(-10.0f, 10.0f, -10.0f, 10.0f, near_plane, far_plane);

    const GLfloat near_plane = 0.001f, far_plane = 10.0f;
    glm::mat4 lightProjection = glm::ortho(-10.0f, 10.0f, -10.0f, 10.0f, near_plane, far_plane);
    
    
    glm::mat4 lightSpaceTrMatrix = lightProjection * lightView;
    //glm::mat4 lightSpaceTrMatrix = lightProjection * cameraView;
    return lightSpaceTrMatrix;



}

int randomPositionX[102];
int randomPositionY[102];
void renderTeapot(gps::Shader shader, gps::Shader shaderNew, bool depthPass) {
    // select active shader program

    shader.useShaderProgram();

    modelNew2 = glm::rotate(glm::mat4(1.0f), glm::radians(angle), glm::vec3(0.0f, 1.0f, 0.0f));
    glUniformMatrix4fv(glGetUniformLocation(shader.shaderProgram, "model"), 1, GL_FALSE, glm::value_ptr(model));
    
    /*model = glm::rotate(glm::mat4(1.0f), glm::radians(angle), glm::vec3(0.0f, 1.0f, 0.0f));
    glUniformMatrix4fv(glGetUniformLocation(shader.shaderProgram, "model"), 1, GL_FALSE, glm::value_ptr(model));*/
   


    shaderNew.useShaderProgram();

    //model = glm::rotate(glm::mat4(1.0f), glm::radians(angle), glm::vec3(0.0f, 1.0f, 0.0f));
    glUniformMatrix4fv(glGetUniformLocation(shaderNew.shaderProgram, "model"), 1, GL_FALSE, glm::value_ptr(model));
    teapot.Draw(shader);
    int k = 0;
    for (int i = 0; i < 21; i++) {
        modelNew = glm::translate(glm::mat4(1.0f), glm::vec3(5.0f + randomPositionX[k], -1.0f, 0.0f + randomPositionY[k]));
        modelNew = glm::scale(modelNew, glm::vec3(0.1f));
        glUniformMatrix4fv(glGetUniformLocation(shader.shaderProgram, "model"), 1, GL_FALSE, glm::value_ptr(modelNew));
        cup[i].Draw(shader);
        k++;
    }
    for (int i = 0; i < 21; i++) {
        modelNew = glm::translate(glm::mat4(1.0f), glm::vec3(5.0f + randomPositionX[k], -1.0f, 0.0f + randomPositionY[k]));
        modelNew = glm::scale(modelNew, glm::vec3(0.01f));
        glUniformMatrix4fv(glGetUniformLocation(shader.shaderProgram, "model"), 1, GL_FALSE, glm::value_ptr(modelNew));
        plant[i].Draw(shader);
        k++;
    }
    for (int i = 0; i < 21; i++) {
        modelNew = glm::translate(glm::mat4(1.0f), glm::vec3(5.0f + randomPositionX[k], -1.0f, 0.0f + randomPositionY[k]));
        modelNew = glm::scale(modelNew, glm::vec3(0.1f));
        glUniformMatrix4fv(glGetUniformLocation(shader.shaderProgram, "model"), 1, GL_FALSE, glm::value_ptr(modelNew));
        dice[i].Draw(shader);
        k++;
    }

    for (int i = 0; i < 21; i++) {
        
        modelNew = glm::translate(glm::mat4(1.0f), glm::vec3(5.0f + randomPositionX[k], -1.0f, 0.0f + randomPositionY[k]));
        modelNew = glm::rotate(modelNew, glm::radians(angle1), glm::vec3(0.0f, 1.0f, 0.0f));
        modelNew = glm::scale(modelNew, glm::vec3(0.05f));
        
        glUniformMatrix4fv(glGetUniformLocation(shader.shaderProgram, "model"), 1, GL_FALSE, glm::value_ptr(modelNew));
        balloon[i].Draw(shader);
        k++;
    }

    

    modelNew = glm::translate(glm::mat4(1.0f), glm::vec3(0.0f, -1.0f, 0.0f));
    modelNew = glm::scale(modelNew, glm::vec3(1.0f));

    glUniformMatrix4fv(glGetUniformLocation(shader.shaderProgram, "model"), 1, GL_FALSE, glm::value_ptr(modelNew));
    
    
    if (!depthPass) {
        normalMatrix = glm::mat3(glm::inverseTranspose(view * model));
        glUniformMatrix3fv(normalMatrixLoc, 1, GL_FALSE, glm::value_ptr(normalMatrix));
    }
    ground.Draw(shaderNew);

    modelNew = glm::translate(glm::mat4(1.0f), glm::vec3(20.0f, -1.0f, 0.0f));
    modelNew = glm::scale(modelNew, glm::vec3(1.0f));
    glUniformMatrix4fv(glGetUniformLocation(shaderNew.shaderProgram, "model"), 1, GL_FALSE, glm::value_ptr(modelNew));
    ground2.Draw(shaderNew);

    modelNew = glm::translate(glm::mat4(1.0f), glm::vec3(-20.0f, -1.0f, 0.0f));
    modelNew = glm::scale(modelNew, glm::vec3(1.0f));
    glUniformMatrix4fv(glGetUniformLocation(shaderNew.shaderProgram, "model"), 1, GL_FALSE, glm::value_ptr(modelNew));
    ground3.Draw(shaderNew);

    modelNew = glm::translate(glm::mat4(1.0f), glm::vec3(0.0f, -1.0f, 20.0f));
    modelNew = glm::scale(modelNew, glm::vec3(1.0f));
    glUniformMatrix4fv(glGetUniformLocation(shaderNew.shaderProgram, "model"), 1, GL_FALSE, glm::value_ptr(modelNew));
    ground4.Draw(shaderNew);

    modelNew = glm::translate(glm::mat4(1.0f), glm::vec3(20.0f, -1.0f, 20.0f));
    modelNew = glm::scale(modelNew, glm::vec3(1.0f));
    glUniformMatrix4fv(glGetUniformLocation(shaderNew.shaderProgram, "model"), 1, GL_FALSE, glm::value_ptr(modelNew));
    ground5.Draw(shaderNew);

    modelNew = glm::translate(glm::mat4(1.0f), glm::vec3(-20.0f, -1.0f, 20.0f));
    modelNew = glm::scale(modelNew, glm::vec3(1.0f));
    glUniformMatrix4fv(glGetUniformLocation(shaderNew.shaderProgram, "model"), 1, GL_FALSE, glm::value_ptr(modelNew));
    ground6.Draw(shaderNew);

    modelNew = glm::translate(glm::mat4(1.0f), glm::vec3(0.0f, -1.0f, -20.0f));
    modelNew = glm::scale(modelNew, glm::vec3(1.0f));
    glUniformMatrix4fv(glGetUniformLocation(shaderNew.shaderProgram, "model"), 1, GL_FALSE, glm::value_ptr(modelNew));
    ground7.Draw(shaderNew);

    modelNew = glm::translate(glm::mat4(1.0f), glm::vec3(20.0f, -1.0f, -20.0f));
    modelNew = glm::scale(modelNew, glm::vec3(1.0f));
    glUniformMatrix4fv(glGetUniformLocation(shaderNew.shaderProgram, "model"), 1, GL_FALSE, glm::value_ptr(modelNew));
    ground8.Draw(shaderNew);

    modelNew = glm::translate(glm::mat4(1.0f), glm::vec3(-20.0f, -1.0f, -20.0f));
    modelNew = glm::scale(modelNew, glm::vec3(1.0f));
    glUniformMatrix4fv(glGetUniformLocation(shaderNew.shaderProgram, "model"), 1, GL_FALSE, glm::value_ptr(modelNew));
    ground9.Draw(shaderNew);




    

    //modelNew2 = glm::translate(glm::mat4(1.0f), glm::vec3(0.0f, -1.0f, 0.0f));
    //modelNew2 = glm::scale(modelNew2, glm::vec3(0.001f));
    //glUniformMatrix4fv(glGetUniformLocation(shader.shaderProgram, "model"), 1, GL_FALSE, glm::value_ptr(model));
    //cup.Draw(shader);



    mySkyBox.Draw(skyboxShader, view, projection);
    

}

void initFBO() {
    //TODO - Create the FBO, the depth texture and attach the depth texture to the FBO
    //generate FBO ID
    glGenFramebuffers(1, &shadowMapFBO);

    //create depth texture for FBO
    glGenTextures(1, &depthMapTexture);
    glBindTexture(GL_TEXTURE_2D, depthMapTexture);
    glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, SHADOW_WIDTH, SHADOW_HEIGHT, 0, GL_DEPTH_COMPONENT, GL_FLOAT, NULL);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
    float borderColor[] = { 1.0f, 1.0f, 1.0f, 1.0f };
    glTexParameterfv(GL_TEXTURE_2D, GL_TEXTURE_BORDER_COLOR, borderColor);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);

    //attach texture to FBO
    glBindFramebuffer(GL_FRAMEBUFFER, shadowMapFBO);
    glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, depthMapTexture, 0);

    //bind nothing to attachment points
    glDrawBuffer(GL_NONE);
    glReadBuffer(GL_NONE);

    //unbind until ready to use
    glBindFramebuffer(GL_FRAMEBUFFER, 0);

}




void renderScene() {
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);


    


    angle1 += 0.1;
    depthMapShader.useShaderProgram();
    glUniformMatrix4fv(glGetUniformLocation(depthMapShader.shaderProgram, "lightSpaceTrMatrix"), 1, GL_FALSE, glm::value_ptr(computeLightSpaceTrMatrix()));
    glViewport(0, 0, SHADOW_WIDTH, SHADOW_HEIGHT);
    glBindFramebuffer(GL_FRAMEBUFFER, shadowMapFBO);
    glClear(GL_DEPTH_BUFFER_BIT);
    //render scene = draw objects
    renderTeapot(depthMapShader, myBasicShader, true);
    glBindFramebuffer(GL_FRAMEBUFFER, 0);


    glViewport(0, 0, myWindow.getWindowDimensions().width, myWindow.getWindowDimensions().height);

    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);







    myBasicShader.useShaderProgram();

    
    

    lightRotation = glm::rotate(glm::mat4(1.0f), glm::radians(lightAngle), glm::vec3(0.0f, 1.0f, 0.0f));
    glUniform3fv(lightDirLoc, 1, glm::value_ptr(glm::inverseTranspose(glm::mat3(view * lightRotation)) * lightDir));
    
   
    


    glActiveTexture(GL_TEXTURE3);
    glBindTexture(GL_TEXTURE_2D, depthMapTexture);
    glUniform1i(glGetUniformLocation(myBasicShader.shaderProgram, "shadowMap"), 3);

    glUniformMatrix4fv(glGetUniformLocation(myBasicShader.shaderProgram, "lightSpaceTrMatrix"),
        1,
        GL_FALSE,
        glm::value_ptr(computeLightSpaceTrMatrix()));

    renderTeapot(myBasicShader, myBasicShader, false);


    


}

void cleanup() {
    myWindow.Delete();
    glDeleteFramebuffers(1, &shadowMapFBO);
    //cleanup code for your own data
}

int main(int argc, const char * argv[]) {

    int parity = 0;
    for (int i = 0; i < 100; i++) {
        parity = rand() % 4;
        if (parity == 0) {
            randomPositionX[i] = rand() % 25;
            randomPositionY[i] = rand() % 25;
        }
        else {
            if (parity == 1) {
                randomPositionX[i] = -rand() % 25;
                randomPositionY[i] = rand() % 25;
            }
            else {
                if (parity == 2) {
                    randomPositionX[i] = rand() % 25;
                    randomPositionY[i] = -rand() % 25;
                }
                else {
                    randomPositionX[i] = -rand() % 25;
                    randomPositionY[i] = -rand() % 25;
                }
            }
        }
    }
    bool changed = false;
    do{
        changed = false;
        for (int i = 0; i < 100 && changed == false; i++) {
            for (int j = i + 1; j < 100 && changed == false; j++) {
                if (randomPositionX[i] == randomPositionX[j] && randomPositionY[i] == randomPositionY[j]) {
                    randomPositionX[j] = rand() % 25;
                    randomPositionY[j] = rand() % 25;
                    changed = true;
                }
            }
        }
    
    
    }while (changed == true);




    try {
        initOpenGLWindow();
    } catch (const std::exception& e) {
        std::cerr << e.what() << std::endl;
        return EXIT_FAILURE;
    }

    createSkyBox();
    initOpenGLState();
    
	initModels();
	initShaders();
	initUniforms();
    initFBO();
    setWindowCallbacks();

	glCheckError();
	// application loop
	while (!glfwWindowShouldClose(myWindow.getWindow())) {
        processMovement();
	    renderScene();

		glfwPollEvents();
		glfwSwapBuffers(myWindow.getWindow());

		glCheckError();
	}

	cleanup();

    return EXIT_SUCCESS;
}
