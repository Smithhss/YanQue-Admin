package cn.yanque.common.threadlocal;

import cn.yanque.models.student.pojo.entity.StudentEntity;

public class StudentThreadLocal {

    public static ThreadLocal<StudentEntity> studentEntityThreadLocal = new ThreadLocal<>();

    public static StudentEntity get() {
        return studentEntityThreadLocal.get();
    }

    public static void set(StudentEntity student) {
        studentEntityThreadLocal.set(student);
    }

    public static void remove() {
        studentEntityThreadLocal.remove();
    }
}
