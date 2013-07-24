delete from st_service_bean where service_code in ('P33004','S34303','S34304','S34305');

insert into st_service_bean value ('P33004', 'savePicRequest', '保存照片请求', 'N');
insert into st_service_bean value ('S34303', 'getPicRequest', '获取照片请求', 'Y');
insert into st_service_bean value ('S34304', 'getPicRequestRunTime', '获取照片请求执行时间', 'Y');
insert into st_service_bean value ('S34305', 'getPicRequestSusFail', '获取照片请求执行成功率', 'Y');
