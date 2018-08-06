package app.pictograma.com.activity;

/**
 * Created by darioalarcon on 13/5/17.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import app.pictograma.com.R;
import app.pictograma.com.config.Constants;


public class ImagenSlidePageFragment extends Fragment {


    /**
     * Key to insert the background color into the mapping of a Bundle.
     */
    private static final String BACKGROUND_IMAGEN = "IMAGEN1";


    /**
     * Key to insert the index page into the mapping of a Bundle.
     */
    private static final String INDEX = "index";

    private String imagen1;

    private int index;

    /**
     * Instances a new fragment with a background color and an index page.
     *
     * @param img1
     *            background color
     * @param index
     *            index page
     * @return a new page
     */
    public static ImagenSlidePageFragment newInstance(String img1, int index) {

        // Instantiate a new fragment
        ImagenSlidePageFragment fragment = new ImagenSlidePageFragment();

        // Save the parameters
        Bundle bundle = new Bundle();
        bundle.putString(BACKGROUND_IMAGEN, img1);
        bundle.putInt(INDEX, index);



        fragment.setArguments(bundle);
        fragment.setRetainInstance(true);

        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Load parameters when the initial creation of the fragment is done
        this.imagen1 = getArguments().getString(BACKGROUND_IMAGEN);

        this.index = (getArguments() != null) ? getArguments().getInt(INDEX)
                : -1;
        this.getArguments().clear();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_imagen_slide_page, container, false);


        ImageView image1=(ImageView) rootView.findViewById(R.id.imagen1);
        image1.setImageBitmap(Constants.decodeBase64(this.imagen1));


        return rootView;

    }
}
