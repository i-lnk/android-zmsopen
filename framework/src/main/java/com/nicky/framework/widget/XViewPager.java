package com.nicky.framework.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Date:    2014/11/25.
 * Description: customize ViewPager did scroll enable or not
 */
public class XViewPager extends ViewPager {

    private boolean isEnableScroll = true;

    public void setEnableScroll(boolean isEnableScroll) {
        this.isEnableScroll = isEnableScroll;
    }

    public XViewPager(Context context) {
        super(context);
    }

    public XViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
    	if (!isEnableScroll) {
            return false;
        }
        try {
        	return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!isEnableScroll) {
            return false;
        }
        return super.onTouchEvent(ev);
    }
}
