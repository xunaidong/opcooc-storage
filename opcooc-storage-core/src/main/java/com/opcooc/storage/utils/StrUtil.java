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
package com.opcooc.storage.utils;

import org.apache.commons.lang3.StringUtils;

/**
 *
 *
 * @author shenqicheng
 * @since 2020-08-22 10:30
 */
public class StrUtil {

    public static String subAfter(String string, String separator) {
        if (StringUtils.isEmpty(string)) {
            return string;
        }
        if (separator == null) {
            return StorageConstant.EMPTY;
        }
        final String str = string;
        final String sep = separator;
        final int pos = str.lastIndexOf(sep);
        if (StorageConstant.INDEX_NOT_FOUND == pos || (string.length() - 1) == pos) {
            return StorageConstant.EMPTY;
        }
        return str.substring(pos + separator.length());
    }


    public static String checkFolder(String path){
        if (null == path) {
            return "";
        }
        return path.endsWith("/") ? path : path + "/";
    }

}
