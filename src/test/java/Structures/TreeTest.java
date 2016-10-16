package Structures;

import Structures.Tree.Tree;
import Structures.Tree.TreeSearchCallback;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * Created by jack on 10/7/2016.
 */
public class TreeTest {
    @Test
    public void testChildrenMethods() throws Exception {
        Tree<String> testTree = new Tree<String>();
        testTree.addChild("2");
        assertEquals("2", testTree.getChildren().get(0).data);
        Tree<String> testStr = testTree.addChild("TEST");
        assertEquals(true, testTree.getChildren().get(1).data.equals("TEST"));
        testStr.addChild(new String("FOUND ME!"));
        assertEquals(true, testStr.getChildren().get(0).data.equals("FOUND ME!"));
        assertEquals(true, testTree.getChild(1).getChild(0).data.equals("FOUND ME!"));
    }

    @Test
    public void testSearch() throws Exception {
        TreeSearchCallback callback = (node) -> node.addChild("ReChild");
        Tree testTree = new Tree();
        testTree.forEachNode(callback);
        TreeSearchCallback testCallbac = (node) -> {
            assertEquals(true, (node.hasChildren())? node.getChild(0).equals("ReChild") : true);
        };

    }
}