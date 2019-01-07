package com.weex.flowview.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.widget.Scroller

/**
 * Created by xuwx on 2018/12/29.
 */
class FlowView : View {
    companion object {
        const val FLOW_ORIENTATION_VERTICAL = 0
        const val FLOW_ORIENTATION_HORIZONTAL = 1
    }

    private var mLeftPadding: Float = 20f
    private var mRightPadding: Float = 20f
    private var mTopPadding: Float = 20f
    private var mBottomPadding: Float = 20f

    private var mCombineLength = dp2px(20f)

    private lateinit var mFlowData: MutableList<String>
    private var mFlowTextColor = Color.WHITE
    private var mFlowTextSize = sp2px(12f)
    private var mFlowTextPadding = dp2px(5f)
    private lateinit var fontMetrics: Paint.FontMetrics
    private var textHeight: Float = 0f

    private var startX = mLeftPadding
    private var startY = mTopPadding

    private var orientation = FLOW_ORIENTATION_VERTICAL

    private lateinit var mPaint: Paint
    private lateinit var mRectPaint: Paint
    private lateinit var mBorderPaint: Paint
    private lateinit var combinePaint: Paint
    private val mRectCorner = dp2px(5f)

    private lateinit var scroller: Scroller

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        init()
    }

    fun changeOrientation() {
        orientation = if (orientation == FLOW_ORIENTATION_VERTICAL) FLOW_ORIENTATION_HORIZONTAL else FLOW_ORIENTATION_VERTICAL
        reset()
        requestLayout()
    }

    /**
     * 设置方向
     */
    fun setOrientation(orientation: Int) {
        this.orientation = orientation
        reset()
        requestLayout()
    }

    fun addItem(item: String) {
        mFlowData.add(item)
        reset()
        requestLayout()
    }

    fun setUpData(data: List<String>) {
        mFlowData.clear()
        mFlowData.addAll(data)
        reset()
        requestLayout()
    }

    private fun reset() {
        startX = mLeftPadding
        startY = mTopPadding
    }

    private fun init() {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.color = mFlowTextColor
        mPaint.textSize = mFlowTextSize
        mPaint.textAlign = Paint.Align.LEFT
        fontMetrics = mPaint.fontMetrics
        textHeight = Math.abs(Math.abs(fontMetrics.bottom) - Math.abs(fontMetrics.top))

        mRectPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mRectPaint.color = Color.parseColor("#00c6ae")

        mBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mBorderPaint.color = Color.parseColor("#a8e3dc")
        mBorderPaint.strokeWidth = dp2px(1f)
        mBorderPaint.style = Paint.Style.STROKE

        combinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        combinePaint.color = Color.parseColor("#00c6ae")

        scroller = Scroller(context)

        mFlowData = mutableListOf("Weex", "longlonglonglonglongtext", "Hone", "Max", "New yori", "suns", "test for number 8347734")
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var width = MeasureSpec.getSize(widthMeasureSpec)
        var widthMode = MeasureSpec.getMode(widthMeasureSpec)
        var height = MeasureSpec.getSize(heightMeasureSpec)
        var heightMode = MeasureSpec.getMode(heightMeasureSpec)
        setMeasuredDimension(measureWidth(width, widthMode), measureHeight(height, heightMode))
    }

    private fun measureWidth(width: Int, widthMode: Int): Int {
        if (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.UNSPECIFIED) {
            return if (orientation == FLOW_ORIENTATION_VERTICAL) {
                (calculateMaxWidth() + mLeftPadding + mRightPadding + mFlowTextPadding * 2).toInt()
            } else {
                var width = 0
                for (index in 0 until mFlowData.size) {
                    width += (mPaint.measureText(mFlowData[index]) + mFlowTextPadding * 2).toInt()
                    if (index != mFlowData.size - 1) {
                        width += mCombineLength.toInt()
                    }
                }
                (width + mLeftPadding + mRightPadding).toInt()
            }
        } else {
            return width
        }
    }

    private fun measureHeight(height: Int, heightMode: Int): Int {
        if (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED) {
            return if (orientation == FLOW_ORIENTATION_VERTICAL) {
                var height = 0
                for (index in 0 until mFlowData.size) {
                    height += (textHeight + mFlowTextPadding * 2).toInt()
                    if (index != mFlowData.size - 1) {
                        height += mCombineLength.toInt()
                    }
                }
                (height + mTopPadding + mBottomPadding).toInt()
            } else {
                (textHeight + mFlowTextPadding * 2 + mTopPadding + mBottomPadding).toInt()
            }
        } else {
            return height
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        var itemWidth = calculateMaxWidth() + mLeftPadding + mRightPadding + mFlowTextPadding * 2
        if (orientation == FLOW_ORIENTATION_HORIZONTAL) {
            for (index in 0 until mFlowData.size) {
                var textWidth = mPaint.measureText(mFlowData[index])
                canvas!!.drawRoundRect(mLeftPadding + startX, startY, mLeftPadding + textWidth + mFlowTextPadding * 2 + startX
                        , startY + mFlowTextPadding * 2 + textHeight, mRectCorner, mRectCorner, mRectPaint)
                canvas!!.drawRoundRect(mLeftPadding + startX, startY, mLeftPadding + textWidth + mFlowTextPadding * 2 + startX
                        , startY + mFlowTextPadding * 2 + textHeight, mRectCorner, mRectCorner, mBorderPaint)
                canvas!!.drawText(mFlowData[index], startX + mLeftPadding + mFlowTextPadding, startY + mFlowTextPadding + (Math.abs(fontMetrics.bottom) - fontMetrics.top) / 2, mPaint)
                startX += mLeftPadding + mFlowTextPadding * 2 + textWidth + mRightPadding

                if(index != mFlowData.size - 1){
                    canvas.drawLine(startX, textHeight / 2 + mFlowTextPadding + mTopPadding, startX + mCombineLength, textHeight / 2 + mFlowTextPadding + startY, combinePaint)
                }

                startX += mCombineLength
            }
        } else {
            for (index in 0 until mFlowData.size) {
                var textWidth = mPaint.measureText(mFlowData[index])
                startX = itemWidth / 2 - (textWidth / 2 + mFlowTextPadding)
                canvas!!.drawRoundRect(startX, startY, startX + textWidth + mFlowTextPadding * 2
                        , mFlowTextPadding * 2 + textHeight + startY, mRectCorner, mRectCorner, mRectPaint)
                canvas!!.drawRoundRect(startX, startY, startX + textWidth + mFlowTextPadding * 2
                        , mFlowTextPadding * 2 + textHeight + startY, mRectCorner, mRectCorner, mBorderPaint)
                canvas!!.drawText(mFlowData[index], startX + mFlowTextPadding, startY + mFlowTextPadding + (Math.abs(fontMetrics.bottom) - fontMetrics.top) / 2, mPaint)
                startY += textHeight + mFlowTextPadding * 2
                if (index != mFlowData.size - 1) {
                    canvas.drawLine(itemWidth / 2, startY, itemWidth / 2, startY + mCombineLength, combinePaint)
                }
                startY += mCombineLength
            }
        }
    }

    private fun calculateMaxWidth(): Float {
        var width = 0f
        for (item in mFlowData) {
            width = if (mPaint.measureText(item) > width) mPaint.measureText(item) else width
        }
        return width
    }

    /**
     * dp转px
     *
     * @param dpValue dp值
     * @return px值
     */
    fun dp2px(dpValue: Float): Float {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f)
    }

    /**
     * sp转px
     *
     * @param spValue sp值
     * @return px值
     */
    fun sp2px(spValue: Float): Float {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toFloat()
    }

}