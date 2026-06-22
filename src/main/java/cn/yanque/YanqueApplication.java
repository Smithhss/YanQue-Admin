package cn.yanque;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScans({
        @MapperScan("cn.yanque.models.users.mapper"),
        @MapperScan("cn.yanque.common.dataConfig.mapper"),
        @MapperScan("cn.yanque.models.teaching.campus.mapper"),
        @MapperScan("cn.yanque.models.teaching.course.mapper"),
        @MapperScan("cn.yanque.models.teaching.clazz.mapper"),
        @MapperScan("cn.yanque.models.teaching.schedule.mapper"),
        @MapperScan("cn.yanque.models.teaching.duty.mapper"),
        @MapperScan("cn.yanque.models.teaching.homework.mapper"),
        @MapperScan("cn.yanque.models.order.prepay.mapper"),
        @MapperScan("cn.yanque.models.order.product.mapper"),
        @MapperScan("cn.yanque.models.order.refund.mapper"),
        @MapperScan("cn.yanque.models.exam.question.mapper"),
        @MapperScan("cn.yanque.models.exam.paper.mapper"),
        @MapperScan("cn.yanque.models.exam.exam.mapper"),
        @MapperScan("cn.yanque.models.student.mapper"),
        @MapperScan("cn.yanque.models.student.coursehour.mapper"),
        @MapperScan("cn.yanque.models.teaching.attendance.mapper")
})
public class YanqueApplication {

    public static void main(String[] args) {
        SpringApplication.run(YanqueApplication.class);
    }
}
