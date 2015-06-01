// my assets
import assets.*;

public class appRun {
    
    public static void main(String[] args) {   
        
        new Thread( () -> {
			try {
				new Library().go();
            }
            catch(Exception e) {
				e.printStackTrace();
			}
		}).start();
        
    }
    
}