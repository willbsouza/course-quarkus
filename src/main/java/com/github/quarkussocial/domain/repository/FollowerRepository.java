package com.github.quarkussocial.domain.repository;

import com.github.quarkussocial.domain.model.Follower;
import com.github.quarkussocial.domain.model.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class FollowerRepository implements PanacheRepository<Follower> {

    public boolean follows(User follower, User user){
        Map<String, Object> params = Parameters.with("follower", follower).and("user", user).map();
        Optional<Follower> result = find("follower = :follower and user = :user", params).firstResultOptional();
        return result.isPresent();
    }

    public List<Follower> findByUser(Long userId){
        return find("user.id", userId).list();
    }

    public void deleteByFollowerAndUser(Long followerId, Long userId) {
        Map<String, Object> params = Parameters.with("userId", userId).and("followerId", followerId).map();
        delete("follower.id = :followerId and user.id = :userId", params);
    }
}
