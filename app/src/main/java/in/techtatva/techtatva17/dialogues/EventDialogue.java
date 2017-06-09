package in.techtatva.techtatva17.dialogues;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.widget.RelativeLayout;

import in.techtatva.techtatva17.R;

public class EventDialogue extends Dialog {
    int width;

    public EventDialogue(@NonNull Context context,int width) {
        super(context);
        this.width=width;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_dialogue);






    }
}
