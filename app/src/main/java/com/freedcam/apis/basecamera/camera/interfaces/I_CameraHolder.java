package com.freedcam.apis.basecamera.camera.interfaces;

import android.view.SurfaceHolder;

import java.util.HashMap;

/**
 * Created by troop on 15.08.2014.
 */
public interface I_CameraHolder
{
    boolean OpenCamera(int camera);
    void CloseCamera();

    int CameraCout();
    boolean IsRdy();
    boolean SetCameraParameters(HashMap<String, String> parameters);
    boolean SetSurface(SurfaceHolder texture);
    void StartPreview();
    void StopPreview();

}