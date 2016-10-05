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

package io.github.guaidaodl.espresso.utils;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;

public class ColorFormat {
    private ColorFormat() {
    }

    /**
     * converts a color associated with given resourceId to hex string without alpha,
     * such as '#FFFFFF'.
     */
    @NonNull
    public static String toHexFormat(@NonNull Context context, @ColorRes int resourceId) {
        //noinspection deprecation
        int color = context.getResources().getColor(resourceId);

        return toHexFormat(color);
    }

    /**
     * converts a color integer to hex string without alpha, such as '#FFFFFF'.
     *
     * @param color the color integter.
     */
    @NonNull
    public static String toHexFormat(@ColorInt int color) {
        return String.format("#%06X", (0xFFFFFF & color));
    }
}
