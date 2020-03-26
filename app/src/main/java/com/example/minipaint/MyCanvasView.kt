package com.example.minipaint

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.content.res.ResourcesCompat
import kotlin.math.abs

class MyCanvasView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : View(context, attrs, defStyle) {

    private lateinit var extraCanvas: Canvas
    private lateinit var extraBitmap: Bitmap
    private val backgroundColor = ResourcesCompat.getColor(resources, R.color.colorBackground, null)

    // paint variables
    private var STROKE_WIDTH = 12f // has to be float
    private var drawColor = ResourcesCompat.getColor(resources, R.color.colorPaint, null)
    private var paint = Paint().apply {
        color = drawColor
        isAntiAlias = true // Smooths out edges of what is drawn without affecting shape.
        isDither = true // Dithering affects how colors with higher-precision than the device are down-sampled.
        style = Paint.Style.STROKE // default: FILL
        strokeJoin = Paint.Join.ROUND // default: MITER
        strokeCap = Paint.Cap.ROUND // default: BUTT
        strokeWidth = STROKE_WIDTH // default: Hairline-width (really thin)
    }
    private var path = Path()
    private var motionTouchEventX = 0f
    private var motionTouchEventY = 0f
    private var currentX = 0f
    private var currentY = 0f
    private val touchTolerance = ViewConfiguration.get(context).scaledTouchSlop // draw only when finger moves more than this tolerance

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if(::extraBitmap.isInitialized) extraBitmap.recycle()
        extraBitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888)
        extraCanvas = Canvas(extraBitmap)
        extraCanvas.drawColor(backgroundColor)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawBitmap(extraBitmap,0f,0f,null)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            motionTouchEventX = event.x
            motionTouchEventY = event.y

            when(event.action){
                MotionEvent.ACTION_DOWN -> touchStart()
                MotionEvent.ACTION_MOVE -> touchMove()
                MotionEvent.ACTION_UP -> touchUp()
            }
            return true
        } else return false
    }

    /*
    Reset the path, move to the x-y coordinates of the touch event
    (motionTouchEventX and motionTouchEventY), and assign currentX
    and currentY to that value.
     */
    private fun touchStart() {
        path.reset()
        path.moveTo(motionTouchEventX,motionTouchEventY)
        currentX = motionTouchEventX
        currentY = motionTouchEventY
    }


    /*
     Calculate the traveled distance (dx, dy), create a curve between the two points
     and store it in path, update the running currentX and currentY tally,
     and draw the path. Then call invalidate() to force redrawing of the
     screen with the updated path.
     */
    private fun touchMove() {
        val dx = abs(motionTouchEventX - currentX)
        val dy = abs(motionTouchEventY - currentY)
        if(dx >= touchTolerance || dy >= touchTolerance){
            // QuadTo() adds a quadratic bezier from the last point,
            // approaching control point (x1,y1), and ending at (x2,y2).
            // Using quadTo() instead of lineTo() create a smoothly drawn line without corners.
            path.quadTo(currentX, currentY, (motionTouchEventX + currentX) / 2, (motionTouchEventY + currentY) / 2)
            //path.lineTo(currentX, currentY)
            currentX = motionTouchEventX
            currentY = motionTouchEventY
            // Draw the path in the extra bitmap to cache it.
            extraCanvas.drawPath(path, paint)
        }
        invalidate()
    }

    private fun touchUp() {
        // Reset the path so it doesn't get drawn again.
        path.reset()
    }

    fun clearCanvas(){
        extraCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
    }

    fun changeToBrush(){
        STROKE_WIDTH = 24f
        paint = Paint().apply {
            color = drawColor
            isAntiAlias = true // Smooths out edges of what is drawn without affecting shape.
            isDither = true // Dithering affects how colors with higher-precision than the device are down-sampled.
            style = Paint.Style.STROKE // default: FILL
            strokeJoin = Paint.Join.ROUND // default: MITER
            strokeCap = Paint.Cap.ROUND // default: BUTT
            strokeWidth = STROKE_WIDTH // default: Hairline-width (really thin)
        }
    }

    fun changeToPencil(){
        STROKE_WIDTH = 12f
        paint = Paint().apply {
            color = drawColor
            isAntiAlias = true // Smooths out edges of what is drawn without affecting shape.
            isDither = true // Dithering affects how colors with higher-precision than the device are down-sampled.
            style = Paint.Style.STROKE // default: FILL
            strokeJoin = Paint.Join.ROUND // default: MITER
            strokeCap = Paint.Cap.ROUND // default: BUTT
            strokeWidth = STROKE_WIDTH // default: Hairline-width (really thin)
        }
    }
}