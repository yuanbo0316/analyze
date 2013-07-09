
delete from st_service_bean where service_code in ('S34103','S34301','S34302');

insert into st_service_bean value ('S34103', 'getHomeDetail', '获取首页展示信息', 'Y');
insert into st_service_bean value ('S34301', 'getReqWarn', '获取请求异常信息', 'Y');
insert into st_service_bean value ('S34302', 'getReqWarnCount', '获取请求异常统计', 'Y');