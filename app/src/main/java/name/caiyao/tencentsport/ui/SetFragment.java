package name.caiyao.tencentsport.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import name.caiyao.tencentsport.R;

/**
 * Created by yeqinfu on 5/10/2018.
 */

public class SetFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener, SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesMode(Context.MODE_PRIVATE);
        try{
            getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
            addPreferencesFromResource(R.xml.preference2);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       // findPreference("version").setSummary(BuildConfig.VERSION_NAME);
    }
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        String SETTING_CHANGED = "name.caiyao.tencentsport.SETTING_CHANGED";
        Intent intent = new Intent(SETTING_CHANGED);
        //intent.putExtra("isOpen", getPreferenceManager().getSharedPreferences().getBoolean("autoincrement", false));
        String s1=getPreferenceManager().getSharedPreferences().getString("exValue", "10000");
        Toast.makeText(getActivity(),s1,Toast.LENGTH_SHORT).show();
        intent.putExtra("exValue",s1 );
        if (getActivity() != null) {
            getActivity().sendBroadcast(intent);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        return false;
    }
}
