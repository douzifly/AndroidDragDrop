package douzi.android.dragdrop;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import douzi.android.dragdrop.DragController.OnDragAction;

public class DragDropDemoActivity extends Activity {
	
	ListView listView;
	DragController controller;
	Button target;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ViewGroup container = (ViewGroup) findViewById(R.id.container);
        controller = new DragController(container);
        listView = (ListView) findViewById(R.id.listView);
        target = (Button) findViewById(R.id.button2);
        
        listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
			}
        	
        });
        
        listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				DragableTextView dragView = (DragableTextView) view;
				dragView.startDrag(controller,dragView);
				target.setVisibility(View.VISIBLE);
				return true;
			}
		});
        
        listView.setFadingEdgeLength(0);
        listView.setAdapter(mAdapter);
        
        controller.setOnDragActionListener(new OnDragAction() {
			
			@Override
			public void onDragEnd() {
				//target.setVisibility(View.GONE);
			}
			
			@Override
			public void onDragBegin() {
				
			}
		});
        
        target.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				target.setVisibility(View.GONE);
			}
		});
    }
    
    
    
    BaseAdapter mAdapter = new BaseAdapter() {
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			DragableTextView text = new DragableTextView(getApplicationContext(), null);
			text.setLayoutParams(new AbsListView.LayoutParams(-1, 60));
			text.setText("ListItem :" + position);
			text.setListView(listView);
			text.setGravity(Gravity.CENTER);
			text.setTextColor(Color.BLACK);
			text.setBackgroundColor(0xffcfcfcf);
			return text;
		}
		
		@Override
		public long getItemId(int position) {
			return 0;
		}
		
		@Override
		public Object getItem(int position) {
			return null;
		}
		
		@Override
		public int getCount() {
			return 30;
		}
	};
}