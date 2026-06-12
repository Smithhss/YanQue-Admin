insert into sys_config (k, v)
values (
    'teaching.schedule.rule',
    '{"classDays":[1,2,3,5,6],"selfStudyDays":[4],"restDays":[7],"holidayRest":true}'
)
on duplicate key update
    v = values(v);
