package com.example.bloggerdemo.repository.custom.impl;

import com.example.bloggerdemo.model.BloggerUser;
import com.example.bloggerdemo.model.Subscription;
import com.example.bloggerdemo.repository.custom.CustomBloggerUserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class CustomBloggerUserRepositoryImpl implements CustomBloggerUserRepository {

    public static final String USER_ID = "userId";
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void subscribeToAuthor(int authorId, int userId) {
        BloggerUser author = entityManager.getReference(BloggerUser.class, authorId);
        BloggerUser user = entityManager.getReference(BloggerUser.class, userId);
        Subscription subscription = new Subscription();
        subscription.setFollower(user);
        subscription.setFollowee(author);
        entityManager.persist(subscription);
    }

    @Override
    public void unsubscribeToAuthor(int authorId, int userId) {
        entityManager.createQuery("delete from Subscription sub where sub.follower.id =:userId and sub.followee.id =:authorId")
                .setParameter("authorId", authorId)
                .setParameter(USER_ID, userId)
                .executeUpdate();
    }

    @Override
    public boolean isUserSubscribeToAuthor(Integer authorId, Integer userId){
        List<Subscription> result = entityManager.createQuery("select sub from Subscription as sub " +
                "where sub.followee.id = :authorId and sub.follower.id = :userId", Subscription.class)
                .setParameter("authorId",authorId)
                .setParameter(USER_ID, userId)
                .getResultList();
        return !result.isEmpty();
    }

    @Override
    public List<BloggerUser> getListOfSubscribedAuthor(int userId) {
        return entityManager.createQuery("select u from BloggerUser as u " +
                "where u.id in (select sub.followee.id from Subscription sub where sub.follower.id = :userId)", BloggerUser.class)
                .setParameter(USER_ID, userId)
                .getResultList();

    }
}
