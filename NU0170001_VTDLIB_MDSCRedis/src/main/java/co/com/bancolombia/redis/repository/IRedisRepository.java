package co.com.bancolombia.redis.repository;

import co.com.bancolombia.redis.template.UserTransactionalRedis;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRedisRepository extends CrudRepository<UserTransactionalRedis, String> {
    List<UserTransactionalRedis> findByNumberDocument(String numberDocument);
    void deleteById(String idSession);
}
