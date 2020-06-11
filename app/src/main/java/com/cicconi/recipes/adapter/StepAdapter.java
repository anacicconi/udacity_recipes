package com.cicconi.recipes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.cicconi.recipes.R;
import com.cicconi.recipes.database.Recipe;
import com.cicconi.recipes.database.Step;
import java.util.ArrayList;
import java.util.List;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepAdapterViewHolder> {

    private StepClickListener mClickListener;

    private List<Step> mStepData  = new ArrayList<>();

    private Context mContext;

    public StepAdapter(StepClickListener clickListener) {
        mClickListener = clickListener;
    }

    public void setStepData(List<Step> stepData) {
        mStepData = stepData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StepAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.step_list_item, parent, false);

        return new StepAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepAdapterViewHolder holder, int position) {
        String shortDescription = mStepData.get(position).shortDescription;

        if(!shortDescription.isEmpty()) {
            holder.mShortDescription.setText(shortDescription);
        } else {
            holder.mShortDescription.setText(R.string.unknown);
        }
    }

    @Override
    public int getItemCount() {
        return mStepData.size();
    }

    public class StepAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView mShortDescription;

        StepAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mShortDescription = itemView.findViewById(R.id.tv_step_short_description);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mClickListener.onStepItemClick(mStepData.get(clickedPosition));
        }
    }

    public interface StepClickListener {
        void onStepItemClick(Step step);
    }
}
