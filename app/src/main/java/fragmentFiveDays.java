import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sematec.weather.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class fragmentFiveDays extends Fragment {
   /* ArrayList<com.sematec.weather.myWeatherClass> lstWeather;
    fragmentFiveDays(ArrayList<com.sematec.weather.myWeatherClass> lst){
        lstWeather=lst;
    }*/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_fivedays,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
       /* if ((lstWeather!=null)&&(lstWeather.size()>0)) {
            TextView txtday1 = view.findViewById(R.id.txtday1);
            txtday1.setText(lstWeather.get(1).getDay());

            ImageView imgStatus1 = view.findViewById(R.id.imgStatus1);
            Picasso.get().load("http://openweathermap.org/img/wn/" + lstWeather.get(1).getIcon() + "@2x.png").into(imgStatus1);

            TextView txtmin1 = view.findViewById(R.id.txtmin1);
            txtday1.setText(lstWeather.get(1).getTempMin());

            TextView txtmax1 = view.findViewById(R.id.txtmax1);
            txtday1.setText(lstWeather.get(1).getTempMax());
        }*/

    }
}
