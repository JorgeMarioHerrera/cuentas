package co.com.bancolombia.redisservice.repository;

import co.com.bancolombia.redisservice.template.UserTransactionalRedis;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRedisRepository extends CrudRepository<UserTransactionalRedis, String> {
    List<UserTransactionalRedis> findByDocNumber(String docNumber);
    void deleteById(String idSession);
}
