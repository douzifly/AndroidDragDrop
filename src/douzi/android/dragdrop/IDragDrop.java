/**
 * @author LiuXiaoyuan
 * @creation_date 2012-6-20
 */
package douzi.android.dragdrop;

/**
 * Interface for something which can do drag and drop action
 * @author LiuXiaoyuan
 * 
 */
public interface IDragDrop {
	/** Begin drag action */
	public void startDrag(DragController controller,Object data);
	/** occurred when drag enter */
	public void onDragEnter();
	/** occurred when drag is over */
	public void onDragOver();
	/** occurred when drop on the view */
	public void onDrop(Object data);
	
}
