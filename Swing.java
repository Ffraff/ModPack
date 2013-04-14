import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class Swing {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Window(args);
	}

	@SuppressWarnings("serial")
	private static class Window extends JFrame {
		private ModPacks packs;
		private File root = new File("C:\\Users\\Greg\\Desktop\\Private pack staging\\test\\Approved");
		private PackEditor editor;
		private JList<ModPack> list;
		private int currentlySelected = -1;
		
		
		public Window(String[] args) {
			this.setTitle("Modpack Builder"); // set the title
            this.setPreferredSize(new Dimension(800,600)); // and the initial size
            
            JSplitPane mainPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
            mainPane.setOneTouchExpandable(true);
            mainPane.setDividerLocation(200);
            this.add(mainPane);
            
            JPanel listPanel = new JPanel();
            listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
            
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(1,2));
            
            JButton addButton = new JButton("+");
            addButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					@SuppressWarnings("unused")
					NewPackWindow window = new NewPackWindow();
				}
            });
            buttonPanel.add(addButton);
            
            JButton updateButton = new JButton("~");
            updateButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					@SuppressWarnings("unused")
					UpdatePackWindow updateWindow = new UpdatePackWindow();
				}
            });
            buttonPanel.add(updateButton);
            
            buttonPanel.setPreferredSize(new Dimension(200,50));
            buttonPanel.setMaximumSize(new Dimension(200,50));

            listPanel.add(buttonPanel);
            
            packs = new ModPacks(root);
            
            list = new JList<ModPack>(packs);
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            list.addListSelectionListener(new listUpdater());
            
            loadPacks();
            
            JScrollPane scrList = new JScrollPane();
            scrList.getViewport().setView(list);
            scrList.setPreferredSize(new Dimension(200,this.getHeight()-50));
            
            listPanel.add(scrList);
            
            mainPane.setLeftComponent(listPanel);
            
            JTabbedPane tabbedPane = new JTabbedPane();
            
            JPanel rightPane = new JPanel();
            rightPane.setLayout(new BoxLayout(rightPane, BoxLayout.Y_AXIS));
            
            JPanel buttonPanel2 = new JPanel();
            buttonPanel2.setLayout(new BoxLayout(buttonPanel2, BoxLayout.X_AXIS));
            buttonPanel2.setAlignmentX(0f);
            
            JButton save = new JButton("Save");
            save.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					saveCurrentPack();
					savePacksToXML();
				}
            });
            buttonPanel2.add(save);
            
            rightPane.add(buttonPanel2, BorderLayout.NORTH);
            
            editor = new PackEditor();
            editor.setVisible(false);
            editor.setAlignmentX(0f);
            
            rightPane.add(editor);
            
            tabbedPane.addTab("Edit", rightPane);
            
            JPanel updatePane = new JPanel();
            
            updatePane.setLayout(new BoxLayout(updatePane, BoxLayout.Y_AXIS));
            
            JPanel buttonPanel3 = new JPanel();
            buttonPanel3.setAlignmentX(0f);
            buttonPanel3.setLayout(new BoxLayout(buttonPanel3, BoxLayout.X_AXIS));
            JButton loadUpdateButton = new JButton("Update pack");
            buttonPanel3.add(loadUpdateButton);
            JButton buildPackButton = new JButton("Build");
            buttonPanel3.add(buildPackButton);
            
            updatePane.add(buttonPanel3);
            
            PackUpdater updater = new PackUpdater();
            
            updatePane.add(updater);
            
            
            tabbedPane.addTab("Update", updatePane);
            
            mainPane.setRightComponent(tabbedPane);
            
            JMenuBar menuBar = new JMenuBar(); // create the menu
            JMenu menu = new JMenu("File"); // with the submenus
            menuBar.add(menu);
            
            JMenuItem keyList = new JMenuItem("Key List");
            
            	//listen to all the menu items and then add them to the menus
            keyList.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					String keys = "";
					for(int i=0; i<packs.getSize(); i++) {
						keys = keys+packs.elementAt(i).getPackKey()+",";
					}
					keys = keys.substring(0,keys.length()-1);
					new showStringWindow(keys);
				}
            });
            menu.add(keyList);
            
            this.setJMenuBar(menuBar); // set the main frame to use this menu bar
            
            pack();
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setVisible(true);
		}
		
		public void addPack(String name, String author, String shortName) {
			ModPack pack = new ModPack(name,author,shortName,root);
			pack.getPackRoot().mkdir();
			packs.addElement(pack);
			setSelectedPack(packs.getSize()-1);
		}
		
		public void setSelectedPack(int packNum) {
			saveCurrentPack();
			currentlySelected = packNum;
			
			ModPack pack = list.getModel().getElementAt(packNum);
			editor.setPack(pack);
			editor.setVisible(true);
			editor.repaint();
		}
		
		public void saveCurrentPack() {
			if(currentlySelected != -1) {
				DefaultListModel<ModPack> model = (DefaultListModel<ModPack>) list.getModel();
				model.setElementAt(editor.getPack(), currentlySelected);
			}
		}
		
		public void loadPacks() {
			packs.clear();
			
			File[] packDirs = root.listFiles(new DirectoryFilter());
			File[] filesInPack;
			ModPack pack;
			for (File dir : packDirs) {
				filesInPack = dir.listFiles(new XMLFilter());
				if (filesInPack.length > 0) {
					pack = XMLIO.readPackFromFile(filesInPack[0]);
					pack.doneLoading();
					packs.addElement(pack);
				}
			}
		}
		
		private class DirectoryFilter implements FileFilter {
			public boolean accept(File pathname) {
				if (pathname.isDirectory()) return true;
				else return false;
			}
		}
		
		private class XMLFilter implements FileFilter {
			public boolean accept(File pathname) {
				if (getExtension(pathname.getName()).equals("xml")) return true;
				else return false;
			}
			
			private String getExtension(String name) {
				String extension = "";
				
				int i = name.lastIndexOf('.');
				if (i > 0) {
				    extension = name.substring(i+1);
				}
				
				return extension;
			}
		}
	
		private class listUpdater implements ListSelectionListener {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(!(list.getSelectedIndex() == currentlySelected)) {
					setSelectedPack(list.getSelectedIndex());
				}
			}
	    }
		
		private void savePacksToXML() {
			XMLIO.writePackToFile(list.getModel().getElementAt(currentlySelected));
			XMLIO.writePacksToFile(packs);
		}
		
		private class NewPackWindow extends JFrame {
			private LabelAndField name;
			private LabelAndField author;
			private LabelAndField shortName;
			
			public NewPackWindow() {
				this.setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
				
				name = new LabelAndField("Name");
				author = new LabelAndField("Author");
				shortName = new LabelAndField("Short Name");
				
				this.add(name);
				this.add(author);
				this.add(shortName);
				
				JPanel buttonPanel = new JPanel();
				buttonPanel.setLayout(new GridLayout(1,1));
				
				JButton create = new JButton("Create");
				create.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						addPack(name.getFieldValue(),author.getFieldValue(),shortName.getFieldValue());
						dispose();
					}
				});
				buttonPanel.add(create);
				
				this.add(buttonPanel);

				Dimension size = new Dimension(400,140);
				this.setPreferredSize(size);
				this.setMaximumSize(size);
				pack();
	            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	            setVisible(true);
			}
			
			private class LabelAndField extends JPanel {
				JTextField field = null;
				
				public LabelAndField(String labelText) {
					this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
					this.setAlignmentX(0f);
					
					Dimension size = new Dimension(80,23);
					
					JLabel label = new JLabel(labelText);
					label.setPreferredSize(size);
					label.setMinimumSize(size);
					label.setMaximumSize(size);
					this.add(label);
					
					Dimension size2 = new Dimension(5000,(int) size.getHeight());
					
					field = new JTextField();
					field.setPreferredSize(size2);
					field.setMaximumSize(size2);
					this.add(field);
				}
				
				public String getFieldValue() {
					return field.getText();
				}
			}
		}
		
		private class UpdatePackWindow extends JFrame {
			private LabelAndField name;
			private LabelAndField author;
			private LabelAndField shortName;
			
			public UpdatePackWindow() {
				this.setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
				
				name = new LabelAndField("Name");
				author = new LabelAndField("Author");
				shortName = new LabelAndField("Short Name");
				
				this.add(name);
				this.add(author);
				this.add(shortName);
				
				JPanel buttonPanel = new JPanel();
				buttonPanel.setLayout(new GridLayout(1,1));
				
				JButton create = new JButton("Create");
				create.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						addPack(name.getFieldValue(),author.getFieldValue(),shortName.getFieldValue());
						dispose();
					}
				});
				buttonPanel.add(create);
				
				this.add(buttonPanel);

				Dimension size = new Dimension(400,140);
				this.setPreferredSize(size);
				this.setMaximumSize(size);
				pack();
	            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	            setVisible(true);
			}
			
			private class LabelAndField extends JPanel {
				JTextField field = null;
				
				public LabelAndField(String labelText) {
					this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
					this.setAlignmentX(0f);
					
					Dimension size = new Dimension(80,23);
					
					JLabel label = new JLabel(labelText);
					label.setPreferredSize(size);
					label.setMinimumSize(size);
					label.setMaximumSize(size);
					this.add(label);
					
					Dimension size2 = new Dimension(5000,(int) size.getHeight());
					
					field = new JTextField();
					field.setPreferredSize(size2);
					field.setMaximumSize(size2);
					this.add(field);
				}
				
				public String getFieldValue() {
					return field.getText();
				}
			}
		}
		
		private class showStringWindow extends JFrame {
			private showStringWindow(String string) {
				JTextArea field = new JTextArea();
				field.setLineWrap(true);
				field.setText(string);
				
				JScrollPane scroll = new JScrollPane(field);
				
				add(scroll);

	            setPreferredSize(new Dimension(300,300));
	            pack();
	            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	            setVisible(true);
			}
		}
	}
}
