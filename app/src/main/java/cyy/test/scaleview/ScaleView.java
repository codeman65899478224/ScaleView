package cyy.test.scaleview;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author chenyyb
 * @date 2019/9/8
 */

public class ScaleView extends AppCompatImageView implements View.OnClickListener{
    public static final String TAG = ScaleView.class.getSimpleName();
    private float lastX = 0;
    private float lastY = 0;
    private int left = 0;
    private int right = 0;
    private int top = 0;
    private int bottom = 0;
    private int initLeft = 0;
    private int initRight = 0;
    private int initTop = 0;
    private int initBottom = 0;
    private static final float SCALE_DEFAULT= 1.0f;
    private static final float SCALE_MIN= 0.5f;

    /**
     * 两指水平间距
     */
    private double oldOffset = 0.00D;

    private double moveOffset = 0.00D;

    /**
     * 是否正在拖拽
     */
    private boolean isDragging = false;

    /**
     * 是否被缩放
     */
    private boolean isScaled = false;


    public ScaleView(Context context) {
        this(context, null);
    }

    public ScaleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScaleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        initLeft = getLeft();
        initRight = getRight();
        initTop = getTop();
        initBottom = getBottom();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int e = event.getAction();

        switch (e & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "按下");
                isDragging = false;
                lastX = event.getX();
                lastY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //Log.i(TAG, "移动");
                if(event.getPointerCount() == 2){
                    Log.i(TAG, "两点触控");
                    double offset = computeOffsetOfTwoPoint(event);
                    moveOffset = offset - oldOffset;
                    if (Math.abs(moveOffset) > 3) {
                        float scale = (float) (getScaleX() + moveOffset / getWidth());
                        if (scale <= SCALE_MIN) {
                            scaleView(SCALE_MIN);
                        } else {
                            scaleView(scale);
                        }
                    }
                }else if (event.getPointerCount() == 1) {
                    //根据抬起的指头判断偏移量
                    //Log.i(TAG, "单点触控");
                    float offsetX = event.getX() - lastX;
                    float offsetY = event.getY() - lastY;

                    left = (int) (getLeft() + offsetX);
                    right = getWidth() + left;
                    top = (int) (getTop() + offsetY);
                    bottom = getHeight() + top;

                    if (Math.abs(offsetX) >= 5 || Math.abs(offsetY) >= 5) {
                        Log.i(TAG, "left: " + left + " right: " + right + " top: " + top + " bottom: " + bottom);
                        this.layout(left, top, right, bottom);
                    }
                }
                isDragging = true;
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "抬起");
                if(getScaleX() < SCALE_DEFAULT || getScaleY() < SCALE_DEFAULT) {
                    resetLocation();
                }
                performClick();
                isDragging = false;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.i(TAG, "手指按下");
                if (event.getPointerCount() == 2) {
                    oldOffset = computeOffsetOfTwoPoint(event);
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                Log.i(TAG, "手指抬起");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.i(TAG, "取消");
                isDragging = false;
                break;
            default:
        }
        return true;
    }

    private void resetLocation() {
        Log.i(TAG, "重置");
        setScaleX(SCALE_DEFAULT);
        setScaleY(SCALE_DEFAULT);
        isScaled = false;
        this.layout(initLeft, initTop, initRight, initBottom);
    }

    private double computeOffsetOfTwoPoint(MotionEvent event){
        //两点水平间距
        float x = event.getX(0) - event.getX(1);
        //两点垂直间距
        float y = event.getY(0) - event.getY(1);

        return Math.sqrt(x * x + y * y);
    }

    private void scaleView(float scale){
        setScaleX(scale);
        setScaleY(scale);
        isScaled = true;
    }

    @Override
    public boolean performClick() {
        Log.i(TAG, "performClick");
        return super.performClick();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public void onClick(View view) {
        Log.i(TAG, "onClick");
    }
}
