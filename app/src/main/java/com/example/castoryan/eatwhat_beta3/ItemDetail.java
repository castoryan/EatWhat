package com.example.castoryan.eatwhat_beta3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Network;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;


public class ItemDetail extends Fragment {
    private static int RESULT_LOAD_IMAGE = 1;
    int currentResID;
    TextView tx_name;
    TextView tx_addr;
    TextView tx_num;
    TextView tx_gps;
    double res_lat;
    double res_lon;
    Resturant theRes;
    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);


        return rootView;


    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //return the result of selecting image
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == getActivity().RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            DataBaseHandler dbhand = new DataBaseHandler(getActivity().getApplicationContext());
            dbhand.updateRestaurantImage(picturePath,theRes.name);

            ImageView imageView = (ImageView) getActivity().findViewById(R.id.imgView);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }
    }
    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);


        tx_name =(TextView)rootView.findViewById(R.id.name_dis);
        tx_addr =(TextView)rootView.findViewById(R.id.addr_dis);
        tx_num =(TextView)rootView.findViewById(R.id.num_dis);
        tx_gps =(TextView)rootView.findViewById(R.id.gps_dis);
        Bundle bundle1 = getArguments();
        theRes = bundle1.getParcelable("key");
        currentResID=theRes._id;
        tx_name.setText(theRes.name);
        tx_addr.setText(theRes.address);
        tx_num.setText(theRes.tele_num);
        tx_gps.setText(String.valueOf(theRes.latitute)+"  "+String.valueOf(theRes.longtitue));

        res_lat = theRes.latitute;
        res_lon = theRes.longtitue;

        if(theRes.image != null){
            String image_path = theRes.image;
            ImageView imageView = (ImageView) getActivity().findViewById(R.id.imgView);
            imageView.setImageBitmap(BitmapFactory.decodeFile(image_path));}
        else{

        }

        View imageView = getActivity().findViewById(R.id.imgView);
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

/*        tx_gps.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean  onLongClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(),
                        "尼玛", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        tx_gps.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(),
                       "哈哈哈哈哈", Toast.LENGTH_SHORT).show();
            }
        });*/


        // BUTTON GPS POSITION

        Button btn_gps = (Button)getActivity().findViewById(R.id.btn_gps);
        btn_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    Fragment newFra = new MyMapFragment();

                    Bundle bundle = new Bundle();
                    //bundle.putString("alt", String.valueOf(mylocation.getAltitude()));
                    bundle.putString("lat", String.valueOf(res_lat));
                    bundle.putString("lon", String.valueOf(res_lon));

                    newFra.setArguments(bundle);
                    ft.replace(R.id.container, newFra);
                    ft.addToBackStack(null);
                    ft.commit();
            }
        });

    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onDetach(){
        super.onDetach();
    }


}