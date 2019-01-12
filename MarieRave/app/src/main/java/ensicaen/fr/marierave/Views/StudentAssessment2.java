package ensicaen.fr.marierave.Views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import ensicaen.fr.marierave.R;
import ensicaen.fr.marierave.Utils;

public class StudentAssessment2 extends Fragment
{
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.student_assessment_2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        TextView textWelcome =view.findViewById(R.id.welcome);
        textWelcome.setText("Bienvenue " + args.getString("Prénom"));
        final AutoCompleteTextView textView = view.findViewById(R.id.skill);
        String[] skills = getResources().getStringArray(R.array.skills);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, skills);
        textView.setAdapter(adapter);

        ImageView imageView = view.findViewById(R.id.back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Bundle bundle = new Bundle();
                Utils.replaceFragments(StudentAssessment1.class, getActivity(), bundle, true);
            }
        });

        ImageView imageView2 = view.findViewById(R.id.group);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Bundle bundle = new Bundle();
                TextView textView = view.findViewById(R.id.skillName);
                bundle.putString("Skill",textView.getText().toString());
                Utils.replaceFragments(StudentAssessment3.class, getActivity(), bundle, true);
            }
        });
    }
}
