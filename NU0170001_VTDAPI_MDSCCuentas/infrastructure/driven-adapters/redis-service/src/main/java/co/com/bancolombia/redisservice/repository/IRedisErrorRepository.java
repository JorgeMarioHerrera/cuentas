package co.com.bancolombia.redisservice.repository;

import co.com.bancolombia.redisservice.template.TransactionalErrorsRedis;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRedisErrorRepository extends CrudRepository<TransactionalErrorsRedis, String> {
    void deleteById(String idSession);
}
