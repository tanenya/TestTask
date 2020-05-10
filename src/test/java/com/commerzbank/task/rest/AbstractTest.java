package com.commerzbank.task.rest;

import com.commerzbank.task.configuration.TestConfiguration;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfiguration.class)
public abstract class AbstractTest {
}
