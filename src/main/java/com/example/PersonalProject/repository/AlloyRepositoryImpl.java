package com.example.PersonalProject.repository;

import com.example.PersonalProject.entity.Alloy;
import com.example.PersonalProject.entity.User;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Repository
public class AlloyRepositoryImpl {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Alloy> searchAlloys(ObjectId carModelId, double size, double width, String state, String city) {


        List<AggregationOperation> pipeline = new ArrayList<>();

        // --- Step 1: Build initial criteria for Alloy fields (size, width) ---
        List<Criteria> alloyCriteria = new ArrayList<>();

        if (size != 0.0) {
            alloyCriteria.add(new Criteria("size").is(size));
        }
        if (width != 0.0) {
            alloyCriteria.add(new Criteria("width").is(width));
        }

        // Add the match operation for Alloy fields if any criteria exist
        if (!alloyCriteria.isEmpty()) {
            pipeline.add(Aggregation.match(new Criteria().andOperator(alloyCriteria.toArray(new Criteria[0]))));
        }

        // --- Step 2: Handle CarModel filtering (if carModelId is provided) ---
        if (carModelId != null) {
            // Lookup to join with CarModel collection to filter by carModelId
            LookupOperation lookupCarModels = Aggregation.lookup(
                    "car_models", // The collection to join with
                    "compatibleModels.$id", // Local field in Alloy (the _id of the DBRef)
                    "_id", // Foreign field in CarModel
                    "matchedCarModels" // Alias for the joined documents
            );
            pipeline.add(lookupCarModels);

            // Match on the looked-up car models
            // We check if the matchedCarModels array contains the carModelId
            MatchOperation matchCarModel = Aggregation.match(
                    new Criteria("matchedCarModels._id").is(carModelId)
            );
            pipeline.add(matchCarModel);
        }

        // --- Step 3: Handle DealerProfile filtering (if state or city are provided) ---
        List<Criteria> dealerProfileCriteria = new ArrayList<>();

        // Check for "Any" or null for state
        if (StringUtils.hasText(state) && !state.equalsIgnoreCase("Any")) {
            dealerProfileCriteria.add(new Criteria("dealerInfo.dealerProfile.shopState").regex(state, "i"));
        }

        // Check for "Any" or null for city
        if (StringUtils.hasText(city) && !city.equalsIgnoreCase("Any")) {
            dealerProfileCriteria.add(new Criteria("dealerInfo.dealerProfile.shopCity").regex(city, "i"));
        }

        if (!dealerProfileCriteria.isEmpty()) {
            // Lookup to join with User collection (dealer)
            LookupOperation lookupDealer = Aggregation.lookup(
                    "users", // The collection to join with
                    "dealer.$id", // Local field in Alloy (the _id of the DBRef)
                    "_id", // Foreign field in User
                    "dealerInfo" // Alias for the joined dealer documents
            );
            pipeline.add(lookupDealer);

            // Unwind the dealerInfo array (since lookup always returns an array)
            UnwindOperation unwindDealerInfo = Aggregation.unwind("dealerInfo");
            pipeline.add(unwindDealerInfo);

            // Match on dealerProfile fields (nested within dealerInfo)
            pipeline.add(Aggregation.match(new Criteria().andOperator(dealerProfileCriteria.toArray(new Criteria[0]))));
        }

        // --- Step 4: Project to shape the output back to an Alloy object ---
        // This projection only includes the fields necessary for the Alloy object.
        // Temporary fields like 'matchedCarModels' and 'dealerInfo' (added by lookups)
        // will be implicitly excluded because they are not listed here.
        ProjectionOperation projectToAlloy = Aggregation.project()
                .and("$_id").as("id") // Map MongoDB's _id to Java's id field
                .and("name").as("name")
                .and("size").as("size")
                .and("width").as("width")
                .and("offset").as("offset")
                .and("pcd").as("pcd")
                .and("price").as("price")
                .and("imageUrl").as("imageUrl")
                .and("imageKey").as("imageKey")
                .and("dealer").as("dealer")
                .and("compatibleModels").as("compatibleModels");
        pipeline.add(projectToAlloy);

        // Build the aggregation pipeline
        Aggregation aggregation = Aggregation.newAggregation(pipeline);

        // Execute the aggregation
        AggregationResults<Alloy> results = mongoTemplate.aggregate(aggregation, "alloys", Alloy.class);

        return results.getMappedResults();
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
