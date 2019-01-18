package ensicaen.fr.marierave.Views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ensicaen.fr.marierave.R;

public class StudentAssessment3 extends Fragment
{
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.student_assessment_3, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        TextView textWelcome =view.findViewById(R.id.skill);
        textWelcome.setText(args.getString("Skill")+ " : BLABLABLBALBLABABLABLABLABLABLABLABLA");
        ImageView imageView = view.findViewById(R.id.back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
				getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }
}
