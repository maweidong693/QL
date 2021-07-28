package com.tencent.qcloud.tim.uikit.modules.conversation.base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;


import androidx.appcompat.widget.AppCompatImageView;

public class RoundImage extends AppCompatImageView {
    /*圆角的半径，依次为左上角xy半径，右上角，右下角，左下角*/
    //此处可根据自己需要修改大小
    private float[] rids = {10f,40f, 10f, 10f, 10f, 10f, 10f,10f,};


    public RoundImage(Context context) {
        super(context);
    }


    public RoundImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public RoundImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = View.MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width, width);
    }

    /**
     * 画图
     *
     * @param canvas
     */
    protected void onDraw(Canvas canvas) {
        Path path = new Path();
        int w = this.getWidth();
        int h = this.getHeight();
        /*向路径中添加圆角矩形。radii数组定义圆角矩形的四个圆角的x,y半径。radii长度必须为8*/
        path.addRoundRect(new RectF(0, 0, w, h), rids, Path.Direction.CW);
        canvas.clipPath(path);
        super.onDraw(canvas);
    }
}
