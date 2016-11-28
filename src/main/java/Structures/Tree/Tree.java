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

    /**
     * Returns whether or not this tree is the root and has no parent.
     * @return Boolean returning true if this node has no parent.
     */
    public boolean isRoot(){
        return (this.parent == null);
    }

    /**
     * Returns a list of this node's children
     * @return A list of this node's children
     */
    public List<Tree> getChildren() {
        return this.children;
    }

    /**
     * Returns this node's parent
     * @return This node's parent
     */
    public Tree getParent(){
        return this.parent;
    }

    /**
     * Creates a new tree with the data specified, and then add that new tree as a child of this one.
     * @param data The data of the node you want to add as a child
     * @return The child just added
     */
    public Tree addChild(Object data) { //Returns just added child
        Tree newChild = new Tree(this, data);
        this.children.add(newChild);
        return newChild;
    }

    /**
     * Adds a tree as a child of this tree
     * @param newTree Adds a tree as a child of this node
     * @return The input
     */
    public Tree addChild(Tree newTree){
        newTree.parent = this;
        this.children.add(newTree);
        return newTree;
    }

    /**
     * Returns true if one of our children has the child.
     * @param data The data of the child you are looking for
     * @return Boolean indicating if the child exists.
     */
    public boolean hasChild(T data){
        for(int i = 0; i<this.children.size(); i++){
            if(this.getChild(i).data.equals(data)){
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a list of children to this node.
     * @param newChildren A list of children to add as children to this node
     */
    public void addChildren(List<T> newChildren){
        for(int i = 0; i<newChildren.size(); i++){
            this.addChild(newChildren.get(i));
        }
    }

    /**
     * Adds multiple trees as children to this node.
     * @param newChildren The trees to add as children.
     */
    public void addTreeChildren(List<Tree> newChildren){
        this.children.addAll(newChildren);
    }

    /**
     * Gets the child
     * @param index The index of the child you want
     * @return The child at the specified index.
     */
    public Tree getChild(int index){
        if(index >= this.children.size()){
            return null;
        }
        return this.getChildren().get(index);
    }

    /**
     * Removes a child at the specified index
     * @param index The index of the child you want removed.
     */
    public void removeChild(int index){
        this.children.remove(index);
    }

    /**
     * Replaces this node
     * @param replace The new tree you want this to be
     */
    public void replaceThis(Tree<T> replace){
        if(this.parent == null){
            this.children = replace.getChildren();
            this.data = replace.data;
            return;
        }
        for(int i = 0; i<this.parent.getChildren().size(); i++){
            if(this.parent.getChild(i).equals(this)){
                this.parent.replaceChild(i, replace);
                break;
            }
        }
    }

    /**
     * Replaces this node's data.
     * @param replace The new data
     */
    public void replaceThis(T replace){
        this.data = replace;
    }

    /**
     * Replaces a child
     * @param index The index of the child you want to replace
     * @param replace What you want as your new child
     */
    public void replaceChild(int index, T replace){
        this.children.remove(index);
        this.children.add(index, new Tree(replace));
    }
    /**
     * Does the callback for every node in this tree.
     * @param callback The callback to execute on every node.
     */
    public void forEachNode(TreeSearchCallback callback) {
        this.recursiveNodeSearch(callback);
    }
    private void recursiveNodeSearch(TreeSearchCallback callback){
        if(this.hasChildren()){
            for(int i = 0; i<this.children.size(); i++){
                this.getChild(i).recursiveNodeSearch(callback);
            }
        }
        callback.forEachNode(this);
    }

    /**
     * Gets the path from the root node to this node.
     * @return A LinkedList
     */
    public LinkedList<Integer> getPathFromRoot(){
        if(this.parent == null){
            return new LinkedList<>();
        }
        for(int i = 0; i<parent.getChildren().size(); i++){
            if(parent.getChild(i).equals(this)){
                LinkedList<Integer> parentPath = parent.getPathFromRoot();
                parentPath.add(i);
                return parentPath;
            }
        }
        return new LinkedList<>();
    }

    /**
     * Returns a LinkedList. If you iterate through the linked list, getting the nth child of the next node, you will come to the node you are looking for.
     * @param toFind The object you are trying to find
     * @return A LinkedList of children to follow to get to your child from this node.
     */
    public List<LinkedList<Integer>> findPaths(T toFind){ //Returns linked list of a route to take to find the said child
        List<LinkedList<Integer>> paths = new ArrayList<LinkedList<Integer>>();
        if (this.hasChildren()) {
            for(int i = 0; i<this.getChildren().size(); i++){
                if(this.getChild(i).data.equals(toFind)){
                    LinkedList<Integer> toReturn = new LinkedList<Integer>();
                    toReturn.add(new Integer(i));
                    paths.add(toReturn);
                }
                List<LinkedList<Integer>> possiblePaths = this.getChild(i).findPaths(toFind);
                if(!possiblePaths.isEmpty()){
                    for(int j = 0; j<possiblePaths.size(); j++){
                        LinkedList<Integer> currentPath = possiblePaths.get(j);
                        currentPath.addFirst(i);
                        paths.add(currentPath);
                    }
                }
            }
        }
        return paths;
    }

    /**
     * Finds paths to all objects that are the same type as obj
     * @param obj The object to look for it's type
     * @return A LinkedList of children to follow to get to your child from this node.
     */
    public List<LinkedList<Integer>> findPathsOfType(Object obj){
        List<LinkedList<Integer>> paths = new ArrayList<LinkedList<Integer>>();
        if (this.hasChildren()) {
            for(int i = 0; i<this.getChildren().size(); i++){
                if(this.getChild(i).data.getClass().equals(obj.getClass())){
                    LinkedList<Integer> toReturn = new LinkedList<Integer>();
                    toReturn.add(new Integer(i));
                    paths.add(toReturn);
                }
                List<LinkedList<Integer>> possiblePaths = this.getChild(i).findPathsOfType(obj);
                if(!possiblePaths.isEmpty()){
                    for(int j = 0; j<possiblePaths.size(); j++){
                        LinkedList<Integer> currentPath = possiblePaths.get(j);
                        currentPath.addFirst(i);
                        paths.add(currentPath);
                    }
                }
            }
        }
        return paths;
    }

    /**
     * Follows a path to return the child
     * @param path The LinkedList path to follow
     * @return The child node that is along your path. Returns null is path leads nowhere.
     */
    public Tree<T> getChildThroughPath(LinkedList<Integer> path){
        if(path.isEmpty()){
            return this;
        }
        return this.continuePath(path, 0);
    }
    private Tree continuePath(LinkedList<Integer> path, int index){
        if(index == path.size()-1){
            return this;
        }
        return this.getChild(path.get(index)).continuePath(path, index + 1);
    }
    /**
     * Prints the tree to the console.
     */
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

