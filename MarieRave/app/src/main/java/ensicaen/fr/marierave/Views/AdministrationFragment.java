package ensicaen.fr.marierave.Views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import ensicaen.fr.marierave.R;
import ensicaen.fr.marierave.Utils;

public class AdministrationFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        getClasses(inflater);
        return inflater.inflate(R.layout.administration_fragment, container, false);
    }

    @Override
    public void onViewCreated(@Nullable final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton btn_exit = view.findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }
        });

        Button btn_admin = view.findViewById(R.id.btn_Admin);
        btn_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Utils.replaceFragments(AdministrationHome.class, getActivity(), bundle, true);
            }
        });
    
        Button btnCP = view.findViewById(R.id.btnCP);
		btnCP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Utils.replaceFragments(HomeClassroom.class, getActivity(), bundle, true);
            }
        });
    
        Button btnCE1 = view.findViewById(R.id.btnCE1);
		btnCE1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Utils.replaceFragments(HomeClassroom.class, getActivity(), bundle, true);
            }
        });
        
        
    }

    private void getClasses(LayoutInflater inflater) {
        List<String> classes = new ArrayList<>();
        ListIterator lit = classes.listIterator();

        /*while(lit.hasNext()) {

        }*/

    }
}
