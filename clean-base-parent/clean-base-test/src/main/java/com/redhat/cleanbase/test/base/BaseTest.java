package com.redhat.cleanbase.test.base;


import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseTest {
}
