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
package com.wideplay.warp.persist.jdo;

import com.wideplay.warp.persist.*;
import com.wideplay.warp.persist.internal.InternalWorkManager;
import org.aopalliance.intercept.*;

import javax.jdo.*;
import java.lang.reflect.Method;

/**
 * @author Miroslav Genov
 */
class JdoLocalTxnInterceptor implements MethodInterceptor {
 private final InternalWorkManager<PersistenceManager> internalWorkManager;
    private final UnitOfWork unitOfWork;

    //TODO this is a clunky hack, make a TransactionalImpl and make it customizable
    @Transactional
    private static class Internal { }

    public JdoLocalTxnInterceptor(InternalWorkManager<PersistenceManager> internalWorkManager, UnitOfWork unitOfWork) {
        this.internalWorkManager = internalWorkManager;
        this.unitOfWork = unitOfWork;
    }

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Transactional transactional = readTransactionMetadata(methodInvocation);

        if (transactional.type() != null && transactional.type() == TransactionType.READ_ONLY) {
            throw new UnsupportedOperationException("Transaction type READ_ONLY is not supported from JPA");
        }

        PersistenceManager em = this.internalWorkManager.beginWork();


        //allow joining of transactions if there is an enclosing @Transactional method
        if (em.currentTransaction().isActive())
            return methodInvocation.proceed();

        //otherwise...

        //start txn
        final Transaction txn = em.currentTransaction();
        txn.begin();

        Object result;
        try {
            result = methodInvocation.proceed();

        } catch (Exception e) {
            //commit transaction only if rollback didnt occur
            if (rollbackIfNecessary(transactional, e, txn))
                txn.commit();
            
            //propagate whatever exception is thrown anyway
            throw e;
        } finally {
            //close the pm if necessary (this code doesnt run unless above catch/rethrow fired)
            if (isUnitOfWorkTransaction() && !txn.isActive()) {
                closePersistenceManager();

            }
        }


        //everything was normal so commit the txn (do not move into try block as it interferes from the advised method's throwing semantics)
        try {
            txn.commit();
        } finally {
            //close the pm if necessary
            if (isUnitOfWorkTransaction()) {
                closePersistenceManager();
            }
        }

        //or return result
        return result;
    }

    private void closePersistenceManager() {
        this.internalWorkManager.endWork();
    }

    private Transactional readTransactionMetadata(MethodInvocation methodInvocation) {
        Transactional transactional;
        Method method = methodInvocation.getMethod();

        //if none on method, try the class
        Class<?> targetClass = methodInvocation.getThis().getClass().getSuperclass();

        //if there is no transactional annotation of Warp's present, use the default
        if (method.isAnnotationPresent(Transactional.class))
            transactional = method.getAnnotation(Transactional.class);

        else if (targetClass.isAnnotationPresent(Transactional.class))
            transactional = targetClass.getAnnotation(Transactional.class);

        else
            transactional = Internal.class.getAnnotation(Transactional.class);
        return transactional;
    }

    /**
     *
     * @param transactional The metadata annotaiton of the method
     * @param e The exception to test for rollback
     * @param txn A Hibernate Transaction to issue rollbacks against
     * @return returns Returns true if rollback DID NOT HAPPEN (i.e. if commit should continue)
     */
    private boolean rollbackIfNecessary(Transactional transactional, Exception e, Transaction txn) {
        boolean commit = true;

        //check rollback clauses
        for (Class<? extends Exception> rollBackOn : transactional.rollbackOn()) {

            //if one matched, try to perform a rollback
            if (rollBackOn.isInstance(e)) {
                commit = false;

                //check exceptOn clauses (supercedes rollback clause)
                for (Class<? extends Exception> exceptOn : transactional.exceptOn()) {

                    //An exception to the rollback clause was found, DONT rollback (i.e. commit and throw anyway)
                    if (exceptOn.isInstance(e)) {
                        commit = true;
                        break;
                    }
                }

                //rollback only if nothing matched the exceptOn check
                if (!commit) {
                    txn.rollback();
                }
                //otherwise continue to commit

                break;
            }
        }

        return commit;
    }
    private boolean isUnitOfWorkTransaction() {
        return this.unitOfWork == UnitOfWork.TRANSACTION;
    }
}
