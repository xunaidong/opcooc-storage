/*
 * Copyright © 2020-2020 organization opcooc
 * <pre>
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
 * <pre/>
 */
package com.opcooc.storage.arguments;

import com.opcooc.storage.exception.StorageException;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author shenqicheng
 * @since 2020-09-20 20:20
 */
public abstract class BaseArgs {

    /**
     * 基础构造器
     *
     * @param <A> 子类继承者
     * @param <B> 子类Builder实现者
     */
    public abstract static class Builder<A extends BaseArgs, B extends Builder<A, B>> {
        // 基础参数赋值操作集合
        protected List<Consumer<A>> operations;

        /**
         * 基础参数检验器
         *
         * @param args 子类
         */
        protected abstract void validate(A args);

        public Builder() {
            //参数赋值初始化
            this.operations = new ArrayList<>();
        }

        @SuppressWarnings("unchecked")
        private A newInstance() {
            try {
                for (Constructor<?> constructor : this.getClass().getEnclosingClass().getConstructors()) {
                    if (constructor.getParameterCount() == 0) {
                        return (A) constructor.newInstance();
                    }
                }
                throw new StorageException("opcooc-storage - %s must have no argument constructor",
                        this.getClass().getEnclosingClass());
            } catch (Exception e) {
                throw new StorageException(e);
            }
        }

        public A build() {
            //得到对象
            A args = newInstance();
            //填充参数
            operations.forEach(operation -> operation.accept(args));
            //检验对象参数是否合法
            validate(args);
            return args;
        }

    }
}
