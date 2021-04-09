package com.example.bloggerdemo.repository;

import com.example.bloggerdemo.model.BloggerUser;
import com.example.bloggerdemo.model.Subscription;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class BloggerUserRepositoryTest {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    private BloggerUserRepository bloggerUserRepository;

    @Test
    @Transactional
    void findOneByUsername() {
        BloggerUser user = entityManager.find(BloggerUser.class, 1);
        String username = user.getUsername();
        BloggerUser userFetched = bloggerUserRepository.findOneByUsername(username).orElse(new BloggerUser());
        assertEquals(user.getDisplayName(), userFetched.getDisplayName());
    }

    @Test
    @Transactional
    void findByDisplayNameContainingIgnoreCase() {
        final String searchTerm = "martin";
        List<BloggerUser> result = entityManager.createQuery("select u from BloggerUser u " +
                "where lower(u.displayName) like lower(concat('%',:searchTerm,'%'))", BloggerUser.class)
                .setParameter("searchTerm", searchTerm).getResultList();
        List<BloggerUser> users = bloggerUserRepository
                .findByDisplayNameContainingIgnoreCase(searchTerm);
        assertEquals(result.size(),users.size());
    }

    @Test
    @Transactional
    void existsByUsername() {
        final String username = "dan_abramov";
        Query countUserByUsername = entityManager.createQuery("select count(r) from BloggerUser r where r.username = :username");
        long count = (long) countUserByUsername.setParameter("username", username).getSingleResult();
        assertEquals(1, count);
        assertTrue(bloggerUserRepository.existsByUsername(username));

        final String usernameNonExist = "fwewefhoiiel fef";
        long countNonExist = (long) countUserByUsername.setParameter("username", usernameNonExist).getSingleResult();
        assertEquals(0, countNonExist);
        assertFalse(bloggerUserRepository.existsByUsername(usernameNonExist));
    }

    @Test
    @Transactional
    void existsByDisplayName() {
        final String displayName = "Dan Abramov";
        Query countUserByDisplayName = entityManager.createQuery("select count(r) from BloggerUser r where r.displayName = :displayName");
        long count = (long) countUserByDisplayName.setParameter("displayName", displayName).getSingleResult();
        assertEquals(1, count);
        assertTrue(bloggerUserRepository.existsByDisplayName(displayName));

        final String usernameNonExist = "fwewefhoiiel fef";
        long countNonExist = (long) countUserByDisplayName.setParameter("displayName", usernameNonExist).getSingleResult();
        assertEquals(0, countNonExist);
        assertFalse(bloggerUserRepository.existsByDisplayName(usernameNonExist));
    }

    @Test
    @Transactional
    void isUserSubscribeToAuthorTest(){
        assertTrue(bloggerUserRepository.isUserSubscribeToAuthor(1, 2));
        assertFalse(bloggerUserRepository.isUserSubscribeToAuthor(1, 4));
    }

    @Test
    @Transactional
    void subscribeToAuthorTest(){
        assertFalse(bloggerUserRepository.isUserSubscribeToAuthor(1, 4));
        bloggerUserRepository.subscribeToAuthor(1,4);
        assertTrue(bloggerUserRepository.isUserSubscribeToAuthor(1, 4));
    }

    @Test
    @Transactional
    void unsubscribeToAuthorTest(){
        assertTrue(bloggerUserRepository.isUserSubscribeToAuthor(1, 2));
        bloggerUserRepository.unsubscribeToAuthor(1,2);
        assertFalse(bloggerUserRepository.isUserSubscribeToAuthor(1, 2));
    }

    @Test
    @Transactional
    void test(){
        final int userId = 1;
        Subscription sub1 = new Subscription();
        sub1.setFollower(entityManager.getReference(BloggerUser.class,userId));
        sub1.setFollowee(entityManager.getReference(BloggerUser.class,2));

        Subscription sub2 = new Subscription();
        sub2.setFollower(entityManager.getReference(BloggerUser.class,userId));
        sub2.setFollowee(entityManager.getReference(BloggerUser.class,3));

        entityManager.persist(sub1);
        entityManager.persist(sub2);

        long subscriptionsOfUser = (long) entityManager
                .createQuery("select count(sub) from Subscription sub " +
                        "where sub.follower.id = :userId")
                .setParameter("userId", userId)
                .getSingleResult();
        List<BloggerUser> subscribedAuthors = this.bloggerUserRepository.getListOfSubscribedAuthor(userId);

        assertEquals(subscriptionsOfUser, subscribedAuthors.size());
    }

}