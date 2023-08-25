package com.dspread.demoui.utils;

import android.content.Context;
import android.util.DisplayMetrics;


public class DisplayUtils
{
    public static DisplayMetrics getDisplayMetrics(final Context pContext)
    {
        return pContext.getResources().getDisplayMetrics();
    }

    public static int getDisplayWidthPixels(final Context pContext)
    {
        return DisplayUtils.getDisplayMetrics(pContext).widthPixels;
    }

    public static int getDisplayHeightPixels(final Context pContext)
    {
        return DisplayUtils.getDisplayMetrics(pContext).heightPixels;
    }

    public static float getDisplayXDpi(final Context pContext)
    {
        return DisplayUtils.getDisplayMetrics(pContext).xdpi;
    }

    public static float getDisplayYDpi(final Context pContext)
    {
        return DisplayUtils.getDisplayMetrics(pContext).ydpi;
    }

    public static float getDisplayDensity(final Context pContext)
    {
        return DisplayUtils.getDisplayMetrics(pContext).density;
    }

    public static int dipToPx(final Context pContext, final int pDip)
    {
        return (int) (pDip * DisplayUtils.getDisplayDensity(pContext) + 0.5f);
    }
}