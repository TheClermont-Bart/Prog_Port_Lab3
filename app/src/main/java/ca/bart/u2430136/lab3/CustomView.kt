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
        var REFERENCE_SIZE = 0f

        val RED_LINE_PAINT = Paint()

        init {
            RED_LINE_PAINT.color = Color.RED
            RED_LINE_PAINT.style = Paint.Style.STROKE
            RED_LINE_PAINT.strokeWidth = 3f
        }
    }

    var radius = 0f
    var scale = 5f

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

    var rotationAngle = time * 30f

    var side : Int = 0
        get() = field
        set(value) {
            field = value
            invalidate()
        }

    var startAngle : Float = 0f
        get() = field
        set(value) {
            field = value
            invalidate()
        }

    init {

        val attributes = context.theme.obtainStyledAttributes(attrs, R.styleable.CustomView, defStyle, 0)
        try {

            paint.color = attributes.getColor(R.styleable.CustomView_color, Color.RED)
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 3f

            time = 3600f

            side = attributes.getInt(R.styleable.CustomView_side, 3)
            if(side>7){side=7}
            startAngle = attributes.getFloat(R.styleable.CustomView_startAngle, 0f)
            REFERENCE_SIZE = attributes.getDimensionPixelSize(R.styleable.CustomView_size, 0).toFloat()

        } finally {

            attributes.recycle()
        }
    }

    override fun onDraw(canvas: Canvas) {
        val cx = width / 2f
        val cy = height / 2f

        canvas.translate(cx, cy)
        canvas.rotate(rotationAngle + startAngle )
        canvas.scale(scale, scale)
        drawPolygon(canvas, REFERENCE_SIZE, side)
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

            if(sides>3){
                canvas.save()
                canvas.translate(size,0f)
                canvas.rotate(rotationAngle + startAngle )
                drawPolygon(canvas,size/2f,sides-1)
                canvas.restore()
            }

            canvas.rotate(centralAngle)
        }
    }

    fun update() {

        if (trackedId == -1) {
            time += MainActivity.SEC_PER_FRAME
            rotationAngle += MainActivity.ROTATION_SPEED_DEG_PER_SEC * MainActivity.SEC_PER_FRAME
            if (rotationAngle >= 360f) {
                rotationAngle -= 360f
            }
        }
    }

}