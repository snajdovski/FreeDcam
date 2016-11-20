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

package freed.cam.apis.basecamera;

import android.content.Context;
import android.view.SurfaceView;

import freed.ActivityInterface;
import freed.cam.apis.basecamera.modules.ModuleHandlerAbstract;
import freed.cam.apis.basecamera.parameters.AbstractParameterHandler;
import freed.utils.AppSettingsManager;
import freed.utils.RenderScriptHandler;

/**
 * Created by troop on 09.12.2014.
 */
public interface CameraWrapperInterface extends CameraStateEvents
{
    /**
     * Start the Camera
     */
    void StartCamera();

    /**
     * Stop the Camera
     */
    void StopCamera();
    void StartPreview();
    void StopPreview();
    /**
     * Starts a new work with the current active module
     * the module must handle the workstate on its own if it gets hit twice while work is already in progress
     */
    void DoWork();

    /**
     * Get the current active CameraHolderSony
     * @return
     */
    CameraHolderInterface GetCameraHolder();

    /**
     * get the active parameterhandler
     * @return
     */
    AbstractParameterHandler GetParameterHandler();
    /**
     * get the appsettings
     */
    AppSettingsManager GetAppSettingsManager();
    ModuleHandlerAbstract GetModuleHandler();
    SurfaceView getSurfaceView();
    AbstractFocusHandler getFocusHandler();

    /**
     * get the left margine between display and preview
     * @return
     */
    int getMargineLeft();
    /**
     * get the right margine between display and preview
     * @return
     */
    int getMargineRight();
    /**
     * get the top margine between display and preview
     * @return
     */
    int getMargineTop();
    /**
     * get the preview width
     * @return
     */
    int getPreviewWidth();
    /**
     * get the preview height
     * @return
     */
    int getPreviewHeight();

    boolean isAeMeteringSupported();

    Context getContext();

    FocuspeakProcessor getFocusPeakProcessor();

    RenderScriptHandler getRenderScriptHandler();

    ActivityInterface getActivityInterface();

    /* *//**
 * gets thrown when camera starts open
 * @param message
 */
    void onCameraOpen(String message);
    /**
 * gets thrown when camera open has finish
 * @param message
 */
    void onCameraOpenFinish(String message);
    /**
 * gets thrown when camera is closed
 * @param message
 */
    void onCameraClose(String message);
    /**
 * gets thrown when preview is running
 * @param message
 */
    void onPreviewOpen(String message);
    /**
 * gets thrown when preview gets closed
 * @param message
 */
    void onPreviewClose(String message);
    /**
 * gets thrown when camera has a problem
 * @param error to send
 */
    void onCameraError(String error);
    /**
 * gets thrown when camera status changed
 * @param status that has changed
 */
    void onCameraStatusChanged(String status);

}
