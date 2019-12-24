<template>
  <div class="mod-config">
    <el-row>
      <el-col :span="4">
        <el-card>
          <div slot="header">
            <span>菜单列表</span>
          </div>
          <div>
            <el-tree
              :data="menuList"
              :props="menuListTreeProps"
              node-key="menuId"
              ref="menuListTree"
              @node-click="menuListTreeCurrentChangeHandle"
              :default-expand-all="true"
              :highlight-current="true"
              :expand-on-click-node="false">
            </el-tree>
          </div>
        </el-card>
      </el-col>
      <el-col :span="20">
        <el-card>
          <div slot="header">
            <span>菜单列表</span>
          </div>
          <div>
            <el-form :inline="true" :model="dataForm">
              <el-form-item>
                <el-button v-if="isAuth('sys:menu:save')" type="primary" @click="addHandle(menuParentId)">新增</el-button>
              </el-form-item>
            </el-form>
            <el-table
              :data="dataList"
              border
              style="width: 100%;">
              <el-table-column
                prop="menuId"
                header-align="center"
                align="center"
                width="80"
                label="ID">
              </el-table-column>
              <el-table-column
                prop="parentName"
                header-align="center"
                align="center"
                width="120"
                label="上级菜单">
              </el-table-column>
              <table-tree-column
                prop="name"
                header-align="center"
                treeKey="menuId"
                width="120"
                label="名称">
              </table-tree-column>
              <el-table-column
                header-align="center"
                width="50"
                align="center"
                label="图标">
                <template slot-scope="scope">
                  <icon-svg :name="scope.row.icon || ''"></icon-svg>
                </template>
              </el-table-column>
              <el-table-column
                prop="type"
                width="70"
                header-align="center"
                align="center"
                label="类型">
                <template slot-scope="scope">
                  <el-tag v-if="scope.row.type === 0" size="small">目录</el-tag>
                  <el-tag v-else-if="scope.row.type === 1" size="small" type="success">菜单</el-tag>
                  <el-tag v-else-if="scope.row.type === 2" size="small" type="info">按钮</el-tag>
                </template>
              </el-table-column>
              <el-table-column
                prop="orderNum"
                width="70"
                header-align="center"
                align="center"
                label="排序号">
              </el-table-column>
              <el-table-column
                prop="url"
                header-align="center"
                align="center"
                width="200"
                :show-overflow-tooltip="true"
                label="菜单URL">
              </el-table-column>
              <!--<el-table-column
                prop="perms"
                header-align="center"
                align="center"
                :show-overflow-tooltip="true"
                label="授权标识">
              </el-table-column>-->
              <el-table-column
                header-align="center"
                align="center"
                :show-overflow-tooltip="true"
                label="授权标识">
                <template slot-scope="scope">
                  <el-tag
                    :key="index"
                    v-for="(tag, index) in scope.row.permsObject"
                    :type="tag.type"
                    :disable-transitions="false"
                  >
                    {{tag.operatename}}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column
                fixed="right"
                header-align="center"
                align="center"
                width="120"
                label="操作">
                <template slot-scope="scope">
                  <el-button v-if="isAuth('sys:menu:update')" type="text" size="small" @click="addOrUpdateHandle(scope.row.menuId)">修改</el-button>
                  <el-button v-if="isAuth('sys:menu:delete')" type="text" size="small" @click="deleteHandle(scope.row.menuId)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-card>
      </el-col>
      </el-row>
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="refreshData"></add-or-update>
  </div>
</template>

<script>
import AddOrUpdate from './menu-add-or-update'
import { treeDataTranslate } from '@/utils'
import TableTreeColumn from '@/components/table-tree-column'
export default {
  data () {
    return {
      dataForm: {},
      dataList: [],
      dataListLoading: false,
      addOrUpdateVisible: false,
      menuList: [],
      menuParentId: 0,
      menuListTreeProps: {
        label: 'name',
        children: 'children'
      }
    }
  },
  components: {
    AddOrUpdate,
    TableTreeColumn
  },
  activated () {
    this.getDataList()
    this.initMenu()
  },
  methods: {
    refreshData () {
      this.getDataList()
      this.initMenu()
    },
    // 获取数据列表
    getDataList () {
      this.dataListLoading = true
      this.$http({
        url: this.$http.adornUrl('/admin/sys/menu/listbyid'),
        method: 'get',
        params: this.$http.adornParams({
          'parentid': this.menuParentId,
          //  'page': this.pageIndex,
          //  'limit': this.pageSize,
          'roleName': this.dataForm.roleName
        })
      }).then(({data}) => {
        data.forEach(row => {
          if (row.perms) {
            row.permsObject = JSON.parse(row.perms)
          }
        })
        // console.log(data)
        this.dataList = data
        this.dataListLoading = false
      })
    },
    // 新增 / 修改
    addOrUpdateHandle (id) {
      this.addOrUpdateVisible = true
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(id)
      })
    },
    // 新增
    addHandle (id) {
      this.addOrUpdateVisible = true
      this.$nextTick(() => {
        this.$refs.addOrUpdate.add(id)
      })
    },
    // 删除
    deleteHandle (id) {
      this.$confirm(`确定对[id=${id}]进行[删除]操作?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: this.$http.adornUrl(`/admin/sys/menu/delete/${id}`),
          method: 'delete',
          data: this.$http.adornData()
        }).then(({data}) => {
          if (data && data.code === 200) {
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.refreshData()
              }
            })
          } else {
            this.$message.error(data.msg)
          }
        })
      }).catch(() => {})
    },
    // 初始化菜单
    initMenu () {
      this.$http({
        url: this.$http.adornUrl('/admin/sys/menu/select'),
        method: 'get',
        params: this.$http.adornParams()
      }).then(({data}) => {
        this.menuList = treeDataTranslate(data.menuList, 'menuId')
      })
    },
    // 菜单树选中
    menuListTreeCurrentChangeHandle (data, node) {
      this.menuParentId = data.menuId
      // console.log(this.menuParentId)
      this.refreshData()
    }
  }
}
</script>
