
package com.song.da;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AndroidProjectDependency {

    public AndroidProject buildProjectTree(String folder) {
        HashSet<AndroidProject> builtProject = new HashSet<AndroidProject>();
        return buildProjectTree(builtProject, new File(folder));
    }

    public void print(AndroidProject project) {
        StringBuilder builder = new StringBuilder();
        print(builder, "", project);
        System.out.println(builder.toString());
    }

    void print(StringBuilder builder, String prefix, AndroidProject project) {
        builder.append(prefix + project.mProjectName + "-" + project.hashCode()).append("\n");
        for (AndroidProject subProject : project.mSubProjects) {
            print(builder, prefix + "    ", subProject);
        }
    }

    AndroidProject buildProjectTree(Set<AndroidProject> builtProjects, File projectFolder) {
        Path p1 = FileSystems.getDefault().getPath(projectFolder.getAbsolutePath());
        for (AndroidProject aBuiltProject : builtProjects) {
            Path p2 = FileSystems.getDefault().getPath(aBuiltProject.mProjectPath);
            try {
                if (Files.isSameFile(p1, p2)) {
                    return aBuiltProject;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String projectName = extractProjectName(projectFolder);
        String projectPath = projectFolder.getAbsolutePath();
        AndroidProject result = AndroidProject.createAndroidProject(projectName, projectPath);
        builtProjects.add(result);
        ArrayList<File> libProjectFiles = findLibProjectes(projectFolder);
        for (File aFile : libProjectFiles) {
            AndroidProject aLibProject = buildProjectTree(builtProjects, aFile);
            result.addLibProject(aLibProject);
        }

        return result;
    }

    ArrayList<File> findLibProjectes(File projectFolder) {
        File projectPropertiesFile = new File(projectFolder, "project.properties");
        if (!projectPropertiesFile.isFile()) {
            throw new IllegalAccessError("invalid project.properties file for " + projectFolder.getAbsolutePath());
        }
        ArrayList<File> result = new ArrayList<File>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(projectPropertiesFile));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("android.library.reference")) {
                    int start = line.indexOf("=");
                    String libProject = line.substring(start + 1);
                    result.add(new File(projectFolder, libProject));
                }
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
            }
        }
        return result;
    }

    String extractProjectName(File projectFolder) {
        File projectFile = new File(projectFolder, ".project");
        if (!projectFile.isFile()) {
            throw new IllegalAccessError("invalid .project file for " + projectFolder.getAbsolutePath());
        }
        BufferedReader reader = null;
        String result = null;
        try {
            reader = new BufferedReader(new FileReader(projectFile));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals("<projectDescription>")) {
                    line = reader.readLine();
                    int start = line.indexOf("<name>") + "<name>".length();
                    int end = line.indexOf("</name>");
                    result = line.substring(start, end);
                }
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
            }
        }
        return result;
    }
}
