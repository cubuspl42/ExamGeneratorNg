package pl.edu.pg.examgeneratorng.util;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class DomUtils {
    public static List<Node> getChildren(Node node) {
        NodeList nodeList = node.getChildNodes();
        List<Node> resultList = new ArrayList<>(nodeList.getLength());
        for (int i = 0; i < nodeList.getLength(); ++i) {
            resultList.add(nodeList.item(i));
        }
        return resultList;
    }

    public static void removeAllChildren(Node node) {
        while (node.hasChildNodes())
            node.removeChild(node.getFirstChild());
    }

    public static void appendChildren(Node parent, List<Node> children) {
        children.forEach(parent::appendChild);
    }
}
