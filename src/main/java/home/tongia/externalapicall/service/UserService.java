package home.tongia.externalapicall.service;

import home.tongia.externalapicall.dto.Newspaper;
import home.tongia.externalapicall.dto.Newspapers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {

    private final static String URL = "https://chroniclingamerica.loc.gov/newspapers.json";
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private CacheManager cacheManager;

    @Cacheable("users")
    public List<Newspaper> getAllNewspapers() {
        System.out.println("Calling All Newspapers API");
        ResponseEntity<Newspapers> response = restTemplate.getForEntity(URL, Newspapers.class);
        return response.getBody().newspapers();
    }

    @Cacheable("user")
    public Newspaper getNewspaperById(String id) {
        System.out.println("Calling for " + id);
        Cache usersCache = cacheManager.getCache("users");
        if (usersCache != null) {
            // Cache key will be "getAllUsers[]" because the getAllUsers method takes no arguments
            Cache.ValueWrapper cachedValue = usersCache.get(SimpleKey.EMPTY);
            if (cachedValue != null) {
                List<Newspaper> allUsers = (List<Newspaper>) cachedValue.get();
                return allUsers.stream()
                        .filter(user -> id.equals(user.lccn()))
                        .findFirst()
                        .orElseThrow(() -> new NoSuchElementException("User not found with id " + id));
            }
        }
        throw new RuntimeException("Could not fetch users from cache");
    }
}
