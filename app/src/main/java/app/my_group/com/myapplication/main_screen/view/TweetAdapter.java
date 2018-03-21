package app.my_group.com.myapplication.main_screen.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.twitter.sdk.android.core.models.Tweet;

import java.util.ArrayList;
import java.util.List;

import app.my_group.com.myapplication.R;
import app.my_group.com.myapplication.database.MyTweet;
import app.my_group.com.myapplication.utils.ImageHelper;

/**
 * Created by MyUser on 19.03.2018.
 */

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    private List<MyTweet> data;

    public List<MyTweet> getData() {
        return data;
    }

    public void addData(List<MyTweet> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    TweetAdapter() {
        data = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(data.get(position).getUser().getName() != null) {
            holder.name.setText(data.get(position).getUser().getName());
        }
        if(data.get(position).getText() != null) {
            holder.text.setText(data.get(position).getText());
        }
        ImageHelper.loadAvatar(data.get(position).getUser().getProfileBackgroundImageUrl(), holder.avatar);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView text;
        ImageView avatar;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            text = itemView.findViewById(R.id.text);
            avatar = itemView.findViewById(R.id.avatar);
        }
    }

}
