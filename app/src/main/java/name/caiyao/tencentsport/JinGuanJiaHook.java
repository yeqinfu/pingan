package name.caiyao.tencentsport;


import java.util.Set;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findClass;

/**
 * Created by yeqinfu on 5/3/2018. 金管家内置计步器修改
 */

public class JinGuanJiaHook implements IXposedHookLoadPackage {
	public static String	JinGuanJia	= "com.pingan.lifeinsurance";
    private static boolean isOpen=false;

	@Override
	public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
       /* final Object activityThread = XposedHelpers.callStaticMethod(XposedHelpers.findClass("android.app.ActivityThread", null), "currentActivityThread");
        final Context systemContext = (Context) XposedHelpers.callMethod(activityThread, "getSystemContext");

        IntentFilter intentFilter = new IntentFilter();
        String SETTING_CHANGED = "name.caiyao.tencentsport.SETTING_CHANGED";
        intentFilter.addAction(SETTING_CHANGED);
        systemContext.registerReceiver(new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                isOpen = intent.getExtras().getBoolean("isOpen", false);

                XposedBridge.log("==onReceive======================"+isOpen);
            }
        }, intentFilter);*/


        if (loadPackageParam.packageName.equals(JinGuanJia)) {//
            if (!isOpen){
                isOpen=!isOpen;
                XposedBridge.log("金管家启动===============" + loadPackageParam.packageName);
                hook(loadPackageParam);
            }

		}
	}
    Set<XC_MethodHook.Unhook> mUnhookSet;
    private boolean x=false;
    private int i=0;
    private void hook(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        final Class<?> sensorEL = findClass("android.hardware.SystemSensorManager$SensorEventQueue", loadPackageParam.classLoader);
        XC_MethodHook dd=new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
              /*  if (!isOpen){
                    return;
                }*/
               /* ((float[]) param.args[1])[0] = ((float[]) param.args[1])[0] +10050;*/

               step=step%40000;
                ((float[]) param.args[1])[0] = ((float[]) param.args[1])[0] +step;

                LogApp.log("sensor = " + ((float[]) param.args[1])[0]+"===step="+step);
                i++;
                if (i%10==0){
                    step+=10100;
                }

            }
        };
        mUnhookSet=XposedBridge.hookAllMethods(sensorEL, "dispatchSensorEvent",dd);
    }

    int step=11000;


}
