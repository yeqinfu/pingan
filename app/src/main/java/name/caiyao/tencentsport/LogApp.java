package name.caiyao.tencentsport;

import android.text.TextUtils;

import de.robv.android.xposed.XposedBridge;

/**
 * Created by yeqinfu on 7/16/2018.
 */

public class LogApp {
    public  static void log(String s){
        if (TextUtils.isEmpty(s)){
            return;
        }
        XposedBridge.log(s);
    }
}
