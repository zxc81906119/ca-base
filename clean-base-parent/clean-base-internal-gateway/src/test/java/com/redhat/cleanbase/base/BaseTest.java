package com.redhat.cleanbase.base;

import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseTest {
}
