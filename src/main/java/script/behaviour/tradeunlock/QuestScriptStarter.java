package script.behaviour.tradeunlock;
import org.dreambot.api.script.ScriptManager;

public class QuestScriptStarter implements Runnable{
    @Override
    public void run() {
        ScriptManager manager = ScriptManager.getScriptManager();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        manager.start("tmp\'s Ten QP");
    }
}