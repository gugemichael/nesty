package com.nesty.test.neptune;

import org.nesty.commons.annotations.*;
import org.nesty.commons.constant.http.RequestMethod;
import org.nesty.core.httpserver.rest.HttpSession;
import org.nesty.example.httpserver.handler.model.ProjectModel;

import java.util.List;

@RequestMapping("/web/api")
@Controller
public class ModelController {
    /**
     * 特征的处理 读取接口. enum类型
     */
    @RequestMapping(value = "/model/featureSettingEnum.json", method = RequestMethod.GET)
    public
    @ResponseBody
    ApiResult<Void> featureSettingEnumGet(
            @RequestParam(value = "projectId", required = false) Long projectId,
            @RequestParam(value = "modelId", required = false) Long modelId,
            @RequestParam(value = "featureId", required = false) Long featureId) {
        System.out.println(String.format("featureSettingEnumGet %d ", modelId));
        return new ApiResult<Void>();
    }

    /**
     * 特征的处理 设置接口. enum类型
     */
    @RequestMapping(value = "/model/featureSettingEnum.json", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public
    @ResponseBody
    ApiResult<Boolean> featureSettingEnumPost(
            @RequestParam(value = "projectId", required = false) Long projectId,
            @RequestBody ProjectModel featureSettingEnumVo) {
        System.out.println(String.format("featureSettingEnumGet %d ", projectId));
        return new ApiResult<Boolean>();
    }

    /**
     * 特征的处理 读取接口. numeric类型
     */
    @RequestMapping(value = "/model/featureSettingNumeric.json", method = RequestMethod.GET)
    public
    @ResponseBody
    ApiResult<Void> featureSettingNumericGet(
            @RequestParam(value = "projectId", required = false) Long projectId,
            @RequestParam(value = "modelId", required = false) Long modelId,
            @RequestParam(value = "featureId", required = false) Long featureId) {
        System.out.println(String.format("featureSettingNumericGet %d ", modelId));
        return new ApiResult<Void>();
    }

    /**
     * 特征的处理 设置接口. numeric类型
     */
    @RequestMapping(value = "/model/featureSettingNumeric.json", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public
    @ResponseBody
    ApiResult<Boolean> featureSettingNumericPost(
            @RequestParam(value = "projectId", required = false) Long projectId,
            @RequestBody ProjectModel featureSettingNumericVo) {
        System.out.println(String.format("featureSettingNumericGet %d ", featureSettingNumericVo.getProjectId()));
        return new ApiResult<Boolean>();
    }

    /**
     * 模型的 特征的 分析 展现接口 这里对数据库中的json数据直接返回即可
     */
    @RequestMapping(value = "/model/featureAnalysis.json", method = RequestMethod.GET)
    public
    @ResponseBody
    ApiResult<Void> getModelFeatureAnalysis(
            @RequestParam(value = "projectId", required = false) Long projectId,
            @RequestParam(value = "modelId", required = false) Long modelId,
            @RequestParam(value = "featureId", required = false) Long featureId) {
        System.out.println(String.format("getModelFeatureAnalysist %d ", featureId));
        return new ApiResult<Void>();
    }

    /**
     * 模型克隆
     */
    @RequestMapping(value = "/model/clone.json", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public
    @ResponseBody
    ApiResult<Long> clone(
            @RequestParam(value = "projectId", required = false) Long projectId,
            @RequestBody ProjectModel cloneModelVo) {
        System.out.println(String.format("getModelFeatureAnalysist %d ", projectId));
        return new ApiResult<Long>();
    }

    /**
     * 重命名
     */
    @RequestMapping(value = "/model/rename.json", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public
    @ResponseBody
    ApiResult<Boolean> rename(
            @RequestParam(value = "projectId", required = false) Long projectId,
            @RequestParam(value = "modelId", required = false) Long modelId,
            @RequestParam(value = "modelName", required = false) String modelName,
            HttpSession httpSession) {
        System.out.println(String.format("getModelFeatureAnalysist %d requestId %s", projectId, httpSession.getId()));
        return new ApiResult<Boolean>();
    }

    /**
     * 重命名文件夹
     */
    @RequestMapping(value = "/model/renameFolder.json", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public
    @ResponseBody
    ApiResult<Boolean> renameFolder(
            @RequestParam(value = "projectId", required = false) Long projectId,
            @RequestBody ProjectModel folderRenameVo, HttpSession httpSession) {
        System.out.println(String.format("renameFolder %d %s requestId %s", projectId, folderRenameVo.getProjectName(), httpSession.getId()));
        return new ApiResult<Boolean>();
    }

    /**
     * 功能：获取项目下的全部模型配置信息树.包括 文件夹和模型. 参数：project id 返回值：返回这个project下所有的模型列表
     */
    @RequestMapping(value = "/model/tree.json", method = RequestMethod.GET)
    public
    @ResponseBody
    ApiResult<List<Boolean>> getModelTree(
            @RequestParam(value = "projectId", required = false) final Long projectId,
            HttpSession httpSession) {
        System.out.println(String.format("getModelTree %d requestId %s", projectId, httpSession.getId()));
        return new ApiResult<List<Boolean>>();
    }

    /**
     * 功能：获取模型的详细信息,只获取模型本身的信息，不获取模型特征列表， 模型特征列表通过“/model/featureList.json”获取
     * 参数：project id 和 model id 返回值：返回模型的详细信息
     */
    @RequestMapping(value = "/model/detail.json", method = RequestMethod.GET)
    public
    @ResponseBody
    ApiResult<Boolean> getModelDetail(
            @RequestParam(value = "projectId", required = false) Long projectId,
            @RequestParam(value = "modelId", required = false) Long modelId, HttpSession httpSession) {
        System.out.println(String.format("getModelDetail %d %d requestId %s", projectId, modelId, httpSession.getId()));
        return new ApiResult<Boolean>();
    }

    /*
     * 功能：获取模型列表 参数： projectId:工程id status：要查询的模型状态 pageSize：每一页显示的模型的数量
     * pageNum：显示的页数编号 filterType：筛选类型 filterCondition：具体的删选条件，一个Json串
     * orderField: 排序字段 orderDirection：排序方向 返回值：返回模型列表信息
     */
    @RequestMapping(value = "/model/list.json", method = RequestMethod.GET)
    public
    @ResponseBody
    ApiResult<Boolean> getModelList(
            @RequestParam(value = "projectId", required = false) Long projectId,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "pageNum", required = false) Integer pageNum,
            @RequestParam(value = "filterType", required = false) Integer filterType,
            @RequestParam(value = "filterCondition", required = false) String filterCondition,
            @RequestParam(value = "orderField", required = false) String orderField,
            @RequestParam(value = "orderDirection", required = false) Boolean orderDirection,
            HttpSession httpSession) {
        System.out.println(String.format("getModelDetail %d %d requestId %s", projectId, pageSize, httpSession.getId()));
        return new ApiResult<Boolean>();
    }

    /*
     * 功能：获取模型训练结果报告信息 参数：project id 和 model id 返回值：返回模型训练结果报告
     */
    // 获取模型的训练结果信息，暂时返回一个String，需要再详细讨论
    @RequestMapping(value = "/model/report.json", method = RequestMethod.GET)
    public
    @ResponseBody
    ApiResult<Boolean> getModelReport(
            @RequestParam(value = "projectId", required = false) Long projectId,
            @RequestParam(value = "modelId", required = false) Long modelId, HttpSession httpSession) {
        System.out.println(String.format("getModelReport %d %d requestId %s", projectId, modelId, httpSession.getId()));
        return new ApiResult<Boolean>();
    }

    /**
     * 功能：新建一个model 参数：model的目录id， model名称， model使用的样本名称，model所属的项目名称
     * 返回值：新建的model的id
     */
    @RequestMapping(value = "/model/create.json", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public
    @ResponseBody
    ApiResult<Long> createModel(
            @RequestParam(value = "projectId", required = false) Long projectId,
            @RequestBody ProjectModel createModelParamVo, HttpSession httpSession) {
        System.out.println(String.format("createModel %d %d requestId %s", projectId, createModelParamVo.getProjectId(), httpSession.getId()));
        return new ApiResult<Long>();
    }

    /**
     * 功能：新建一个model的文件夹 参数：nodeId就是父文件夹的id,如果是0就是第一级的文件夹 返回值：boolean
     */
    @RequestMapping(value = "/model/createFolder.json", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public
    @ResponseBody
    ApiResult<Boolean> createModelFolder(
            @RequestParam(value = "projectId", required = false) Long projectId,
            @RequestBody ProjectModel createModelParamVo, HttpSession httpSession) {
        System.out.println(String.format("createModelFolder %d %d requestId %s", projectId, createModelParamVo.getProjectId(), httpSession.getId()));
        return new ApiResult<Boolean>();
    }

    /**
     * 功能：删除一个model的配置(对应实验的tree) 参数：project id， model id 返回值：删除成功与否
     */
    @RequestMapping(value = "/model/deleteConfig.json", method = RequestMethod.POST)
    public
    @ResponseBody
    ApiResult<Boolean> deleteModelConfig(
            @RequestParam(value = "projectId", required = false) Long projectId,
            @RequestBody ProjectModel idWraperVo, HttpSession httpSession) {
        System.out.println(String.format("deleteModelConfig %d %d requestId %s", projectId, idWraperVo.getProjectId(), httpSession.getId()));
        return new ApiResult<Boolean>();
    }

    /**
     * 功能：删除一个model的目录, 需要把它下面的内容全删掉.有递归操作 参数：project id， model id 返回值：删除成功与否
     */
    @RequestMapping(value = "/model/deleteFolder.json", method = RequestMethod.POST)
    public
    @ResponseBody
    ApiResult<Boolean> deleteModelFolder(
            @RequestParam(value = "projectId", required = false) Long projectId,
            @RequestBody ProjectModel idWraperVo, HttpSession httpSession) {
        System.out.println(String.format("deleteModelFolderr %d %d requestId %s", projectId, idWraperVo.getProjectId(), httpSession.getId()));
        return new ApiResult<Boolean>();
    }

    /**
     * 功能：删除一个model的报告(对应模型的列表) 参数：project id， model id 返回值：删除成功与否
     */
    @RequestMapping(value = "/model/deleteReport.json", method = RequestMethod.POST)
    public
    @ResponseBody
    ApiResult<Boolean> deleteModelReport(
            @RequestParam(value = "projectId", required = false) Long projectId,
            @RequestBody ProjectModel idWraperVo, HttpSession httpSession) {
        System.out.println(String.format("deleteModelReportr %d %d requestId %s", projectId, idWraperVo.getProjectId(), httpSession.getId()));
        return new ApiResult<Boolean>();
    }

    /**
     * 功能：更新model的信息（不包含模型特征信息） 参数：model的相关参数 返回值：更新操作成功与否的标志
     */
    @RequestMapping(value = "/model/update.json", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public
    @ResponseBody
    ApiResult<Boolean> updateModel(
            @RequestParam(value = "projectId", required = false) Long projectId,
            @RequestBody ProjectModel modelInfoVo, HttpSession httpSession) {
        System.out.println(String.format("updateModelr %d %d requestId %s", projectId, modelInfoVo.getProjectId(), httpSession.getId()));
        return new ApiResult<Boolean>();
    }

    /**
     * 功能：新增一条模型特征 参数：project id， model id， 模型特征id 返回值：新增操作成功与否的标志
     */
    @RequestMapping(value = "/model/addFeature.json", method = RequestMethod.POST)
    public
    @ResponseBody
    ApiResult<Boolean> addModelFeature(
            @RequestParam(value = "projectId", required = false) Long projectId,
            @RequestBody ProjectModel addFeatureVo, HttpSession httpSession) {
        System.out.println(String.format("addModelFeaturer %d %d requestId %s", projectId, addFeatureVo.getProjectId(), httpSession.getId()));
        return new ApiResult<Boolean>();
    }

    /**
     * 删除模型 和 删除模型报告 是分别删除的,互不影响显示 功能：删除一条模型特征 参数：project id， model id， model
     * feature id 返回值：删除操作成功与否的标志
     */
    @RequestMapping(value = "/model/deleteFeature.json", method = RequestMethod.POST)
    public
    @ResponseBody
    ApiResult<Boolean> deleteModelFeature(
            @RequestParam(value = "projectId", required = false) Long projectId,
            @RequestBody ProjectModel modelFeatureIdVo, HttpSession httpSession) {
        System.out.println(String.format("deleteModelFeaturer %d %d requestId %s", projectId, modelFeatureIdVo.getProjectId(), httpSession.getId()));
        return new ApiResult<Boolean>();
    }

    /**
     * 功能：获取模型特征列表 参数： projectId:工程id modelId：模型id pageSize：每一页显示的模型的数量
     * pageNum：显示的页数编号 orderField: 排序字段 orderDirection：排序方向 返回值：返回模型特征列表信息
     */
    @RequestMapping(value = "/model/featureList.json", method = RequestMethod.GET)
    public
    @ResponseBody
    ApiResult<Boolean> getModelFeatureList(
            @RequestParam(value = "projectId", required = false) Long projectId,
            @RequestParam(value = "modelId", required = false) Long modelId,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "pageNum", required = false) Integer pageNum,
            @RequestParam(value = "orderField", required = false) String orderField,
            @RequestParam(value = "orderDirection", required = false) Boolean orderDirection,
            HttpSession httpSession) {
        System.out.println(String.format("getModelFeatureList %d %d requestId %s", projectId, pageNum, httpSession.getId()));
        return new ApiResult<Boolean>();
    }

    /**
     * 功能：重新处理一个模型下的所有特征(初始化 和 常规的处理) 参数：project id， model id 返回值：删除成功与否
     */
    @RequestMapping(value = "/model/dealModelAllFeature.json", method = RequestMethod.POST)
    public
    @ResponseBody
    ApiResult<Boolean> dealModelAllFeature(
            @RequestParam(value = "projectId", required = false) Long projectId,
            @RequestBody ProjectModel idWraperVo, HttpSession httpSession) {
        System.out.println(String.format("dealModelAllFeature %d %d requestId %s", projectId, idWraperVo.getProjectId(), httpSession.getId()));
        return new ApiResult<Boolean>();
    }

    @RequestMapping(value = "/model/brief-detail.json", method = RequestMethod.GET)
    public
    @ResponseBody
    ApiResult<Boolean> getModelReportSettingDetail(
            HttpSession httpSession,
            @RequestParam(value = "projectId", required = true) Long projectId,
            @RequestParam(value = "modelId", required = true) Long modelId) {
        System.out.println(String.format("getModelReportSettingDetail %d %d requestId %s", projectId, modelId, httpSession.getId()));
        return new ApiResult<Boolean>();
    }

}