/**
 * @author LiuXiaoyuan
 * @creation_date 2012-6-20
 */
package douzi.android.dragdrop;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.PopupWindow;

/**
 * 
 * Controller for drag and drop action
 * 
 * @author LiuXiaoyuan
 * 
 */
public class DragController implements OnTouchListener {

	public final static String TAG = "DragDrop";

	// container for all drag and drop views
	private ViewGroup mContainer;
	// drag source
	private IDragDrop mSource;
	// drop target
	private IDragDrop mTarget;
	// popup window for shadow
	private PopupWindow mShadowPop;
	
	private OnDragAction mDragActionListener;
	
	private Object mData;
	
	public DragController(ViewGroup container) {
		mContainer = container;
	}
	
	public void setOnDragActionListener(OnDragAction l){
		mDragActionListener = l;
	}
	

	public void startDrag(IDragDrop source,Object data, DragShadowBuilder builder,ViewGroup parent) {
		mSource = source;
		mData = data;
		mShadowPop = new PopupWindow(mContainer.getContext());
		ShadowView shadowView = new ShadowView(mContainer.getContext(), null,
				builder);
		mShadowPop.setWidth(builder.getView().getWidth());
		mShadowPop.setHeight(builder.getView().getHeight());
		mShadowPop.setContentView(shadowView);
		mShadowPop.setBackgroundDrawable(null);
		
		if(parent != null){
			parent.setOnTouchListener(this);
		}else{
			View sourceView = (View) source;
			sourceView.setOnTouchListener(this);
		}
	}

	/**
	 * ShadowView used for drag begin , and show shadow of drag view on the
	 * pointer location
	 */
	private static class ShadowView extends View {

		private DragShadowBuilder mBuilder;

		public ShadowView(Context context, AttributeSet attrs,
				DragShadowBuilder builder) {
			super(context, attrs);
			mBuilder = builder;
			mPaint.setColor(Color.RED);
			mPaint.setStyle(Style.STROKE);
			mPaint.setStrokeWidth(10);
		}
		
		Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

		@Override
		protected void onDraw(Canvas canvas) {
			mBuilder.onDrawShadow(canvas);
			canvas.drawRect(canvas.getClipBounds(), mPaint);
		}

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		Log.d(TAG,"touch");
		if (mShadowPop == null) {
			return false;
		}
		int x = (int) event.getX();
		int y = (int) event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
		{
			if (!mShadowPop.isShowing()) {
				mShadowPop.showAtLocation(mContainer, Gravity.NO_GRAVITY, x, y);
			}
			mShadowPop.update(x, y, mShadowPop.getWidth(),
					mShadowPop.getHeight());
			IDragDrop target = findTarget(x, y);
			if(target != null && mTarget != target){
				mTarget = target;
				target.onDragEnter();
				if(mDragActionListener != null){
					mDragActionListener.onDragBegin();
				}
			}else if(target == null){
				if(mTarget != null){
					mTarget.onDragOver();
					mTarget = null;
				}
			}
			break;
		}
		case MotionEvent.ACTION_UP:
		{	
			IDragDrop target = findTarget(x, y);
			if(target != null){
				target.onDrop(mData);
			}
			if(mDragActionListener != null){
				mDragActionListener.onDragEnd();
			}
			mShadowPop.dismiss();
			mShadowPop = null;
			break;
		}
		default:
			break;
		}

		return false;
	}

	private IDragDrop findTarget(int x, int y) {
		int LEN = mContainer.getChildCount();
		for (int i = 0; i < LEN; i++) {
			View child = mContainer.getChildAt(i);
			if(x > child.getLeft() && x < child.getRight() && y < child.getBottom() 
					&& y > child.getTop()){
//				Log.d("Touch","in target");
				if(child instanceof IDragDrop){
					// only returned when target is not source
					return (IDragDrop) child == mSource ? null : (IDragDrop) child;
				}
			}
		}
//		Log.d("Touch","not in target");
		return null;
	}

	public interface OnDragAction{
		public void onDragBegin();
		public void onDragEnd();
	}
	
	public static class DragShadowBuilder {
		private final WeakReference<View> mView;

		public DragShadowBuilder(View view) {
			mView = new WeakReference<View>(view);
		}

		public DragShadowBuilder() {
			mView = new WeakReference<View>(null);
		}

		final public View getView() {
			return mView.get();
		}

		public void onProvideShadowMetrics(Point shadowSize,
				Point shadowTouchPoint) {
			final View view = mView.get();
			if (view != null) {
				shadowSize.set(view.getWidth(), view.getHeight());
				shadowTouchPoint.set(shadowSize.x / 2, shadowSize.y / 2);
			} else {
//				Log.e(TAG, "Asked for drag thumb metrics but no view");
			}
		}

		public void onDrawShadow(Canvas canvas) {
			final View view = mView.get();
			if (view != null) {
				view.draw(canvas);
			} else {
//				Log.e(TAG, "Asked to draw drag shadow but no view");
			}
		}
	}
}
