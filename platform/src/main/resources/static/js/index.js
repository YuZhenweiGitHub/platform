var index_func = {
  initSocket : function () {

  },
  fileImport : function () {
      /*导入数据*/
      var FormDatas = new FormData($("#streetFileForm")[0]);
      var fileName=$("#streetFile").val();
      if(fileName == '') {
        layer.msg('请选择文件！');
        return false;
      }
      //验证文件格式
      var fileType = (fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length)).toLowerCase();
      if (fileType != 'xlsx' && fileType != 'xls') {
        layer.msg('文件格式不正确！');
        return false;
      }
      $.ajax({
        type:'post',
        url: basePath +'/street/uploadExcel.json',
        async : false,
        cache : false,
        contentType : false,
        processData : false,
        data:FormDatas,
        success: function(data){
          if(data == "error"){
            layer.msg("文件导入失败，请重新上传！", {
              shade: [0.3, '#393D49'], // 透明度  颜色
              time:5000
            });
            return false;
          }else{
            layer.msg("文件导入成功！", {
              shade: [0.3, '#393D49'], // 透明度  颜色
              time:5000
            });
            window.location.reload();
            return false;
          }
        },
        error : function(data){
          console.log(data.msg);
        }
      });
  }
};