package com.example.bloggerdemo.service.business;

import com.example.bloggerdemo.model.BloggerUser;
import com.example.bloggerdemo.repository.BloggerUserRepository;
import com.example.bloggerdemo.service.strategy.checkunique.CheckUnique;
import com.example.bloggerdemo.service.strategy.checkunique.CheckUniqueStrategy;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;

@Service
@Log4j2
public class AccountService {
    private final BloggerUserRepository bloggerUserRepository;
    private final EnumMap<CheckUniqueStrategy, CheckUnique> checkUniqueStrategies = new EnumMap<>(CheckUniqueStrategy.class);

    @Autowired
    public AccountService(BloggerUserRepository bloggerUserRepository,
                          List<CheckUnique> strategies) {
        this.bloggerUserRepository = bloggerUserRepository;
        strategies.forEach(checkUnique ->
            this.checkUniqueStrategies.put(checkUnique.getStrategyName(),checkUnique));
    }

    public void registerUser(BloggerUser bloggerUser) {
        this.bloggerUserRepository.save(bloggerUser);
    }

    public boolean checkIfFieldUnique(String fieldName, String fieldValue){
        log.debug("Checking if field unique. FieldName: {}. FieldValue: {}", fieldName, fieldValue);
        CheckUniqueStrategy strategy = CheckUniqueStrategy.fromAbbr(fieldName);
        return checkUniqueStrategies.get(strategy).isUnique(fieldValue);
    }
}
