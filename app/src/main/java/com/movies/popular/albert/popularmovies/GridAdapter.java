package com.movies.popular.albert.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.movies.popular.albert.popularmovies.networkutils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Alberto Casas Ortiz.
 */
public class GridAdapter extends RecyclerView.Adapter<GridAdapter.GridViewHolder> {
    //Number of items in the RecyclerView
    private int numItems;
    //List of posters names.
    private ArrayList<String> posters;
    //Context of the MainActivity.
    private Context contextMain;

    //OnClickListener
    final private ItemListenerClick clickListener;

    /**
     * Constructor for class GridAdapter.
     * @param context Context of the MainActivity.
     * @param numItems Number of items in the RecyclerView
     * @param posters List of posters names.
     * @param clickListener OnClickListener
     */
    public GridAdapter(Context context, int numItems, ArrayList<String> posters, ItemListenerClick clickListener){
        this.numItems = numItems;
        this.posters = posters;
        this.contextMain = context;
        this.clickListener = clickListener;
    }

    /**
     * Interface to handle clicks.
     */
    public interface ItemListenerClick{
        public void onItemClick(int clickedItemIndex);
    }

    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int id = R.layout.grid_movies;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(id, parent, shouldAttachToParentImmediately);
        GridViewHolder gridViewHolder = new GridViewHolder(view);

        return gridViewHolder;
    }

    @Override
    public void onBindViewHolder(GridViewHolder holder, int position) {
        holder.bind(posters.get(position));
    }

    @Override
    public int getItemCount() {
        return this.numItems;
    }

    /**
     * GridViewHolder class.
     */
    public class GridViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //ImageView with the poster.
        private ImageView image;

        /**
         * Constructor of GridViewHolder.
         * @param itemView Item.
         */
        public GridViewHolder(View itemView){
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.iv_grid);
            itemView.setOnClickListener(this);
        }

        /**
         * Bind data to the image.
         * @param posterName Name of the path of the poster.
         */
        public void bind(String posterName){
            Picasso.with(image.getContext()).load(NetworkUtils.imageURL(posterName).toString()).into(image);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            clickListener.onItemClick(clickedPosition);
        }
    }

}
