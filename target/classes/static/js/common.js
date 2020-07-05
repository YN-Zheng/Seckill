//展示loading
function g_showLoading(){
	var idx = layer.msg('处理中...', {icon: 16,shade: [0.5, '#f5f5f5'],scrollbar: false,offset: '0px', time:100000}) ;  
	return idx;
}

function login(){
	$("#loginForm").validate({

		// 验证通过后的回调方法
		submitHandler:function(form){
			doLogin();
		}
	});
}

function doLogin(){
	g_showLoading();

	var inputPass = $("#password").val();    // 用户输入的明文密码
	var salt = g_passsword_salt;
	var str = ""+salt.charAt(0)+salt.charAt(2) + inputPass +salt.charAt(5) + salt.charAt(4);
	var password = md5(str);

	var mobile = $("#mobile").val();
	$.ajax({
		url: "/login/do_login",
		type: "POST",
		data:{
			mobile:mobile,
			password: password
		},
		success:function(data){
			layer.closeAll();   // 关闭 Loading... 框
			if(data.code===0){
				//成功
				layer.msg(data.msg);
				window.location.href = "/goods/to_list";
			}else{
				layer.msg(data.msg );

			}
			console.log(data);
		},
		error:function(){
			layer.closeAll();
		}
	});
}

//salt
var g_passsword_salt="1a2b3c4d";
