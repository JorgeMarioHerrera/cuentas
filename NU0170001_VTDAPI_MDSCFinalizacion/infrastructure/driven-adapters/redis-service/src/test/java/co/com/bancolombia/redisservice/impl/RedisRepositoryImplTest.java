package co.com.bancolombia.redisservice.impl;

import co.com.bancolombia.LoggerApp;
import co.com.bancolombia.logging.technical.LoggerFactory;
import co.com.bancolombia.model.either.Either;
import co.com.bancolombia.model.messageerror.ErrorExeption;
import co.com.bancolombia.model.redis.UserTransactional;
import co.com.bancolombia.redisservice.repository.IRedisRepository;
import co.com.bancolombia.redisservice.template.UserTransactionalRedis;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@Disabled
class RedisRepositoryImplTest {

    @Mock
    private IRedisRepository iRedisRepository;
    private RedisRepositoryImpl redisRepositoryImpl;
    private ModelMapper maper;
    private final LoggerApp loggerApp = new LoggerApp(LoggerFactory.getLog(String.valueOf(this.getClass())));

    @Test
    void testShouldSaveUser() throws IOException {
        /*
         * Arrange
         * */
        // Create class, Inject IRedisRepository with Mock, Inject Log created for test
        redisRepositoryImpl = new RedisRepositoryImpl(iRedisRepository, maper, loggerApp);
        // Create UserTransactional for test
        UserTransactional userTransactional
                = generateModelUserTransactional("UserTransactionalModelAndEntity.json");

        /*
         * Act
         * */
        Either<ErrorExeption, Boolean> resultSaveUser = redisRepositoryImpl.saveUser(userTransactional);

        /*
         * Assertions
         * */
        assertEquals(Boolean.TRUE, resultSaveUser.getRight());
    }

    @Test
    void testShouldThrowIllegalArgumentExceptionInSaveUserParameterPassNull() throws IOException {
        /*
         * Arrange
         * */
        // Create class, Inject IRedisRepository with Mock, Inject Log created for test
        redisRepositoryImpl = new RedisRepositoryImpl(iRedisRepository, maper, loggerApp);
        // Error response
        ErrorExeption errorExeptionTest = generateErrorException("ErrorExceptionIllegalArgumentException.json");

        /*
         * Act
         * */
        Either<ErrorExeption, Boolean> resultSaveUser = redisRepositoryImpl.saveUser(null);

        /*
         * Assertions
         * */
        assertEquals(errorExeptionTest.getCode(), resultSaveUser.getLeft().getCode());
        assertEquals(errorExeptionTest.getDescription(), resultSaveUser.getLeft().getDescription());
    }

    @Test
    void testShouldThrowIllegalArgumentExceptionInSaveUserForProblemInRepository() throws IOException {
        /*
         * Arrange
         * */
        // Mock error in save of repository
        when(iRedisRepository.save(any())).thenThrow(IllegalArgumentException.class);
        // Create class, Inject IRedisRepository with Mock, Inject Log created for test
        redisRepositoryImpl = new RedisRepositoryImpl(iRedisRepository, maper, loggerApp);
        // Create UserTransactional for test
        UserTransactional userTransactional
                = generateModelUserTransactional("UserTransactionalWithoutFields.json");
        // Error response
        ErrorExeption errorExeptionTest = generateErrorException("ErrorExceptionIllegalArgumentException.json");

        /*
         * Act
         * */
        Either<ErrorExeption, Boolean> resultSaveUser = redisRepositoryImpl.saveUser(userTransactional);

        /*
         * Assertions
         * */
        assertEquals(errorExeptionTest.getCode(), resultSaveUser.getLeft().getCode());
        assertEquals(errorExeptionTest.getDescription(), resultSaveUser.getLeft().getDescription());
    }

    @Test
    void testShouldThrowExceptionInSaveUserForProblemInRepository() throws IOException {
        /*
         * Arrange
         * */
        // Create class, Inject IRedisRepository with Mock, Inject Log created for test
        redisRepositoryImpl = new RedisRepositoryImpl(null,  maper, loggerApp);
        // Create UserTransactional for test
        UserTransactional userTransactional
                = generateModelUserTransactional("UserTransactionalWithoutFields.json");
        // Error response
        ErrorExeption errorExeptionTest = generateErrorException("ErrorExceptionException.json");
        // Mock error in save of repository

        /*
         * Act
         * */
        Either<ErrorExeption, Boolean> resultSaveUser = redisRepositoryImpl.saveUser(userTransactional);

        /*
         * Assertions
         * */
        assertEquals(errorExeptionTest.getCode(), resultSaveUser.getLeft().getCode());
        assertEquals(errorExeptionTest.getDescription(), resultSaveUser.getLeft().getDescription());
    }

    @Test
    void testShouldDeleteUser() {
        /*
         * Arrange
         * */
        // Create class, Inject IRedisRepository with Mock, Inject Log created for test
        redisRepositoryImpl = new RedisRepositoryImpl(iRedisRepository, maper, loggerApp);

        /*
         * Act
         * */
        Either<ErrorExeption, Boolean> resultDeleteUser = redisRepositoryImpl.deleteUser("ID-SESS-TEST");

        /*
         * Assertions
         * */
        assertEquals(Boolean.TRUE, resultDeleteUser.getRight());
    }

    @Test
    void testShouldThrowIllegalArgumentExceptionInDeleteUserParameterPassNull() throws IOException {
        /*
         * Arrange
         * */
        // Create class, Inject IRedisRepository with Mock, Inject Log created for test
        redisRepositoryImpl = new RedisRepositoryImpl(iRedisRepository, maper, loggerApp);
        // Error response
        ErrorExeption errorExeptionTest = generateErrorException("ErrorExceptionIllegalArgumentException.json");

        /*
         * Act
         * */
        Either<ErrorExeption, Boolean> resultDeleteUser = redisRepositoryImpl.deleteUser(null);

        /*
         * Assertions
         * */
        assertEquals(errorExeptionTest.getCode(), resultDeleteUser.getLeft().getCode());
        assertEquals(errorExeptionTest.getDescription(), resultDeleteUser.getLeft().getDescription());
    }

    @Test
    void testShouldThrowIllegalArgumentExceptionInDeleteUserForProblemInRepository() throws IOException {
        /*
         * Arrange
         * */
        // Mock error in save of repository
        doThrow(new IllegalArgumentException()).when(iRedisRepository).deleteById(any());
        // Create class, Inject IRedisRepository with Mock, Inject Log created for test
        redisRepositoryImpl = new RedisRepositoryImpl(iRedisRepository, maper, loggerApp);
        // Error response
        ErrorExeption errorExeptionTest = generateErrorException("ErrorExceptionIllegalArgumentException.json");

        /*
         * Act
         * */
        Either<ErrorExeption, Boolean> resultDeleteUser = redisRepositoryImpl.deleteUser("ID-SESSION-TEST");

        /*
         * Assertions
         * */
        assertEquals(errorExeptionTest.getCode(), resultDeleteUser.getLeft().getCode());
        assertEquals(errorExeptionTest.getDescription(), resultDeleteUser.getLeft().getDescription());
    }

    @Test
    void testShouldThrowExceptionInDeleteUserForProblemInRepository() throws IOException {
        /*
         * Arrange
         * */
        // Create class, Inject IRedisRepository with Mock, Inject Log created for test
        redisRepositoryImpl = new RedisRepositoryImpl(null,  maper,loggerApp);
        // Error response
        ErrorExeption errorExeptionTest = generateErrorException("ErrorExceptionException.json");
        // Mock error in save of repository

        /*
         * Act
         * */
        Either<ErrorExeption, Boolean> resultDeleteUser = redisRepositoryImpl.deleteUser("ID-SESS-TEST");

        /*
         * Assertions
         * */
        assertEquals(errorExeptionTest.getCode(), resultDeleteUser.getLeft().getCode());
        assertEquals(errorExeptionTest.getDescription(), resultDeleteUser.getLeft().getDescription());
    }

    @Test
    void testShouldGetUser() throws IOException {
        /*
         * Arrange
         * */
        // Create Response repository
        UserTransactionalRedis userTransactionalRedis
                = generateTemplateUserTransactionalRedis("UserTransactionalModelAndEntity.json");
        // Value expected is model (Convert entity response to model)
        UserTransactional userTransactional
                = generateModelUserTransactional("UserTransactionalModelAndEntity.json");
        // Mock Response repository
        when(iRedisRepository.findById(anyString())).thenReturn(java.util.Optional.ofNullable(userTransactionalRedis));
        // Create class, Inject IRedisRepository with Mock, Inject Log created for test
        redisRepositoryImpl = new RedisRepositoryImpl(iRedisRepository, maper, loggerApp);
        // Create UserTransactional for test


        /*
         * Act
         * */
        Either<ErrorExeption, UserTransactional> resultGetUser = redisRepositoryImpl.getUser("ID-SESS-TEST");

        /*
         * Assertions
         * */
        assertEquals(userTransactional, resultGetUser.getRight());
    }

    @Test
    void testShouldGetUserNotFound() {
        /*
         * Arrange
         * */
        // Value expected is model (Convert entity response to model)
        UserTransactional userTransactional = UserTransactional.builder().build();
        // Mock Response repository
        when(iRedisRepository.findById(anyString())).thenReturn(java.util.Optional.empty());
        // Create class, Inject IRedisRepository with Mock, Inject Log created for test
        redisRepositoryImpl = new RedisRepositoryImpl(iRedisRepository, maper, loggerApp);
        // Create UserTransactional for test


        /*
         * Act
         * */
        Either<ErrorExeption, UserTransactional> resultGetUser = redisRepositoryImpl.getUser("ID-SESS-TEST");

        /*
         * Assertions
         * */
        assertEquals(userTransactional, resultGetUser.getRight());
    }

    @Test
    void testShouldThrowIllegalArgumentExceptionInGetUserForParameterPassNull() throws IOException {
        /*
         * Arrange
         * */
        // Create class, Inject IRedisRepository with Mock, Inject Log created for test
        redisRepositoryImpl = new RedisRepositoryImpl(iRedisRepository, maper, loggerApp);
        // Error response
        ErrorExeption errorExeptionTest = generateErrorException("ErrorExceptionIllegalArgumentException.json");

        /*
         * Act
         * */
        Either<ErrorExeption, UserTransactional> resultGetUser = redisRepositoryImpl.getUser(null);

        /*
         * Assertions
         * */
        assertEquals(errorExeptionTest.getCode(), resultGetUser.getLeft().getCode());
        assertEquals(errorExeptionTest.getDescription(), resultGetUser.getLeft().getDescription());
    }

    @Test
    void testShouldThrowIllegalArgumentExceptionInGetUserForProblemInRepository() throws IOException {
        /*
         * Arrange
         * */
        // Mock error in save of repository
        when(iRedisRepository.findById(any())).thenThrow(IllegalArgumentException.class);
        // Create class, Inject IRedisRepository with Mock, Inject Log created for test
        redisRepositoryImpl = new RedisRepositoryImpl(iRedisRepository, maper, loggerApp);
        // Error response
        ErrorExeption errorExeptionTest = generateErrorException("ErrorExceptionIllegalArgumentException.json");

        /*
         * Act
         * */
        Either<ErrorExeption, UserTransactional> resultGetUser = redisRepositoryImpl.getUser("ID-SESS-TEST");

        /*
         * Assertions
         * */
        assertEquals(errorExeptionTest.getCode(), resultGetUser.getLeft().getCode());
        assertEquals(errorExeptionTest.getDescription(), resultGetUser.getLeft().getDescription());
    }

    @Test
    void testShouldThrowExceptionInGetUserForProblemInRepository() throws IOException {
        /*
         * Arrange
         * */
        // Create class, Inject IRedisRepository with Mock, Inject Log created for test
        redisRepositoryImpl = new RedisRepositoryImpl(null,  maper,loggerApp);
        // Error response
        ErrorExeption errorExeptionTest = generateErrorException("ErrorExceptionException.json");
        // Mock error in save of repository

        /*
         * Act
         * */
        Either<ErrorExeption, UserTransactional> resultGetUser = redisRepositoryImpl.getUser("ID-SESS-TEST");

        /*
         * Assertions
         * */
        assertEquals(errorExeptionTest.getCode(), resultGetUser.getLeft().getCode());
        assertEquals(errorExeptionTest.getDescription(), resultGetUser.getLeft().getDescription());
    }

    @Test
    void testShouldGetSessionsConcurrent() throws IOException {
        /*
         * Arrange
         * */
        // List response repository
        List<UserTransactionalRedis> listUserTransactionalRedis = new ArrayList<>();
        // Create Response repository
        UserTransactionalRedis userTransactionalRedisA
                = generateTemplateUserTransactionalRedis("UserTransactionalModelAndEntity.json");
        // Create Response repository
        UserTransactionalRedis userTransactionalRedisB
                = generateTemplateUserTransactionalRedis("UserTransactionalWithoutFields.json");
        listUserTransactionalRedis.add(userTransactionalRedisA);
        listUserTransactionalRedis.add(userTransactionalRedisB);
        // List response model
        List<UserTransactional> listUserTransactional = new ArrayList<>();
        // Value expected is model (Convert entity response to model) - Value A
        UserTransactional userTransactionalA
                = generateModelUserTransactional("UserTransactionalModelAndEntity.json");
        // Value expected is model (Convert entity response to model) - Value A
        UserTransactional userTransactionalB
                = generateModelUserTransactional("UserTransactionalWithoutFields.json");
        listUserTransactional.add(userTransactionalA);
        listUserTransactional.add(userTransactionalB);
        // Mock Response repository
        when(iRedisRepository.findByDocNumber(anyString())).thenReturn(listUserTransactionalRedis);
        // Create class, Inject IRedisRepository with Mock, Inject Log created for test
        redisRepositoryImpl = new RedisRepositoryImpl(iRedisRepository, maper, loggerApp);
        // Create UserTransactional for test


        /*
         * Act
         * */
        Either<ErrorExeption, List<UserTransactional>> resultGetSessionsConcurrent
                = redisRepositoryImpl.getSessionsConcurrent("1234", "ID-SESS-TEST");

        /*
         * Assertions
         * */
        assertEquals(listUserTransactional, resultGetSessionsConcurrent.getRight());
    }

    @Test
    void testShouldThrowIllegalArgumentExceptionInGetSessionsConcurrentForParameterPassNull() throws IOException {
        /*
         * Arrange
         * */
        // Create class, Inject IRedisRepository with Mock, Inject Log created for test
        redisRepositoryImpl = new RedisRepositoryImpl(iRedisRepository, maper, loggerApp);
        // Error response
        ErrorExeption errorExeptionTest = generateErrorException("ErrorExceptionIllegalArgumentException.json");

        /*
         * Act
         * */
        Either<ErrorExeption, List<UserTransactional>> resultGetSessionsConcurrent
                = redisRepositoryImpl.getSessionsConcurrent(null, "ID-SESS-TEST");

        /*
         * Assertions
         * */
        assertEquals(errorExeptionTest.getCode(), resultGetSessionsConcurrent.getLeft().getCode());
        assertEquals(errorExeptionTest.getDescription(), resultGetSessionsConcurrent.getLeft().getDescription());
    }

    @Test
    void testShouldThrowIllegalArgumentExceptionInGetSessionsConcurrentForProblemInRepository() throws IOException {
        /*
         * Arrange
         * */
        // Mock error in save of repository
        when(iRedisRepository.findByDocNumber(any())).thenThrow(IllegalArgumentException.class);
        // Create class, Inject IRedisRepository with Mock, Inject Log created for test
        redisRepositoryImpl = new RedisRepositoryImpl(iRedisRepository, maper, loggerApp);
        // Error response
        ErrorExeption errorExeptionTest = generateErrorException("ErrorExceptionIllegalArgumentException.json");

        /*
         * Act
         * */
        Either<ErrorExeption, List<UserTransactional>> resultGetSessionsConcurrent
                = redisRepositoryImpl.getSessionsConcurrent("1234", "ID-SESS-TEST");

        /*
         * Assertions
         * */
        assertEquals(errorExeptionTest.getCode(), resultGetSessionsConcurrent.getLeft().getCode());
        assertEquals(errorExeptionTest.getDescription(), resultGetSessionsConcurrent.getLeft().getDescription());
    }

    @Test
    void testShouldThrowExceptionInGetSessionsConcurrentForProblemInRepository() throws IOException {
        /*
         * Arrange
         * */
        // Create class, Inject IRedisRepository with Mock, Inject Log created for test
        redisRepositoryImpl = new RedisRepositoryImpl(null,  maper,loggerApp);
        // Error response
        ErrorExeption errorExeptionTest = generateErrorException("ErrorExceptionException.json");
        // Mock error in save of repository

        /*
         * Act
         * */
        Either<ErrorExeption, List<UserTransactional>> resultGetSessionsConcurrent
                = redisRepositoryImpl.getSessionsConcurrent("1234", "ID-SESS-TEST");

        /*
         * Assertions
         * */
        assertEquals(errorExeptionTest.getCode(), resultGetSessionsConcurrent.getLeft().getCode());
        assertEquals(errorExeptionTest.getDescription(), resultGetSessionsConcurrent.getLeft().getDescription());
    }

    private UserTransactional generateModelUserTransactional(String nameObjectJSON) throws IOException {
        // convert JSON string to Book object
        String locationJSONTests = System.getProperty("user.dir") + File.separator + "src" +
                File.separator + "test" + File.separator + "resources" +
                File.separator;
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(Paths.get(locationJSONTests + nameObjectJSON).toFile(), UserTransactional.class);
    }

    private ErrorExeption generateErrorException(String nameObjectJSON) throws IOException {
        // convert JSON string to Book object
        String locationJSONTests = System.getProperty("user.dir") + File.separator + "src" +
                File.separator + "test" + File.separator + "resources" +
                File.separator;
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(Paths.get(locationJSONTests + nameObjectJSON).toFile(), ErrorExeption.class);
    }

    private UserTransactionalRedis generateTemplateUserTransactionalRedis(String nameObjectJSON) throws IOException {
        // convert JSON string to Book object
        String locationJSONTests = System.getProperty("user.dir") + File.separator + "src" +
                File.separator + "test" + File.separator + "resources" +
                File.separator;
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(Paths.get(locationJSONTests + nameObjectJSON).toFile(), UserTransactionalRedis.class);
    }
}
