package com.example.ceubetjava.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ceubetjava.R;
import com.example.ceubetjava.data.User;
import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {
    private List<User> users = new ArrayList<>();

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);

        // Medalhas para os 3 primeiros
        String medal = "";
        switch (position) {
            case 0: medal = "🥇 "; break;
            case 1: medal = "🥈 "; break;
            case 2: medal = "🥉 "; break;
        }

        holder.positionText.setText((position + 1) + "º");
        holder.usernameText.setText(medal + user.getUsername());
        holder.creditsText.setText("Créditos: ver por jogo");
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView positionText;
        TextView usernameText;
        TextView creditsText;

        UserViewHolder(View itemView) {
            super(itemView);
            positionText = itemView.findViewById(R.id.positionText);
            usernameText = itemView.findViewById(R.id.usernameText);
            creditsText = itemView.findViewById(R.id.creditsText);
        }
    }
} 