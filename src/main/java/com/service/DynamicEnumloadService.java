package com.service;


import com.constant.enums.Campus;
import com.util.DynamicEnumUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.logging.Logger;

/**
 * 项目启动后就会执行此类，
 */
@Service
public class DynamicEnumloadService implements InitializingBean {

    private static Logger logger = Logger.getLogger("");

    @Override
    public void afterPropertiesSet() {
        InputStream inputStream = DynamicEnumloadService.class.getResourceAsStream("/constant.yml");
        //实例化Properties类
        Yaml yaml = new Yaml();
        Map<String, Map<String, String>> parentMap = null;
        //调用load()方法加载yaml文件，load里面传入InputSteam类型的参数或者Reader类型的参数
        try {
            parentMap = (Map) yaml.load(new InputStreamReader(inputStream, "UTF-8"));
        } catch (IOException e) {
            logger.info("动态枚举加载失败......");
            e.printStackTrace();
        }


        for (String parentKey : parentMap.keySet()) {
            Map<String, String> childMap = parentMap.get(parentKey);

            for (String key : childMap.keySet()) {

                String value = childMap.get(key);
                String[] split = value.split("\\|");

                try {
                    addTestEnum((Class<? extends Enum<?>>) Class.forName(parentKey), key,
                            new Object[]{Integer.valueOf(split[0]), split[1]});
                } catch (ClassNotFoundException e) {
                    logger.info("动态枚举加载失败......");
                    e.printStackTrace();
                }

            }
        }

    }

    /**
     * 新增枚举项
     *
     * @param clazz    枚举类
     * @param enumName 枚举名称
     * @param objects  枚举项
     */
    private static <T extends Enum<?>> void addTestEnum(Class<? extends Enum<?>> clazz, String enumName, Object[] objects) {
        DynamicEnumUtil.addEnum(clazz, enumName, new Class<?>[]
                {Integer.class, String.class}, objects);

    }
}