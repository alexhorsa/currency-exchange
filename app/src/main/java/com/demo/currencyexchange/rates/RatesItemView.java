package com.demo.currencyexchange.rates;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.demo.currencyexchange.MainActivity;
import com.demo.currencyexchange.R;
import com.demo.currencyexchange.util.CurrencyDefinition;

public class RatesItemView extends View {

    private Drawable flagIcon;
    private StaticLayout codeLayout;
    private StaticLayout nameLayout;
    private StaticLayout valueLayout;

    private TextPaint codePaint;
    private TextPaint namePaint;
    private TextPaint valuePaint;
    private Paint debugPaint;

    public RatesItemView(Context context) {
        super(context);
        init();
    }

    public RatesItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RatesItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RatesItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = (int) dp(72);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
//        canvas.drawLine(0f, 0f, getWidth(), 0f, debugPaint);
//        canvas.drawLine(0f, getHeight(), getWidth(), getHeight(), debugPaint);
        codeLayout.draw(canvas);
        canvas.translate(0f, codeLayout.getHeight());
        nameLayout.draw(canvas);
        canvas.translate(MainActivity.SCREEN_SIZE.x - valueLayout.getWidth(), (getHeight() / 2f) - codeLayout.getHeight() - (valueLayout.getHeight() / 2f));
        valueLayout.draw(canvas);
        canvas.restore();
    }

    private void init() {
        Resources resources = getResources();
        Typeface robotoMedium = ResourcesCompat.getFont(getContext(), R.font.roboto_medium);
        Typeface robotoRegular = ResourcesCompat.getFont(getContext(), R.font.roboto_regular);

        codePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        codePaint.setColor(resources.getColor(R.color.colorTextPrimary));
        codePaint.setTextSize(sp(16));
        codePaint.setTypeface(robotoMedium);

        namePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        namePaint.setColor(resources.getColor(R.color.colorTextSecondary));
        namePaint.setTextSize(sp(14));
        namePaint.setTypeface(robotoRegular);

        valuePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        valuePaint.setColor(resources.getColor(R.color.colorTextPrimary));
        valuePaint.setTextSize(sp(20));
        valuePaint.setTypeface(robotoMedium);

        debugPaint = new Paint();
        debugPaint.setColor(Color.BLACK);
        debugPaint.setStrokeWidth(1f);
    }

    public void setRate(CurrencyDefinition definition, String value) {
        final String codeString = getResources().getString(definition.code);
        final String nameString = getResources().getString(definition.name);
        float codeTextWidth = codePaint.measureText(codeString);
        float nameTextWidth = namePaint.measureText(nameString);
        float valueTextWidth = valuePaint.measureText(value);
        codeLayout = new StaticLayout(codeString, codePaint, (int)codeTextWidth, Layout.Alignment.ALIGN_LEFT, 1f, 1f, true);
        nameLayout = new StaticLayout(nameString, namePaint, (int)nameTextWidth, Layout.Alignment.ALIGN_LEFT, 1f, 1f, true);
        valueLayout = new StaticLayout(value, valuePaint, (int)valueTextWidth, Layout.Alignment.ALIGN_RIGHT, 1f, 1f, true);

        requestLayout();
        invalidate();
    }

    private float sp(float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

    private float dp(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
