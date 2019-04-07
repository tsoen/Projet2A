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
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ensicaen.fr.marierave.Controllers.ChildDAO;
import ensicaen.fr.marierave.Controllers.SkillDAO;
import ensicaen.fr.marierave.Controllers.SkillMarkDAO;
import ensicaen.fr.marierave.Model.Child;
import ensicaen.fr.marierave.R;

public class MarkMultipleChildsDialog extends DialogFragment implements android.view.View.OnClickListener
{
	private ListView _childListview;
	
	private String _skillCode;
	private String _classroomName;
    private String _selectedMark = "";
	private ListviewChildsAdapter childsAdapter;
    private CheckBox cboSelectAll;
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.dialog_mark_multiple_childs, container);
		
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setCancelable(false);
		
		_skillCode = getArguments().getString("Skill");
		_classroomName = getArguments().getString("ClassroomName");
		
		Button btnValidate = view.findViewById(R.id.button16);
		btnValidate.setOnClickListener(this);
		
		Button btnCancel = view.findViewById(R.id.button15);
		btnCancel.setOnClickListener(this);

		TextView skillName = view.findViewById(R.id.textView38);
		skillName.setText(new SkillDAO(getContext()).getSkill(_skillCode).getName());
		
		_childListview = view.findViewById(R.id.listChildsClassMarks);
		reloadChildListview();

		RadioGroup radioGroup = view.findViewById(R.id.radio_group_assess);
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
					case R.id.darkGreen:
						_selectedMark = "A";
						break;
					case R.id.green:
						_selectedMark = "B";
						break;
					case R.id.yellow:
						_selectedMark = "C";
						break;
					case R.id.red:
						_selectedMark = "D";
						break;
					default:
						_selectedMark = "";
						break;
				}
			}
		});

        cboSelectAll = view.findViewById(R.id.checkBox2);
		cboSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
                    for (int i = 0; i < childsAdapter._childList.size(); i++) {
						if (!childsAdapter._selectedPositions.contains(i)) {
							childsAdapter._selectedPositions.add(i);
						}
					}
				} else {
					for (int i = 0; i < _childListview.getChildCount(); i++) {
						childsAdapter._selectedPositions.remove(Integer.valueOf(i));
					}
				}

				childsAdapter.notifyDataSetChanged();
			}
		});
		
		return view;
	}

    @Override
    public void onResume() {
        super.onResume();
		WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
		getDialog().getWindow().setAttributes(params);
    }
	
	public void reloadChildListview()
	{
		List<Child> childList = new ChildDAO(getContext()).getAllChildsInClassroom(_classroomName);
		childsAdapter = new ListviewChildsAdapter(getActivity(), childList);
		_childListview.setAdapter(childsAdapter);
	}
	
	@Override
	public void onClick(View v)
	{
		switch (v.getId()) {
            case R.id.button16:
                for (int i = 0; i < childsAdapter._childList.size(); i++) {
                    if (childsAdapter._selectedPositions.contains(i)) {
                        SkillMarkDAO skillMarkDAO = new SkillMarkDAO(getContext());

                        if (skillMarkDAO.skillMarkExists(childsAdapter._childList.get(i).getId(), _skillCode)) {
                            skillMarkDAO.updateSkillMark(childsAdapter._childList.get(i).getId(), _skillCode, _selectedMark);
                        } else {
                            skillMarkDAO.addSkillMark(childsAdapter._childList.get(i).getId(), _skillCode, _selectedMark);
                        }
                    }
                }

                reloadChildListview();
                cboSelectAll.setChecked(false);
				break;

            case R.id.button15:
				dismiss();
				break;
			default:
				dismiss();
				break;
		}
	}
	
	private class ListviewChildsAdapter extends BaseAdapter
	{
		private List<Child> _childList;
		private Activity _activity;
		private List<Integer> _selectedPositions = new ArrayList<>();
		
		ListviewChildsAdapter(Activity activity, List<Child> childList)
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
			return _childList.get(position).getId();
		}
		
		private class ViewHolder
		{
			private CheckBox _box;
			private TextView _txtName;
			private TextView _txtSurname;
			private TextView _txtMark;
		}
		
		@Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

			if (convertView == null) {
				convertView = _activity.getLayoutInflater().inflate(R.layout.listview_skill_assesment_dialog_item, null);
                holder = new ViewHolder();

				holder._box = convertView.findViewById(R.id.checkBox3);
				holder._txtName = convertView.findViewById(R.id.textView39);
				holder._txtSurname = convertView.findViewById(R.id.textView40);
				holder._txtMark = convertView.findViewById(R.id.textView41);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
			
			Child child = _childList.get(position);
			
			holder._txtName.setText(child.getName());
			holder._txtSurname.setText(child.getFirstname());
			
			switch (new SkillMarkDAO(getContext()).getSkillMark(child.getId(), _skillCode)) {
				case "A":
					holder._txtMark.setBackgroundColor(Color.parseColor("#088A08"));
					break;

				case "B":
					holder._txtMark.setBackgroundColor(Color.parseColor("#00FF40"));
					break;

				case "C":
					holder._txtMark.setBackgroundColor(Color.parseColor("#FFD500"));
					break;

				case "D":
					holder._txtMark.setBackgroundColor(Color.parseColor("#FF0000"));
					break;

				default:
					holder._txtMark.setBackgroundColor(0x00000000);
					break;
			}

            holder._box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (!childsAdapter._selectedPositions.contains(position)) {
                            childsAdapter._selectedPositions.add(position);
                        }
                    } else {
                        if (childsAdapter._selectedPositions.contains(position)) {
                            childsAdapter._selectedPositions.remove(Integer.valueOf(position));
                        }
                    }
                }
            });

            if (_selectedPositions.contains(position)) {
                holder._box.setChecked(true);
            } else {
                holder._box.setChecked(false);
            }

			return convertView;
		}
	}
}