package troop.com.themesample.handler;


import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.troop.freedcam.camera.CameraUiWrapper;
import com.troop.freedcam.camera2.CameraUiWrapperApi2;
import com.troop.freedcam.i_camera.AbstractCameraUiWrapper;
import com.troop.freedcam.i_camera.FocusRect;
import com.troop.freedcam.sonyapi.CameraUiWrapperSony;
import com.troop.freedcam.ui.AbstractFocusImageHandler;
import com.troop.freedcam.ui.I_Activity;
import com.troop.freedcam.ui.ImageViewTouchAreaHandler;

import troop.com.themesample.R;


/**
 * Created by troop on 02.09.2014.
 */
public class FocusImageHandler extends AbstractFocusImageHandler
{
    private AbstractCameraUiWrapper wrapper;
    protected ImageView focusImageView;
    final int crosshairShowTime = 3000;
    int disHeight;
    int disWidth;
    int recthalf;
    protected ImageView cancelFocus;
    protected ImageView meteringArea;
    protected ImageView awbArea;
    FocusRect meteringRect;
    FocusRect awbRect;
    static final int MAX_DURATION = 3500;
    private boolean focusWasVisible = false;
    private boolean meteringWasVisible = false;
    private boolean wbWasVisible = false;


    public FocusImageHandler(View view, Fragment fragment, I_Activity activity)
    {
        super(view,fragment, activity);
        init(view);

        recthalf = fragment.getResources().getDimensionPixelSize(R.dimen.crosshairwidth)/2;
        //focusImageView.setVisibility(View.GONE);

        cancelFocus.setVisibility(View.GONE);
        cancelFocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                wrapper.cameraHolder.CancelFocus();
                cancelFocus.setVisibility(View.GONE);
            }
        });

        meteringArea.setOnTouchListener(new ImageViewTouchAreaHandler(meteringArea, activity, meteringTouch, true));
        meteringArea.setVisibility(View.GONE);

        awbArea.setOnTouchListener(new ImageViewTouchAreaHandler(awbArea, activity, awbTouch, true));
        awbArea.setVisibility(View.GONE);

    }

    public void HideImages(boolean hide)
    {
        if (hide)
        {
            if (focusImageView.getVisibility() == View.VISIBLE)
                focusWasVisible = true;
            else
                focusWasVisible = false;
            if (meteringArea.getVisibility() == View.VISIBLE)
                meteringWasVisible = true;
            else
                meteringWasVisible = false;
            if (awbArea.getVisibility() == View.VISIBLE)
                wbWasVisible = true;
            else
                wbWasVisible = false;
            focusImageView.setVisibility(View.GONE);
            meteringArea.setVisibility(View.GONE);
            awbArea.setVisibility(View.GONE);
        }
        else {
            if (focusWasVisible)
                focusImageView.setVisibility(View.VISIBLE);
            if (wbWasVisible)
                awbArea.setVisibility(View.VISIBLE);
            if (meteringWasVisible)
                meteringArea.setVisibility(View.VISIBLE);
        }
    }

    protected void init(View view)
    {
        focusImageView = (ImageView)view.findViewById(troop.com.themesample.R.id.imageView_Crosshair);
        cancelFocus = (ImageView)view.findViewById(troop.com.themesample.R.id.imageViewFocusClose);
        meteringArea = (ImageView)view.findViewById(troop.com.themesample.R.id.imageView_meteringarea);
        awbArea = (ImageView)view.findViewById(troop.com.themesample.R.id.imageView_awbarea);
    }

    public void SetCamerUIWrapper(AbstractCameraUiWrapper cameraUiWrapper)
    {
        this.wrapper = cameraUiWrapper;
        if(cameraUiWrapper instanceof CameraUiWrapper || cameraUiWrapper instanceof CameraUiWrapperApi2) {
            meteringRect = centerImageView(meteringArea);
            meteringArea.setVisibility(View.VISIBLE);
        }
        else
        {
            meteringArea.setVisibility(View.GONE);
        }
        if(cameraUiWrapper instanceof CameraUiWrapperApi2)
        {
            awbRect = centerImageView(awbArea);
            awbArea.setVisibility(View.VISIBLE);
        }
        else
            awbArea.setVisibility(View.GONE);
        if (wrapper.Focus != null)
            wrapper.Focus.focusEvent = this;
    }

    @Override
    public void FocusStarted(FocusRect rect)
    {

        focusImageView.removeCallbacks(hideFocus);
        if (!(wrapper instanceof CameraUiWrapperSony))
        {

            if (rect == null)
            {
                Point size = getSize();
                int halfwidth = size.x / 2;
                int halfheight = size.y / 2;
                rect = new FocusRect(halfwidth - recthalf, halfheight - recthalf, halfwidth + recthalf, halfheight + recthalf);
            }
            RelativeLayout.LayoutParams mParams = (RelativeLayout.LayoutParams) focusImageView.getLayoutParams();
            mParams.leftMargin = rect.left;
            //mParams.rightMargin = x +half;
            mParams.topMargin = rect.top;

            focusImageView.setLayoutParams(mParams);
            focusImageView.setBackgroundResource(troop.com.themesample.R.drawable.crosshair_circle_normal);
            focusImageView.setVisibility(View.VISIBLE);

            /*RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

//Setup anim with desired properties
            anim.setInterpolator(new LinearInterpolator());
            anim.setRepeatCount(Animation.INFINITE); //Repeat animation indefinitely
            anim.setDuration(5000); //Put desired duration per anim cycle here, in milliseconds*/

//Start animation
            Animation anim = AnimationUtils.loadAnimation(focusImageView.getContext(), R.anim.scale_focusimage);
            focusImageView.startAnimation(anim);
        }
    }

    @Override
    public void FocusFinished(final boolean success)
    {
        if (!(wrapper instanceof CameraUiWrapperSony))
        {
            focusImageView.post(new Runnable() {
                @Override
                public void run() {
                    if (success)
                        focusImageView.setBackgroundResource(troop.com.themesample.R.drawable.crosshair_circle_success);
                    else
                        focusImageView.setBackgroundResource(troop.com.themesample.R.drawable.crosshair_circle_failed);

                    focusImageView.setAnimation(null);
                    focusImageView.postDelayed(hideFocus, crosshairShowTime);
                }
            });
        }

    }

    private Runnable hideFocus = new Runnable() {
        @Override
        public void run() {
            focusImageView.setVisibility(View.GONE);
            wrapper.Focus.SetFocusFalse();
        }
    };


    @Override
    public void FocusLocked(final boolean locked)
    {
        cancelFocus.post(new Runnable() {
            @Override
            public void run() {
                if (locked)
                    cancelFocus.setVisibility(View.VISIBLE);
                else
                    cancelFocus.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void TouchToFocusSupported(boolean isSupported)
    {
        if (!isSupported)
            focusImageView.setVisibility(View.GONE);
    }

    @Override
    public void AEMeteringSupported(final boolean isSupported)
    {
        meteringArea.post(new Runnable() {
            @Override
            public void run() {
                if (isSupported)
                    meteringArea.setVisibility(View.VISIBLE);
                else
                    meteringArea.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void AWBMeteringSupported(boolean isSupported) {
        if (isSupported)
            awbArea.setVisibility(View.VISIBLE);
        else
            awbArea.setVisibility(View.GONE);
    }


    /*private Handler handler = new Handler();
    Runnable hideCrosshair = new Runnable() {
        @Override
        public void run()
        {
            focusImageView.setBackgroundResource(R.drawable.crosshair_normal);
            focusImageView.setVisibility(View.GONE);
        }
    };*/

    ImageViewTouchAreaHandler.I_TouchListnerEvent meteringTouch = new ImageViewTouchAreaHandler.I_TouchListnerEvent() {
        @Override
        public void onAreaCHanged(FocusRect imageRect, int previewWidth, int previewHeight) {
            if (wrapper != null)
                wrapper.Focus.SetMeteringAreas(imageRect, previewWidth, previewHeight);
        }

        @Override
        public void OnAreaClick(int x, int y) {
            OnClick(x,y);
        }
    };

    ImageViewTouchAreaHandler.I_TouchListnerEvent awbTouch = new ImageViewTouchAreaHandler.I_TouchListnerEvent() {
        @Override
        public void onAreaCHanged(FocusRect imageRect, int previewWidth, int previewHeight) {
            if (wrapper != null)
                wrapper.Focus.SetAwbAreas(imageRect, previewWidth, previewHeight);
        }

        @Override
        public void OnAreaClick(int x, int y) {
            OnClick(x,y);
        }
    };

    public void OnClick(int x, int y)
    {
        if (wrapper == null || wrapper.Focus == null)
            return;
        disWidth = activity.GetPreviewWidth();
        disHeight = activity.GetPreviewHeight();

        FocusRect rect = new FocusRect(x - recthalf, x + recthalf, y - recthalf, y + recthalf);
        if (wrapper.Focus != null)
            wrapper.Focus.StartTouchToFocus(rect, meteringRect, disWidth, disHeight);
    }

    private Point getSize()
    {
        if (Build.VERSION.SDK_INT >= 17) {
            WindowManager wm = (WindowManager) fragment.getActivity().getSystemService(Context.WINDOW_SERVICE);
            Point size = new Point();
            wm.getDefaultDisplay().getRealSize(size);
            return size;
        }
        else {
            DisplayMetrics metrics = fragment.getActivity().getResources().getDisplayMetrics();
            Point size = new Point();
            size.set(metrics.widthPixels, metrics.heightPixels);
            return size;
        }
    }

    private FocusRect centerImageView(ImageView imageview)
    {
        int width = 0;
        int height = 0;

        if(fragment == null || fragment.getActivity() == null)
            return null;

        Point size =  getSize();
        if (fragment.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            width = size.x;
            height = size.y;
        }
        else
        {
            height = size.x;
            width = size.y;
        }
        imageview.setX(width/2 - recthalf);
        imageview.setY(height/2 - recthalf);

        return new FocusRect((int)imageview.getX() - recthalf, (int)imageview.getX() + recthalf, (int)imageview.getY() - recthalf, (int)imageview.getY() + recthalf);
    }



}
