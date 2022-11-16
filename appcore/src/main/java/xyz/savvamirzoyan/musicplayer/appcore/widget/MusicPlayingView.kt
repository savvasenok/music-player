package xyz.savvamirzoyan.musicplayer.appcore.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.View

class MusicPlayingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint: Paint

    private var firstBarHeight = 0f // full height
    private var secondBarHeight = 24f // min height
    private var thirdBarHeight = 8f // medium height

    private var isFirstGrow = false
    private var isSecondGrow = true
    private var isThirdGrow = false

    private var _width: Int = 32
    private var _height: Int = 32

    init {
        paint = Paint().apply {
            color = if (Build.VERSION.SDK_INT >= 31) context.getColor(android.R.color.system_accent1_600)
            else context.getColor(android.R.color.holo_orange_light)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        _width = w
        _height = h
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(_width, _height)
    }

    override fun onDraw(canvas: Canvas?) {

        canvas?.drawRect(0f, firstBarHeight, 8f, 32f, paint)
        canvas?.drawRect(12f, secondBarHeight, 20f, 32f, paint)
        canvas?.drawRect(24f, thirdBarHeight, 32f, 32f, paint)

        calculateHeight()

        handler.postDelayed(::invalidate, 30)
    }

    private fun calculateHeight() {

        if (firstBarHeight == 0f) { // has to go down
            isFirstGrow = false
        } else if (firstBarHeight == 24f) {
            isFirstGrow = true
        }

        if (secondBarHeight == 0f) { // has to go down
            isSecondGrow = false
        } else if (secondBarHeight == 24f) {
            isSecondGrow = true
        }

        if (thirdBarHeight == 0f) { // has to go down
            isThirdGrow = false
        } else if (thirdBarHeight == 24f) {
            isThirdGrow = true
        }

        firstBarHeight += 2f * isFirstGrow.int()
        secondBarHeight += 2f * isSecondGrow.int()
        thirdBarHeight += 2f * isThirdGrow.int()
    }

    private fun Boolean.int(): Int = if (this) -1 else 1
}