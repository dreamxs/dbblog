<template>
  <el-dialog
    :title="!dataForm.categoryId ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="120px">
      <el-form-item label="菜单名称" prop="name">
        <el-input v-model="dataForm.name" placeholder="菜单名称"></el-input>
      </el-form-item>
      <el-form-item label="上级目录" prop="parentId">
        <el-popover
          ref="categoryListPopover"
          placement="bottom-start"
          v-model="popoverVisible"
          trigger="click">
          <el-tree
            :data="categoryList"
            :props="categoryListTreeProps"
            node-key="id"
            ref="categoryListTree"
            @node-click="categoryListTreeCurrentChangeHandle"
            :default-expand-all="true"
            :highlight-current="true"
            :expand-on-click-node="false">
          </el-tree>
          <el-input v-model="dataForm.parentName" slot="reference" :readonly="true" placeholder="点击选择上级分类" class="menu-list__input"></el-input>
        </el-popover>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { treeDataTranslate } from '@/utils'
export default {
  data () {
    return {
      visible: false,
      popoverVisible: false,
      dataForm: {
        rank: 0,
        type: '',
        parentId: 0,
        parentName: ''
      },
      dataRule: {
        name: [
          { required: true, message: '名称不能为空', trigger: 'blur' }
        ],
        parentId: [
          { required: true, message: '父主键不能为空', trigger: 'blur' }
        ]
      },
      categoryList: [],
      categoryListTreeProps: {
        label: 'name',
        children: 'children'
      }
    }
  },
  methods: {
    init (id) {
      // console.log(id)
      this.dataForm.id = (id === -1 || !id) ? '' : id
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
      })
      this.getCategorySelect()
      if (this.dataForm.id) {
        this.$http({
          url: this.$http.adornUrl(`/admin/operation/category/info/${this.dataForm.id}`),
          method: 'get',
          params: this.$http.adornParams()
        }).then(({data}) => {
          if (data && data.code === 200) {
            this.dataForm = data.category
          }
          this.categoryListTreeSetCurrentNode()
        })
      } else {
        this.dataForm = {
          rank: 0,
          type: '',
          parentId: -1,
          parentName: ''
        }
      }
    },
    // 获取目录列表
    getCategorySelect () {
      this.$http({
        url: this.$http.adornUrl('/admin/operation/category/select'),
        method: 'get',
        params: this.$http.adornParams({type: this.dataForm.type})
      }).then(({data}) => {
        if (data && data.code === 200) {
          this.categoryList = treeDataTranslate(data.categoryList)
        } else {
          this.categoryList = []
        }
      })
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.$http({
            url: this.$http.adornUrl(`/admin/operation/category/${!this.dataForm.id ? 'save' : 'update'}`),
            method: 'post',
            data: this.$http.adornData(this.dataForm)
          }).then(({data}) => {
            if (data && data.code === 200) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.visible = false
                  this.$emit('refreshDataList')
                }
              })
            } else {
              this.$message.error(data.msg)
            }
          })
        }
      })
    },
    categoryListTreeNode (data, node) {
      this.popoverVisible = false
    },
    // 分类列表树选中
    categoryListTreeCurrentChangeHandle (data, node) {
      this.dataForm.parentId = data.id
      this.dataForm.parentName = data.name
    },
    // 分类列表树设置当前选中节点
    categoryListTreeSetCurrentNode () {
      this.popoverVisible = false
      this.$refs.categoryListTree.setCurrentKey(this.dataForm.parentId)
      this.dataForm.parentName = (this.$refs.categoryListTree.getCurrentNode() || {})['name']
    }
  }
}
</script>
