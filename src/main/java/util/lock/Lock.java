package util.lock;

import com.infofuse.cache.CacheManager;
import com.infofuse.cache.exception.CacheException;
import com.infofuse.util.ServiceLocator;

/**
 * Redis Lock
 *
 * @author luffy
 * @date 15/8/18
 */
public class Lock implements AutoCloseable {

    CacheManager cacheManager;
    String region;
    String key;

    public Lock(String region, String key, long expire, boolean reentrantLock) throws LockException {
        try {
            this.cacheManager = ServiceLocator.getBean(CacheManager.class, "cacheManager");
            this.region = region;
            this.key = key;
            this.cacheManager.acquireLock(region, key, expire, 5, reentrantLock);
        } catch (Exception e) {
            throw new LockException(e.getMessage(), e);
        }
    }

    public Lock(String region, String key) throws LockException {
        this(region, key, 3, true);
    }

    public Lock(String region, String key, boolean reentrantLock) throws LockException {
        this(region, key, 3, reentrantLock);
    }

    @Override
    public void close() throws LockException {
        try {
            this.cacheManager.releaseLock(this.region, this.key);
        } catch (CacheException e) {
            //throw new LockException(e.getMessage(), e);
        }
    }
}
