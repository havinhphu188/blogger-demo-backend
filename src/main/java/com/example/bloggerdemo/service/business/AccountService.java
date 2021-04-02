package com.example.bloggerdemo.service.business;

import com.example.bloggerdemo.model.BloggerUser;
import com.example.bloggerdemo.repository.BloggerUserRepository;
import com.example.bloggerdemo.service.strategy.checkunique.CheckUnique;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AccountService {
    private final BloggerUserRepository bloggerUserRepository;
    private final Map<String, CheckUnique> checkUniqueStrategies;

    @Autowired
    public AccountService(BloggerUserRepository bloggerUserRepository, Map<String, CheckUnique> checkUniqueStrategies) {
        this.bloggerUserRepository = bloggerUserRepository;
        this.checkUniqueStrategies = checkUniqueStrategies;
    }

    public void registerUser(BloggerUser bloggerUser) {
        this.bloggerUserRepository.save(bloggerUser);
    }

    public boolean checkIfFieldUnique(String fieldName, String fieldValue){
        if (!checkUniqueStrategies.containsKey(fieldName)) {
            throw new IllegalArgumentException("Field " + fieldName + " does not exist. ");
        }
        return checkUniqueStrategies.get(fieldName).isUnique(fieldValue);
    }
}
