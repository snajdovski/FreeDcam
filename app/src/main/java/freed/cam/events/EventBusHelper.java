package freed.cam.events;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.EventBusException;

import freed.utils.Log;

public class EventBusHelper {

    public static void register(Object ob)
    {
        if (!EventBus.getDefault().isRegistered(ob))
            EventBus.getDefault().register(ob);
    }

    public static void unregister(Object ob)
    {
        if (EventBus.getDefault().isRegistered(ob))
            EventBus.getDefault().unregister(ob);
    }

    public static void post(Object ob)
    {
        try {
            EventBus.getDefault().post(ob);
        }
        catch (EventBusException ex)
        {
            Log.WriteEx(ex);
        }
    }

    public static void postSticky(Object ob)
    {
        EventBus.getDefault().postSticky(ob);
    }
}
