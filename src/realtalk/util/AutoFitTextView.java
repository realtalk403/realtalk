package realtalk.util;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

public class AutoFitTextView extends TextView {
    private float minTextSize;
    private float maxTextSize;
    private static final int MAX_TEXT_THRESHOLD = 35;
    private static final int MAX_TEXT_SIZE = 30;
    private static final int MIN_TEXT_SIZE = 20;
    
    public AutoFitTextView(Context context) {
        super(context);
        init();
    }
    
    public AutoFitTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    private void init() {
    
        maxTextSize = this.getTextSize();
        if (maxTextSize < MAX_TEXT_THRESHOLD) {
            maxTextSize = MAX_TEXT_SIZE;
        }
        minTextSize = MIN_TEXT_SIZE;
    }
    
    private void refitText(String text, int textWidth) {
        if (textWidth > 0) {
            int availableWidth = textWidth - this.getPaddingLeft()
                    - this.getPaddingRight();
            float trySize = maxTextSize;
    
            this.setTextSize(TypedValue.COMPLEX_UNIT_PX, trySize);
            while ((trySize > minTextSize)
                    && (this.getPaint().measureText(text) > availableWidth)) {
                trySize -= 1;
                if (trySize <= minTextSize) {
                    trySize = minTextSize;
                    break;
                }
                this.setTextSize(TypedValue.COMPLEX_UNIT_PX, trySize);
            }
            this.setTextSize(TypedValue.COMPLEX_UNIT_PX, trySize);
        }
    }
    
    @Override
    protected void onTextChanged(final CharSequence text, final int start,
            final int before, final int after) {
        refitText(text.toString(), this.getWidth());
    }
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != oldw) {
            refitText(this.getText().toString(), w);
        }
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        refitText(this.getText().toString(), parentWidth);
    }
    
    public float getMinTextSize() {
        return minTextSize;
    }
    
    public void setMinTextSize(int minTextSize) {
        this.minTextSize = minTextSize;
    }
    
    public float getMaxTextSize() {
        return maxTextSize;
    }
    
    public void setMaxTextSize(int minTextSize) {
        this.maxTextSize = minTextSize;
    }
}
