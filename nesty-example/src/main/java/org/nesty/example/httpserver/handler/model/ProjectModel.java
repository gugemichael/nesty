package org.nesty.example.httpserver.handler.model;

import org.nesty.commons.annotations.Controller;

@Controller
public class ProjectModel {

    private Integer projectId;
    private String projectName;
    private String projectOwener;

    public Integer getProjectId() {
        return projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getProjectOwener() {
        return projectOwener;
    }
}

