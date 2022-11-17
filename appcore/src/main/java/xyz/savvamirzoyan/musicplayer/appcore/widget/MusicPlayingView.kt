package xyz.savvamirzoyan.musicplayer.appcore.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View

@SuppressLint("ResourceType")
class MusicPlayingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint: Paint

    private var _width: Int = 40
    private var _height: Int = 40
    private val barWidth: Float
        get() = _width / 4f
    private val gapWidth: Float
        get() = _width / 8f
    private val barHeightMax: Float
        get() = _height.toFloat()
    private val barHeightMin: Float
        get() = _height / 12f

    private var firstBarHeight = 0f // full height
    private var secondBarHeight = barHeightMax / 1.5f // min height
    private var thirdBarHeight = barHeightMax / 3f // medium height

    private var isFirstGrow = false
    private var isSecondGrow = true
    private var isThirdGrow = false

    init {

        val typedValue = TypedValue()

        paint = Paint().apply {
            if (Build.VERSION.SDK_INT >= 31) {
                color = context.getColor(android.R.color.system_accent2_400)
            } else {
                context.theme.resolveAttribute(androidx.appcompat.R.attr.colorAccent, typedValue, true)
                color = context.getColor(typedValue.resourceId)
            }
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

        canvas?.drawRect(0f, firstBarHeight, barWidth, barHeightMax, paint)
        canvas?.drawRect(barWidth + gapWidth, secondBarHeight, 2 * barWidth + gapWidth, barHeightMax, paint)
        canvas?.drawRect(2 * (barWidth + gapWidth), thirdBarHeight, 3 * barWidth + 2 * gapWidth, barHeightMax, paint)

        calculateHeight()
//
        handler.postDelayed(::invalidate, 30)
    }

    private fun calculateHeight() {

        if (firstBarHeight <= 0) { // has to go down
            isFirstGrow = false
        } else if (firstBarHeight >= barHeightMax - 2 * barHeightMin) {
            isFirstGrow = true
        }

        if (secondBarHeight <= 0f) { // has to go down
            isSecondGrow = false
        } else if (secondBarHeight >= barHeightMax - 2 * barHeightMin) {
            isSecondGrow = true
        }

        if (thirdBarHeight <= 0f) { // has to go down
            isThirdGrow = false
        } else if (thirdBarHeight >= barHeightMax - 2 * barHeightMin) {
            isThirdGrow = true
        }

        firstBarHeight += 2f * isFirstGrow.int()
        secondBarHeight += 2f * isSecondGrow.int()
        thirdBarHeight += 2f * isThirdGrow.int()
    }

    private fun Boolean.int(): Int = if (this) -1 else 1
}