package com.diane.blog.util;

import java.util.UUID;

/**
 * @author dianedi
 * @date 2021/2/5 16:36
 * @Destription  uuid生成32位主键
 */
public class CreateKeyUtil {
    public String getUUIDKey(){return UUID.randomUUID().toString().replace("-","");

    }
}
