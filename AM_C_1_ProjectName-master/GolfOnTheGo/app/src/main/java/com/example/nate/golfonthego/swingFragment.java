package com.example.nate.golfonthego;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.FrameLayout;

import com.example.nate.golfonthego.Models.GolfBag;

import java.util.Observable;
import java.util.Observer;

import static android.content.Context.SENSOR_SERVICE;

public class swingFragment extends Fragment implements SensorEventListener, Observer {

    public CourseActivity Course;
    private OnFragmentInteractionListener mListener;    //sensor stuff
    private FrameLayout frame;
    private FragmentManager swingManager;
    private GolfBag bag;
    private int courseCheck = 0;

    private Swinger playerSwing;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Sensor manager and accelerometer
        Sensor accelSensor;
        SensorManager SM;
        SM = (SensorManager) getContext().getSystemService(SENSOR_SERVICE);
        accelSensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SM.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_GAME);
        bag = GolfBag.getBag();
        bag.addObserver(this);

        //TODO switch parameter to be player indicated lefty or righty (where true is lefty)
        playerSwing = Swinger.getSwinger();
        View swingFragView = inflater.inflate(R.layout.fragment_swing, container, false);
        final Button clubBagButton = (Button) swingFragView.findViewById(R.id.changeClub);
        clubBagButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0){
                bag.ShowBag(getContext());
            }
        });
        final Button swingFragButton = (Button) swingFragView.findViewById(R.id.swingFragSwing);
        swingFragButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                swingFragButton.setVisibility(View.INVISIBLE);
                swingFragButton.setVisibility(View.GONE);
                clubBagButton.setVisibility(View.INVISIBLE);
                clubBagButton.setVisibility(View.GONE);
                if (playerSwing.swingTrack == 0) {
                    playerSwing.swingTrack = 1;
                }
            }
        });

        return swingFragView;
    }

    //Leo's accelerometer stuff
    @Override
    public void onSensorChanged(SensorEvent sensorEvent)
    {
        //saving accelerometer values
        if(playerSwing.swingLefty) {
            playerSwing.x = -sensorEvent.values[0];
            playerSwing.y = -sensorEvent.values[1];
            playerSwing.z = -sensorEvent.values[2];
        }
        else{
            playerSwing.x = sensorEvent.values[0];
            playerSwing.y = sensorEvent.values[1];
            playerSwing.z = sensorEvent.values[2];
        }
        playerSwing.swang();

        if(!playerSwing.first){
            playerSwing.first = true;

            //remove fragment, return to courseActivity
            //.done will update observers(Gameplay)
            playerSwing.done();
            frame.removeAllViews();
            Course.setScoreText();
            playerSwing.clearSwing();
            swingManager.beginTransaction().remove(this).commit();
        }
    }

    @Override
    public void update(Observable o, Object n){
        Course.setScoreText();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i)
    {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(courseCheck == 0) {
            Course = (CourseActivity) getActivity();
            frame = Course.findViewById(R.id.swingFrame);
            swingManager = Course.getSupportFragmentManager();
        }
        //saving the frame and swingManager when attached
        System.out.println("Attached");
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;

        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }

    @Override
    public void onDetach() {
        courseCheck ++;
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
