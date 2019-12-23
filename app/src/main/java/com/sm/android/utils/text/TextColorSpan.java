package com.sm.android.utils.text;

import android.os.Parcel;
import android.text.style.ForegroundColorSpan;

/**
 * @author evanyu
 * @date 17/12/13
 */
public class TextColorSpan extends ForegroundColorSpan implements ITextSpan, Cloneable {

    public TextColorSpan(int color) {
        super(color);
    }

    public TextColorSpan(Parcel src) {
        super(src);
    }

    @Override
    public TextColorSpan clone() throws CloneNotSupportedException {
        return (TextColorSpan) super.clone();
    }
}
