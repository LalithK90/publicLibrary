package lk.wasity_institute.asset.batch.service;



import lk.wasity_institute.asset.batch.dao.BatchDao;
import lk.wasity_institute.asset.batch.entity.Batch;
import lk.wasity_institute.asset.batch.entity.enums.ClassDay;
import lk.wasity_institute.asset.batch.entity.enums.Grade;
import lk.wasity_institute.asset.batch.entity.enums.Medium;
import lk.wasity_institute.asset.common_asset.model.enums.LiveDead;
import lk.wasity_institute.util.interfaces.AbstractService;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class BatchService implements AbstractService<Batch, Integer > {
  private final BatchDao batchDao;

  public BatchService(BatchDao batchDao) {
    this.batchDao = batchDao;
  }

  public List< Batch > findAll() {
    return batchDao.findAll();
  }

  public Batch findById(Integer id) {
    return batchDao.getOne(id);
  }

  public Batch persist(Batch batch) {
    if ( batch.getId() == null ) {
      batch.setLiveDead(LiveDead.ACTIVE);
    }
    return batchDao.save(batch);
  }

  public boolean delete(Integer id) {
    Batch batch = batchDao.getOne(id);
    batch.setLiveDead(LiveDead.STOP);
    batchDao.save(batch);
    return false;
  }

  public List< Batch > search(Batch batch) {
    return null;
  }

  public Batch lastBatchOnDB() {
    return batchDao.findFirstByOrderByIdDesc();
  }


  public Batch findByName(String name) {
    return batchDao.findByName(name);
  }

  public List< Batch > findByGrade(Grade grade) {
    return batchDao.findByGrade(grade);
  }
//  public List< Batch > findByMedium(Medium medium) {
//    return batchDao.findByMedium(medium);
//  }

  public List< Batch > findByClassDay(ClassDay classDay) {
    return batchDao.findByClassDay(classDay);
  }

  public Batch findByYearAndClassDayAndStartAtIsBetweenAndEndAtIsBetween(String year, ClassDay classDay, LocalTime startAt, LocalTime endAt, LocalTime startAt1,
                                                                         LocalTime endAt1) {
  return batchDao.findByYearAndClassDayAndStartAtIsBetweenAndEndAtIsBetween(year,classDay,startAt,endAt,startAt1,endAt1);
  }
}
