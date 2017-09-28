package in.tt.tt17.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.tt.tt17.R;
import in.tt.tt17.models.result.ResultModel;

/**
 * Created by Sapta on 6/16/2017.
 */

public class QualifiedTeamsAdapter extends RecyclerView.Adapter<QualifiedTeamsAdapter.QualifiedTeamViewHolder> {
    private List<ResultModel> resultsList;
    private Context context;

    public QualifiedTeamsAdapter(List<ResultModel> resultsList, Context context) {
        this.resultsList = resultsList;
        this.context = context;
    }
    @Override
    public QualifiedTeamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new QualifiedTeamViewHolder(LayoutInflater.from(context).inflate(R.layout.item_qualified_teams, parent, false));
    }
    @Override
    public void onBindViewHolder(QualifiedTeamViewHolder holder, int position) {
        ResultModel result = resultsList.get(position);
        holder.teamID.setText(result.getTeamID());
    }
    @Override
    public int getItemCount() {
        return resultsList.size();
    }

    class QualifiedTeamViewHolder extends RecyclerView.ViewHolder {
        TextView teamID;
        public QualifiedTeamViewHolder(View itemView) {
            super(itemView);
            teamID = (TextView)itemView.findViewById(R.id.qualified_teams_id);
        }
    }
}