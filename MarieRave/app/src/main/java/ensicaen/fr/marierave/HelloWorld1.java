package ensicaen.fr.marierave;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class HelloWorld1 extends Fragment
{
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.hello_world_1, container, false);
	}
	
	@Override
	public void onViewCreated(@NonNull final View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		
		Button btnGoTo = view.findViewById(R.id.btnGoTo);
		btnGoTo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Bundle bundle = new Bundle();
				bundle.putString("myParameterText", "I got my text from Hello World 1 !");
				Utils.replaceFragments(HelloWorld2.class, getActivity(), bundle, true);
			}
		});
	}
}
