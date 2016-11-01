package Structures.Tree;

import java.util.ArrayList;
import java.util.LinkedList;
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
        return (this.children.size() != 0);
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
    public boolean hasChild(T data){
        for(int i = 0; i<this.children.size(); i++){
            if(this.getChild(i).data.equals(data)){
                return true;
            }
        }
        return false;
    }
    public void addChildren(List<T> newChildren){
        for(int i = 0; i<newChildren.size(); i++){
            this.addChild(newChildren.get(i));
        }
    }
    public void addTreeChildren(List<Tree> newChildren){
        this.children.addAll(newChildren);
    }
    public Tree getChild(int index){
        return this.getChildren().get(index);
    }
    public void forEachNode(TreeSearchCallback callback) {
        this.recursiveNodeSearch(callback);
    }
    private void recursiveNodeSearch(TreeSearchCallback callback){
        if(this.hasChildren()){
            System.out.println(this.children.size());
            for(int i = 0; i<this.children.size(); i++){
                this.getChild(i).recursiveNodeSearch(callback);
            }
        }
        callback.forEachNode(this);
    }
    public LinkedList<Integer> getPathFromRoot(){
        if(this.parent == null){
            return new LinkedList<>();
        }
        for(int i = 0; i<parent.children.size(); i++){
            if(parent.getChild(i).equals(this)){
                LinkedList<Integer> parentPath = parent.getPathFromRoot();
                parentPath.add(i);
                return parentPath;
            }
        }
        return new LinkedList<>();
    }
    public LinkedList<Integer> findPath(T toFind){ //Returns linked list of a route to take to find the said child
        if (this.hasChildren()) {
            for(int i = 0; i<this.getChildren().size(); i++){
                if(this.getChild(i).equals(toFind)){
                    LinkedList<Integer> toReturn = new LinkedList<Integer>();
                    toReturn.add(new Integer(i));
                    return toReturn;
                }
                LinkedList<Integer> possiblePath = this.getChild(i).findPath(toFind);
                if (possiblePath != null) {
                    LinkedList<Integer> path = new LinkedList<Integer>();
                    path.add(new Integer(i));
                    path.addAll(possiblePath);
                    return possiblePath;
                }
            }
        }
        return null;
    }
    public void print(){
        this.print(1);
    }
    private void print(int level) {
        for (int i = 1; i < level; i++) {
            System.out.print("\t"); //Newline
        }
        if(this.data == null){
            System.out.println("null");
        }
        else{
            System.out.println(this.data.getClass().getName());
        }
        for (Tree child : this.children) {
            child.print(level + 1);
        }
    }


}

