package name.caiyao.tencentsport;

import android.content.Context;

import java.util.Set;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findClass;

/**
 * Created by yeqinfu on 5/24/2018.
 */

public class SportHook implements IXposedHookLoadPackage {
    public static String	sportTestPackage	= "cn.bluemobi.dylan.step";
    private static  int exValue=10000;
    public static String actionString="android.net.conn.ppandroid.pppp";
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        final Object activityThread = XposedHelpers.callStaticMethod(XposedHelpers.findClass("android.app.ActivityThread", null), "currentActivityThread");
        final Context systemContext = (Context) XposedHelpers.callMethod(activityThread, "getSystemContext");
        if (loadPackageParam.packageName.equals(sportTestPackage)) {//
         /*   XposedBridge.log("修改计步器。。。。。。。打开");
            Intent it=new Intent();
            it.setAction(actionString);
            Bundle bundle=new Bundle();
            bundle.putString("msg","修改计步器。。。。。。。打开");
            it.putExtra("msg",bundle);
            it.putExtra("haha",1);
            systemContext.sendBroadcast(it);*/
            hook(loadPackageParam);
        }
    }

    Set<XC_MethodHook.Unhook> mUnhookSet;
    private boolean x=false;

    private void hook(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        final Class<?> sensorEL = findClass("android.hardware.SystemSensorManager$SensorEventQueue", loadPackageParam.classLoader);
        XC_MethodHook dd=new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

               // ((float[]) param.args[1])[0] = ((float[]) param.args[1])[0] +(x==false?step:20000);

                LogApp.log("yeqinfu SportHook = " + ((float[]) param.args[1])[0]+"===step="+step);
            }
        };
        mUnhookSet=XposedBridge.hookAllMethods(sensorEL, "dispatchSensorEvent",dd);
    }
    int step=11000;



}
