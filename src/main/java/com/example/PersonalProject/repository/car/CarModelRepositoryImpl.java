package com.example.PersonalProject.repository.car;

import com.example.PersonalProject.entity.CarModel;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CarModelRepositoryImpl {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<CarModel> findModelsByCarId(ObjectId id) {
        Criteria criteria = new Criteria();
        if(id!=null)criteria.and("brand.id").is(id);
        return mongoTemplate.find(Query.query(criteria), CarModel.class);
    }

}