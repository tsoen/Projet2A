package ensicaen.fr.marierave.Views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ensicaen.fr.marierave.R;
import ensicaen.fr.marierave.Utils;

public class GeneralAccFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.general_acc_fragment, container, false);
    }

    @Override
	public void onViewCreated(@NonNull final View view, Bundle savedInstanceState)
	{
        super.onViewCreated(view, savedInstanceState);
	
		Button btn_enseignant = view.findViewById(R.id.btn_ens);
        btn_enseignant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
				Utils.replaceFragments(TeacherHome.class, getActivity(), bundle, true);
            }
        });

        Button btn_eval = view.findViewById(R.id.btn_eval);
        btn_eval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Utils.replaceFragments(StudentAssessment1.class, getActivity(), bundle, true);
            }
        });

    }
}
