package ensicaen.fr.marierave;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;


public class StudentAssessment1 extends Fragment
{
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.student_assessment_1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        final AutoCompleteTextView textView = view.findViewById(R.id.student_name);
        String[] names = getResources().getStringArray(R.array.student_names);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, names);
        textView.setAdapter(adapter);
        Object item = adapter.getItem(0);
        TextView prénom1 = view.findViewById(R.id.prénom1);
        prénom1.setText(item.toString());
        item = adapter.getItem(1);
        TextView prénom2 = view.findViewById(R.id.prénom2);
        prénom2.setText(item.toString());
        item = adapter.getItem(2);
        TextView prénom3 = view.findViewById(R.id.prénom3);
        prénom3.setText(item.toString());
        ImageView imageView = view.findViewById(R.id.prénom1_img);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Bundle bundle = new Bundle();
                TextView textView = view.findViewById(R.id.prénom1);
                bundle.putString("Prénom",textView.getText().toString());
                Utils.replaceFragments(StudentAssessment2.class, getActivity(), bundle, true);
            }
        });
        ImageView imageView2 = view.findViewById(R.id.prénom2_img);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Bundle bundle = new Bundle();
                TextView textView = view.findViewById(R.id.prénom2);
                bundle.putString("Prénom",textView.getText().toString());
                Utils.replaceFragments(StudentAssessment2.class, getActivity(), bundle, true);
            }
        });
        ImageView imageView3 = view.findViewById(R.id.prénom3_img);
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Bundle bundle = new Bundle();
                TextView textView = view.findViewById(R.id.prénom3);
                bundle.putString("Prénom",textView.getText().toString());
                Utils.replaceFragments(StudentAssessment2.class, getActivity(), bundle, true);
            }
        });

    }
}
