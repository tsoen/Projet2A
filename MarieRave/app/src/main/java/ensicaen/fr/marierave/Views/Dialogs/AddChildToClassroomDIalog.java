package ensicaen.fr.marierave.Views.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ensicaen.fr.marierave.Controllers.ChildDAO;
import ensicaen.fr.marierave.Model.Child;
import ensicaen.fr.marierave.R;

public class AddChildToClassroomDIalog extends Dialog implements android.view.View.OnClickListener
{
	
	private Activity _activity;
	private String _classroomName;
	private String _mode;
	private GridView gridview;
	
	public AddChildToClassroomDIalog(Activity a, String classroomName, String mode)
	{
		super(a);
		_activity = a;
		_classroomName = classroomName;
		_mode = mode;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCancelable(false);
		
		setContentView(R.layout.dialog_add_child_to_classroom);
		
		Button btnValidate = findViewById(R.id.btn_validate);
		btnValidate.setOnClickListener(this);
		
		Button btnCancel = findViewById(R.id.btn_cancel);
		btnCancel.setOnClickListener(this);
		
		List<Child> subjectList = new ArrayList<>();
		if (_mode.equals("Add")) {
			subjectList = new ChildDAO(_activity).getAllChildsNotInClassroom(_classroomName);
		}
		else if (_mode.equals("Delete")) {
			subjectList = new ChildDAO(_activity).getAllChildsInClassroom(_classroomName);
		}
		
		final GridViewAdapter adapter = new GridViewAdapter(_activity, subjectList);
		gridview = findViewById(R.id.gridview_childsToAdd);
		gridview.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		
		gridview.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				if (!adapter._selectedPositions.contains(position)) {
					adapter._selectedPositions.add(position);
				}
				else {
					adapter._selectedPositions.remove(Integer.valueOf(position));
				}
				
				TextView txtNumberOfSelected = findViewById(R.id.textView14);
				txtNumberOfSelected.setText(Integer.toString(adapter._selectedPositions.size()));
				
				adapter.notifyDataSetChanged();
			}
		});
		
		CheckBox cboSelectAll = findViewById(R.id.checkBox);
		cboSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if (isChecked) {
					for (int i = 0; i < gridview.getChildCount(); i++) {
						if (!adapter._selectedPositions.contains(i)) {
							adapter._selectedPositions.add(i);
						}
					}
				}
				else {
					for (int i = 0; i < gridview.getChildCount(); i++) {
						adapter._selectedPositions.remove(Integer.valueOf(i));
					}
				}
				
				TextView txtNumberOfSelected = findViewById(R.id.textView14);
				txtNumberOfSelected.setText(Integer.toString(adapter._selectedPositions.size()));
				adapter.notifyDataSetChanged();
			}
		});
	}
	
	@Override
	public void onClick(View v)
	{
		switch (v.getId()) {
			case R.id.btn_validate:
				
				ChildDAO childDAO = new ChildDAO(getContext());
				
				for (int i : ((GridViewAdapter) gridview.getAdapter())._selectedPositions) {
					Child c = (Child) gridview.getAdapter().getItem(i);
					if (_mode.equals("Add")) {
						c.setClassroom(_classroomName);
					}
					else if (_mode.equals("Delete")) {
						c.setClassroom("Ecole");
					}
					
					childDAO.updateChild(c);
				}
				
				dismiss();
				break;
			
			case R.id.btn_cancel:
				dismiss();
				break;
			default:
				dismiss();
				break;
		}
	}
	
	private class GridViewAdapter extends BaseAdapter
	{
		private List<Child> _productList;
		private Activity _activity;
		private List<Integer> _selectedPositions = new ArrayList<>();
		
		GridViewAdapter(Activity activity, List<Child> productList)
		{
			super();
			_activity = activity;
			_productList = productList;
		}
		
		@Override
		public int getCount()
		{
			return _productList.size();
		}
		
		@Override
		public Object getItem(int position)
		{
			return _productList.get(position);
		}
		
		@Override
		public long getItemId(int position)
		{
			return position;
		}
		
		private class ViewHolder
		{
			private ImageView _profilePic;
			private TextView _txtName;
			private TextView _txtSurname;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			if (convertView == null) {
				
				
				convertView = _activity.getLayoutInflater().inflate(R.layout.gridview_classroom_item, null);
				ViewHolder holder = new ViewHolder();
				holder._profilePic = convertView.findViewById(R.id.imgProfilePicture);
				holder._txtName = convertView.findViewById(R.id.txtName);
				holder._txtSurname = convertView.findViewById(R.id.txtSurname);
				
				holder._profilePic.setImageResource(R.mipmap.ic_launcher_round);
				holder._txtName.setText(_productList.get(position).getName());
				holder._txtSurname.setText(_productList.get(position).getFirstname());
			}
			
			if (_selectedPositions.contains(position)) {
				convertView.setBackgroundColor(Color.GRAY);
			}
			else {
				convertView.setBackgroundColor(Color.TRANSPARENT);
			}
			
			return convertView;
		}
	}
}