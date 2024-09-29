package com.example.actronomicalhandbook

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

abstract class Shape {
    abstract fun draw(mvpMatrix: FloatArray)
}

class OpenGLRenderer(context: Context) : GLSurfaceView.Renderer {
    private val square = Square(context)
    private val cube = Cube()
    private val mvpMatrix = FloatArray(16)
    private val modelMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private var angle = 0f

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        GLES20.glClearColor(0f, 0f, 0f, 1f)  // Чёрный фон
        GLES20.glEnable(GLES20.GL_DEPTH_TEST) // Включение теста глубины
        GLES20.glEnable(GLES20.GL_BLEND)      // Включение смешивания для прозрачности
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA)

        square.init()
        cube.init()
        setupViewMatrix() // Видовая матрица устанавливается один раз
    }

    override fun onDrawFrame(gl: GL10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // Отрисовка квадрата
        drawObject(square, 3f, 3f, 3f, 0f, -0.15f, 0f)

        // Отрисовка куба
        drawObject(cube, 0.25f, 0.25f, 0.25f, 0f,0.25f, -2f, angle)
        angle += 1f
    }

    private fun setupViewMatrix() {
        // Камера находится выше по оси Y и смотрит вниз на центр куба
        Matrix.setLookAtM(
            viewMatrix, 0,
            0f, 1f, -6f,  // Позиция камеры (над кубом, Y = 5)
            0f, 0f, 0f,  // Точка, на которую смотрит камера (центр куба)
            0f, 1f, 0f  // Вектор вверх (по оси -Z)
        )
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        val ratio: Float = width.toFloat() / height.toFloat()

        // Проекция сверху, чтобы куб оставался в пределах экрана
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 10f)
    }

    private fun drawObject(
        shape: Shape, scaleX: Float, scaleY: Float, scaleZ: Float,
        translateX: Float, translateY: Float, translateZ: Float,
        rotationY: Float = 0f
    ) {
        // Установка модели для объекта
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.translateM(modelMatrix, 0, translateX, translateY, translateZ)
        Matrix.scaleM(modelMatrix, 0, scaleX, scaleY, scaleZ)
        if (rotationY != 0f) {
            Matrix.rotateM(modelMatrix, 0, rotationY, 0f, 1f, 0f)
        }

        // Подготовка итоговой матрицы для отрисовки (MVP = Projection * View * Model)
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
        Matrix.multiplyMM(mvpMatrix, 0, mvpMatrix, 0, modelMatrix, 0)

        // Рендеринг объекта
        shape.draw(mvpMatrix)
    }
}
