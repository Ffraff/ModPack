import java.io.File;



public class ModPack {
	private File packRoot;
	
	private String name = "";//
	private String author = "";//
	private String recomendedVersion = "1";//
	private String[] versions = {"1"};//
	private String shortName = "";//
	private String minecraftVersion = "1.4.7";//
	private String description = "";//
	private String[] modList = null;//
	private String packKey = "blank";//
	private String icon = "";
	private String splash = "";
	private String zip = "";
	private boolean packChanged = false;
	private boolean loading = true;
	
	public ModPack() {
		this("Blank Name","Blank Author","blank",new File(""));
	}
	
	public ModPack (String nameIn, String authorIn, String shortNameIn, File root) {
		name = nameIn;
		author = authorIn;
		shortName = shortNameIn;
		setRoot(root);
	}
	
	public File getPackRoot() {
		return packRoot;
	}

	public void setPackRoot(File packRoot) {
		if(!loading && !this.packRoot.equals(packRoot)) {
			packChanged = true;
			System.out.println("Root was changed");
		}
		this.packRoot = packRoot;
	}
	
	public void setRoot(File root) {
		packRoot = new File(root.getAbsolutePath()+"\\"+shortName);
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		if(!loading && !this.name.equals(name)) {
			packChanged = true;
			System.out.println("Name was changed");
		}
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		if(!loading && !this.author.equals(author)) {
			packChanged = true;
			System.out.println("Author was changed");
		}
		this.author = author;
	}

	public String getRecomendedVersion() {
		return recomendedVersion;
	}

	public void setRecomendedVersion(String recomendedVersion) {
		if(!loading && !this.recomendedVersion.equals(recomendedVersion)) {
			packChanged = true;
			System.out.println("Recomended Version was changed");
		}
		this.recomendedVersion = recomendedVersion;
	}
	
	public String getStringVersions(String[] versionsIn) {
		String stringVersions = versionsIn[0];
		for(int i=1; i<versionsIn.length; i++) {
			stringVersions = stringVersions+";"+versionsIn[i];
		}
		return stringVersions;
	}

	public String[] getVersions() {
		return versions;
	}

	public void setVersions(String[] versions) {
		if(!loading && !getStringVersions(this.versions).equals(getStringVersions(versions))) {
			packChanged = true;
			System.out.println("Versions was changed");
		}
		this.versions = versions;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		if(!loading && !this.shortName.equals(shortName)) {
			packChanged = true;
			System.out.println("ShortName was changed");
		}
		this.shortName = shortName;
	}

	public String getMinecraftVersion() {
		return minecraftVersion;
	}

	public void setMinecraftVersion(String minecraftVersion) {
		if(!loading && !this.minecraftVersion.equals(minecraftVersion)) {
			packChanged = true;
			System.out.println("Minecraft Version was changed");
		}
		this.minecraftVersion = minecraftVersion;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		if(!loading && !this.description.equals(description)) {
			packChanged = true;
			System.out.println("Description was changed");
		}
		this.description = description;
	}
	
	public String getStringModList(String[] modListIn) {
		String stringModList = "";
		if(modListIn.length > 0) stringModList = modListIn[0];
		for(int i=1; i<modListIn.length; i++) {
			stringModList = stringModList+"; "+modListIn[i];
		}
		return stringModList;
	}

	public String[] getModList() {
		return modList;
	}

	public void setModList(String[] modList) {
		if(!loading && !getStringModList(this.modList).equals(getStringModList(modList))) {
			packChanged = true;
			System.out.println("ModList was changed");
		}
		this.modList = modList;
	}

	public String getPackKey() {
		return packKey;
	}

	public void setPackKey(String packKey) {
		if(!loading && !this.packKey.equals(packKey)) {
			packChanged = true;
			System.out.println("PackKey was changed");
		}
		this.packKey = packKey;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		if(!loading && !this.icon.equals(icon)) {
			packChanged = true;
			System.out.println("Icon was changed");
		}
		this.icon = icon;
	}

	public String getSplash() {
		return splash;
	}

	public void setSplash(String splash) {
		if(!loading && !this.splash.equals(splash)) {
			packChanged = true;
			System.out.println("Splash was changed");
		}
		this.splash = splash;
	}

	public String getZipName() {
		return zip;
	}

	public void setZipName(String zip) {
		if(!loading && !this.zip.equals(zip)) {
			packChanged = true;
			System.out.println("Zip was changed");
		}
		this.zip = zip;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public void doneLoading() {
		loading = false;
	}
	
	public boolean isChanged() {
		return packChanged;
	}
	
	public String isChangedString() {
		if(packChanged) {
			return "true";
		} else {
			return "false";
		}
	}
}
