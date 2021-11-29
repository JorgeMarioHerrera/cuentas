package co.com.bancolombia.redisservice.repository;

import co.com.bancolombia.redisservice.template.TransactionalErrorsRedis;
import co.com.bancolombia.redisservice.template.UserTransactionalRedis;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRedisErrorRepository extends CrudRepository<TransactionalErrorsRedis, String> {
    void deleteById(String idSession);
}
