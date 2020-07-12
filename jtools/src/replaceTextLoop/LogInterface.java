package replaceTextLoop;

//an interface for firing events from panes and other objects that eventually get displayed in ReplaceUI's log
public interface LogInterface {
	public void onLogOutput(String msg);
}
