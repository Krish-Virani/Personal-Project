package com.example.PersonalProject.repository;

import com.example.PersonalProject.entity.Alloy;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class AlloyRepositoryImpl {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Alloy> searchAlloys(ObjectId carModelId, double size, double width) {

        Criteria criteria = new Criteria();
        if(carModelId!=null)criteria.and("compatibleModels.id").is(carModelId);
        if (size != 0) criteria.and("size").is(size);
        if (width != 0) criteria.and("width").is(width);

        return mongoTemplate.find(Query.query(criteria), Alloy.class);
    }



    public List<Alloy> findByUserId(ObjectId userId) {
        Criteria criteria = new Criteria();

        criteria.and("dealer.id").is(userId);

        Query query = new Query(criteria);
        query.with(Sort.by(Sort.Direction.ASC, "size"));
        return mongoTemplate.find(query, Alloy.class);
    }

    public List<Alloy> findAlloysByName(String name, ObjectId userId) {
        Criteria criteria = new Criteria();

        criteria.and("dealer.id").is(userId);
        criteria.and("name").is(name);

        Query query = new Query(criteria);
        return mongoTemplate.find(query, Alloy.class);
    }


}
