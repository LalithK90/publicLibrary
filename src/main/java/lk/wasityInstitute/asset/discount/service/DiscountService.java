package lk.wasityInstitute.asset.discount.service;


import lk.wasityInstitute.asset.commonAsset.model.Enum.LiveDead;
import lk.wasityInstitute.asset.discount.dao.DiscountDao;
import lk.wasityInstitute.asset.discount.entity.Discount;
import lk.wasityInstitute.asset.discount.entity.Discount;
import lk.wasityInstitute.util.interfaces.AbstractService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscountService implements AbstractService< Discount,Integer> {
    private final DiscountDao discountDao;

    public DiscountService(DiscountDao discountDao) {
        this.discountDao = discountDao;
    }

    public List<Discount> findAll() {
        return discountDao.findAll();
    }

    public Discount findById(Integer id) {
        return discountDao.getOne(id);
    }

    public Discount persist(Discount discount) {
        if(discount.getId()==null){
            discount.setLiveDead(LiveDead.ACTIVE);}
        return discountDao.save(discount);
    }

    public boolean delete(Integer id) {
        Discount discount =  discountDao.getOne(id);
        discount.setLiveDead(LiveDead.STOP);
        discountDao.save(discount);
        return false;
    }

    public List<Discount> search(Discount discount) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Discount> discountExample = Example.of(discount, matcher);
        return discountDao.findAll(discountExample);
    }
}
