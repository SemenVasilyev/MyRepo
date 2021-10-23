package rus.dreamer.Logic;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public  class XMLReader {

    /**
     * Распарсиваем наш файл config.xml
     *
     * @param file
     * @return
     */
    public static Map<String, String> Reader(File file) {
        Map<String, String> config = new HashMap<String, String>();
        try {

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();


            NodeList nodeLst = doc.getElementsByTagName("settings");

            for (int i = 0; i < nodeLst.getLength(); i++) {
                Node fstNode = nodeLst.item(i);
                if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
                    NodeList fstNodeList = fstNode.getChildNodes(); // Берем все элементы в  Тэге <settings>

                    for (int j = 0; j < fstNodeList.getLength(); j++) {
                        Node scndNode = fstNodeList.item(j);
                        if (scndNode.getNodeType() == Node.ELEMENT_NODE) {
                            NodeList scndNodeList = scndNode.getChildNodes();

                            for (int k = 0; k < scndNodeList.getLength(); k++) {
                                Node thrdNode = scndNodeList.item(k);
                                if (thrdNode.getNodeType() == Node.ELEMENT_NODE) {
                                    Element thrdElement = (Element) thrdNode;
                                    if (thrdElement.getChildNodes().getLength() != 0)
                                        config.put(thrdElement.getTagName(), thrdElement.getChildNodes().item(0).getNodeValue());
                                    else
                                        config.put(thrdElement.getTagName(), "");

                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {

            e.printStackTrace();
            Log.logAdd(e.getMessage(), "error");

        }
        return config;
    }


    public static Boolean writer(Map<String, String> map) {
        File file = new File("config.xml");
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();

            // Переписываем дерево, текущими значениями
            for(Map.Entry<String,String> pair : map.entrySet()){
                String key = pair.getKey();
                String value = pair.getValue();
                NodeList nodeLst = doc.getElementsByTagName(key);
                nodeLst.item(0).setTextContent(value);
            }

            //Записываем в файл
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);

        } catch (Exception e) {
            e.printStackTrace();
            Log.logAdd(e.getMessage(), "error");
        }

        return true;
    }


}

