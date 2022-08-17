package com.ziojio.zuiapp.log;

import com.ziojio.zuiapp.ZuiApp;
import com.ziojio.zuiapp.database.room.dao.TrackLogDao;
import com.ziojio.zuiapp.database.room.entity.TrackLog;

import androidz.util.ThreadUtil;

public class TrackLogUtil {

    private static TrackLogDao trackLogDao() {
        return ZuiApp.getDB().trackLogDao();
    }

    public static void saveLog(String type, String name, String data) {
        ThreadUtil.execute(() -> trackLogDao().insert(new TrackLog(type, name, data)));
    }

}
