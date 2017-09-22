package in.techtatva.techtatva17.resources;

import android.content.Context;
import android.util.Log;

import in.techtatva.techtatva17.R;

/**
 * Created by bennyhawk on 16/9/17.
 */

public class IconCollection {


    private final int acumen = R.drawable.acumen;
    private final int airborne = R.drawable.airborne;
    private final int alacrity = R.drawable.alacrity;
    private final int bizzmaestro = R.drawable.bizzmaestro;
    private final int cheminova = R.drawable.cheminova;
    private final int chrysalis = R.drawable.chrysalis;
//    private final int conclave = R.drawable.tt_themanipalconclave;
    private final int constructure = R.drawable.constructure;
    private final int cosmiccon = R.drawable.cosmiccon;
    private final int cryptoss = R.drawable.cryptoss;
    private final int electrific = R.drawable.electrific;
    private final int energia = R.drawable.energia;
    private final int epsilon = R.drawable.epsilon;
    private final int fuelRC = R.drawable.fuel_rc;
    private final int gaming = R.drawable.gaming;
    private final int kraftwagen = R.drawable.kraftwagen;
    private final int mechanize = R.drawable.mechanize;
    private final int mechatron = R.drawable.mechatron;
//    private final int open = R.drawable.tt_open;
    private final int paper = R.drawable.paper_presentation;
    private final int qi = R.drawable.qi;
    private final int robowars = R.drawable.robowars;
    private final int robotrek = R.drawable.robotrek;
    private final int turing = R.drawable.turing;
    private final int vedanth = R.drawable.vedanth;
    String TAG = "IconCollection";


    public IconCollection() {
    }

    public int getIconResource(Context context, String catName){
        switch(catName.toLowerCase()){
            case "acumen": return acumen;
            case "alacrity": return alacrity;
            case "airborne": return airborne;
            case "bizzmaestro": return bizzmaestro;
            case "cheminova": return cheminova;
            case "chrysalis": return chrysalis;
            case "constructure": return constructure;
            case "cosmic con": return cosmiccon;
            case "cryptoss": return cryptoss;
            case "electrific": return electrific;
            case "energia" : return energia;
            case "epsilon": return epsilon;
            case "paper presentation": return paper;
            case "fuel rc": return fuelRC;
            case "gaming": return gaming;
            case "kraftwagen ": return kraftwagen;
//            case "the manipal conclave": return conclave;
            case "mechanize": return mechanize;
            case "mechatron": return mechatron;
//            case "open category": return open;
            case "questionable intelligence": return qi;
            case "robotrek": return robotrek;
            case "robowars": return robowars;
            case "turing": {Log.i(TAG,"Turing case called"); return turing;}
            case "vedanth 7.0":{
                Log.i(TAG,"Vedanth case called"); return vedanth;}
            default: {
                Log.i(TAG,catName);
                return R.mipmap.ic_launcher;}
        }

    }


}
