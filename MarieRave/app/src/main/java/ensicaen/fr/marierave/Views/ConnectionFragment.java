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

public class ConnectionFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.connection_fragment, container, false);
    }

    @Override
    public void onViewCreated(@Nullable final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btn_valid = view.findViewById(R.id.btn_valid);
        btn_valid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
               Utils.replaceFragments(GeneralAccFragment.class, getActivity(), bundle, true);
            }
        });

    }
}
