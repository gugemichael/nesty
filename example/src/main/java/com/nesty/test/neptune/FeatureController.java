package com.nesty.test.neptune;

import org.nesty.commons.annotations.*;
import org.nesty.commons.constant.http.RequestMethod;
import org.nesty.example.httpserver.handler.model.ProjectModel;
import org.nesty.example.httpserver.handler.model.ServiceResponse;

import java.util.List;

@RequestMapping("/web/api")
@Controller
public class FeatureController {
    @RequestMapping(value = "/feature/rename.json", method = RequestMethod.POST)
    public ServiceResponse rename(
            @RequestParam(value = "projectId", required = false) Long projectId,
            @RequestParam(value = "featureId", required = false) Long featureId,
            @RequestParam(value = "featureName", required = false) String featureName) {
        System.out.println(String.format("rename %d %d %s", projectId, featureId, featureName));
        return new ServiceResponse();
    }

    @RequestMapping(value = "/feature/getFeatureTree.json", method = RequestMethod.GET)
    public ServiceResponse getFeatureTree(
            @RequestParam(value = "projectId", required = false) Long projectId) {
        System.out.println(String.format("getFeatureTreek %d ", projectId));
        return new ServiceResponse();
    }

    @RequestMapping(value = "/feature/createFeature.json", method = RequestMethod.POST)
    public ServiceResponse createFeature(
            @RequestParam(value = "projectId", required = false) Long projectId,
            @RequestBody(required = false) ProjectModel createFeatureVo) {
        System.out.println(String.format("createFeaturek %s ", createFeatureVo.getProjectName()));
        return new ServiceResponse();
    }

    @RequestMapping(value = "/feature/updateFeature.json", method = RequestMethod.POST)
    public ServiceResponse updateFeature(
            @RequestParam(value = "projectId", required = false) Long projectId,
            @RequestParam(value = "featureId", required = false) Long featureId,
            @RequestBody ProjectModel updateFeatureParamVo) {
        System.out.println(String.format("updateFeaturek %s ", updateFeatureParamVo.getProjectName()));
        return new ServiceResponse();
    }

    @RequestMapping(value = "/feature/getFeature.json", method = RequestMethod.GET)
    public
    @ResponseBody
    ApiResult<Void> getFeature(
            @RequestParam(value = "projectId", required = false) Long projectId,
            @RequestParam(value = "featureId", required = false) Long featureId) {
        System.out.println(String.format("getFeature %d %d ", projectId, featureId));
        return new ApiResult<Void>();
    }

    @RequestMapping(value = "/feature/getFeatureList.json", method = RequestMethod.GET)
    public
    @ResponseBody
    ApiResult<Void> getFeatureList(
            @RequestParam(value = "projectId", required = false) Long projectId,
            @RequestParam(value = "type", required = false) Integer type,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "pageNum", required = false) Integer pageNum,
            @RequestParam(value = "orderField", required = false) String orderField,
            @RequestParam(value = "orderDirection", required = false) String orderDirection,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "modelId", required = false) Long modelId) {
        System.out.println(String.format("getFeatureList %d ", projectId));
        return new ApiResult<Void>();
    }

    @RequestMapping(value = "/feature/deleteFeature.json", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public
    @ResponseBody
    ApiResult<Void> deleteFeature(
            @RequestParam(value = "projectId", required = false) Long projectId,
            @RequestBody ProjectModel idWraperVo) {
        System.out.println(String.format("deleteFeaturee %d ", projectId));
        return new ApiResult<Void>();
    }

    @RequestMapping(value = "/feature/getCategory.json", method = RequestMethod.GET)
    public
    @ResponseBody
    ApiResult<Void> getCategory(
            @RequestParam(value = "projectId", required = false) Long projectId) {
        System.out.println(String.format("getCategory %d ", projectId));
        return new ApiResult<Void>();
    }

    @RequestMapping(value = "/feature/category.json", method = RequestMethod.GET)
    public
    @ResponseBody
    ApiResult<Void> getCategory(
            @RequestParam(value = "projectId", required = false) Long projectId,
            @RequestParam(value = "idPath", required = false) String idPath) {
        System.out.println(String.format("getCategory %d %s", projectId, idPath));
        return new ApiResult<Void>();
    }

    @RequestMapping(value = "/feature/getBrand.json", method = RequestMethod.GET)
    public
    @ResponseBody
    ApiResult<List<Void>> getBrand(
            @RequestParam(value = "projectId", required = false) Long projectId,
            @RequestParam(value = "categoryId", required = true) Long categoryId,
            @RequestParam(value = "brandName", required = false) String brandName,
            @RequestParam(value = "level", required = true) Integer level,
            @RequestParam(value = "idPath", required = false) String idPath)


    {
        System.out.println(String.format("getBrandy %d %s %s", projectId, idPath, brandName));
        return new ApiResult<List<Void>>();
    }


    @RequestMapping(value = "/feature/deleteFolder.json", method = RequestMethod.POST)
    public
    @ResponseBody
    ApiResult<Boolean> deleteFeatureFolder(@RequestBody ProjectModel idWraperVo,
                                           @RequestParam(value = "projectId", required = true) Long projectId) {
        System.out.println(String.format("deleteFeatureFolder %d %s", projectId, idWraperVo.getProjectName()));
        return new ApiResult<Boolean>();
    }

    @RequestMapping(value = "/feature/createFolder.json", method = RequestMethod.POST)
    public
    @ResponseBody
    ApiResult<Boolean> insertFeatureFolder(@RequestBody(required = true) ProjectModel body,
                                           @RequestParam(value = "projectId", required = true) Long projectId) {
        System.out.println(String.format("insertFeatureFolder %d %s", projectId, body.getProjectName()));
        return new ApiResult<Boolean>();
    }


    @RequestMapping(value = "/feature/updateFolder.json", method = RequestMethod.POST)
    public
    @ResponseBody
    ApiResult<Boolean> updateFeatureFolderName(
            @RequestBody ProjectModel body,
            @RequestParam(value = "projectId", required = true) Long projectId) {
        System.out.println(String.format("updateFeatureFolderName %d %s", projectId, body.getProjectName()));
        return new ApiResult<Boolean>();
    }
}