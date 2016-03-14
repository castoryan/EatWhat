package com.example.castoryan.eatwhat_beta3;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by CastorYan on 2014/12/31.
 */
public class Resturant implements Parcelable {
    int _id;
    String name;
    String address;
    String tele_num;
    int grade_my;
    String image;
    double latitute;
    double longtitue;


    public Resturant(int _id,String name,String address,String tele_num,int grade_my,String image,double latitute, double longtitue){
        this._id = _id;
        this.name=name;
        this.address=address;
        this.tele_num=tele_num;
        this.grade_my=grade_my;
        this.image=image;
        this.latitute = latitute;
        this.longtitue = longtitue;
    }

    public Resturant(){

    }
    public int getID(){
        return this._id;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        out.writeInt(_id);
        out.writeString(name);
        out.writeString(address);
        out.writeString(tele_num);
        out.writeInt(grade_my);
        out.writeString(image);
        out.writeDouble(latitute);
        out.writeDouble(longtitue);
    }

    public static final Parcelable.Creator<Resturant> CREATOR = new Creator<Resturant>()
    {
        @Override
        public Resturant[] newArray(int size)
        {
            return new Resturant[size];
        }

        @Override
        public Resturant createFromParcel(Parcel in)
        {
            return new Resturant(in);
        }
    };
    public Resturant(Parcel in){
        name = in.readString();
        address = in.readString();
        _id = in.readInt();
        tele_num = in.readString();
        grade_my = in.readInt();
        image = in.readString();
        latitute = in.readDouble();
        longtitue = in.readDouble();
    }
}
