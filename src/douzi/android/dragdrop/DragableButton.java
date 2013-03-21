/**
 * @author LiuXiaoyuan
 * @creation_date 2012-6-20
 */
package douzi.android.dragdrop;

import douzi.android.dragdrop.DragController.DragShadowBuilder;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * @author LiuXiaoyuan
 *
 */
public class DragableButton extends Button implements IDragDrop{
	

	/**
	 * @param context
	 * @param attrs
	 */
	public DragableButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	

	@Override
	public void startDrag(DragController controller,Object data) {
		controller.startDrag(this,data, new DragShadowBuilder(this),null);
	}

	@Override
	public void onDragEnter() {
		setText("Enter");
	}

	@Override
	public void onDragOver() {
		setText("Out");
	}

	@Override
	public void onDrop(Object data) {
		DragableTextView text = (DragableTextView) data;
		setText(text.getText());
	}

}
