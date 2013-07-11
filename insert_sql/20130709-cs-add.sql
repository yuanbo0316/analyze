delete from st_service_bean where service_code in ('S34104','S34105','S34106','S34107','S34108','S34109','S34110','S34111','S34204','S34205');

insert into st_service_bean value ('S34104', 'getAllUsername', '获取所有用户名', 'Y');
insert into st_service_bean value ('S34105', 'getUserLoginDetail', '获取所有用户名', 'Y');
insert into st_service_bean value ('S34106', 'getUserDetail', '获取登录人数', 'Y');
insert into st_service_bean value ('S34107', 'getLoginHis', '获取登录历史', 'Y');
insert into st_service_bean value ('S34108', 'getUsernameCountByIp', '获取不同ip用户名个数', 'Y');
insert into st_service_bean value ('S34109', 'getErrLogin', '获取失败登录', 'Y');
insert into st_service_bean value ('S34110', 'getErrLoginDetail', '获取失败登录统计', 'Y');
insert into st_service_bean value ('S34111', 'getLoginHisByIp', '获取IP登录历史', 'Y');
insert into st_service_bean value ('S34204', 'getSerivceRunTime', '获取服务运行时间(月)', 'Y');
insert into st_service_bean value ('S34205', 'getServiceRunDetail', '获取服务运行时间（天）', 'Y');
