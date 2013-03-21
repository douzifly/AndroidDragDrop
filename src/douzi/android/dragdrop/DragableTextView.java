/**
 * @author LiuXiaoyuan
 * @creation_date 2012-6-20
 */
package douzi.android.dragdrop;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
import android.widget.TextView;
import douzi.android.dragdrop.DragController.DragShadowBuilder;

/**
 * @author LiuXiaoyuan
 *
 */
public class DragableTextView extends TextView implements IDragDrop{

	/**
	 * @param context
	 * @param attrs
	 */
	public DragableTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	private ListView lst;
	public void setListView(ListView lstView){
		lst = lstView;
	}

	@Override
	public void startDrag(DragController controller,Object data) {
		controller.startDrag(this, data,new DragShadowBuilder(this),lst);
	}

	@Override
	public void onDragEnter() {
	}

	@Override
	public void onDragOver() {
	}

	@Override
	public void onDrop(Object data) {
	}

}
