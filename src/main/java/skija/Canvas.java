package skija;

import java.lang.ref.Cleaner;

public class Canvas {
    public static enum PointMode {
        POINTS, LINES, POLYGON
    }

    Surface mSurface;

    Canvas(long nativeInstance, Surface surface) {
        mNativeInstance = nativeInstance;
        mSurface = surface;
    }
    
    public void drawPoint(float x, float y, Paint paint) {
        nDrawPoint(mNativeInstance, x, y, paint.mNativeInstance);
    }

    public void drawPoints(PointMode mode, float[] coords, Paint paint) {
        nDrawPoints(mNativeInstance, mode.ordinal(), coords, paint.mNativeInstance);
    }

    public void drawLine(float x0, float y0, float x1, float y1, Paint paint) {
        nDrawLine(mNativeInstance, x0, y0, x1, y1, paint.mNativeInstance);
    }

    public void drawArc(float left, float top, float width, float height, float startAngle, float sweepAngle, boolean includeCenter, Paint paint) {
        nDrawArc(mNativeInstance, left, top, width, height, startAngle, sweepAngle, includeCenter, paint.mNativeInstance);
    }

    public void drawRectInscribed(RectInscribed r, Paint paint) {
        nDrawRectInscribed(mNativeInstance, r.left, r.top, r.width, r.height, r.radii, paint.mNativeInstance);
    }
    
    public void clear(long color) { nClear(mNativeInstance, color); }
    public void drawPaint(Paint paint) { nDrawPaint(mNativeInstance, paint.mNativeInstance); }

    public void translate(float dx, float dy) { nConcat(mNativeInstance, 1, 0, dx, 0, 1, dy, 0, 0, 1); }
    public void scale(float sx, float sy) { nConcat(mNativeInstance, sx, 0, 0, 0, sy, 0, 0, 0, 1); }
    public void rotate(float deg) {
        double rad = Math.toRadians(deg);
        double sin = Math.sin(rad);
        double cos = Math.cos(rad);
        double tolerance = 1f / (1 << 12);
        if (Math.abs(sin) <= tolerance) sin = 0;
        if (Math.abs(cos) <= tolerance) cos = 0;
        nConcat(mNativeInstance, (float) cos, (float) -sin, 0, (float) sin, (float) cos, 0, 0, 0, 1);
    }

    public int save() { return nSave(mNativeInstance); }
    public int getSaveCount() { return nGetSaveCount(mNativeInstance); }
    public void restore() { nRestore(mNativeInstance); }
    public void restoreToCount(int saveCount) { nRestoreToCount(mNativeInstance, saveCount); }

    
    long mNativeInstance;
    // private Cleaner.Cleanable mFinalizer;
    // private static NativeAllocationRegistry mAllocations = new NativeAllocationRegistry(nGetNativeFinalizer());
    
    public long getNativeInstance() {
        return mNativeInstance;
    }

    public String toString() {
        return "[SkCanvas 0x" + Long.toString(mNativeInstance, 16) + "]";
    }

    
    // public Canvas release() {
    //     mFinalizer.clean();
    //     mNativeInstance = 0;
    //     return null;
    // }

    // private static native long nGetNativeFinalizer();
    private static native void nDrawPoint(long nativeCanvas, float x, float y, long nativePaint);
    private static native void nDrawPoints(long nativeCanvas, int mode, float[] coords, long nativePaint);
    private static native void nDrawLine(long nativeCanvas, float x0, float y0, float x1, float y1, long nativePaint);
    private static native void nDrawArc(long nativeCanvas, float left, float top, float width, float height, float startAngle, float sweepAngle, boolean includeCenter, long nativePaint);
    private static native void nDrawRectInscribed(long nativeCanvas, float left, float top, float width, float height, float radii[], long nativePaint);
    private static native void nClear(long nativeCanvas, long color);
    private static native void nDrawPaint(long nativeCanvas, long nativePaint);
    private static native void nConcat(long nativeCanvas,
        float scaleX, float skewX,  float transX,
        float skewY,  float scaleY, float transY,
        float persp0, float persp1, float persp2);

    private static native int nSave(long nativeCanvas);
    private static native int nGetSaveCount(long nativeCanvas);
    private static native void nRestore(long nativeCanvas);
    private static native void nRestoreToCount(long nativeCanvas, int saveCount);
}