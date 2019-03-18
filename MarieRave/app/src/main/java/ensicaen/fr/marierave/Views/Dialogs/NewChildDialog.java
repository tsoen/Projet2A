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

public class NewChildDialog extends DialogFragment implements android.view.View.OnClickListener
{
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.dialog_new_child, container);
		
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setCancelable(false);
		
		Spinner spinnerClassrooms = view.findViewById(R.id.spinner_classrooms);
		List<Classroom> classrooms = new ClassroomDAO(getContext()).getAllClassrooms();
		ArrayAdapter<Classroom> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, classrooms);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerClassrooms.setAdapter(adapter);
		
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
				EditText edtChildSurname = getDialog().findViewById(R.id.edit_childSurname);
				Spinner spinnerClassrooms = getDialog().findViewById(R.id.spinner_classrooms);
				
				ChildDAO childDAO = new ChildDAO(getContext());
				
				if (!edtChildName.getText().toString().isEmpty() && !edtChildSurname.getText().toString().isEmpty()) {
					childDAO.addChild(new Child(edtChildName.getText().toString(), edtChildSurname.getText().toString(),
							((Classroom) spinnerClassrooms.getSelectedItem())
							.getName()));
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
