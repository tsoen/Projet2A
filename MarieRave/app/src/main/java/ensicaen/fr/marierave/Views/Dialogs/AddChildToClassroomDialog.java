package ensicaen.fr.marierave.Views.Dialogs;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
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
import ensicaen.fr.marierave.Views.AdministrationClassroom;

public class AddChildToClassroomDialog extends DialogFragment implements View.OnClickListener
{
	private String _classroomName;
	private String _mode;
	private GridView _childsGridview;
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		final View view = inflater.inflate(R.layout.dialog_add_child_to_classroom, container);
		
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setCancelable(false);
		
		_mode = getArguments().getString("mode");
		_classroomName = getArguments().getString("classroomName");
		
		Button btnValidate = view.findViewById(R.id.btn_validate);
		btnValidate.setOnClickListener(this);
		
		Button btnCancel = view.findViewById(R.id.btn_cancel);
		btnCancel.setOnClickListener(this);
		
		List<Child> childList = new ArrayList<>();
		if (_mode.equals("Add")) {
			childList = new ChildDAO(getContext()).getAllChildsNotInClassroom(_classroomName);
		}
		else if (_mode.equals("Delete")) {
			childList = new ChildDAO(getContext()).getAllChildsInClassroom(_classroomName);
		}
		
		final GridViewAdapter adapter = new GridViewAdapter(getActivity(), childList);
		_childsGridview = view.findViewById(R.id.gridview_childsToAdd);
		_childsGridview.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		
		_childsGridview.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id)
			{
				if (!adapter._selectedPositions.contains(position)) {
					adapter._selectedPositions.add(position);
				}
				else {
					adapter._selectedPositions.remove(Integer.valueOf(position));
				}
				
				TextView txtNumberOfSelected = view.findViewById(R.id.textView14);
				txtNumberOfSelected.setText(Integer.toString(adapter._selectedPositions.size()));
				
				adapter.notifyDataSetChanged();
			}
		});
		
		CheckBox cboSelectAll = view.findViewById(R.id.checkBox);
		cboSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if (isChecked) {
					for (int i = 0; i < _childsGridview.getChildCount(); i++) {
						if (!adapter._selectedPositions.contains(i)) {
							adapter._selectedPositions.add(i);
						}
					}
				}
				else {
					for (int i = 0; i < _childsGridview.getChildCount(); i++) {
						adapter._selectedPositions.remove(Integer.valueOf(i));
					}
				}
				
				TextView txtNumberOfSelected = view.findViewById(R.id.textView14);
				txtNumberOfSelected.setText(Integer.toString(adapter._selectedPositions.size()));
				adapter.notifyDataSetChanged();
			}
		});
		
		
		return view;
	}
	
	@Override
	public void onClick(View v)
	{
		switch (v.getId()) {
			case R.id.btn_validate:
				
				ChildDAO childDAO = new ChildDAO(getContext());
				for (int i : ((GridViewAdapter) _childsGridview.getAdapter())._selectedPositions) {
					Child c = (Child) _childsGridview.getAdapter().getItem(i);
					if (_mode.equals("Add")) {
						c.setClassroom(_classroomName);
					}
					else if (_mode.equals("Delete")) {
						c.setClassroom("Ecole");
					}
					
					childDAO.updateChild(c);
				}
				
				((AdministrationClassroom) getTargetFragment()).reloadChildsGridview();
				
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
		private Activity _activity;
		private List<Child> _childList;
		private List<Integer> _selectedPositions = new ArrayList<>();
		
		GridViewAdapter(Activity activity, List<Child> childList)
		{
			super();
			_activity = activity;
			_childList = childList;
		}
		
		@Override
		public int getCount()
		{
			return _childList.size();
		}
		
		@Override
		public Object getItem(int position)
		{
			return _childList.get(position);
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
				holder._txtName.setText(_childList.get(position).getName());
				holder._txtSurname.setText(_childList.get(position).getFirstname());
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