package lk.wasity_institute.asset.subject.dao;



import lk.wasity_institute.asset.subject.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectDao extends JpaRepository<Subject, Integer> {

  Subject findFirstByOrderByIdDesc();
}
