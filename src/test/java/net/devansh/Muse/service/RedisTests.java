package net.devansh.Muse.service;

import net.devansh.Muse.config.TestConfig;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest

public class RedisTests {
    
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    @Disabled
    void testRedisOperations() {
        // Given
        String key = "test:email";
        String expectedEmail = "devansh@email.com";
        
        // When
        redisTemplate.opsForValue().set(key, expectedEmail);
        String actualEmail = (String) redisTemplate.opsForValue().get(key);
        
        // Then
        assertEquals(expectedEmail, actualEmail, "The retrieved email should match the stored one");
    }
}
