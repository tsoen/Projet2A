package ensicaen.fr.marierave.Views.Dialogs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

import ensicaen.fr.marierave.Controllers.ChildDAO;
import ensicaen.fr.marierave.Controllers.ClassroomDAO;
import ensicaen.fr.marierave.Model.Child;
import ensicaen.fr.marierave.Model.Classroom;
import ensicaen.fr.marierave.R;
import ensicaen.fr.marierave.Views.AdministrationChilds;

public class NewOrUpdateChildDialog extends DialogFragment implements android.view.View.OnClickListener
{
	private String mode;
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.dialog_new_child, container);
		
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setCancelable(false);
		
		mode = getArguments().getString("mode");
		
		Spinner spinnerClassrooms = view.findViewById(R.id.spinner_classrooms);
		List<Classroom> classrooms = new ClassroomDAO(getContext()).getAllClassrooms();
		ArrayAdapter<Classroom> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, classrooms);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerClassrooms.setAdapter(adapter);
		
		if (mode.equals("edit")) {
			ChildDAO childDAO = new ChildDAO(getContext());
			Child child = childDAO.getChild(getArguments().getInt("childId"));
			
			EditText edtChildName = view.findViewById(R.id.edit_childName);
			edtChildName.setText(child.getName());
			
			EditText edtChildSurname = view.findViewById(R.id.edit_childSurname);
			edtChildSurname.setText(child.getFirstname());
			
			spinnerClassrooms.setSelection(adapter.getPosition(new ClassroomDAO(getContext()).getClassroom(child.getClassroom())));
		}
		
		Button btnValidate = view.findViewById(R.id.btn_validate);
		btnValidate.setOnClickListener(this);
		
		Button btnCancel = view.findViewById(R.id.btn_cancel);
		btnCancel.setOnClickListener(this);
		
		return view;
	}
	
	@Override
	public void onClick(View v)
	{
		switch (v.getId()) {
			case R.id.btn_validate:
				EditText edtChildName = getDialog().findViewById(R.id.edit_childName);
				EditText edtChildFirstname = getDialog().findViewById(R.id.edit_childSurname);
				Spinner spinnerClassrooms = getDialog().findViewById(R.id.spinner_classrooms);
				
				ChildDAO childDAO = new ChildDAO(getContext());

				if (!edtChildName.getText().toString().isEmpty() && !edtChildFirstname.getText().toString().isEmpty() && spinnerClassrooms.getSelectedItem() != null) {
					if (mode.equals("edit")) {
						Child child = childDAO.getChild(getArguments().getInt("childId"));
						child.setName(edtChildName.getText().toString());
						child.setFirstname(edtChildFirstname.getText().toString());
						child.setClassroom(((Classroom) spinnerClassrooms.getSelectedItem()).getName());
						
						childDAO.updateChild(child);
					}
					else if (mode.equals("new")) {
						childDAO.addChild(new Child(edtChildName.getText().toString(), edtChildFirstname.getText().toString(), ((Classroom) spinnerClassrooms
								.getSelectedItem()).getName()));
					}
				}
				
				((AdministrationChilds) getTargetFragment()).reloadChildtListView();
				
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
}
