package in.techtatva.techtatva17.fragments;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import in.techtatva.techtatva17.R;


public class AboutUsFragment extends Fragment {
    String URL_SNAPCHAT  = "http://www.snapchat.com/add/techtatva";
    String URL_TWITTER  = "http://www.twitter.com/MITtechtatva";
    String URL_FB  = "http://www.facebook.com/MITtechtatva";
    String URL_INSTA  = "http://www.instagram.com/MITtechtatva";
    ImageView instaIV, fbIV, snapchatIV, twitterIV;
    Context context;
    String TAG = "AboutUsFragment";
    public AboutUsFragment() {
        // Required empty public constructor
    }

    public static AboutUsFragment newInstance() {
        AboutUsFragment fragment = new AboutUsFragment();
        return fragment;
    }

        @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);
        instaIV = (ImageView) view.findViewById(R.id.insta_image);
        fbIV = (ImageView)view.findViewById(R.id.fb_image);
        twitterIV = (ImageView)view.findViewById(R.id.twitter_image);
        snapchatIV = (ImageView)view.findViewById(R.id.snapchat_image);
        context = container.getContext();
        instaIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchURL(URL_INSTA);
            }
        });
        fbIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchURL(URL_FB);
            }
        });
        snapchatIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchURL(URL_SNAPCHAT);
            }
        });
        twitterIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchURL(URL_TWITTER);
            }
        });
        return view;
    }
    public void launchURL(String URL){
        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
            context.startActivity(browserIntent);
        }catch(ActivityNotFoundException e2){
            Log.e(TAG, e2.getMessage()+"\n Perhaps user does not have a Browser installed ");
        }
    }
}
