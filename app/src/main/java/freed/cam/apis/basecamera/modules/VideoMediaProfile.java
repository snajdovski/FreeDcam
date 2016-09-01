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

package freed.cam.apis.basecamera.modules;

import android.media.CamcorderProfile;

import freed.utils.Logger;
import freed.utils.StringUtils;

/**
 * Created by troop on 04.02.2016.
 * Thats based on CamcorderProfile http://developer.android.com/reference/android/media/CamcorderProfile.html
 */
public class VideoMediaProfile
{
    private static final String TAG = VideoMediaProfile.class.getSimpleName();
    //The target audio output bit rate in bits per second
    public int audioBitRate;
    //The number of audio channels used for the audio track
    public int audioChannels;
    //The audio encoder being used for the audio track.
    public int audioCodec;
    //The audio sampling rate used for the audio track
    public int audioSampleRate;
    //Default recording duration in seconds before the session is terminated.
    public int duration;
    //The file output format of the camcorder profile see MediaRecorder.OutputFormat
    private final int fileFormat;
    //The quality level of the camcorder profile
    private final int quality;
    public int videoBitRate;
    public int videoCodec;
    public int videoFrameHeight;
    public int videoFrameRate;
    public int videoFrameWidth;

    public String ProfileName;
    public VideoMode Mode;

    public boolean isAudioActive;

    public enum VideoMode
    {
        Normal,
        Highspeed,
        Timelapse,
    }

    public VideoMediaProfile(CamcorderProfile ex,String ProfileName, VideoMode mode, boolean isAudioActive)
    {
        audioBitRate = ex.audioBitRate;
        audioChannels = ex.audioChannels;
        audioCodec = ex.audioCodec;
        audioSampleRate = ex.audioSampleRate;
        duration = ex.duration;
        fileFormat = ex.fileFormat;
        quality = ex.quality;
        videoBitRate = ex.videoBitRate;
        videoCodec = ex.videoCodec;
        videoFrameRate = ex.videoFrameRate;
        videoFrameHeight = ex.videoFrameHeight;
        videoFrameWidth = ex.videoFrameWidth;
        this.ProfileName = ProfileName;
        Mode = mode;
        this.isAudioActive = isAudioActive;
        Logger.d(TAG, "ProfileName:"+ ProfileName+ "Duration:"+ duration +"FileFormat:"+ fileFormat +"Quality:"+ quality);
        Logger.d(TAG, "ABR:"+ audioBitRate +"AChannels:"+ audioChannels +"Acodec:"+ audioCodec +"AsampleRate"+ audioSampleRate +"audio_active:" + isAudioActive);
        Logger.d(TAG,"VBitrate:"+ videoBitRate +"VCodec:"+ videoCodec +"VFrameRate:"+ videoFrameRate +"VWidth:"+ videoFrameWidth +"Vheight:"+ videoFrameHeight);
    }

    protected VideoMediaProfile(int v1, int v2, int v3, int v4, int v5, int v6, int v7, int v8, int v9, int v10, int v11, int v12, String ProfileName, VideoMode mode, boolean isAudioActive)
    {
        audioBitRate = v1;
        audioChannels = v2;
        audioCodec = v3;
        audioSampleRate = v4;
        duration = v5;
        fileFormat = v6;
        quality = v7;
        videoBitRate = v8;
        videoCodec = v9;
        videoFrameRate = v10;
        videoFrameHeight = v11;
        videoFrameWidth = v12;
        this.ProfileName = ProfileName;
        Mode = mode;
        this.isAudioActive = isAudioActive;
        Logger.d(TAG, "ProfileName:"+ ProfileName+ "Duration:"+ duration +"FileFormat:"+ fileFormat +"Quality:"+ quality);
        Logger.d(TAG, "ABR:"+ audioBitRate +"AChannels:"+ audioChannels +"Acodec:"+ audioCodec +"AsampleRate"+ audioSampleRate +"audio_active:" + isAudioActive);
        Logger.d(TAG,"VBitrate:"+ videoBitRate +"VCodec:"+ videoCodec +"VFrameRate:"+ videoFrameRate +"VWidth:"+ videoFrameWidth +"Vheight:"+ videoFrameHeight);
    }

    public VideoMediaProfile(String t) {
        String[] ar = t.split(" ");
        audioBitRate = Integer.parseInt(ar[0]);
        audioChannels = Integer.parseInt(ar[1]);
        audioCodec = Integer.parseInt(ar[2]);
        audioSampleRate = Integer.parseInt(ar[3]);
        duration = Integer.parseInt(ar[4]);
        fileFormat = Integer.parseInt(ar[5]);
        quality = Integer.parseInt(ar[6]);
        videoBitRate = Integer.parseInt(ar[7]);
        videoCodec = Integer.parseInt(ar[8]);
        videoFrameRate = Integer.parseInt(ar[9]);
        videoFrameHeight = Integer.parseInt(ar[10]);
        videoFrameWidth = Integer.parseInt(ar[11]);
        ProfileName = ar[12];
        Mode = VideoMode.valueOf(ar[13]);
        isAudioActive = ar.length == 14 || Boolean.parseBoolean(ar[14]);

        Logger.d(TAG, "ProfileName:" + ProfileName + "Duration:" + duration + "FileFormat:" + fileFormat + "Quality:" + quality);
        Logger.d(TAG, "ABR:" + audioBitRate + "AChannels:" + audioChannels + "Acodec:" + audioCodec + "AsampleRate" + audioSampleRate + "audio_active:" + isAudioActive);
        Logger.d(TAG, "VBitrate:" + videoBitRate + "VCodec:" + videoCodec + "VFrameRate:" + videoFrameRate + "VWidth:" + videoFrameWidth + "Vheight:" + videoFrameHeight);
    }

    public String GetString()
    {
        String b = audioBitRate + " " +
                audioChannels + " " +
                audioCodec + " " +
                audioSampleRate + " " +
                duration + " " +
                fileFormat + " " +
                quality + " " +
                videoBitRate + " " +
                videoCodec + " " +
                videoFrameRate + " " +
                videoFrameHeight + " " +
                videoFrameWidth + " " +
                ProfileName + " " +
                Mode + " " +
                isAudioActive + " ";
        return b;
    }

    public VideoMediaProfile clone()
    {
        return new VideoMediaProfile(audioBitRate, audioChannels, audioCodec, audioSampleRate, duration, fileFormat, quality, videoBitRate, videoCodec, videoFrameRate, videoFrameHeight, videoFrameWidth, ProfileName, Mode, isAudioActive);
    }


    public static final String MEDIAPROFILESPATH = StringUtils.GetFreeDcamConfigFolder+"CustomMediaProfiles.txt";

    /*public static void loadCustomProfiles(HashMap<String, VideoMediaProfile> list) throws IOException
    {
        File mprof = new File(MEDIAPROFILESPATH);
        if(mprof.exists())
        {
            Logger.d(TAG, "CustomMediaProfile exists loading....");
            BufferedReader br = new BufferedReader(new FileReader(mprof));
            String line;

            while ((line = br.readLine()) != null)
            {
                if (!line.startsWith("#")) {
                    VideoMediaProfile m = new VideoMediaProfile(line);
                    list.put(m.ProfileName, m);
                }
            }
            br.close();
        }
        else
            Logger.d(TAG, "No CustomMediaProfiles found");

    }

    public static void saveCustomProfiles(HashMap<String, VideoMediaProfile> list)
    {
        File mprof = new File(MEDIAPROFILESPATH);
        try {
            if (!mprof.getParentFile().exists())
                mprof.getParentFile().mkdirs();
            mprof.createNewFile();
            Logger.d(TAG,"wrote MediaProfiles to txt");
        } catch (IOException e) {
            Logger.exception(e);
        }
        if(mprof.exists()) {
            try
            {
                BufferedWriter br = new BufferedWriter(new FileWriter(mprof));
                br.write("#audiobitrate audiochannels audioCodec audiosamplerate duration fileFormat quality videoBitrate videoCodec videoFrameRate videoFrameHeight videoFrameWidth ProfileName RecordMode isAudioActive \n");
                for (VideoMediaProfile profile : list.values())
                    br.write(profile.GetString() +"\n");
                br.close();
            } catch (IOException e)
            {
                Logger.exception(e);
            }
        }
    }*/
}
