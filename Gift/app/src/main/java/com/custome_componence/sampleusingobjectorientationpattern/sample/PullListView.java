package com.custome_componence.sampleusingobjectorientationpattern.sample;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.custome_componence.sampleusingobjectorientationpattern.R;


public class PullListView extends ListView {

    /**
     * ??????<br/>
     * ???????????
     */
    public static final int GET_MORE_TYPE_AUTO = 0;

    /**
     * ??????<br/>
     * ????????
     */
    public static final int GET_MORE_TYPE_CLICK = 1;

    /**
     * ??????
     */
    public interface OnRefreshListener {
        public void onRefresh();
    }

    /**
     * ??????
     */
    public interface OnGetMoreListener {
        public void onGetMore();
    }

    private static final String TAG = PullListView.class.getSimpleName();

    // ??????????
    private final static int NONE = 0;

    // ??????
    private final static int PULL_TO_REFRESH = 1;

    // ??????
    private final static int RELEASE_TO_REFRESH = 2;

    // ??????
    private final static int REFRESHING = 3;

    // ???padding???????????????????????????
    private final static float RATIO = 1.7f;

    private LayoutInflater inflater;

    // ????????????
    private ViewGroup headView;

    // ??????
    private TextView tvHeadTitle;

    // ??????
    private ImageView ivHeadArrow;

    // ??????
    private ProgressBar pbHeadRefreshing;

    // ????????????
    private View footView;

    // ??????
    private TextView tvFootTitle;

    // ???????
    private ProgressBar pbFootRefreshing;

    // ????
    private RotateAnimation animation;
    // ??????
    private RotateAnimation reverseAnimation;

    // ????
    private int headViewHeight;


    // ??
    private int state = NONE;

    /**
     * ?????????????????????
     * ?????????????????ACTION_DOWN??????<br/>
     * ???ACTION_MOVE??????????????????
     */
    private boolean isStartRecorded = false;

    // ???????????Y?
    private float startY;

    /**
     * ?????????????????????Header<br/>
     * <p/>
     * ???false?????Header??????????PullListView???????Header???????<br/>
     * ??????????Header????Header??????Header???<br/>
     * ??????????Header???,?????????true?????????????addPullHeader()??????????Header
     */
    private boolean addPullHeaderByUser = false;
    /**
     * ?????<br/>
     * ????????<br/>
     * ??????????????
     */
    private int getMoreType = GET_MORE_TYPE_AUTO;

    /**
     * ????????????????????????????????<br/>
     * true ?????????????
     */
    private boolean isFromReleaseToRefresh;


    // ???????????header??
    private boolean addPullHeaderFlag = false;

    // ???????????footer??
    private boolean addGetMoreFooterFlag = false;

    // ??????????
    private boolean hasMoreDataFlag = true;

    /**
     * Scroll???????Item????????????????
     */
    private int reachLastPositionCount = 0;

    private OnRefreshListener refreshListener;
    private OnGetMoreListener getMoreListener;

    private boolean canRefresh;
    private boolean isGetMoreing = false;

    public PullListView(Context context) {
        this(context, null, 0);
    }

    public PullListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    /**
     * ???
     *
     * @param context
     */
    private void init(Context context, AttributeSet attrs) {
        //????
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.PullListView, 0, 0);
        if (arr != null) {
            addPullHeaderByUser = arr.getBoolean(R.styleable.PullListView_addPullHeaderByUser, addPullHeaderByUser);
            getMoreType = arr.getInt(R.styleable.PullListView_getMoreType, GET_MORE_TYPE_AUTO);
            arr.recycle();
        }
        initAnimation();
        inflater = LayoutInflater.from(context);

        /**
         * ??
         */
        headView = (LinearLayout) inflater.inflate(R.layout.pull_list_view_head, null);
        ivHeadArrow = (ImageView) headView.findViewById(R.id.iv_head_arrow);
        pbHeadRefreshing = (ProgressBar) headView.findViewById(R.id.pb_head_refreshing);
        tvHeadTitle = (TextView) headView.findViewById(R.id.tv_head_title);
        measureView(headView);
        headViewHeight = headView.getMeasuredHeight();
        headView.setPadding(0, -headViewHeight, 0, 0);
        headView.invalidate();

        if (!addPullHeaderByUser) {
            addHeaderView(headView, null, false);
            addPullHeaderFlag = true;
        }

        /**
         * ??
         */
        footView = inflater.inflate(R.layout.pull_list_view_foot, this, false);
        tvFootTitle = (TextView) footView.findViewById(R.id.tv_foot_title);
        pbFootRefreshing = (ProgressBar) footView.findViewById(R.id.pb_foot_refreshing);
        footView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkCanClickGetMore()) {
                    getMore();
                }
            }
        });


        // ????
        setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                doOnScrollStateChanged(view, scrollState);

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                doOnScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);

            }
        });

        state = NONE;
        canRefresh = false;
    }

    /**
     * ?????
     */
    private void initAnimation() {
        // ??
        animation = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(300);
        animation.setFillAfter(true);

        //????
        reverseAnimation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        reverseAnimation.setInterpolator(new LinearInterpolator());
        reverseAnimation.setDuration(300);
        reverseAnimation.setFillAfter(true);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        // ??????
        if (!canRefresh) {
            return super.dispatchTouchEvent(event);
        }

        int action = event.getAction();
        float tempY = event.getRawY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (state == PULL_TO_REFRESH) {
                    state = NONE;
                    changeHeaderViewByState();
                } else if (state == RELEASE_TO_REFRESH) {
                    state = REFRESHING;
                    changeHeaderViewByState();
                    refresh();
                }
                isStartRecorded = false;
                isFromReleaseToRefresh = false;
                break;

            case MotionEvent.ACTION_MOVE:
                if (!checkCanPullDown() || state == REFRESHING) {
                    break;
                }
                if (!isStartRecorded) {
                    startY = tempY;
                   // Log.v(TAG, "????????????");
                    Log.v(TAG, "Record the initial position at the start of the slide");
                    isStartRecorded = true;
                }
                float deltaY = tempY - startY;
                float realDeltaY = deltaY / RATIO;

                // ?????
                if (state == NONE) {
                    if (realDeltaY > 0) {
                        state = PULL_TO_REFRESH;
                        changeHeaderViewByState();
                       // Log.v(TAG, "??????????????");
                        Log.v(TAG, "From the initial state to the drop-down refresh state");
                    }
                }
                // ??????????????PULL_TO_REFRESH??
                else if (state == PULL_TO_REFRESH) {

                    headView.setPadding(0, -headViewHeight + (int) realDeltaY, 0, 0);

                    // ???????RELEASE_TO_REFRESH???
                    if (realDeltaY >= headViewHeight) {
                        state = RELEASE_TO_REFRESH;
                        isFromReleaseToRefresh = true;
                        changeHeaderViewByState();
                        //Log.v(TAG, "?????????????");
                        Log.v(TAG, "Pull-down refresh refresh state transition to loosen");
                    }
                    // ?????
                    else if (realDeltaY <= 0) {
                        state = NONE;
                        changeHeaderViewByState();
                        //Log.v(TAG, "???????????");
                        Log.v(TAG, "Refresh the drop-down transition to the initial state");
                    }

                }
                // ????????
                else if (state == RELEASE_TO_REFRESH) {

                    headView.setPadding(0, -headViewHeight + (int) realDeltaY, 0, 0);

                    // ??????????????head??????????????????
                    if (realDeltaY < headViewHeight && realDeltaY > 0) {
                        state = PULL_TO_REFRESH;
                        changeHeaderViewByState();
                       // Log.v(TAG, "????????????????");
                        Log.v(TAG, "Refresh the release state to the pull-down refresh state");
                    }
                    // ???????
                    else if (realDeltaY <= 0) {
                        state = NONE;
                        changeHeaderViewByState();
                        Log.v(TAG, "Refresh the release state to the initial state");
                    }
                }

                break;
        }

        return super.dispatchTouchEvent(event);
    }


    // ???????????????????
    private void changeHeaderViewByState() {
        switch (state) {
            case NONE:
                headView.setPadding(0, -1 * headViewHeight, 0, 0);
                pbHeadRefreshing.setVisibility(View.GONE);
                ivHeadArrow.clearAnimation();
                ivHeadArrow.setImageResource(R.mipmap.pull_list_view_progressbar_bg);
               // tvHeadTitle.setText("????");
                tvHeadTitle.setText("Pull-down refresh");
                break;

            case PULL_TO_REFRESH:
                pbHeadRefreshing.setVisibility(View.GONE);
                tvHeadTitle.setVisibility(View.VISIBLE);
                ivHeadArrow.clearAnimation();
                ivHeadArrow.setVisibility(View.VISIBLE);
               // tvHeadTitle.setText("????");
                tvHeadTitle.setText("Pull-down refresh");
                // ??RELEASE_To_REFRESH??????
                if (isFromReleaseToRefresh) {
                    isFromReleaseToRefresh = false;
                    ivHeadArrow.clearAnimation();
                    ivHeadArrow.startAnimation(reverseAnimation);
                }
                break;

            case RELEASE_TO_REFRESH:
                ivHeadArrow.setVisibility(View.VISIBLE);
                pbHeadRefreshing.setVisibility(View.GONE);
                tvHeadTitle.setVisibility(View.VISIBLE);

                ivHeadArrow.clearAnimation();
                ivHeadArrow.startAnimation(animation);

               // tvHeadTitle.setText("????");
                tvHeadTitle.setText("Loosen Refresh");

                break;

            case REFRESHING:

                headView.setPadding(0, 0, 0, 0);

                pbHeadRefreshing.setVisibility(View.VISIBLE);
                ivHeadArrow.clearAnimation();
                ivHeadArrow.setVisibility(View.GONE);
                //tvHeadTitle.setText("????...");
                tvHeadTitle.setText("Refreshing...");

                break;

        }
    }

    // ??
    private void refresh() {
        //????
        if (refreshListener != null) {
            refreshListener.onRefresh();
        }

        //??????
        if (footView != null) {
            footView.setVisibility(View.VISIBLE);
            pbFootRefreshing.setVisibility(View.GONE);
           // tvFootTitle.setText("????");
            tvFootTitle.setText("Load more");
            isGetMoreing = false;
            hasMoreDataFlag = true;
        }
    }

    // ????
    private void getMore() {
        //??????
        if (getMoreListener != null) {
            isGetMoreing = true;
            pbFootRefreshing.setVisibility(View.VISIBLE);
            //tvFootTitle.setText("????...");
            tvFootTitle.setText("Loading...");
            getMoreListener.onGetMore();

        }
    }

    // ????
    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    /**
     * ????????<br/>
     * ?????ListView????????
     *
     * @return
     */
    private boolean checkCanPullDown() {
        if (getFirstVisiblePosition() > 0) {
            return false;
        }
        return !canScroll(-1);
    }

    /**
     * ????????????<br/>
     *
     * @return
     */
    private boolean checkCanAutoGetMore() {
        if (getMoreType != GET_MORE_TYPE_AUTO) {
            return false;
        }
        if (footView == null) {
            return false;
        }
        if (getMoreListener == null) {
            return false;
        }
        if (isGetMoreing) {
            return false;
        }
        if (!hasMoreDataFlag) {
            return false;
        }
        if (getAdapter() == null) {
            return false;
        }
        if (!canScroll(1) && !canScroll(-1)) {
            return false;
        }
        if (getLastVisiblePosition() != getAdapter().getCount() - 1) {
            return false;
        }
        if (reachLastPositionCount != 1) {
            return false;
        }
        return true;
    }

    /**
     * ????????????
     *
     * @return
     */
    private boolean checkCanClickGetMore() {
        if (getMoreType != GET_MORE_TYPE_CLICK) {
            return false;
        }
        if (footView == null) {
            return false;
        }
        if (getMoreListener == null) {
            return false;
        }
        if (getAdapter() == null) {
            return false;
        }
        if (isGetMoreing) {
            return false;
        }
        if (!hasMoreDataFlag) {
            return false;
        }
        return true;
    }

    /**
     * ??ListView??????
     *
     * @param direction
     * @return
     */
    private boolean canScroll(int direction) {
        final int childCount = getChildCount();
        if (childCount == 0) {
            return false;
        }

        final int firstPosition = getFirstVisiblePosition();
        final int listPaddingTop = getPaddingTop();
        final int listPaddingBottom = getPaddingTop();
        final int itemCount = getAdapter().getCount();

        if (direction > 0) {
            final int lastBottom = getChildAt(childCount - 1).getBottom();
            final int lastPosition = firstPosition + childCount;
            return lastPosition < itemCount || lastBottom > getHeight() - listPaddingBottom;
        } else {
            final int firstTop = getChildAt(0).getTop();
            return firstPosition > 0 || firstTop < listPaddingTop;
        }
    }

    public void setCanRefresh(boolean canRefresh) {
        this.canRefresh = canRefresh;
    }

    /**
     * ??????????<br/>
     * ????????????
     */
    public void performRefresh() {
        state = REFRESHING;
        changeHeaderViewByState();
        refresh();
    }

    /**
     * ?????????
     *
     * @param refreshListener
     */
    public void setOnRefreshListener(OnRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
        canRefresh = true;
    }

    /**
     * ?????????
     *
     * @param getMoreListener
     */
    public void setOnGetMoreListener(OnGetMoreListener getMoreListener) {
        this.getMoreListener = getMoreListener;
        if (!addGetMoreFooterFlag) {
            addGetMoreFooterFlag = true;
            this.addFooterView(footView);
        }
    }

    /**
     * ??????
     */
    public void refreshComplete() {
        state = NONE;
        changeHeaderViewByState();
    }

    /**
     * ??????
     */
    public void getMoreComplete() {
        pbFootRefreshing.setVisibility(View.GONE);
       // tvFootTitle.setText("????");
        tvFootTitle.setText("Load more");
        isGetMoreing = false;
    }

    /**
     * ??????????<br/>
     * ??????????
     */
    public void setNoMore() {
        hasMoreDataFlag = false;
        if (footView != null) {
            footView.setVisibility(View.GONE);
        }
    }

    /**
     * ????????
     */
    public void setHasMore() {
        hasMoreDataFlag = true;
        if (footView != null) {
            footView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * ??????????
     */
    public void reAddPullHeaderView() {
        if (headView != null) {
            removeHeaderView(headView);
            addHeaderView(headView, null, false);
            addPullHeaderFlag = true;
        }
    }

    /**
     * ????????
     */
    public void addPullHeaderView() {
        if (headView != null && !addPullHeaderFlag) {
            addHeaderView(headView, null, false);
            addPullHeaderFlag = true;
        }
    }


    /**
     * ???????????????PullListView?OnScrollListener<br/>
     * ????listener?onScrollStateChanged???????????PullListView?????
     *
     * @param view
     * @param scrollState
     */
    public void doOnScrollStateChanged(AbsListView view, int scrollState) {
    }

    /**
     * ???????????????PullListView?OnScrollListener<br/>
     * ????listener?onScroll???????????PullListView?????
     *
     * @param view
     * @param firstVisibleItem
     * @param visibleItemCount
     * @param totalItemCount
     */
    public void doOnScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if (getAdapter() == null) {
            return;
        }

        if (getLastVisiblePosition() == getAdapter().getCount() - 1) {
            reachLastPositionCount++;
        } else {
            reachLastPositionCount = 0;
        }


        if (checkCanAutoGetMore()) {
            getMore();
        }


    }
}

