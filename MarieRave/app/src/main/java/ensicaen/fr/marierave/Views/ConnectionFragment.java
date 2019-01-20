package ensicaen.fr.marierave.Views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import ensicaen.fr.marierave.Controllers.TeacherDAO;
import ensicaen.fr.marierave.R;
import ensicaen.fr.marierave.Utils;

public class ConnectionFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.connection_fragment, container, false);
    }

    @Override
	public void onViewCreated(@NonNull final View view, Bundle savedInstanceState)
	{
        super.onViewCreated(view, savedInstanceState);
	
		final EditText editLogin = view.findViewById(R.id.textView_user_id);
		final EditText editPassword = view.findViewById(R.id.editText_user_pass);
		
        Button btn_valid = view.findViewById(R.id.btn_valid);
        btn_valid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				if (new TeacherDAO(getContext()).teacherExists(editLogin.getText().toString(), editPassword.getText().toString())) {
		
					Utils.teacherLoggedInId = new TeacherDAO(getContext()).getTeacher(editLogin.getText().toString()).getId();
					Utils.teacherLoggedInLogin = editLogin.getText().toString();
		
					Utils.replaceFragments(GeneralAccFragment.class, getActivity(), null, true);
				}
            }
        });
	
	}
}
