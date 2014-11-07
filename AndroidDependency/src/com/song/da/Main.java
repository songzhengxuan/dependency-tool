
package com.song.da;

public class Main {

    public static void main(String[] args) {
        System.out.println("start analysis project" + args[0]);
        AndroidProjectDependency depency = new AndroidProjectDependency();
        AndroidProject project = depency.buildProjectTree(args[0]);
        depency.print(project);
    }

}
