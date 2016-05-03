package com.troop.freedcam.camera.modules;

import com.troop.freedcam.camera.BaseCameraHolder;
import com.troop.freedcam.camera.modules.image_saver.MediatekSaver;
import com.troop.freedcam.i_camera.modules.ModuleEventHandler;

/**
 * Created by troop on 24.11.2014.
 */
public class PictureModuleMTK extends PictureModule
{
    private static String TAG = PictureModuleMTK.class.getSimpleName();
    public PictureModuleMTK(BaseCameraHolder baseCameraHolder, ModuleEventHandler eventHandler)
    {
        super(baseCameraHolder, eventHandler);

    }

    @Override
    public boolean DoWork()
    {
        if (!this.isWorking)
        {
            startworking();
            final MediatekSaver mtksaver = new MediatekSaver(baseCameraHolder, this);
            mtksaver.TakePicture();

        }
        return true;
    }

}
