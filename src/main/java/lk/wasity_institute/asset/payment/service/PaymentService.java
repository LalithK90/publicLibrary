package lk.wasity_institute.asset.payment.service;


import lk.wasity_institute.asset.batch_student.entity.BatchStudent;
import lk.wasity_institute.asset.common_asset.model.enums.LiveDead;
import lk.wasity_institute.asset.payment.dao.PaymentDao;
import lk.wasity_institute.asset.payment.entity.Payment;
import lk.wasity_institute.asset.payment.entity.enums.PaymentStatus;
import lk.wasity_institute.util.interfaces.AbstractService;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.List;

@Service
public class PaymentService implements AbstractService< Payment, Integer > {
  private final PaymentDao paymentDao;

  public PaymentService(PaymentDao paymentDao) {
    this.paymentDao = paymentDao;
  }

  public List< Payment > findAll() {
    return paymentDao.findAll();
  }

  public Payment findById(Integer id) {
    return paymentDao.getOne(id);
  }

  public Payment persist(Payment payment) {
    if ( payment.getId() == null )
      payment.setLiveDead(LiveDead.ACTIVE);
    return paymentDao.save(payment);
  }

  public boolean delete(Integer id) {
    Payment payment = paymentDao.getOne(id);
    payment.setLiveDead(LiveDead.STOP);
    paymentDao.save(payment);
    return false;
  }

  public List< Payment > search(Payment payment) {
    return null;
  }


  public Payment lastStudentOnDB() {
    return paymentDao.findFirstByOrderByIdDesc();
  }

  public Payment findByMonthAndBatchStudent(Month month, BatchStudent batchStudent) {
    return paymentDao.findByBatchStudentAndMonth(batchStudent, month);
  }

  public Payment findByMonthAndBatchStudentAndPaymentStatus(Month month, BatchStudent batchStudent, PaymentStatus noPaid) {
    return paymentDao.findByMonthAndBatchStudentAndPaymentStatus(batchStudent, month,noPaid);
  }
}
