import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
 
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
 
public class XMLIO {
	public static void writePackToFile(ModPack pack) {
		if(pack.isChanged()) {
			System.out.println("Pack "+pack.getName()+" is changed");
		}
		try {

			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			Element rootElement = doc.createElement("modpacks");
			doc.appendChild(rootElement);

			Element modpack = doc.createElement("modpack");

			modpack.setAttribute("name", pack.getName());
			modpack.setAttribute("author", pack.getAuthor());
			modpack.setAttribute("version", pack.getRecomendedVersion());
			modpack.setAttribute("repoVersion", pack.getRecomendedVersion().replace(".","_"));
			modpack.setAttribute("logo", pack.getIcon());
			modpack.setAttribute("url", pack.getZipName());
			modpack.setAttribute("image", pack.getSplash());
			modpack.setAttribute("dir", pack.getShortName());
			modpack.setAttribute("mcVersion", pack.getMinecraftVersion());
			modpack.setAttribute("serverPack", "");
			modpack.setAttribute("description", pack.getDescription());
			modpack.setAttribute("mods", pack.getStringModList(pack.getModList()));
			modpack.setAttribute("oldVersions", pack.getStringVersions(pack.getVersions()));

			rootElement.appendChild(modpack);

			// write the content into xml file
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			DOMSource source = new DOMSource(doc);

			File XMLOut = new File(pack.getPackRoot()+"\\"+pack.getPackKey()+".xml");
			if(XMLOut.exists()) {
				XMLOut.delete();
			}

			StreamResult result = new StreamResult(XMLOut);
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

			//System.out.println("File saved!");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}
	
	public static ModPack readPackFromFile(File file) {
		DocumentBuilder dBuilder;
		Document doc = null;
		try {
			dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			try {
				doc = dBuilder.parse(file);
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		}
		
		doc.getDocumentElement().normalize();

		Node nNode = doc.getElementsByTagName("modpack").item(0);
		
		ModPack pack = null;
		String temp[] = null;
		
		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
			 
			Element eElement = (Element) nNode;
			
			pack = new ModPack(eElement.getAttribute("name"),eElement.getAttribute("author"),
					eElement.getAttribute("dir"),file.getParentFile().getParentFile());
			pack.setRecomendedVersion(eElement.getAttribute("version"));
			pack.setMinecraftVersion(eElement.getAttribute("mcVersion"));
			pack.setDescription(eElement.getAttribute("description"));
			pack.setPackKey(file.getName().substring(0, file.getName().lastIndexOf('.')));
			pack.setIcon(eElement.getAttribute("logo"));
			pack.setSplash(eElement.getAttribute("image"));
			pack.setZipName(eElement.getAttribute("url"));
			
			temp = eElement.getAttribute("mods").split("; ");
			pack.setModList(temp);
			temp = eElement.getAttribute("oldVersions").split(";");
			pack.setVersions(temp);
		}
		
		return pack;
	}
	
	public static void writePacksToFile(ModPacks packs) {
		File modpacksFile = new File(packs.getRoot().getAbsolutePath()+"\\modpacks.xml");

		try {

			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			Element rootElement = doc.createElement("modpacks");
			doc.appendChild(rootElement);

			for(int i=0; i< packs.getSize(); i++) {
				Element modpack = doc.createElement("modpack");
				modpack.setAttribute("name",packs.elementAt(i).getShortName());
				Element modpackChanged = doc.createElement("changed");
				modpackChanged.setTextContent(packs.elementAt(i).isChangedString());
				modpack.appendChild(modpackChanged);
				rootElement.appendChild(modpack);
			}

			// write the content into xml file
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			DOMSource source = new DOMSource(doc);

			if(modpacksFile.exists()) {
				modpacksFile.delete();
			}

			StreamResult result = new StreamResult(modpacksFile);

			transformer.transform(source, result);

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}
	
	public static ModPacks readPacksFromFile(File file) {
		return null;
	}
}