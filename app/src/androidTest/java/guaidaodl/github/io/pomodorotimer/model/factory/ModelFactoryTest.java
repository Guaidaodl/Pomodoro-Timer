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

package guaidaodl.github.io.pomodorotimer.model.factory;

import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import guaidaodl.github.io.pomodorotimer.model.RealmBaseTest;
import guaidaodl.github.io.pomodorotimer.model.realm.Tomato;
import io.realm.RealmResults;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class ModelFactoryTest extends RealmBaseTest {
    @Before
    @Override
    public void setUp() {
        super.setUp();
    }

    @After
    @Override
    public void tearDown() {
        super.tearDown();
    }

    /**
     * 测试能否成功的创建
     */
    @Test
    public void testCreateTomato() {
        RealmResults<Tomato> allTomato = mRealm.where(Tomato.class).findAll();
        assertThat("Before create, no tomato", allTomato.size(), equalTo(0));

        // create new one
        long endTime = System.currentTimeMillis();
        long startTime = endTime - 25L * 60L * 1000L;
        ModelFactory.getInstance().newTomato(startTime, endTime);

        allTomato = mRealm.where(Tomato.class).findAll();
        assertThat("After create a new tomato, there is one tomato", allTomato.size(), equalTo(1));

        Tomato tomato = allTomato.get(0);
        assertThat(startTime, equalTo(tomato.getStartTime()));
        assertThat(endTime, equalTo(tomato.getEndTime()));

        // error
        try {
            ModelFactory.getInstance().newTomato(0, -1);
            fail();
        } catch (IllegalArgumentException ignore) {
        }

        try {
            ModelFactory.getInstance().newTomato(1, 0);
            fail();
        } catch (IllegalArgumentException ignore) {
        }
    }
}