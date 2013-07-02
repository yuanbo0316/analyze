delete from st_service_bean where service_code in ('S34001','S34002', 'S34101');

insert into st_service_bean value ('S34001', 'userLoginPl', '用户登录', 'N');
insert into st_service_bean value ('S34002', 'userLogoutPl', '用户注销', 'N');
insert into st_service_bean value ('S34101', 'regJtgzfwUser', '用户注册分析', 'Y');

delete from st_err_msg where err_code in (310003);

insert into st_err_msg value (310003, '用户名或密码错误');

commit;
