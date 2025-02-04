/*
 * Copyright (c) 2014 mgamelabs
 * To see our full license terms, please visit https://github.com/mgamelabs/mengine/blob/master/LICENSE.md
 * All rights reserved.
 */

package lightEngine.gameObjects.modules.renderable;

import lightEngine.graphics.GraphicsController;
import lightEngine.graphics.Renderer;
import lightEngine.util.input.Input;
import lightEngine.util.math.vectors.VectorHelper;
import lightEngine.util.time.TimeHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluLookAt;

public class Camera extends ModuleRenderable {

    public Camera() {
        super();
    }

    public void onUpdate() {}

    public Matrix4f getViewProjectionMatrix() {

        Matrix4f projectionMatrix = new Matrix4f();
        Matrix4f viewMatrix = new Matrix4f();
        Matrix4f viewProjectionMatrix = new Matrix4f();

        float aspectRatio = (float) Display.getWidth() / (float)Display.getHeight();
        float farPlane = GraphicsController.renderDistance;
        float nearPlane = 0.1f;
        float y_scale = (float)(1f / Math.tan(Math.toRadians(GraphicsController.fieldOfView / 2)));
        float x_scale = y_scale / aspectRatio;
        float frustum_length = farPlane - nearPlane;

        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((farPlane + nearPlane) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * nearPlane * farPlane) / frustum_length);
        projectionMatrix.m33 = 0;

        Matrix4f.rotate((float) Math.toRadians(parent.rotation.x), new Vector3f(1, 0, 0), viewMatrix, viewMatrix);
        Matrix4f.rotate((float) Math.toRadians(parent.rotation.y), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
        Matrix4f.rotate((float) Math.toRadians(parent.rotation.z), new Vector3f(0, 0, 1), viewMatrix, viewMatrix);
        Matrix4f.translate(VectorHelper.negateVector(parent.position), viewMatrix, viewMatrix);

        Matrix4f.mul(projectionMatrix, viewMatrix, viewProjectionMatrix);

        return viewProjectionMatrix;

    }

    @Override
    public void addToRenderQueue() {

        Renderer.currentRenderQueue.setCamera(this);

    }

}