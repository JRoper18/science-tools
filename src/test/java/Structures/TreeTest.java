package Structures;

import Structures.Tree.Tree;
import Structures.Tree.TreeSearchCallback;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

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
    public void testFindParent() throws Exception {
        Tree<String> testTree = new Tree<String>();
        testTree.addChild("2");
        Tree nextBr = testTree.addChild("3");
        nextBr.addChild("3.5");
        nextBr = nextBr.addChild("4");
        nextBr.addChild("5");
        nextBr.addChild("6");
        nextBr = nextBr.addChild("7");
        LinkedList<Integer> path = (nextBr.getPathFromRoot()); //Gets a path to the "7" node
        Tree currentlySelected = testTree.getChild(path.get(0));
        for(int i = 1; i<path.size(); i++){
            currentlySelected = currentlySelected.getChild(path.get(i));
        }
        assertEquals("Should be 7", currentlySelected.data, "7");
    }

    @Test
    public void testSearch() throws Exception {
        TreeSearchCallback callback = (node) -> {
            node.addChild(3);
        };
        Tree testTree = new Tree();
        testTree.addChild("ReChild");
        testTree.forEachNode(callback);
        List<LinkedList<Integer>> paths = testTree.findPaths(3);
        assertEquals(2, paths.size());

    }
}