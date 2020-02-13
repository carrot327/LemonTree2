package com.kantong.android.utils.text;

import android.os.Parcel;
import android.text.style.AbsoluteSizeSpan;

/**
 * @author evanyu
 * @date 17/12/13
 */
public class TextSizeSpan extends AbsoluteSizeSpan implements ITextSpan, Cloneable {

    public TextSizeSpan(int size) {
        super(size);
    }

    public TextSizeSpan(int size, boolean dip) {
        super(size, dip);
    }

    public TextSizeSpan(Parcel src) {
        super(src);
    }

    @Override
    public TextSizeSpan clone() throws CloneNotSupportedException {
        return (TextSizeSpan) super.clone();
    }
}
