package com.example.PersonalProject.repository;

import com.example.PersonalProject.entity.Alloy;
import com.example.PersonalProject.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<User> getAllUnverifiedDealers() {
        Criteria criteria = new Criteria();

        criteria.and("dealer.dealerProfile.isVerified").is(false); // nested check

        Query query = new Query(criteria);
        return mongoTemplate.find(query, User.class);
    }

    public List<User> getUserByStateAndCity(String state,  String city) {
        Criteria criteria = new Criteria();
        if(state!=null && !state.equals("Any")) criteria.and("dealer.dealerProfile.state").is(state);
        if(city!=null && !city.equals("Any")) criteria.and("dealer.dealerProfile.city").is(city);
        Query query = new Query(criteria);
        return mongoTemplate.find(query, User.class);
    }

}
