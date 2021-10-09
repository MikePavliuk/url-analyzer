package com.mykhailopavliuk;

import com.mykhailopavliuk.repository.UserRepositoryTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.suite.api.SelectClasses;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SelectClasses({
        UserRepositoryTest.class
})
public class SpringApplicationTests {
}