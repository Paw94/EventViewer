package com.example.paw.myapplication.recycleview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.paw.myapplication.model.Event;
import com.example.paw.myapplication.MyApplication;
import com.example.paw.myapplication.R;
import com.example.paw.myapplication.room.config.AppDatabase;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;

public class MyRecycleViewAdapter extends RecyclerView.Adapter<CustomViewHolder>
implements ItemTouchHelperAdapter {

    private ArrayList<Event> items = new ArrayList<>();
    private int lastAnimatedPosition = 3;
    private boolean delayEnterAnimation = true;
    private boolean animationsLocked = false;
    private LayoutInflater layoutInflater;
    private View view;

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        view = viewGroup;
        layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View cellForRow = layoutInflater.inflate(R.layout.my_row, viewGroup, false);


        return new CustomViewHolder(cellForRow);
    }

    public void addEvent(Event event){
        items.add(event);
        AppDatabase.getInstance(view.getContext()).eventDao().insert(event);
        notifyItemInserted(getItemCount());
    }

    public void setEvents(ArrayList<Event> events){
        items = events;
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder customViewHolder, int position) {
        items = MyApplication.getInstance().getEventsToDisplay();
        //runEnterAnimation(customViewHolder.itemView, position);
        Animation animation = AnimationUtils.loadAnimation(customViewHolder.itemView.getContext(),
                (position > lastAnimatedPosition) ? R.anim.up_down
                        : R.anim.down_up);
        customViewHolder.itemView.startAnimation(animation);
        lastAnimatedPosition = position;



        customViewHolder.titleView.setText(items.get(position).getTitle());
        customViewHolder.dateView.setText(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(items.get(position).getDate()));
        customViewHolder.descriptionView.setText(items.get(position).getDescription());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(items, fromPosition, toPosition);

        System.out.println("Moved  " + fromPosition + " "  + toPosition );
        System.out.println(items);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    private void runEnterAnimation(View view, int position) {



//        if (position > lastAnimatedPosition) {
//            lastAnimatedPosition = position;
//            view.setTranslationY(100);
//            view.setAlpha(0.f);
//            view.animate()
//                    .translationY(0).alpha(1.f)
//                    .setStartDelay(delayEnterAnimation ? 40 * (position) : 0)
//                    .setInterpolator(new AccelerateInterpolator(2.f))
//                    .setDuration(1000)
//                    .setListener(new AnimatorListenerAdapter() {
//                        @Override
//                        public void onAnimationEnd(Animator animation) {
//                            animationsLocked = true;
//                        }
//                    })
//                    .start();
//        }
    }


    @Override
    public void onItemDismiss(int position) {
        AppDatabase.getInstance(view.getContext()).eventDao().deleteEvent(items.get(position).getId());

        System.out.println("#### Rozmiar = " + AppDatabase.getInstance(layoutInflater.getContext()).eventDao().getAll().size());
        items.remove(position);
        notifyItemRemoved(position);

    }
}
