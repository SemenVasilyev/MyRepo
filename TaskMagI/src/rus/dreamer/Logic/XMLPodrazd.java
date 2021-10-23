package rus.dreamer.Logic;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Map;


public  class XMLPodrazd {

    /**
     * Распарсиваем наш файл config.xml
     */
    public static Map<Integer, String> Reader(File file) {
        Map<Integer, String> podrazd = new HashMap<Integer, String>();
        try {

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();


            NodeList nodeLst = doc.getElementsByTagName("RECORDS");
            String[][] pars = new String[1][2];
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
                                    if (thrdElement.getChildNodes().getLength() != 0) {
                                        if (pars[0][0] == null) {
                                            pars[0][0] = thrdElement.getChildNodes().item(0).getNodeValue();
                                        }
                                        else if (pars[0][1] == null) {
                                            pars[0][1] = thrdElement.getChildNodes().item(0).getNodeValue();
                                        }
                                        if (pars[0][0] != null && pars[0][1] != null){
                                            podrazd.put(Integer.parseInt(pars[0][0]), pars[0][1]);
                                            pars[0][0] = null;
                                            pars[0][1] = null;
                                        }

                                    }

                                }
                            }
                        }
                    }
                }
            }

        }
        catch (Exception e) {

            e.printStackTrace();
            Log.logAdd(e.getMessage(), "error");

        }
        return podrazd;
    }
}

