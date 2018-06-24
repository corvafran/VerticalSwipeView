package com.example.corva.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.media.Image;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import java.util.List;

public class VerticalSwipeView extends LinearLayout{
    private OnFinishSwipeItem onFinishSwipeItem;
    private Adapter adapter;
    private final static int SWIPE_DURATION = 200;
    private float startY;
    private float lastMoveY;
    private final Rect mTmpContainerRect = new Rect();
    private int heightView;
    private int currentPosition;
    private boolean isScrollEnabled;
    private View emptyView;
    private boolean firstCharge;
    //private String[] image = {"https://www.ecestaticos.com/imagestatic/clipping/b19/9c8/b199c8d80de10570337e05163a1fbe50/imagen-sin-titulo.jpg?mtime=1529612743",
        //"https://upload.wikimedia.org/wikipedia/commons/thumb/d/d9/Lionel_Messi_2018.png/245px-Lionel_Messi_2018.png",
         //                       "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ3_4uVxippb-zrfJyjx4n_Lj9YKNJZkt4p_vfUoseW3CTMaOBTOA",
          //                      "https://www.infobae.com/new-resizer/vXoiuWUZrsHAbzCMxZaSLahd9Ac=/600x0/filters:quality(100)/s3.amazonaws.com/arc-wordpress-client-uploads/infobae-wp/wp-content/uploads/2018/05/28131359/Mauricio-Macri-pide-racionalidad-a-Peronistas.jpg"};

    public VerticalSwipeView(Context context) {
        super(context);
    }

    public VerticalSwipeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VerticalSwipeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public VerticalSwipeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

  /*  @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final View child = getChildAt(0);
        final View child2 = getChildAt(1);
        child.layout(l, t, r, b);
        child2.layout(l, b, r, b * 2);
    }*/

    {
        isScrollEnabled = true;
        currentPosition = 0;
        firstCharge = true;
    /*    Glide.with(getContext())
                .load(image[currentPosition])
                .into((ImageView) mView1);*/
    }

    private View inflateView(Context context, int resourse){
        return LayoutInflater.from(context).inflate(resourse, this, false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final View fillChild = getChildAt(0);
        final View emptyChild = getChildAt(1);
        heightView = MeasureSpec.getSize(heightMeasureSpec);
        if(fillChild != null)
        fillChild.measure(widthMeasureSpec, heightMeasureSpec);
        if (emptyChild != null)
        emptyChild.measure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        /*if (image.length > 1 && currentPosition < image.length -1)
            Glide.with(getContext())
                .load(image[currentPosition + 1])
                .into((ImageView) getChildAt(1));
        else {

        }*/
        if (adapter != null) {
            if (adapter.getItemCount() > 0 && firstCharge) {
                adapter.onBindView(getChildAt(0), 0);
                firstCharge = false;
            }
            if (adapter.getItemCount() > 1) {
                if (currentPosition < adapter.getItemCount() - 1) {
                    adapter.onBindView(getChildAt(1), currentPosition + 1);
                } else {
                    adapter.onBindEmptyView(getChildAt(0));
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isScrollEnabled) return false;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startY = event.getY();
                lastMoveY = event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                float progress = startY - event.getY();
                final float currentY = event.getY();
                if (progress > 0 ){
                  //  if (lastMoveY == currentY || lastMoveY > currentY){
                    getChildAt(0).setTranslationY(-progress);
                    getChildAt(1).setTranslationY(-progress);
                    lastMoveY = event.getY();
                   // }
                }
                return true;
            case MotionEvent.ACTION_UP:
                float translationChild  = -getChildAt(0).getTranslationY();
                final View view = getChildAt(0);
                if (translationChild > heightView * 0.2){
                    isScrollEnabled = false;
                    tranlateYView(getChildAt(1), SWIPE_DURATION, -(heightView ))
                            .start();
                    ObjectAnimator objectAnimator = tranlateYView(view, SWIPE_DURATION, -(heightView ));
                    objectAnimator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            VerticalSwipeView.this.onSwipeAnimationEnd(view);
                        }
                    });
                    objectAnimator.start();
                }else {
                    long duration = Math.round((SWIPE_DURATION/2) * (translationChild / (heightView * 0.2)));
                    tranlateYView(view, duration, 0f).start();
                    tranlateYView(getChildAt(1), duration, 0f).start();
                }
                return true;
        }
        return super.onTouchEvent(event);
    }

    public void swipeView(){
        if (isScrollEnabled && !(adapter.getItemCount() == 0) && !(adapter.getItemCount() == currentPosition)) {
            final View view = getChildAt(0);
            isScrollEnabled = false;
            tranlateYView(getChildAt(1), SWIPE_DURATION, -(heightView))
                    .start();
            ObjectAnimator objectAnimator = tranlateYView(getChildAt(0), SWIPE_DURATION, -(heightView));
            objectAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    VerticalSwipeView.this.onSwipeAnimationEnd(view);
                }
            });
            objectAnimator.start();
        }
    }

    private void onSwipeAnimationEnd(View view) {
        removeViewAt(0);
        getChildAt(0).setTranslationY(0);
        currentPosition++;
        Log.i("current Position", String.valueOf(currentPosition));
        if (currentPosition == adapter.getItemCount() - 1) {
            emptyView.setTranslationY(0);
            addView(emptyView);
        } else {
            if (currentPosition != adapter.getItemCount()) {
                view.setTranslationY(0);
                addView(view);
            }
        }
        isScrollEnabled = currentPosition != adapter.getItemCount();
        if (onFinishSwipeItem!= null) onFinishSwipeItem.onFinishSwipeItem(currentPosition);
    }

    private ObjectAnimator tranlateYView(View view2, long duration, float translationY) {
        ObjectAnimator animation = ObjectAnimator.ofFloat(view2, "translationY", translationY);
        AccelerateDecelerateInterpolator overshootInterpolator = new AccelerateDecelerateInterpolator();
        animation.setInterpolator(overshootInterpolator);
        animation.setDuration(duration);
        return animation;
    }

    public void setOnFinishSwipeItem(OnFinishSwipeItem onFinishSwipeItem) {
        this.onFinishSwipeItem = onFinishSwipeItem;
    }

    public int getCurrentPosition(){
        return currentPosition;
    }

    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
        View mView1 = LayoutInflater.from(getContext()).inflate(adapter.getViewLayoutResource(), this, false);
        View mView2 = inflateView(getContext(), adapter.getViewLayoutResource());
        View mView3 = inflateView(getContext(),adapter.getViewLayoutResource());
        mView2.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));
        emptyView = LayoutInflater.from(getContext()).inflate(adapter.getEmptyViewLayoutResource(), this, false);
        mView2.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));
        addView(mView1);
        addView(mView2);
    }

    public static abstract class Adapter{

        public abstract void onBindView(View view, int position);

        public abstract void onBindEmptyView(View view);

        public abstract int getItemCount();

        public abstract int getViewLayoutResource();

        public abstract int getEmptyViewLayoutResource();
    }

    public interface OnFinishSwipeItem{
        void onFinishSwipeItem(int position);
    }
}
