package com.example.PersonalProject.utils;

import com.example.PersonalProject.entity.Location;
import com.example.PersonalProject.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LocationSeeder implements CommandLineRunner {

    @Autowired
    private LocationRepository locationRepository;

    @Override
    public void run(String... args) {
        if (locationRepository.count() == 0) {
            List<Location> locations = List.of(
                    new Location(null, "Andhra Pradesh", List.of("Visakhapatnam", "Vijayawada", "Guntur", "Nellore")),
                    new Location(null, "Arunachal Pradesh", List.of("Itanagar", "Tawang", "Bomdila")),
                    new Location(null, "Assam", List.of("Guwahati", "Silchar", "Dibrugarh", "Jorhat")),
                    new Location(null, "Bihar", List.of("Patna", "Gaya", "Bhagalpur", "Muzaffarpur")),
                    new Location(null, "Chhattisgarh", List.of("Raipur", "Bhilai", "Bilaspur", "Korba")),
                    new Location(null, "Goa", List.of("Panaji", "Margao", "Vasco da Gama")),
                    new Location(null, "Gujarat", List.of("Ahmedabad", "Surat", "Vadodara", "Rajkot")),
                    new Location(null, "Haryana", List.of("Gurgaon", "Faridabad", "Panipat", "Ambala")),
                    new Location(null, "Himachal Pradesh", List.of("Shimla", "Manali", "Dharamshala", "Solan")),
                    new Location(null, "Jharkhand", List.of("Ranchi", "Jamshedpur", "Dhanbad", "Bokaro")),
                    new Location(null, "Karnataka", List.of("Bengaluru", "Mysuru", "Mangalore", "Hubli")),
                    new Location(null, "Kerala", List.of("Kochi", "Thiruvananthapuram", "Kozhikode", "Thrissur")),
                    new Location(null, "Madhya Pradesh", List.of("Bhopal", "Indore", "Jabalpur", "Gwalior")),
                    new Location(null, "Maharashtra", List.of("Mumbai", "Pune", "Nagpur", "Nashik")),
                    new Location(null, "Manipur", List.of("Imphal", "Thoubal", "Bishnupur")),
                    new Location(null, "Meghalaya", List.of("Shillong", "Tura", "Nongpoh")),
                    new Location(null, "Mizoram", List.of("Aizawl", "Lunglei", "Champhai")),
                    new Location(null, "Nagaland", List.of("Kohima", "Dimapur", "Mokokchung")),
                    new Location(null, "Odisha", List.of("Bhubaneswar", "Cuttack", "Rourkela", "Puri")),
                    new Location(null, "Punjab", List.of("Ludhiana", "Amritsar", "Jalandhar", "Patiala")),
                    new Location(null, "Rajasthan", List.of("Jaipur", "Jodhpur", "Udaipur", "Ajmer")),
                    new Location(null, "Sikkim", List.of("Gangtok", "Namchi", "Geyzing")),
                    new Location(null, "Tamil Nadu", List.of("Chennai", "Coimbatore", "Madurai", "Salem")),
                    new Location(null, "Telangana", List.of("Hyderabad", "Warangal", "Karimnagar", "Nizamabad")),
                    new Location(null, "Tripura", List.of("Agartala", "Udaipur", "Kailashahar")),
                    new Location(null, "Uttar Pradesh", List.of("Lucknow", "Kanpur", "Varanasi", "Agra")),
                    new Location(null, "Uttarakhand", List.of("Dehradun", "Haridwar", "Nainital", "Rishikesh")),
                    new Location(null, "West Bengal", List.of("Kolkata", "Howrah", "Asansol", "Siliguri")),

                    // Union Territories
                    new Location(null, "Delhi", List.of("New Delhi", "Dwarka", "Saket", "Rohini")),
                    new Location(null, "Chandigarh", List.of("Chandigarh")),
                    new Location(null, "Puducherry", List.of("Puducherry", "Karaikal", "Mahe")),
                    new Location(null, "Jammu and Kashmir", List.of("Srinagar", "Jammu", "Anantnag")),
                    new Location(null, "Ladakh", List.of("Leh", "Kargil")),
                    new Location(null, "Andaman and Nicobar Islands", List.of("Port Blair")),
                    new Location(null, "Dadra and Nagar Haveli and Daman and Diu", List.of("Daman", "Silvassa"))
            );

            locationRepository.saveAll(locations);
            System.out.println("✅ Locations seeded successfully.");
        } else {
            System.out.println("ℹ️ Locations already exist. Skipping seeding.");
        }
    }
}

