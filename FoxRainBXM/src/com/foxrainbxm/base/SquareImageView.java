package com.foxrainbxm.base;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class SquareImageView extends ImageView
{
  private Paint a = new Paint();
  private ImageView.ScaleType b = getScaleType();

  public SquareImageView(Context paramContext)
  {
    this(paramContext, null);
  }

  public SquareImageView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }

  public SquareImageView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  protected void onDraw(Canvas paramCanvas)
  {
    int i = getWidth();
    if (this.b == ImageView.ScaleType.FIT_CENTER)
    {
      Drawable localDrawable = getDrawable();
      Matrix localMatrix = getImageMatrix();
      if (localDrawable == null){
    	  setScaleType(ImageView.ScaleType.CENTER_CROP);
    	return;  
      }
        
      int j = localDrawable.getIntrinsicWidth();
      int k = localDrawable.getIntrinsicHeight();
      if ((j <= 0) || (k <= 0) || (j >= k)){
    	  setScaleType(ImageView.ScaleType.CENTER_CROP);
    	  return;
      }
      setScaleType(ImageView.ScaleType.MATRIX);
      float f = i / localDrawable.getIntrinsicWidth();
      localMatrix.setScale(f, f);
      setImageMatrix(localMatrix);
    }
    
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    super.onMeasure(paramInt1, paramInt1);
  }
}