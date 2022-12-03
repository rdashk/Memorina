package com.example.memorina;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.List;

/*public class CardAdapter extends ArrayAdapter<OneCard> {


    EasyFlipView easyFlipView;

    public CardAdapter(@NonNull Context context, @NonNull List<OneCard> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.flip_view, parent, false);
        }

        OneCard card = getItem(position);
        ImageView imageCard = itemView.findViewById(R.id.image_card);

        imageCard.setImageResource(card.getImage());
        return itemView;
    }
}*/

/*
public class CardAdapter extends ArrayAdapter<OneCard> {

    View itemView;

    public CardAdapter(@NonNull Context context, @NonNull List<OneCard> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        itemView = convertView;
        if (itemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            itemView = LayoutInflater
                    .from(getContext()).inflate(R.layout.flip_view, parent, false);
        }

        OneCard card = getItem(position);
        ImageView imageCard = itemView.findViewById(R.id.image_card);

        imageCard.setImageResource(card.getBackImage());

        //itemView.setOnFlipListener((easyFlipView, newCurrentSide) -> {
           // Toast.makeText(getContext(),"Вы выбрали " + card, Toast.LENGTH_SHORT).show();
        //});
        return itemView;
    }


}*/
