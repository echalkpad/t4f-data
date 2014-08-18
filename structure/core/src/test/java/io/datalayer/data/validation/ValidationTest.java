/****************************************************************
 * Licensed to the AOS Community (AOS) under one or more        *
 * contributor license agreements.  See the NOTICE file         *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The AOS licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/
/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 */
package io.datalayer.data.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;

import org.apache.bval.jsr303.ApacheValidatorFactory;
import org.junit.Test;

public class ValidationTest {

    @Valid
    private Collection<Foo> foos = new ArrayList<Foo>();

    public ValidationTest() {
        foos.add(new Foo("foo1"));
        foos.add(null);
        foos.add(new Foo("foo3"));
    }

    public class Foo {
        @NotNull
        public String bar;

        public Foo(String bar) {
            this.bar = bar;
        }

    }

    @Test
    public void testValidation() {
        ValidationTest t = new ValidationTest();

        Validator v = ApacheValidatorFactory.getDefault().getValidator();
        Set<ConstraintViolation<ValidationTest>> errors = v.validate(t);
        System.out.println("got errors:");
        for (ConstraintViolation<?> error : errors) {
            System.out.println(error.getPropertyPath());
        }
    }
    
}
