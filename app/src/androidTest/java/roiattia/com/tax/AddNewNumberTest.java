package roiattia.com.tax;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class AddNewNumberTest {

    @Rule public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void clickNumberAddVat_DoCalculation(){
        onView(withId(R.id.rb_add_vat)).perform(click());
        onView(withId(R.id.calc_one)).perform(click());
        onView(withId(R.id.tv_before_calc)).check(matches(withText("1")));
        onView(withId(R.id.tv_vat)).check(matches(withText("0.17")));
        onView(withId(R.id.tv_after_calc)).check(matches(withText("1.17")));
    }

    @Test
    public void clickNumberRemoveVat_DoCalculation(){
        onView(withId(R.id.rb_subtract_vat)).perform(click());
        onView(withId(R.id.calc_one)).perform(click());
        onView(withId(R.id.tv_before_calc)).check(matches(withText("1")));
        onView(withId(R.id.tv_vat)).check(matches(withText("0.145")));
        onView(withId(R.id.tv_after_calc)).check(matches(withText("0.855")));
    }

    @Test
    public void clickDelete_DoCalculation(){
        onView(withId(R.id.rb_add_vat)).perform(click());
        onView(withId(R.id.calc_one)).perform(click());
        onView(withId(R.id.calc_one)).perform(click());
        onView(withId(R.id.calc_delete)).perform(click());
        onView(withId(R.id.tv_before_calc)).check(matches(withText("1")));
        onView(withId(R.id.tv_vat)).check(matches(withText("0.17")));
        onView(withId(R.id.tv_after_calc)).check(matches(withText("1.17")));
    }

    @Test
    public void clickDot_DoCalculation(){
        onView(withId(R.id.rb_add_vat)).perform(click());
        onView(withId(R.id.calc_one)).perform(click());
        onView(withId(R.id.calc_dot)).perform(click());
        onView(withId(R.id.calc_one)).perform(click());
        onView(withId(R.id.tv_before_calc)).check(matches(withText("1.1")));
        onView(withId(R.id.tv_vat)).check(matches(withText("0.187")));
        onView(withId(R.id.tv_after_calc)).check(matches(withText("1.287")));
    }

    @Test
    public void clickNumberGreaterThenThousand_DoCalculation(){
        onView(withId(R.id.rb_add_vat)).perform(click());
        onView(withId(R.id.calc_one)).perform(click());
        onView(withId(R.id.calc_one)).perform(click());
        onView(withId(R.id.calc_one)).perform(click());
        onView(withId(R.id.calc_one)).perform(click());
        onView(withId(R.id.tv_before_calc)).check(matches(withText("1,111")));
        onView(withId(R.id.tv_vat)).check(matches(withText("188.87")));
        onView(withId(R.id.tv_after_calc)).check(matches(withText("1,299.87")));
    }
}
