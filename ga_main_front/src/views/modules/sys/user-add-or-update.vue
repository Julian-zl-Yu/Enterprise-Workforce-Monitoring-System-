<template>
    <el-dialog
            :visible.sync="visible"
            :title="!dataForm.id ? $t('add') : $t('update')"
            :close-on-click-modal="false"
            :close-on-press-escape="false"
    >
        <el-form
                ref="dataForm"
                :model="dataForm"
                :rules="dataRule"
                label-width="120px"
                @keyup.enter.native="dataFormSubmitHandle()"
        >
            <el-form-item
                    :label="$t('user.username')"
                    prop="username"
            >
                <el-input
                        v-model="dataForm.username"
                        :placeholder="$t('user.username')"
                />
            </el-form-item>
            <el-form-item
                    :label="$t('user.deptName')"
                    prop="deptName"
            >
                <ren-dept-tree
                        v-model="dataForm.deptId"
                        :placeholder="$t('dept.title')"
                        :dept-name.sync="dataForm.deptName"
                />
            </el-form-item>
            <el-form-item
                    :label="$t('user.password')"
                    :class="{ 'is-required': !dataForm.id }"
                    prop="password"
            >
                <el-input
                        v-model="dataForm.password"
                        :placeholder="$t('user.password')"
                        type="password"
                />
            </el-form-item>
            <el-form-item
                    :label="$t('user.confirmPassword')"
                    :class="{ 'is-required': !dataForm.id }"
                    prop="confirmPassword"
            >
                <el-input
                        v-model="dataForm.confirmPassword"
                        :placeholder="$t('user.confirmPassword')"
                        type="password"
                />
            </el-form-item>
            <el-form-item
                    :label="$t('user.realName')"
                    prop="realName"
            >
                <el-input
                        v-model="dataForm.realName"
                        :placeholder="$t('user.realName')"
                />
            </el-form-item>
            <el-form-item
                    :label="$t('user.gender')"
                    prop="gender"
            >
                <ren-radio-group
                        v-model="dataForm.gender"
                        dict-type="GENDER"
                />
            </el-form-item>
            <el-form-item
                    :label="$t('user.email')"
                    prop="email"
            >
                <el-input
                        v-model="dataForm.email"
                        :placeholder="$t('user.email')"
                />
            </el-form-item>
            <el-form-item
                    :label="$t('user.mobile')"
                    prop="mobile"
            >
                <el-input
                        v-model="dataForm.mobile"
                        :placeholder="$t('user.mobile')"
                />
            </el-form-item>
            <el-form-item
                    :label="$t('user.roleIdList')"
                    prop="roleIdList"
                    class="role-list"
            >
                <el-select
                        v-model="dataForm.roleIdList"
                        :placeholder="$t('user.roleIdList')"
                        multiple
                >
                    <el-option
                            v-for="role in roleList"
                            :key="role.id"
                            :label="role.name"
                            :value="role.id"
                    />
                </el-select>
            </el-form-item>
            <el-form-item
                    :label="$t('user.status')"
                    prop="status"
                    size="mini"
            >
                <el-radio-group v-model="dataForm.status">
                    <el-radio :label="0">
                        {{ $t("user.status0") }}
                    </el-radio>
                    <el-radio :label="1">
                        {{ $t("user.status1") }}
                    </el-radio>
                </el-radio-group>
            </el-form-item>
        </el-form>
        <template slot="footer">
            <el-button @click="visible = false">
                {{ $t("cancel") }}
            </el-button>
            <el-button
                    type="primary"
                    @click="dataFormSubmitHandle()"
            >
                {{ $t("confirm") }}
            </el-button>
        </template>
    </el-dialog>
</template>

<script>
	import debounce from "lodash/debounce";
	import {isEmail, isMobile} from "@/utils/validate.ts";

	export default {
		data() {
			return {
				visible: false,
				roleList: [],
				roleIdListDefault: [],
				dataForm: {
					id: "",
					username: "",
					deptId: "",
					deptName: "",
					password: "",
					confirmPassword: "",
					realName: "",
					gender: 0,
					email: "",
					mobile: "",
					userType: "",
					roleIdList: [],
					status: 1
				}
			};
		},
		computed: {
			dataRule() {
				var validatePassword = (rule, value, callback) => {
					if (!this.dataForm.id && !/\S/.test(value)) {
						return callback(new Error(this.$t("validate.required")));
					}
					callback();
				};
				var validateConfirmPassword = (rule, value, callback) => {
					if (!this.dataForm.id && !/\S/.test(value)) {
						return callback(new Error(this.$t("validate.required")));
					}
					if (this.dataForm.password !== value) {
						return callback(new Error(this.$t("user.validate.confirmPassword")));
					}
					callback();
				};
				var validateEmail = (rule, value, callback) => {
					if (value && !isEmail(value)) {
						return callback(new Error(this.$t("validate.format", {"attr": this.$t("user.email")})));
					}
					callback();
				};
				var validateMobile = (rule, value, callback) => {
					if (value && !isMobile(value)) {
						return callback(new Error(this.$t("validate.format", {"attr": this.$t("user.mobile")})));
					}
					callback();
				};
				return {
					username: [
						{required: true, message: this.$t("validate.required"), trigger: "blur"}
					],
					deptName: [
						{required: true, message: this.$t("validate.required"), trigger: "change"}
					],
					password: [
						{validator: validatePassword, trigger: "blur"}
					],
					confirmPassword: [
						{validator: validateConfirmPassword, trigger: "blur"}
					],
					realName: [
						{required: true, message: this.$t("validate.required"), trigger: "blur"}
					],
					email: [
						{validator: validateEmail, trigger: "blur"}
					],
					mobile: [
						{validator: validateMobile, trigger: "blur"}
					]
				};
			}
		},
		methods: {
			init() {
				this.visible = true;
				this.dataForm.deptId = "";
				this.$nextTick(() => {
					this.$refs["dataForm"].resetFields();
					this.roleIdListDefault = [];
					Promise.all([
						this.getRoleList()
					]).then(() => {
						if (this.dataForm.id) {
							this.getInfo();
						}
					});
				});
			},
			// 获取角色列表
			getRoleList() {
				return this.$http.get("/sys/role/list").then(({data: res}) => {
					if (res.code !== 0) {
						return this.$message.error(res.msg);
					}
					this.roleList = res.data;
				}).catch(error => console.error(error));
			},
			// 获取信息
			getInfo() {
				this.$http.get(`/sys/user/${this.dataForm.id}`).then(({data: res}) => {
					if (res.code !== 0) {
						return this.$message.error(res.msg);
					}
					this.dataForm = {
						...this.dataForm,
						...res.data,
						roleIdList: []
					};
					// 角色配置, 区分是否为默认角色
					for (var i = 0; i < res.data.roleIdList.length; i++) {
						if (this.roleList.filter(item => item.id === res.data.roleIdList[i])[0]) {
							this.dataForm.roleIdList.push(res.data.roleIdList[i]);
							continue;
						}
						this.roleIdListDefault.push(res.data.roleIdList[i]);
					}
				}).catch(error => console.error(error));
			},
			// 表单提交
			dataFormSubmitHandle: debounce(function () {
				this.$refs["dataForm"].validate((valid) => {
					if (!valid) {
						return false;
					}
					this.$http[!this.dataForm.id ? "post" : "put"]("/sys/user", {
						...this.dataForm,
						roleIdList: [
							...this.dataForm.roleIdList,
							...this.roleIdListDefault
						]
					}).then(({data: res}) => {
						if (res.code !== 0) {
							return this.$message.error(res.msg);
						}
						this.$message({
							message: this.$t("prompt.success"),
							type: "success",
							duration: 500,
							onClose: () => {
								this.visible = false;
								this.$emit("refreshDataList");
							}
						});
					}).catch(error => console.error(error));
				});
			}, 1000, {"leading": true, "trailing": false})
		}
	};
</script>

<style lang="scss">
    .mod-sys__user {
        .role-list {
            .el-select {
                width: 100%;
            }
        }
    }
</style>
