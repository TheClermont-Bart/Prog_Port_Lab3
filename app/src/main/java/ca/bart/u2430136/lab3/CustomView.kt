package ca.bart.u2430136.lab3

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.min

class CustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {
    companion object {

        const val TAG = "CustomView"
        const val REFERENCE_SIZE = 100f

        val RED_LINE_PAINT = Paint()

        init {
            RED_LINE_PAINT.color = Color.RED
            RED_LINE_PAINT.style = Paint.Style.STROKE
            RED_LINE_PAINT.strokeWidth = 3f
        }
    }


    var cx = 0f
    var cy = 0f
    var radius = 0f
    var scale = 1f

    private val paint = Paint()

    var trackedId = -1

    var color : Int
        get() = paint.color
        set(value) {
            paint.color = value
            invalidate()
        }

    var time : Float = 0f
        get() = field
        set(value) {
            field = value
            invalidate()
        }

    val inverseTransform = Matrix(Matrix.IDENTITY_MATRIX)

    init {

        val attributes = context.theme.obtainStyledAttributes(attrs, R.styleable.CustomView, defStyle, 0)
        try {

            paint.color = attributes.getColor(R.styleable.CustomView_color, Color.RED)
            // paint.style = if (isInEditMode) Paint.Style.FILL else Paint.Style.STROKE
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 1f

            time = attributes.getFloat(R.styleable.CustomView_time, 0f)

            //attributes.getDimensionPixelSize(R.styleable.CustomView_size, 0)

        } finally {

            attributes.recycle()
        }
    }

    override fun onDraw(canvas: Canvas) {

        canvas.translate(cx, cy)
        canvas.rotate(-90f)
        canvas.scale(scale, scale)

        canvas.getMatrix().invert(inverseTransform)

        //debugDraw(canvas)
        //canvas.drawLine(0f, 0f, REFERENCE_SIZE, 0f, paint)


        canvas.save()
        canvas.translate(ca.bart.u2430136.lab3.CustomView.Companion.REFERENCE_SIZE, 0f)
        canvas.rotate(time * 360f)
        drawPolygon(canvas, ca.bart.u2430136.lab3.CustomView.Companion.REFERENCE_SIZE / 2, 6)


        canvas.restore()

        canvas.save()

        canvas.rotate(time / 3600f * 360f)
        canvas.scale(1f, 2f)
        canvas.drawLine(0f, 0f, ca.bart.u2430136.lab3.CustomView.Companion.REFERENCE_SIZE * 0.9f, 0f, paint)

        canvas.restore()

        canvas.save()

        canvas.rotate(time / (12 * 60 * 60) * 360f)
        canvas.scale(1f, 5f)
        canvas.drawLine(0f, 0f, ca.bart.u2430136.lab3.CustomView.Companion.REFERENCE_SIZE * 0.7f, 0f, paint)

        canvas.restore()

        /*
        canvas.translate(cx, cy)

        debugDraw(canvas)

        canvas.rotate(45f)

        canvas.save()

            canvas.scale(2f, 1f)

            canvas.save()

                canvas.translate(10f, 10f)

            canvas.restore()
        canvas.restore()

        canvas.scale(-1f, 1f)

        debugDraw(canvas)
        */


    }

    private fun drawPolygon(canvas: Canvas, size:Float, sides:Int) {

        val centralAngle = 360f / sides
        val cornerAngle = 180f - centralAngle

        repeat(sides) {

            canvas.save()
            canvas.translate(size, 0f)
            canvas.rotate(180f - cornerAngle / 2)
            canvas.drawLine(0f, 0f, size, 0f, paint)
            canvas.rotate(cornerAngle)
            canvas.drawLine(0f, 0f, size, 0f, paint)
            canvas.restore()

            canvas.rotate(centralAngle)
        }
    }

    fun updatePosition(x:Float, y:Float) {

        val point = floatArrayOf(x, y)
        inverseTransform.mapPoints(point)
        val angle = atan2(point[1], point[0]) * 180 / PI.toFloat()

        Log.d(ca.bart.u2430136.lab3.CustomView.Companion.TAG, "updatePosition(${point[0]}, ${point[1]})=$angle")

        time = angle * (12 * 60 * 60) / 360f
    }

    fun update() {

        if (trackedId == -1) {

            time += MainActivity.SEC_PER_FRAME
        }
    }

}