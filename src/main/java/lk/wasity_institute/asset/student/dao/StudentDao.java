package lk.wasity_institute.asset.student.dao;



import lk.wasity_institute.asset.batch.entity.Batch;
import lk.wasity_institute.asset.batch.entity.enums.Grade;
import lk.wasity_institute.asset.batch_exam.entity.BatchExam;
import lk.wasity_institute.asset.payment.entity.Payment;
import lk.wasity_institute.asset.student.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StudentDao extends JpaRepository<Student, Integer > {

    Student findFirstByOrderByIdDesc();

  List< Student> findByGrade(Grade grade);
    List<Student> findByCreatedAtIsBetween(LocalDateTime startAt, LocalDateTime endAt);
}
