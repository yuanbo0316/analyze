delete from st_service_bean where service_code in ('S34201','S34202'，'S34203');

insert into st_service_bean value ('S34201', 'getWarnInfo', '获取异常服务', 'Y');
insert into st_service_bean value ('S34202', 'getWarnDetail', '获取异常信息图表', 'Y');
insert into st_service_bean value ('S34203', 'getAllWarnDetail', '获取全部异常信息', 'Y');
