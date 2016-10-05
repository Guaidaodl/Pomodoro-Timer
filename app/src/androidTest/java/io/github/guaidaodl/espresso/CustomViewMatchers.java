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

package io.github.guaidaodl.espresso;

import android.content.res.Resources;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import io.github.guaidaodl.espresso.utils.ColorFormat;

public class CustomViewMatchers {
    private CustomViewMatchers() {
    }

    /**
     * Returns a matcher that matches the descendant of {@link TextView} that is displaying
     * text with the color associated with given color resource id.
     *
     * @param colorRes the color resource which the text view's text should be.
     */
    @NonNull
    public static Matcher<View> withTextColor(@ColorRes final int colorRes) {
        return new BoundedMatcher<View, TextView>(TextView.class) {
            private Integer expectedColor = -1;
            private String expectedColorName = null;

            @Override
            public void describeTo(Description description) {
                description.appendText("with color from resource id : ")
                        .appendValue(colorRes);

                if (expectedColorName != null) {
                    description.appendText("[").appendText(expectedColorName).appendText("]");
                }

                if (expectedColor != -1) {
                    description.appendValue(" value: ")
                            .appendText(ColorFormat.toHexFormat(expectedColor));

                }
            }

            @Override
            protected boolean matchesSafely(TextView textView) {
                if (expectedColor == -1) {
                    try {
                        //noinspection deprecation
                        expectedColor = textView.getResources().getColor(colorRes);
                        expectedColorName = textView.getResources().getResourceEntryName(colorRes);
                    } catch (Resources.NotFoundException ignore) {
                    }
                }

                int actualColor = textView.getCurrentTextColor();

                return expectedColor != null && expectedColor == actualColor;
            }
        };
    }

    /**
     * Returns a matcher that matches the desecndant of {@link ViewPager} whose current is
     * same as the given item.
     *
     * @param currentItem the item that the view pager's current item should be.
     */
    public static Matcher<View> withItem(final int currentItem) {
        return new BoundedMatcher<View, ViewPager>(ViewPager.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText("with current item")
                        .appendValue(currentItem);
            }

            @Override
            protected boolean matchesSafely(ViewPager viewPager) {
                return viewPager.getCurrentItem() == currentItem;
            }
        };
    }
}
