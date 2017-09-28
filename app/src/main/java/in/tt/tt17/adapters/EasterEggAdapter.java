package in.tt.tt17.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import in.tt.tt17.R;

/**
 * Created by bennyhawk on 27/9/17.
 */

public class EasterEggAdapter extends RecyclerView.Adapter<EasterEggAdapter.EasterEggViewHolder> {

    private Context context;
    private String [] eggList;

    public EasterEggAdapter(String [] eggList){
        this.eggList = eggList;
    }

    @Override
    public EasterEggAdapter.EasterEggViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new EasterEggViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_egg, parent, false));
    }

    @Override
    public void onBindViewHolder(EasterEggAdapter.EasterEggViewHolder holder, int position) {
        final String link = eggList[position];
        holder.egg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayImagePopup(link);
            }
        });
        Picasso.with(context).load(link).into(holder.egg);

    }

    @Override
    public int getItemCount() {
        return eggList.length;
    }

    public class EasterEggViewHolder extends RecyclerView.ViewHolder {
        public ImageView egg;

        public EasterEggViewHolder(final View itemView) {
            super(itemView);
            egg = (ImageView) itemView.findViewById(R.id.image_egg);
            egg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }

    }
    private void displayImagePopup(String link){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setPositiveButton("lol", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Log.i("EasterEgg ", "lol button Clicked!");
//                dialog.dismiss();
//            }
//        });
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialoglayout = inflater.inflate(R.layout.dialog_meme_enlarged, null);
        ImageView enlargedMeme = (ImageView) dialoglayout.findViewById(R.id.enlarged_meme);
        Picasso.with(context).load(link).into(enlargedMeme);
        builder.setView(dialoglayout);
        builder.show();
    }
}
