/*
 *  Copyright (c) 2016.  Guaidaodl
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */

package guaidaodl.github.io.pomodorotimer.ui.main;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import guaidaodl.github.io.pomodorotimer.R;
import io.github.guaidaodl.espresso.CustomViewMatchers;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static io.github.guaidaodl.espresso.CustomViewMatchers.withTextColor;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testSwitchTab() throws Exception {
        onView(withText(R.string.main_tab_todo_today))
                .check(matches(withTextColor(R.color.secondaryTextColor)))
                .perform(click())
                .check(matches(withTextColor(R.color.primaryTextColor)));
        onView(withId(R.id.main_pager))
                .check(matches(CustomViewMatchers.withItem(1)));

        onView(withText(R.string.main_tab_action_list))
                .check(matches(withTextColor(R.color.secondaryTextColor)))
                .perform(click())
                .check(matches(withTextColor(R.color.primaryTextColor)));
        onView(withId(R.id.main_pager))
                .check(matches(CustomViewMatchers.withItem(2)));

        onView(withText(R.string.main_tab_timer))
                .check(matches(withTextColor(R.color.secondaryTextColor)))
                .perform(click())
                .check(matches(withTextColor(R.color.primaryTextColor)));
        onView(withId(R.id.main_pager))
                .check(matches(CustomViewMatchers.withItem(0)));

    }
}