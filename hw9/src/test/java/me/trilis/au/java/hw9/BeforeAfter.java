package me.trilis.au.java.hw9;

public class BeforeAfter {

    static int beforeCount = 0;
    static int afterCount = 0;
    static int beforeClassCount = 0;
    static int afterClassCount = 0;

    @BeforeClass
    void beforeClass() {
        beforeClassCount++;
    }


    @AfterClass
    void afterClass() {
        afterClassCount++;
    }

    @Before
    void before() {
        beforeCount++;
    }


    @After
    void after() {
        afterCount++;
    }

    @Test
    void test1() {

    }

    @Test
    void test2() {

    }

    @Test
    void test3() {

    }

}
