delete from st_service_bean where service_code in ('P31001', 'P31002','S31001','P31003', 'P31004', 'P31005', 'P31006', 'P31007','P31008');

insert into st_service_bean value ('P31001', 'saveTimeoutSql', '保存运行超时的SQL', 'N');
insert into st_service_bean value ('P31002', 'saveFlashSql', '保存SQL运行情况快照', 'N');
insert into st_service_bean value ('S31001', 'saveAllFlashTask', '保存所有快照', 'N');

insert into st_service_bean value ('P31003', 'saveRunService', '保存服务请求', 'N');
insert into st_service_bean value ('P31004', 'saveFlashService', '保存服务运行快照', 'N');

insert into st_service_bean value ('P31005', 'saveLoginService', '保存用户登录记录', 'N');
insert into st_service_bean value ('P31006', 'saveRequest', '保存url请求', 'N');
insert into st_service_bean value ('P31007', 'saveErrJson', '保存解析异常的json', 'N');
insert into st_service_bean value ('P31008', 'saveWarnInfo', '保存warn级别日志信息', 'N');

delete from st_service_bean where service_code in ('P32001', 'P32002','P32003', 'P32004', 'P32005', 'P32006', 'P32007','P32008');
insert into st_service_bean value ('P32001', 'sqlWarnTask', '保存warn级别日志信息', 'N');
insert into st_service_bean value ('P32002', 'sqlArchiveTask', 'SQL运行情况归档', 'N');
insert into st_service_bean value ('P32003', 'serviceRunTask', '对service运行记录归档', 'N');
insert into st_service_bean value ('P32004', 'serviceArchiveTask', '对service运行快照归档', 'N');


delete from st_err_msg where err_code in (310001, 310002);
insert into st_err_msg value (310001, '没有找到sql字段');
insert into st_err_msg value (310002, '没有找到service_code字段');

commit;
