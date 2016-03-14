package com.example.castoryan.eatwhat_beta3;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class MyMainFragment extends Fragment {
    private ListView DB_list;
    private View rootView;

    DataBaseHandler dbhand;
    TextView v2;
    String del_name;


    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbhand = new DataBaseHandler(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_my_main, container, false);
        DB_list = new ListView(getActivity());
        DB_list=(ListView)rootView.findViewById(R.id.list_main);//Get ListView from layout
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){

        super.onActivityCreated(savedInstanceState);
        Display();
        //The Item Long Click listener of ListView
        DB_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                v2 = (TextView)view;
                del_name = v2.getText().toString();

                Dialog alertDialog = new AlertDialog.Builder(getActivity()).
                        setTitle("Sure to delete？").
                        setMessage("Are you sure to delete this resturant？").
                        setIcon(R.drawable.ic_launcher).
                        setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getActivity().getApplicationContext(),"you are pressing ", Toast.LENGTH_SHORT).show();
                                dbhand.deleteResturantByName(del_name);
                                Display();
                            }
                        }).
                        setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                            }
                        }).
                        create();
                alertDialog.show();
                return true;
            }
        });

        //The Item Click listener of ListView
        DB_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Resturant sele_res;

                TextView vv = (TextView)view;
                String idname = vv.getText().toString();
                sele_res = dbhand.getResturantByName(idname);

                String aaa = ""+sele_res._id+" "+sele_res.name;
                CharSequence aaaa = aaa;
                // When clicked, show a toast with the TextView text
                Toast.makeText(getActivity().getApplicationContext(),
                        aaaa, Toast.LENGTH_SHORT).show();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                Fragment newFra = new ItemDetail();

                //meassage transport
                Bundle bundle = new Bundle();
                bundle.putParcelable("key", sele_res);
                newFra.setArguments(bundle);

                ft.replace(R.id.container, newFra);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public  void onResume(){
        Display();
        super.onResume();
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




    //Method of display all elements in database on the screen
    private void Display(){
        List<Resturant> list_res= new ArrayList<Resturant>();
        list_res = dbhand.getAllResturants();
        List<String> list = new ArrayList<String>();
        for(Resturant res_tem:list_res){
            list.add(res_tem.name);
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,list);
        DB_list.setAdapter(adapter);
    }

}
