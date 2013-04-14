import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileNameExtensionFilter;


@SuppressWarnings("serial")
public class PackEditor extends JPanel {
	private ModPack pack;
	private File root;
	
	private String[] minecraftVersions = {"1.4.7","1.5.1"};
	private String[] versions = {"1","2"};
	
	private LabelAndField name;
	private LabelAndField author;
	private LabelAndField shortName;
	private LabelAndField packKey;
	private LabelAndDropdown recomendedVersion;
	private LabelAndDropdown minecraftVersion;
	private LabelAndScrollableList versionList;
	private LabelAndField description;
	private LabelAndScrollableList modList;
	private LabelAndFile zip;
	private LabelAndFile icon;
	private LabelAndFile splash;
	
	public PackEditor() {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		pack = new ModPack();
		
		int height = 0;
		
		name = new LabelAndField("Name","");
		height+=name.getPreferredSize().getHeight();
		author = new LabelAndField("Author","");
		height+=author.getPreferredSize().getHeight();
		shortName = new LabelAndField("Short Name","");
		height+=shortName.getPreferredSize().getHeight();
		packKey = new LabelAndField("Pack Key","");
		height+=packKey.getPreferredSize().getHeight();
		versionList = new LabelAndScrollableList("Versions","[0-9]*[/.[0-9]*]*",true);
		height+=versionList.getPreferredSize().getHeight();
		recomendedVersion = new LabelAndDropdown("Recommend Version",versionList.getData());
		height+=recomendedVersion.getPreferredSize().getHeight();
		minecraftVersion = new LabelAndDropdown("Minecraft Version",minecraftVersions,minecraftVersions[0]);
		height+=minecraftVersion.getPreferredSize().getHeight();
		description = new LabelAndField("Description","",true,100);
		height+=description.getPreferredSize().getHeight();
		modList = new LabelAndScrollableList("Mod List",150,".*",true,false);
		height+=modList.getPreferredSize().getHeight();
		zip = new LabelAndFile("Zip","bla","zip","",true);
		height+=zip.getPreferredSize().getHeight();
		icon = new LabelAndFile("Icon","bla","png","Icon");
		height+=icon.getPreferredSize().getHeight();
		splash = new LabelAndFile("Splash","bla","png","Splash");
		height+=splash.getPreferredSize().getHeight();
		
		this.setMaximumSize(new Dimension(5000,height));
		
		this.add(name);
		this.add(author);
		this.add(shortName);
		this.add(packKey);
		this.add(recomendedVersion);
		this.add(minecraftVersion);
		this.add(versionList);
		this.add(description);
		this.add(modList);
		this.add(zip);
		this.add(icon);
		this.add(splash);
	}
	
	public void setPack(ModPack pack) {
		this.pack = pack;
		root = pack.getPackRoot();
		name.setFieldValue(pack.getName());
		author.setFieldValue(pack.getAuthor());
		versionList.setData(pack.getVersions());
		updateRecommendedVersion();
		shortName.setFieldValue(pack.getShortName());
		packKey.setFieldValue(pack.getPackKey());
		minecraftVersion.setDropdownValue(pack.getMinecraftVersion());
		description.setFieldValue(pack.getDescription());
		modList.setData(pack.getModList());
		zip.setFileName(pack.getZipName());
		icon.setFileName(pack.getIcon());
		splash.setFileName(pack.getSplash());
	}
	
	public ModPack getPack() {
		pack.setName(name.getFieldValue());
		pack.setAuthor(author.getFieldValue());
		pack.setVersions(versionList.getData());
		pack.setRecomendedVersion(recomendedVersion.getDropdownValue());
		pack.setShortName(shortName.getFieldValue());
		pack.setPackKey(packKey.getFieldValue()); //TODO
		pack.setMinecraftVersion(minecraftVersion.getDropdownValue());
		pack.setDescription(description.getFieldValue());
		pack.setModList(modList.getData());
		pack.setZipName(zip.getName());
		pack.setIcon(icon.getName());
		pack.setSplash(splash.getName());
		return pack;
	}
	
	public void updateRecommendedVersion() {
		recomendedVersion.setData(versionList.getData(),true);
		recomendedVersion.setDropdownValue(pack.getRecomendedVersion());
	}
	
	private class LabelAndField extends JPanel {
		JTextField field = null;
		JTextArea area = null;
		
		public LabelAndField(String labelText, String fieldDefault) {
			this(labelText, fieldDefault, false, 23);
		}
		
		public LabelAndField(String labelText, String fieldDefault, boolean scrolls, int height) {
			this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			this.setAlignmentX(0f);
			
			Dimension size = new Dimension(140,height);
			
			JLabel label = new JLabel(labelText);
			label.setPreferredSize(size);
			label.setMinimumSize(size);
			label.setMaximumSize(size);
			this.add(label);
			
			Dimension size2 = new Dimension(5000,(int) size.getHeight());
			
			if(scrolls) {
				area = new JTextArea(fieldDefault);
				area.setLineWrap(true);
				area.setWrapStyleWord(true);
				
				JScrollPane scrText = new JScrollPane(area);
				scrText.setPreferredSize(size2);
				scrText.setMaximumSize(size2);
			
				this.add(scrText);
			} else {
				field = new JTextField(fieldDefault);
				
				field.setPreferredSize(size2);
				field.setMaximumSize(size2);
				this.add(field);
			}
			
            
		}
		
		public String getFieldValue() {
			if(field != null) {
				return field.getText();
			} else {
				return area.getText();
			}
		}
		
		public void setFieldValue(String value) {
			if(field != null) {
				field.setText(value);
			} else {
				area.setText(value);
			}
		}
	}
	
	private class LabelAndDropdown extends JPanel {
		private JComboBox<String> dropdown;
		
		public LabelAndDropdown(String labelText, String[] data) {
			this(labelText, data, "bla");
		}
		
		public LabelAndDropdown(String labelText, String[] data, String defaultValue) {
			this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			this.setAlignmentX(0f);
			
			Dimension size = new Dimension(140,20);
			
			JLabel label = new JLabel(labelText);
			label.setPreferredSize(size);
			label.setMinimumSize(size);
			label.setMaximumSize(size);
			this.add(label);
			
			Dimension size2 = new Dimension(5000,(int) size.getHeight());
			
			dropdown = new JComboBox<String>(data);
			setDropdownValue(defaultValue);
			dropdown.setPreferredSize(size2);
			dropdown.setMaximumSize(size2);
			this.add(dropdown);
		}
		
		@SuppressWarnings("unused")
		public String[] getData() {
			String[] data = new String[dropdown.getModel().getSize()];
			for(int i=0;i<data.length;i++) {
				data[i] = dropdown.getModel().getElementAt(i);
			}
			return data;
		}
		
		public void setData(String[] data, boolean fromUpdater) {
			dropdown.removeAllItems();
			for(int i=0; i<data.length; i++) {
				dropdown.addItem(data[i]);
			}
		}
		
		public String getDropdownValue() {
			return (String) dropdown.getSelectedItem();
		}
		
		public void setDropdownValue(String value) {
			dropdown.setSelectedItem(value);
		}
	}
	
	private class LabelAndScrollableList extends JPanel {
		private JList<String> textList;
		private DefaultListModel<String> model;
		private boolean isVersionList;
		private String validator;
		
		public LabelAndScrollableList(String labelText, String validator, boolean isVersionList) {
			this(labelText, 50, validator, false, isVersionList);
		}
		
		public LabelAndScrollableList(String labelText, int height, String validator,
				boolean semicolonListEdit, boolean isVersionList) {
			this.isVersionList = isVersionList;
			this.validator = validator;
			this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			this.setAlignmentX(0f);
			
			Dimension size = new Dimension(140,20);
			
			JLabel label = new JLabel(labelText);
			label.setPreferredSize(size);
			label.setMinimumSize(size);
			label.setMaximumSize(size);
			this.add(label);
			
			Dimension size2 = new Dimension(5000,height);
			
			JPanel mainPanel = new JPanel();
			mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.X_AXIS));
            mainPanel.setPreferredSize(size2);
            mainPanel.setMaximumSize(size2);
			
			model = new DefaultListModel<String>();
			setData(versions);
			
			textList = new JList<String>(model);
        	textList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        	
        		// create a scroll pane with the text area in it
            JScrollPane scrText = new JScrollPane();
            scrText.getViewport().setView(textList);
            
            mainPanel.add(scrText);

			Dimension size3 = new Dimension(50,height);
            
            JPanel buttonPanel = new JPanel();
            int panels = 2;
            if(semicolonListEdit) panels = 3;
            buttonPanel.setLayout(new GridLayout(panels,1));
            buttonPanel.setPreferredSize(size3);
            buttonPanel.setMaximumSize(size3);
            
            JButton add = new JButton("+");
            add.addActionListener(new buttonListener());
            buttonPanel.add(add);
            
            JButton remove = new JButton("-");
            remove.addActionListener(new buttonListener());
            buttonPanel.add(remove);
            
            if(semicolonListEdit) {
                JButton edit = new JButton("~");
                edit.addActionListener(new buttonListener());
                buttonPanel.add(edit);
            }
            
            mainPanel.add(buttonPanel);
            
            this.add(mainPanel);
		}
		
		public String[] getData() {
			ListModel<String> data = textList.getModel();
			ArrayList<String> out = new ArrayList<String>();
			
			for(int i=0; i<data.getSize(); i++) {
				out.add(data.getElementAt(i));
			}
			
			return out.toArray(new String[out.size()]);
		}
		
		public void setData(String[] data) {
			model.clear();
			if(data == null) {
				
			} else {
				for(int i=0;i<data.length;i++) {
					model.addElement(data[i]);
				}
			}
		}
		
		private class buttonListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(((AbstractButton) e.getSource()).getText() == "+") {
					String newVersion = JOptionPane.showInputDialog(null, "Version to add", 
							 "Version adder", JOptionPane.PLAIN_MESSAGE);
					if(newVersion != null) {
						if(newVersion.matches(validator)) {
							model.add(0,newVersion);
							if(isVersionList) {
								File versionFolder = new File(root+"\\"+newVersion.replace(".","_"));
								versionFolder.mkdir();
								updateRecommendedVersion();
							}
						} else {
							JOptionPane.showMessageDialog(null, "Invalid Version");
						}
					}
				} else if (((AbstractButton) e.getSource()).getText() == "-"){
					if(textList.getSelectedIndex() != -1) {
						model.remove(textList.getSelectedIndex());
					}
				} else {
					new semicolonListEditor();
				}
			}
		}
		
		private class semicolonListEditor extends JFrame {
			private JTextArea editor;
			
			public semicolonListEditor() {
				this.setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
				
				String[] data = getData();
				String stringData = "";
				for(int i=0;i<data.length;i++) {
					stringData = stringData + data[i] + "; ";
				}
				if(stringData.length()>2) {
					stringData = stringData.substring(0,stringData.length()-2);
				} else {
					stringData = "";
				}
				
				Dimension size = new Dimension(600,300);
				
				editor = new JTextArea();
				editor.setLineWrap(true);
				editor.setText(stringData);
				editor.setWrapStyleWord(true);
				
				JScrollPane scrText = new JScrollPane();
	            scrText.getViewport().setView(editor);
	            scrText.setPreferredSize(size);
	            scrText.setMaximumSize(size);
	            
				this.add(scrText);
				
				Dimension size2 = new Dimension(600,20);
				
				JPanel buttonPanel = new JPanel();
				buttonPanel.setLayout(new GridLayout(1,2));
				buttonPanel.setPreferredSize(size2);
				buttonPanel.setMaximumSize(size2);
				
				JButton saveAndClose = new JButton("Save and Close");
				saveAndClose.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						String[] dataOut = editor.getText().split(";");
						for(int i=0; i<dataOut.length; i++) {
							dataOut[i] = dataOut[i].trim();
						}
						setData(dataOut);
						dispose();
					}
				});
				buttonPanel.add(saveAndClose);
				
				JButton cancelAndClose = new JButton("Cancel and Close");
				cancelAndClose.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				buttonPanel.add(cancelAndClose);
				
				this.add(buttonPanel);
				
				pack();
	            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	            setVisible(true);
			}
		}
	}
	
	private class LabelAndFile extends JPanel {
		private JTextField field;
		private String fileType;
		private String nameExtension;
		private boolean isVersioned;
		private File file;
		
		public LabelAndFile(String labelText, String name, String fileType, String nameExtension) {
			this(labelText, name, fileType, nameExtension, false);
		}
		
		public LabelAndFile(String labelText, String name, String fileType, 
				String nameExtension, boolean isVersioned) {
			this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			this.setAlignmentX(0f);
			
			this.isVersioned = isVersioned;
			this.fileType = fileType;
			this.nameExtension = nameExtension;
			
			Dimension size = new Dimension(140,20);
			
			JLabel label = new JLabel(labelText);
			label.setPreferredSize(size);
			label.setMinimumSize(size);
			label.setMaximumSize(size);
			this.add(label);
			
			field = new JTextField(name);
			field.setEditable(false);
			this.add(field);
			
			JButton edit = new JButton("edit");
			edit.addActionListener(new newFile());
			this.add(edit);
		}
		
		public String getName() {
			return file.getName();
		}
		
		public void setFileName(String name) {
			String path = root.getAbsolutePath();
			if(isVersioned) {
				path = addRecomendedVersion(path);
			}
			path = path+"\\"+name;
			file = new File(path);
			checkFile(name);
		}
		
		private void checkFile(String name) {
			if(file.exists()) {
				field.setText(name);
				
			} else {
				field.setText(name+" is an invalid file, reselect");
			}
		}
		
		public String addRecomendedVersion(String file) {
			return file+"\\"+recomendedVersion.getDropdownValue().replace(".", "_");
		}
		
		private class newFile implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				final JFileChooser chooser = new JFileChooser(root);
				chooser.setFileFilter(new FileNameExtensionFilter(fileType+" files",fileType));
				int returnVal = chooser.showOpenDialog(null);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					File result = chooser.getSelectedFile();
					File destination;
					String name = shortName.getFieldValue()+nameExtension+"."+fileType;
					if(isVersioned) {
						destination = new File(addRecomendedVersion(
								root.getAbsolutePath())+"\\"+name);
						if(!destination.getParentFile().exists()) destination.getParentFile().mkdir();
					} else {
						destination = new File(root.getAbsolutePath()+"\\"+name);
					}
					result.renameTo(destination);
					file = destination;
					checkFile(destination.getName());
				}
			}
		}
	}
}
