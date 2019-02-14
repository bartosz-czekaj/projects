package test;

import com.junit.first.MyMath;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MyMathTest {

    MyMath myMath;

    @BeforeEach
    void init() {
        myMath = new MyMath();
    }


    @Test
    public void sum_3() {


        int ret = myMath.sum(new int []{1,1,1});

        assertEquals(3, ret);
    }

    @Test
    public void sum_1() {

        int ret = myMath.sum(new int []{3});

        assertEquals(3, ret);
    }
}