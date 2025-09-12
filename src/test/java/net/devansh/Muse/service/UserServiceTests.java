package net.devansh.Muse.service;

import net.devansh.Muse.entity.User;
import net.devansh.Muse.repository.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTests {
    @Autowired
    private UserRepository userRepository;

    @Disabled
    @ParameterizedTest
    @ValueSource(strings = {"ball","RAM","dog"})
    public void testFindByUsername(String name) {
        User user = userRepository.findByUsername(name);
        assertNotNull(user);
        assertTrue(5 > 4);
    }
    @ParameterizedTest
    @CsvSource(
            {
                    "1,1,2",
                    "2,3,5",
                    "2,5,7"
            }
    )
    public void test(int a,int b,int expected){
        assertEquals(expected , a+b);
    }



}
