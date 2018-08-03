package name.caiyao.tencentsport.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import name.caiyao.tencentsport.R;

/**
 * Created by yeqinfu on 8/2/2018.
 */

public class LogTask {
    @SuppressLint("SdCardPath")
    private static final String BASE_DIR_LEGACY = "/data/data/de.robv.android.xposed.installer/";
    public static final String BASE_DIR = Build.VERSION.SDK_INT >= 24
            ? "/data/user_de/0/de.robv.android.xposed.installer/" : BASE_DIR_LEGACY;
    private File mFileErrorLog = new File(BASE_DIR + "log/error.log");

    private File mFileErrorLogOld = new File(
            BASE_DIR + "log/error.log.old");
    private Context mContext;
    public  void clear() {
        try {
            new FileOutputStream(mFileErrorLog).close();
            mFileErrorLogOld.delete();
            if (mIgetLog!=null){
                mIgetLog.getLog("log is empty");
            }
            Toast.makeText(mContext, R.string.logs_cleared,
                    Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(mContext, mContext.getString(R.string.logs_clear_failed) + "n" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    public LogTask(Context context, IgetLog igetLog) {
        mContext =context;
        mIgetLog = igetLog;
        new LogsReader().execute(mFileErrorLog);
    }

    private class LogsReader extends AsyncTask<File, Integer, String> {

        private static final int MAX_LOG_SIZE = 1000 * 1024; // 1000 KB
      //  private MaterialDialog mProgressDialog;

        private long skipLargeFile(BufferedReader is, long length) throws IOException {
            if (length < MAX_LOG_SIZE)
                return 0;

            long skipped = length - MAX_LOG_SIZE;
            long yetToSkip = skipped;
            do {
                yetToSkip -= is.skip(yetToSkip);
            } while (yetToSkip > 0);

            int c;
            do {
                c = is.read();
                if (c == -1)
                    break;
                skipped++;
            } while (c != '\n');

            return skipped;

        }

        @Override
        protected void onPreExecute() {
         /*   if (mContext instanceof Activity){
                mProgressDialog = new MaterialDialog.Builder(mContext).content(R.string.loading).progress(true, 0).show();
            }*/
            Toast.makeText(mContext,R.string.loading,Toast.LENGTH_SHORT).show();

        }

        @Override
        protected String doInBackground(File... log) {
            Thread.currentThread().setPriority(Thread.NORM_PRIORITY + 2);

            StringBuilder llog = new StringBuilder(15 * 10 * 1024);
            try {
                File logfile = log[0];
                BufferedReader br;
                br = new BufferedReader(new FileReader(logfile));
                long skipped = skipLargeFile(br, logfile.length());
                if (skipped > 0) {
                    llog.append("-----------------\n");
                    llog.append("Log too long");
                    llog.append("\n-----------------\n\n");
                }

                char[] temp = new char[1024];
                int read;
                while ((read = br.read(temp)) > 0) {
                    llog.append(temp, 0, read);
                }
                br.close();
            } catch (IOException e) {
                llog.append("Cannot read log");
                llog.append(e.getMessage());
            }

            return llog.toString();
        }

        @Override
        protected void onPostExecute(String llog) {
          //  mProgressDialog.dismiss();

            if (mIgetLog!=null){
                if (llog.length() == 0){
                    mIgetLog.getLog(mContext.getResources().getResourceName(R.string.log_is_empty));
                }else{
                    mIgetLog.getLog(llog);
                }
            }


        }

    }

    IgetLog mIgetLog;
}

