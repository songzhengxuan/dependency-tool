
package com.song.da;

import java.util.ArrayList;

public class AndroidProject {
    public static AndroidProject createAndroidProject(String projectName, String projectPath) {
        AndroidProject result = new AndroidProject();
        result.mProjectName = projectName;
        result.mProjectPath = projectPath;
        return result;
    }

    public String mProjectName;

    public String mProjectPath;

    public ArrayList<AndroidProject> mSubProjects = new ArrayList<AndroidProject>();

    public void addLibProject(AndroidProject libProject) {
        mSubProjects.add(libProject);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AndroidProject other = (AndroidProject) obj;
        if (mProjectName == null) {
            if (other.mProjectName != null)
                return false;
        } else if (!mProjectName.equals(other.mProjectName))
            return false;
        if (mProjectPath == null) {
            if (other.mProjectPath != null)
                return false;
        } else if (!mProjectPath.equals(other.mProjectPath))
            return false;
        if (mSubProjects == null) {
            if (other.mSubProjects != null)
                return false;
        } else if (!mSubProjects.equals(other.mSubProjects))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mProjectName == null) ? 0 : mProjectName.hashCode());
        result = prime * result + ((mProjectPath == null) ? 0 : mProjectPath.hashCode());
        result = prime * result + ((mSubProjects == null) ? 0 : mSubProjects.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "AndroidProject [mProjectName=" + mProjectName + ", mProjectPath=" + mProjectPath + "]";
    }
}
