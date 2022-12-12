package zuiapp.log;

import androidz.util.ThreadUtil;
import zuiapp.ZuiApp;
import zuiapp.database.room.dao.TrackLogDao;
import zuiapp.database.room.entity.TrackLog;

public class TrackLogUtil {

    private static TrackLogDao trackLogDao() {
        return ZuiApp.getDB().trackLogDao();
    }

    public static void saveLog(String type, String name, String data) {
        ThreadUtil.execute(() -> trackLogDao().insert(new TrackLog(type, name, data)));
    }

}
