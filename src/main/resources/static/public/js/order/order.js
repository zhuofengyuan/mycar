$(function(){
    var option = {
        url: '../order/list/sys',
        pagination: true,	//显示分页条
        sidePagination: 'server',//服务器端分页
        showRefresh: true,  //显示刷新按钮
        search: true,
        toolbar: '#toolbar',
        striped : true,     //设置为true会有隔行变色效果
        //idField: 'menuId',
        columns: [
            {
                field: 'id',
                title: '序号',
                width: 40,
                formatter: function(value, row, index) {
                    var pageSize = $('#table').bootstrapTable('getOptions').pageSize;
                    var pageNumber = $('#table').bootstrapTable('getOptions').pageNumber;
                    return pageSize * (pageNumber - 1) + index + 1;
                }
            },
            {checkbox:true},
            { title: '商品编码', field: 'id',sortable:true},
            { title: '商品名称', field: 'goodsName'},
            { title: '单价', field: 'goodsPrice', formatter: function(value, row, index) {return "￥" + value}},
            { title: '下单数量', field: 'qty'},
            { title: '总金额', field: 'goodsTotalPrice', formatter: function(value, row, index) {return "￥" + value}},
            { title: '状态', field: 'status', formatter: function(value, row, index) {
                var status = '待处理'
                if(row.status == 2){
                    status = '已发货'
                } else if(row.status == 3){
                    status = "已到货"
                }
                return status
            }}
        ]};
    $('#table').bootstrapTable(option);
});
var vm = new Vue({
    el:'#dtapp',
    data:{
        showList: true,
        title: null,
        sites:[],
        params:{}
    },
    methods:{
        del: function(){
            var id = 'id';
            var rows = getSelectedRows();
            if(rows == null){
                return ;
            }
            var ids = new Array();
            //遍历所有选择的行数据，取每条数据对应的ID
            $.each(rows, function(i, row) {
                ids[i] = row[id];
            });
            $.ajax({
                type: "GET",
                url: "/order/update/status",
                data: {ids: ids, status: 1},
                success : function(r) {
                    if(r.code === 0){
                        layer.alert('操作成功');
                        $('#table').bootstrapTable('refresh');
                    }else{
                        layer.alert(r.msg);
                    }
                },
                error : function() {
                    layer.alert('服务器没有返回数据，可能服务器忙，请重试');
                }
            });
        },
        add: function(){
            var id = 'id';
            var rows = getSelectedRows();
            if(rows == null){
                return ;
            }
            var ids = new Array();
            //遍历所有选择的行数据，取每条数据对应的ID
            $.each(rows, function(i, row) {
                ids[i] = row[id];
            });
            console.log(ids)
            $.ajax({
                type: "GET",
                url: "/order/update/status",
                data: {ids: ids, status: 2},
                success : function(r) {
                    if(r.code === 0){
                        layer.alert('操作成功');
                        $('#table').bootstrapTable('refresh');
                    }else{
                        layer.alert(r.msg);
                    }
                },
                error : function() {
                    layer.alert('服务器没有返回数据，可能服务器忙，请重试');
                }
            });

        },
        update: function (event) {
            var id = 'id';
            var rows = getSelectedRows();
            if(rows == null){
                return ;
            }
            var ids = new Array();
            //遍历所有选择的行数据，取每条数据对应的ID
            $.each(rows, function(i, row) {
                ids[i] = row[id];
            });
            $.ajax({
                type: "GET",
                url: "/order/update/status",
                data: {ids: ids, status: 3},
                success : function(r) {
                    if(r.code === 0){
                        layer.alert('操作成功');
                        $('#table').bootstrapTable('refresh');
                    }else{
                        layer.alert(r.msg);
                    }
                },
                error : function() {
                    layer.alert('服务器没有返回数据，可能服务器忙，请重试');
                }
            });
        },
        saveOrUpdate: function (event) {
            var url = vm.params.id == null ? "../sys/paramsroot/save" : "../sys/paramsroot/update";
            $.ajax({
                type: "POST",
                url: url,
                data: JSON.stringify(vm.params),
                success: function(r){
                    if(r.code === 0){
                        layer.alert('操作成功', function(index){
                            layer.close(index);
                            vm.reload();
                        });
                    }else{
                        layer.alert(r.msg);
                    }
                }
            });
        },
        reload: function (event) {
            vm.showList = true;
            $("#table").bootstrapTable('refresh');
        }
    }
});