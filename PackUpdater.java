import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;


public class PackUpdater extends JPanel {
	ModPack pack;
	
	public PackUpdater() {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		pack = new ModPack();
		
		//JFileChooser chooser = new JFileChooser(new File("C:\\Users\\Greg\\Desktop\\Private pack staging\\test"));
		
		//chooser.showOpenDialog(this);
		
		//File file = chooser.getSelectedFile();
		
		//unzipFile(file);
	}
	
	public void setPack(ModPack pack) {
		this.pack = pack;
	}
	
	public void unzipFile(File fileIn) {
		String zipFile = fileIn.getAbsolutePath();
		System.out.println(zipFile);
		int BUFFER = 2048;
		File file = new File(zipFile);

		ZipFile zip;
		try {
			zip = new ZipFile(file, StandardCharsets.US_ASCII);
			String newPath = zipFile.substring(0, zipFile.length() - 4);

			new File(newPath).mkdir();
			Enumeration zipFileEntries = zip.entries();

			// Process each entry
			while (zipFileEntries.hasMoreElements()) {
				// grab a zip file entry
				ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
				String currentEntry = entry.getName();
				File destFile = new File(newPath, currentEntry);
				// destFile = new File(newPath, destFile.getName());
				File destinationParent = destFile.getParentFile();

				// create the parent directory structure if needed
				destinationParent.mkdirs();

				if (!entry.isDirectory()) {
					BufferedInputStream is = new BufferedInputStream(
							zip.getInputStream(entry));
					int currentByte;
					// establish buffer for writing file
					byte data[] = new byte[BUFFER];

					// write the current file to disk
					FileOutputStream fos = new FileOutputStream(destFile);
					BufferedOutputStream dest = new BufferedOutputStream(fos,
							BUFFER);

					// read and write until last byte is encountered
					while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
						dest.write(data, 0, currentByte);
					}
					dest.flush();
					dest.close();
					is.close();
				}

				if (currentEntry.endsWith(".zip")) {
					// found a zip file, try to open
					// extractFolder(destFile.getAbsolutePath());
				}
			}
		} catch (ZipException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
