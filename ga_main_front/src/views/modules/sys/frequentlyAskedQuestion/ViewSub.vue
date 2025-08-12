<template>
  <SubPage>
    <Card>
      <el-form
        ref="formData"
        :model="formData"
        :rules="dataRule"
        label-width="120px"
        @keyup.enter.native="dataFormSubmitHandle()"
      >
        <el-form-item :label="$t('news.title')" prop="title">
          <el-input v-model="formData.title" :placeholder="$t('news.title')" />
        </el-form-item>
        <el-form-item :label="$t('news.content')" prop="content">
          <!-- 富文本编辑器, 容器 -->
          <div id="J_quillEditor_FAQ" />
          <!-- 自定义上传图片功能 (使用element upload组件) -->
          <el-upload
            :action="uploadUrl"
            :show-file-list="false"
            :before-upload="uploadBeforeUploadHandle"
            :on-success="uploadSuccessHandle"
            style="display: none;"
          >
            <el-button ref="uploadBtn" type="primary" size="small">
              {{ $t("upload.button") }}
            </el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number
            v-model="formData.sort"
            :min="0"
            :label="$t('news.sort')"
            controls-position="right"
          />
        </el-form-item>
        <el-form-item :label="$t('news.pubDate')" prop="pubDate">
          <el-date-picker
            v-model="formData.pubDate"
            :placeholder="$t('news.pubDate')"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
          />
        </el-form-item>
      </el-form>
    </Card>
    <template slot="footer">
      <Btn :configs="{ type: BTN_TYPE.back, click: goMain }" />
      <Btn
        v-if="~['update', 'add', 'saveDeviceCopy'].indexOf(currentView)"
        ref="submitBtn"
        :configs="configsBtnDebounce"
      />
    </template>
  </SubPage>
</template>

<script>
import basePage from "@/mixins/basePage.ts";
import { mixinsSubPage, subPropCurrentView } from "@/mixins/subPage.ts";
import {
  DSLFormItems,
  DSLFormItemRule,
  MODULE_BASE_URL,
  DSLCopyFormItemsQuery,
} from "./dsl";
import { SuccessOrFailed, checkFormData } from "@/utils/index.ts";
import { defaultFormData } from "./state";
import { BTN_TYPE } from "@/components/Types";

import Cookies from "js-cookie";
import "quill/dist/quill.snow.css";
import Quill from "quill";
import { SITE_CONFIG } from "@/main.config";

/*模块名*/
const moduleName = "SysfrequentlyAskedQuestion";
/* 查询框store名 */
const queryFormKeyName = "formQueryData";


export default {
  name: "SysfrequentlyAskedQuestionSub",
  mixins: [basePage, mixinsSubPage, subPropCurrentView],
  data() {
    const handleFormSubmit = this.handleFormSubmit.bind(this);
    return {
      quillEditor: null,
      quillEditorToolbarOptions: [
        ["bold", "italic", "underline", "strike"],
        ["blockquote", "code-block", "image"],
        [{ header: 1 }, { header: 2 }],
        [{ list: "ordered" }, { list: "bullet" }],
        [{ script: "sub" }, { script: "super" }],
        [{ indent: "-1" }, { indent: "+1" }],
        [{ direction: "rtl" }],
        [{ size: ["small", false, "large", "huge"] }],
        [{ header: [1, 2, 3, 4, 5, 6, false] }],
        [{ color: [] }, { background: [] }],
        [{ font: [] }],
        [{ align: [] }],
        ["clean"],
      ],
      uploadUrl: "",
      formData: {
        title: "",
        content: "",
        sort: "",
        pubDate: "",
      },
      configsCFormQuery: {
        moduleName,
        keyForm: queryFormKeyName, //在state里面对应的表单索引字段
        DSLFormItems: DSLCopyFormItemsQuery, //查询表单的查询项描述
      },
      copySubmitData: [],
      configsSubPage: { moduleName },
      /*提交按钮*/
      configsBtnDebounce: {
        type: BTN_TYPE.save,
        textLoading: "正在提交...",
        debounceClick: handleFormSubmit,
      },
      formData: window._.cloneDeep(defaultFormData),
      configsCForm: {
        moduleName,
        DSLFormItems,
        DSLFormItemRule,
      },
    };
  },
computed: {
    dataRule() {
      var validateContent = (rule, value, callback) => {
        if (this.quillEditor.getLength() <= 1) {
          return callback(new Error(this.$t("validate.required")));
        }
        callback();
      };
      return {
        title: [
          {
            required: true,
            message: this.$t("validate.required"),
            trigger: "blur",
          },
        ],
        content: [
          {
            required: true,
            message: this.$t("validate.required"),
            trigger: "blur",
          },
          { validator: validateContent, trigger: "blur" },
        ],
        sort: [
          {
            required: true,
            message: this.$t("validate.required"),
            trigger: "blur",
          },
        ],
        pubDate: [
          {
            required: true,
            message: this.$t("validate.required"),
            trigger: "blur",
          },
        ],
      };
    },
  },
  created() {
    },
  mounted(){
    this.init();
  },
  methods: {
    init() {
      if (["update"].includes(this.currentView)) {
        this.getInfo(this.$route.query.id);
      }
      if (this.quillEditor) {
          this.quillEditor.deleteText(0, this.quillEditor.getLength());
        } else {
          this.quillEditorHandle();
        }
    },
    // 获取表单信息
    async getInfo(id) {
      this.isFormLoading = true;

      try {
        let { data: res } = await this.$http.get(`${MODULE_BASE_URL}/${id}`);
        SuccessOrFailed(
          res,
          {
            /*成功 没有msg属性则不弹出提示信息*/
            fn: () => {
              this.formData = checkFormData(defaultFormData, res.data);
              this.quillEditor.root.innerHTML = res.data.content;
            },
          },
          /*失败 msg必填*/
          { msg: res.msg }
        );
      } catch (error) {
        console.error(error);
      } finally {
        this.isFormLoading = false;
      }
    },
     // 富文本编辑器
    quillEditorHandle() {
      this.quillEditor = new Quill("#J_quillEditor_FAQ", {
        modules: {
          toolbar: this.quillEditorToolbarOptions,
        },
        theme: "snow",
      });
      // 自定义上传图片功能 (使用element upload组件)
      this.uploadUrl = `${
        SITE_CONFIG["apiURL"]
      }/sys/oss/upload?token=${Cookies.get("token")}`;
      this.quillEditor.getModule("toolbar").addHandler("image", () => {
        this.$refs.uploadBtn.$el.click();
      });
      // 监听内容变化，动态赋值
      this.quillEditor.on("text-change", () => {
        this.formData.content = this.quillEditor.root.innerHTML;
      });
    },
    // 上传图片之前
    uploadBeforeUploadHandle(file) {
      if (
        file.type !== "image/jpg" &&
        file.type !== "image/jpeg" &&
        file.type !== "image/png" &&
        file.type !== "image/gif"
      ) {
        this.$message.error("只支持jpg、png、gif格式的图片！");
        return false;
      }
    },
    // 上传图片成功
    uploadSuccessHandle(res, file, fileList) {
      if (res.code !== 0) {
        return this.$message.error(res.msg);
      }
      this.quillEditor.insertEmbed(
        this.quillEditor.getSelection().index,
        "image",
        res.data.src
      );
    },
    // 表单提交
    async handleFormSubmit() {
      await this.$refs["form"]?.formValidate();
      let { data: res } = await this.$http[
        !this.formData.id ? "post" : "put"
      ](MODULE_BASE_URL, {...this.formData,id:this.formData.id},{ headers: { "content-type": "application/x-www-form-urlencoded" }});
      return SuccessOrFailed(
        res,
        {
          msg: "表单信息已保存",
          fn: () => {
            this.$emit("refreshDataList");
            this.goMain();
          },
        },
        { msg: res.msg }
      );
    },
  },
};
</script>
