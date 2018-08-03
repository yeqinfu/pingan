package name.caiyao.tencentsport;

import java.util.UUID;

/**
 * Created by yeqinfu on 7/16/2018.
 */

public class ET_Log {
    public int taskId;
    public static final int LOG = UUID.randomUUID().hashCode();
    public static final int STEP = UUID.randomUUID().hashCode();
    public String msg;
    public int step;

    public ET_Log(int taskId, int step) {
        this.taskId = taskId;
        this.step = step;
    }

    public ET_Log(int taskId) {
        this.taskId = taskId;
    }

    public ET_Log(int taskId, String msg) {
        this.taskId = taskId;
        this.msg = msg;
    }
}
