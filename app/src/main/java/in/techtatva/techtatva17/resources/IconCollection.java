package in.techtatva.techtatva17.resources;

import android.content.Context;

import in.techtatva.techtatva17.R;

/**
 * Created by bennyhawk on 16/9/17.
 */

public class IconCollection {


    private final int acumen = R.drawable.ic_category_acumen;
    private final int airborne = R.drawable.ic_category_airborne;
    private final int alacrity = R.drawable.ic_category_alacrity;
    private final int bizzmaestro = R.drawable.bizzmaestro;
    private final int cheminova = R.drawable.ic_category_cheminova;
    private final int chrysalis = R.drawable.chrysalis;
//    private final int conclave = R.drawable.tt_themanipalconclave;
    private final int constructure = R.drawable.ic_category_constructure;
    private final int cosmiccon = R.drawable.ic_category_cosmiccon;
    private final int cryptoss = R.drawable.ic_category_cryptoss;
    private final int electrific = R.drawable.ic_category_electrific;
    private final int energia = R.drawable.ic_category_energia;
    private final int epsilon = R.drawable.ic_category_epsilon;
    private final int fuelRC = R.drawable.fuel_rc;
    //private final int gaming = R.drawable.gaming;
    private final int kraftwagen = R.drawable.ic_category_kraftwagen;
    private final int mechanize = R.drawable.ic_category_mechanize;
    private final int mechatron = R.drawable.ic_category_mechatron;
//    private final int open = R.drawable.tt_open;
    private final int paper = R.drawable.paper_presentation;
    private final int qi = R.drawable.qi;
    private final int robowars = R.drawable.robowars;
    private final int robotrek = R.drawable.robotrek;
    private final int turing = R.drawable.turing;

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
            case "cosmiccon": return cosmiccon;
            case "cryptoss": return cryptoss;
            case "electrific": return electrific;
            case "energia" : return energia;
            case "epsilon": return epsilon;
            case "featured event-paper presentation": return paper;
            case "fuel rc 5": return fuelRC;
//            case "gaming": return gaming;
            case "kraftwagen": return kraftwagen;
//            case "the manipal conclave": return conclave;
            case "mechanize": return mechanize;
            case "mechatron": return mechatron;
//            case "open category": return open;
            case "questionable intelligence": return qi;
            case "robotrek": return robotrek;
            case "robowars": return robowars;
            case "turing": return turing;
            default: return R.mipmap.ic_launcher;
        }

    }


}
