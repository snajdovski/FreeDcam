/*
 *
 *     Copyright (C) 2015 Ingo Fuchs
 *     This program is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License along
 *     with this program; if not, write to the Free Software Foundation, Inc.,
 *     51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * /
 */

package freed.viewer.screenslide;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicHistogram;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.ortiz.touch.TouchImageView;
import com.troop.freedcam.R.dimen;
import com.troop.freedcam.R.id;
import com.troop.freedcam.R.layout;

import java.io.File;
import java.lang.ref.WeakReference;

import freed.ActivityInterface;
import freed.utils.Logger;
import freed.utils.RenderScriptHandler;
import freed.viewer.holder.FileHolder;
import freed.viewer.screenslide.ScreenSlideFragment.FragmentClickClistner;

/**
 * Created by troop on 21.08.2015.
 */
public class ImageFragment extends Fragment
{

    public interface I_WaitForWorkFinish
    {
        void HistograRdyToSet(int[] histodata, int position);
    }

    public int getPosition;

    private final String TAG = ImageFragment.class.getSimpleName();
    private TouchImageView imageView;
    private FileHolder file;
    private int mImageThumbSize;
    private FragmentClickClistner onClickListener;
    private ProgressBar progressBar;
    private int [] histogramData;
    private boolean isWorking;
    private I_WaitForWorkFinish waitForWorkFinish;
    private int position = -1;

    /**
     * Set the file to load by this fragment
     * @param filepath
     */
    public void SetFilePath(FileHolder filepath)
    {
        file = filepath;
    }

    public boolean IsWorking()
    {
        return isWorking;
    }

    public void SetWaitForWorkFinishLisnter(I_WaitForWorkFinish workFinish, int position)
    {
        this.position = position;
        waitForWorkFinish = workFinish;
    }

    /**
     *
     * @return the File attached to this view
     */
    public FileHolder GetFilePath()
    {
        return file;
    }

    public int[] GetHistogramData()
    {
        return histogramData;
    }

    public void SetOnclickLisnter(FragmentClickClistner onClickListener)
    {
        this.onClickListener = onClickListener;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater,container,savedInstanceState);

        mImageThumbSize = getResources().getDimensionPixelSize(dimen.image_thumbnail_size);
        View view = inflater.inflate(layout.freedviewer_screenslide_imageframent, container, false);
        imageView = (TouchImageView) view.findViewById(id.imageView_PicView);

        progressBar = (ProgressBar) view.findViewById(id.progressBar_screenslideImageview);
        imageView.setOnClickListener(onImageClick);
        progressBar.setVisibility(View.VISIBLE);
        if (file != null) {
            AsyncTask.execute(new ImageLoader(imageView,file.getFile()));
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        histogramData = null;
    }

    private final OnClickListener onImageClick = new OnClickListener() {
        @Override
        public void onClick(View v)
        {
            if (onClickListener != null)
                onClickListener.onClick(ImageFragment.this);
        }
    };


    private class ImageLoader implements Runnable
    {
        private final WeakReference<ImageView> imageViewReference;
        private final File file;

        public ImageLoader(ImageView imageView, File file)
        {
            this.imageViewReference = new WeakReference<ImageView>(imageView);
            this.file = file;
        }
        /**
         * Starts executing the active part of the class' code. This method is
         * called when a thread is started that has been created with a class which
         * implements {@code Runnable}.
         */
        @Override
        public void run() {
            Logger.d(TAG,"loadImage()"+ file.getName());
            isWorking = true;
            final Bitmap response = getBitmap();
            if (imageViewReference != null && response != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.post(new Runnable() {
                        @Override
                        public void run()
                        {
                            progressBar.setVisibility(View.GONE);
                            imageView.setImageBitmap(response);
                        }
                    });
                    createHistogramm(response);
                    if (waitForWorkFinish != null && position >-1)
                        waitForWorkFinish.HistograRdyToSet(histogramData, position);
                    waitForWorkFinish = null;
                    if (isDetached())
                        histogramData = null;
                }
            }
            else {
                histogramData = null;
                progressBar.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                    }
                });

            }
            isWorking = false;
        }
    }


    private Bitmap getBitmap()
    {
        Bitmap response =null;
        try {
            response = ((ActivityInterface)getActivity()).getBitmapHelper().getBitmap(file,false);

        }
        catch (IllegalArgumentException | NullPointerException ex)
        {
            Logger.exception(ex);
        }
        return response;
    }

    private void createHistogramm(Bitmap bitmap)
    {
        /*histogramData = new int [ 256*4];
        RenderScript mRS = RenderScript.create(getContext());
        ScriptIntrinsicHistogram histogram = ScriptIntrinsicHistogram.create(mRS,Element.U8_4(mRS));
        Allocation mHistogramAllocation = Allocation.createSized(mRS, Element.I32_3(mRS), 256);
        Allocation in = Allocation.createFromBitmap(mRS, bitmap);
        histogram.setOutput(mHistogramAllocation);
        histogram.forEach(in);
        mHistogramAllocation.copyTo(histogramData);*/
        Log.d(TAG, "Histodata");
        if(bitmap == null || bitmap.isRecycled())
            return;
        int [] histo = new int [ 256 * 3 ];
        int w = bitmap.getWidth ();
        int h = bitmap.getHeight ();
        int [] pixels = new int [ w * h ];
        bitmap.getPixels(pixels, 0, w, 0, 0, w, h);
        for ( int i = 0 ; i < w ; i ++) {
            for ( int j = 0 ; j < h ; j ++) {
                int index = j * w + i ;
                int r = Color.red ( pixels [ index ]);
                int g = Color.green ( pixels [ index ]);
                int b = Color.blue ( pixels [ index ]);
                histo [ r ]++;
                histo [ 256 + g ]++;
                histo [ 512 + b ]++;
            }
        }
        pixels = null;
        histogramData = histo;
    }

}
