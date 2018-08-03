package name.caiyao.tencentsport;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.Set;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findClass;

/**
 * Created by yeqinfu on 5/24/2018.
 */

public class TestSportHook implements IXposedHookLoadPackage, IXposedHookZygoteInit {
    public static String	sportTestPackage	= "cn.bluemobi.dylan.step";
    private  int exValue=10000;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        final Object activityThread = XposedHelpers.callStaticMethod(XposedHelpers.findClass("android.app.ActivityThread", null), "currentActivityThread");
        final Context systemContext = (Context) XposedHelpers.callMethod(activityThread, "getSystemContext");

        IntentFilter intentFilter = new IntentFilter();
        String SETTING_CHANGED = "name.caiyao.tencentsport.SETTING_CHANGED";
        intentFilter.addAction(SETTING_CHANGED);
        systemContext.registerReceiver(new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                exValue = Integer.parseInt(intent.getExtras().getString("exValue", "10000"));


                XposedBridge.log("*********************************"+exValue);
            }
        }, intentFilter);
        if (loadPackageParam.packageName.equals(sportTestPackage)) {//


            hook(loadPackageParam);
        }
    }

    Set<XC_MethodHook.Unhook> mUnhookSet;
    private void hook(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        final Class<?> sensorEL = findClass("android.hardware.SystemSensorManager$SensorEventQueue", loadPackageParam.classLoader);
        XC_MethodHook dd=new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
              /*  if (!isOpen){
                    return;
                }*/
                sharedPreferences.reload();
                String s=sharedPreferences.getString("exValue","10000");
                ((float[]) param.args[1])[0] = ((float[]) param.args[1])[0] +Integer.parseInt(s);
                XposedBridge.log("********************************* "+s+"====步数 sensor = " + ((float[]) param.args[1])[0]);
            }
        };
        mUnhookSet=XposedBridge.hookAllMethods(sensorEL, "dispatchSensorEvent",dd);
    }
    private XSharedPreferences sharedPreferences;
    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        sharedPreferences = new XSharedPreferences(BuildConfig.APPLICATION_ID);
    }
}
