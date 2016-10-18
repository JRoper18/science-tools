package Structures.Tree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jack on 10/7/2016.
 */

public class Tree<T> {
    public T data;
    private Tree parent;
    private List<Tree> children = new ArrayList<Tree>();

    public Tree(Tree<T> parent) {
        this.parent = parent;
    }

    public Tree(T data) {
        this.parent = null;
        this.data = data;
    }
    public Tree(Tree<T> parent, T data) {
        this.parent = parent;
        this.data = data;
    }
    public Tree() {
        this.parent = null;
    }

    public boolean hasChildren(){
        return (this.children.size() == 0);
    }
    public boolean isRoot(){
        return (this.parent == null);
    }
    public List<Tree> getChildren() {
        return this.children;
    }
    public Tree getParent(){
        return this.parent;
    }
    public Tree addChild(Object data) { //Returns just added child
        Tree newChild = new Tree(this, data);
        this.children.add(newChild);
        return newChild;
    }
    public Tree addChild(Tree newTree){
        this.children.add(newTree);
        return newTree;
    }
    public Tree getChild(int index){
        return this.getChildren().get(index);
    }
    public void forEachNode(TreeSearchCallback callback) {
        this.recursiveNodeSearch(callback, this);
    }
    private void recursiveNodeSearch(TreeSearchCallback callback, Tree<T> node){
        if(node.hasChildren()){
            for(Tree currentNode: node.getChildren()){
                callback.forEachNode(currentNode);
            }
        }
    }
    public void print(){
        this.print(1);
    }
    private void print(int level) {
        for (int i = 1; i < level; i++) {
            System.out.print("\t"); //Newline
        }
        System.out.println(this.data.getClass().getName());
        for (Tree child : this.children) {
            child.print(level + 1);
        }
    }

}

