package com.udacity

import android.animation.AnimatorInflater
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var progress = 0.0
    private var valueAnimator = ValueAnimator()

    var buttonState: ButtonState by Delegates.observable(ButtonState.Completed) { p, old, new ->
    }

    private val rect = RectF()
    private val textBoundRect = Rect()


    private val updateListener = ValueAnimator.AnimatorUpdateListener {
        progress = (it.animatedValue as Float).toDouble()
        invalidate()
        if (progress == 100.0) {
            onDownloadCompleted()
        }
    }

    init {
        isClickable = true
        valueAnimator = AnimatorInflater.loadAnimator(
            context,
            R.animator.animator
        ) as ValueAnimator
        valueAnimator.addUpdateListener(updateListener)
    }


    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 40.0f
        typeface = Typeface.create("", Typeface.BOLD)
        color = resources.getColor(R.color.colorPrimary)
    }


    private fun onDownloadCompleted() {
        valueAnimator.cancel()
        buttonState = ButtonState.Completed
        invalidate()
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val buttonText =
            if (buttonState == ButtonState.Loading) resources.getString(R.string.button_loading)
            else resources.getString(R.string.download)

        canvas?.drawRect(0.0f, 0.0f, width.toFloat(), height.toFloat(), paint)

        if (buttonState == ButtonState.Loading) {
            paint.color = resources.getColor(R.color.colorPrimaryDark)
            canvas?.drawRect(
                0f, 0f,
                (widthSize * (progress / 100)).toFloat(), height.toFloat(), paint
            )

            paint.getTextBounds(buttonText,0,buttonText.length,textBoundRect)
            val centerX = measuredWidth.toFloat() / 2

            paint.color = resources.getColor(R.color.colorAccent)
            rect.set(centerX+textBoundRect.right/2+20.0f, 30.0f, centerX+textBoundRect.right/2+80.0f, measuredHeight.toFloat() -30.0f )
            canvas?.drawArc(
                rect,
                0f, (360 * (progress / 100)).toFloat(),
                true,
                paint
            )

        }
        else if (buttonState == ButtonState.Completed) {
            paint.color = resources.getColor(R.color.colorPrimary)
            canvas?.drawRect(
                0f, 0f,
                (widthSize * (progress / 100)).toFloat(), heightSize.toFloat(), paint
            )
        }

        paint.color = Color.WHITE
        canvas?.drawText(buttonText, (width / 2).toFloat(), ((height + 30) / 2).toFloat(), paint)

        paint.color = resources.getColor(R.color.colorPrimary)
    }

    override fun performClick(): Boolean {
        super.performClick()
        if(buttonState == ButtonState.Loading){
            return false
        }
        if (buttonState == ButtonState.Completed) buttonState = ButtonState.Loading
        valueAnimator.start()
        return true
    }
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        widthSize = w
        heightSize = h
    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }


}