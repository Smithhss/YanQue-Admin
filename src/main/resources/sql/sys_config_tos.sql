insert into sys_config (k, v)
values ('tos.upload.expire.seconds', '600')
on duplicate key update
    v = values(v);

insert into sys_config (k, v)
values ('tos.preview.expire.seconds', '300')
on duplicate key update
    v = values(v);
