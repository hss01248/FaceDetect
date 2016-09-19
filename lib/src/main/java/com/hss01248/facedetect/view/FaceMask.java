package com.hss01248.facedetect.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.faceplusplus.api.FaceDetecter.Face;

public class FaceMask extends View {
    
    private Paint localPaint;
    private Face[] faceInfos;
    private RectF rect;

    public FaceMask(Context context, AttributeSet atti) {
        super(context, atti);
        rect = new RectF();
        localPaint = new Paint();
        localPaint.setColor(0xff248FAF);
        localPaint.setStrokeWidth(5);
        localPaint.setStyle(Paint.Style.STROKE);
    }

    public void setFaceInfo(Face[] faceInfos) {
        this.faceInfos = faceInfos;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (faceInfos == null)
            return;
        for (Face localFaceInfo : faceInfos) {
            rect.set(getWidth() * localFaceInfo.left, getHeight() * localFaceInfo.top,
                    getWidth() * localFaceInfo.right, getHeight() * localFaceInfo.bottom);
            canvas.drawRect(rect, localPaint);
        }
    }
}
