delete from st_service_bean where service_code in ('P31009', 'S34501', 'S10001','S10002');

insert into st_service_bean value ('P31009','saveErrorInfo','保存异常日志','N');
insert into st_service_bean value ('S34501','getErrorInfo','获取异常日志信息','Y');
insert into st_service_bean value ('S10001','cutPageServiceImpl','分页查询服务','N');
insert into st_service_bean value ('S10002','getTableParamet','获取系统参数','N');

commit;
