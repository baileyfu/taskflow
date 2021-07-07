package x.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.theories.Theory;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;

@RunWith(JUnitQuickcheck.class)
public class QuickCheckTest {
	public static void main(String[] args) {
		System.out.println("++==eee=+");
		JUnitCore.runClasses(QuickCheckTest.class);
	}
	@Theory
	@Property public void concatenationLength(String s1, String s2) {
		System.out.println("++===+");
		Assert.assertEquals(s1.length() + s2.length(), (s1 + s2).length());
    }
	@Theory
	@Property public void shouldHold(@From(CharacterGenerator.class) String s) {
        // here you should add your unit test which uses the generated output
        // 
        // assertTrue(doMyUnitTest(s) == expectedResult);

        // the below lines only for demonstration and currently
        // check that the generated random has the expected
        // length and matches the expected pattern
        System.out.println("shouldHold(): " + s);
        Assert.assertTrue(s.length() == CharacterGenerator.CAPACITY);
        Assert.assertTrue(s.matches("[a-zA-Z0-9.\\-\\\\;:_@\\[\\]^/|}{]*"));
    }
}
