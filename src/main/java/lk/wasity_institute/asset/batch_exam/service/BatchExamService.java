package lk.wasity_institute.asset.batch_exam.service;


import lk.wasity_institute.asset.batch.entity.Batch;
import lk.wasity_institute.asset.batch_exam.dao.BatchExamDao;
import lk.wasity_institute.asset.batch_exam.entity.BatchExam;
import lk.wasity_institute.asset.batch_exam.entity.enums.ExamStatus;
import lk.wasity_institute.asset.common_asset.model.enums.LiveDead;
import lk.wasity_institute.asset.time_table.entity.TimeTable;
import lk.wasity_institute.util.interfaces.AbstractService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BatchExamService implements AbstractService<BatchExam, Integer > {
  private final BatchExamDao batchExamDao;

  public BatchExamService(BatchExamDao batchExamDao) {
    this.batchExamDao = batchExamDao;
  }

  public List< BatchExam > findAll() {
    return batchExamDao.findAll();
  }

  public BatchExam findById(Integer id) {
    return batchExamDao.getOne(id);
  }

  public BatchExam persist(BatchExam batchExam) {
    if ( batchExam.getId() == null ) {
      batchExam.setLiveDead(LiveDead.ACTIVE);
      batchExam.setExamStatus(ExamStatus.PENDING);
    }
    return batchExamDao.save(batchExam);
  }

  public boolean delete(Integer id) {
    BatchExam batchExam = batchExamDao.getOne(id);
    batchExam.setLiveDead(LiveDead.STOP);
    batchExamDao.save(batchExam);
    return false;
  }

  public List< BatchExam > search(BatchExam batchExam) {
    return null;
  }


  public int countByBatch(Batch batch) {
    return batchExamDao.countByBatch(batch);
  }

  public BatchExam lastBatchExamDB() {
    return batchExamDao.findFirstByOrderByIdDesc();
  }

  public List< BatchExam> findByStartAtBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
    return batchExamDao.findByStartAtBetween(startDateTime,endDateTime);
  }

  public List< BatchExam> findByStartAtBetweenAndBatch(LocalDateTime startDateTime, LocalDateTime endDateTime, Batch batch) {
    return batchExamDao.findByStartAtBetweenAndBatch(startDateTime,endDateTime,batch);
  }

  public  List<BatchExam> findByBatch(Batch batch) {
    return batchExamDao.findByBatch(batch);
  }
}
