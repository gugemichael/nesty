package org.nesty.example.httpserver.handler;

import org.nesty.commons.annotations.*;
import org.nesty.commons.constant.http.RequestMethod;
import org.nesty.example.httpserver.handler.model.ProjectModel;

@Controller
@RequestMapping("/projects")
public class ServiceController {

    // [POST] http://host:port/projects/1
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ServiceResponse createProject(@Body ProjectModel project) {
        System.out.println("createProject() projectName " + project.getProjectName());
        return new ServiceResponse();
    }

    // [GET] http://host:port/projects
    @RequestMapping("/")
    public ServiceResponse getAllProjects() {
        return new ServiceResponse();
    }

    // [GET] http://host:port/projects/1
    @RequestMapping("/{projectId}")
    public ServiceResponse getProjectById(@PathVariable("projectId") Integer projectId) {
        System.out.println("getProjectById() projectId " + projectId);
        return new ServiceResponse();
    }

    // [GET] http://host:port/projects/name/my_project1?owner=nesty
    @RequestMapping("/name/{projectName}")
    public ServiceResponse getProjectByName(@PathVariable("projectName") String projectName,
                                                                @RequestParam(value = "owner", required = false) String owner) {
        System.out.println("getProjectByNam() projectName " + projectName + ", owner " + owner);
        return new ServiceResponse();
    }

    // [UPDATE] http://host:port/projects/1
    @RequestMapping(value = "/{projectId}", method = RequestMethod.UPDATE)
    public ServiceResponse updateProjectNameById(@PathVariable("projectId") Integer projectId,
                                                                        @Body ProjectModel project) {
        System.out.println("updateProjectNameById projectId " + projectId + ". projectName " + project.getProjectName());
        return new ServiceResponse();
    }
}
