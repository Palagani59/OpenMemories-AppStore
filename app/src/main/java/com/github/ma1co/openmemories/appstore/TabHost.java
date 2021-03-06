package com.github.ma1co.openmemories.appstore;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TabHost extends android.widget.TabHost {
    public static TabHost inflate(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context).cloneInContext(context);
        inflater.setFactory((name, ctx, attrs) -> "TabHost".equals(name) ? new TabHost(ctx, attrs) : null);
        int layout = Resources.getSystem().getIdentifier("tab_content", "layout", "android");
        return (TabHost) inflater.inflate(layout, null);
    }

    public final int tabHeightDp = 26;
    private OnTabChangeListener onBeforeTabChangedListener;
    private LocalActivityManager localActivityManager;

    public TabHost(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnBeforeTabChangedListener(OnTabChangeListener l) {
        onBeforeTabChangedListener = l;
    }

    public Activity getActivity(String tag) {
        return localActivityManager.getActivity(tag);
    }

    @Override
    public void setup(LocalActivityManager m) {
        super.setup(m);
        this.localActivityManager = m;
    }

    @Override
    public void setCurrentTab(int index) {
        if (getCurrentTab() >= 0 && index != getCurrentTab() && onBeforeTabChangedListener != null)
            onBeforeTabChangedListener.onTabChanged(getCurrentTabTag());
        super.setCurrentTab(index);
    }

    @Override
    public void addTab(TabSpec tabSpec) {
        super.addTab(tabSpec);
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, tabHeightDp, getResources().getDisplayMetrics());
        View tab = getTabWidget().getChildAt(getTabWidget().getChildCount() - 1);
        tab.getLayoutParams().height = height;
        tab.setPadding(0, 0, 0, 0);
        TextView title = (TextView) tab.findViewById(android.R.id.title);
        title.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        title.setGravity(Gravity.CENTER);
    }
}
