
import Service.SaltService;
import View.MainView;

public class Start {
    public static void main(String[] args) {
		
		SaltService SS = new SaltService();

		SS.start();
		MainView.Instance().Index();
		
	}
}
