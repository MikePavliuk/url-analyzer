package com.mykhailopavliuk;

import com.mykhailopavliuk.repository.UserRepositoryTest;
import com.mykhailopavliuk.service.UserServiceTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.suite.api.SelectClasses;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SelectClasses({
        UserRepositoryTest.class,
        UserServiceTest.class
})
public class SpringApplicationTests {

}