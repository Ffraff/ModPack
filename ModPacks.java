import java.io.File;

import javax.swing.DefaultListModel;


@SuppressWarnings("serial")
public class ModPacks extends DefaultListModel<ModPack> {
	private File root;
	
	public ModPacks(File rootIn) {
		root = rootIn;
	}
	
	public File getRoot() {
		return root;
	}
}
