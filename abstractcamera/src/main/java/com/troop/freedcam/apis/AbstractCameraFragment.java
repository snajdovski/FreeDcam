package com.troop.freedcam.apis;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.troop.filelogger.Logger;
import com.troop.freedcam.i_camera.AbstractCameraUiWrapper;

/**
 * Created by troop on 06.06.2015.
 */
public abstract class AbstractCameraFragment extends Fragment
{
    private final String TAG = AbstractCameraFragment.class.getSimpleName();

    AbstractCameraUiWrapper cameraUiWrapper;
    protected View view;
    protected CamerUiWrapperRdy onrdy;
    public AbstractCameraFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Logger.d(TAG, "onCreateView");

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {

        super.onViewCreated(view, savedInstanceState);
    }

    public AbstractCameraUiWrapper GetCameraUiWrapper()
    {
        return cameraUiWrapper;
    }

    public void Init(CamerUiWrapperRdy rdy)
    {
        this.onrdy = rdy;
    }



    public void DestroyCameraUiWrapper()
    {
        if (cameraUiWrapper != null)
        {
            Logger.d(TAG, "Destroying Wrapper");
            cameraUiWrapper.camParametersHandler.CLEAR();
            cameraUiWrapper.moduleHandler.moduleEventHandler.CLEAR();
            cameraUiWrapper.moduleHandler.CLEARWORKERLISTNER();
            cameraUiWrapper.StopPreview();
            cameraUiWrapper.StopCamera();
            cameraUiWrapper = null;
            Logger.d(TAG, "destroyed cameraWrapper");
        }
    }

    public interface CamerUiWrapperRdy
    {
        void onCameraUiWrapperRdy(AbstractCameraUiWrapper cameraUiWrapper);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
