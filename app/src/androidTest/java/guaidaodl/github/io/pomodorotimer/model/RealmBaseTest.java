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

package guaidaodl.github.io.pomodorotimer.model;

import org.junit.AfterClass;

import guaidaodl.github.io.pomodorotimer.model.factory.ModelFactory;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * The base class of Realm test case, in the setup, it will set the configuration for test.
 */
public class RealmBaseTest {
    private static RealmConfiguration sTestConfig;

    protected Realm mRealm;
    public void setUp() {
        sTestConfig = new RealmConfiguration.Builder()
                .name("test.realm")
                .schemaVersion(1)
                .build();

        Realm.setDefaultConfiguration(sTestConfig);
        mRealm = Realm.getDefaultInstance();
    }

    public void tearDown() {
        mRealm.beginTransaction();
        mRealm.deleteAll();
        mRealm.commitTransaction();
        mRealm.close();
    }

    @AfterClass
    public static void afterClass() {
        ModelFactory.getInstance().closeRealm();
        Realm.deleteRealm(sTestConfig);
    }
}
