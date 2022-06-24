package com.hj.springconfig;


import com.hj.core.WaterMarkAttribute;
import com.hj.pdf.PdfAddWaterMark;
import org.aopalliance.aop.Advice;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.lang.reflect.Method;

/**
 * 文件转换切面
 */
public class AddWaterMarkAdvisor implements PointcutAdvisor {

    Logger logger = LoggerFactory.getLogger(AddWaterMarkAdvisor.class);


    /*  完成 spel 解析 与 MultiFile 文件替换  */

    @Override
    public Advice getAdvice() {
        return new MethodBeforeAdvice() {

            @Override
            public void before(Method method, Object[] args, Object target) throws Throwable {

                System.out.println(method.getName());

                for (Object arg : args) {
                    if (arg instanceof MultipartFile) {
                        MultipartFile multipartFile = (MultipartFile) arg;
                        //这里实现文件替换操作
                        // 实现动态替换 File
                        MetaObject metaObject = SystemMetaObject.forObject(multipartFile);
                        //删除原来的缓存文件
                        File file = (File) metaObject.getValue("part.fileItem.dfos.outputFile");
                        com.hj.core.AddWaterMark addWaterMark = new PdfAddWaterMark(new WaterMarkAttribute());
                        addWaterMark.transfer(file.getPath(),"/Users/hejie/Desktop/temp.pdf","11111");
                        file.delete();


                        metaObject.setValue("part.fileItem.dfos.outputFile", new File("/Users/hejie/Desktop/temp.pdf"));

                    }

                }

            }
        };
    }

    @Override
    public boolean isPerInstance() {
        return true;
    }

    @Override
    public Pointcut getPointcut() {
        /**
         * 简单的Pointcut定义，匹配所有类的find方法调用。
         */
        return new Pointcut() {

            @Override
            public ClassFilter getClassFilter() {
                return ClassFilter.TRUE;
            }

            @Override
            public MethodMatcher getMethodMatcher() {
                return new MethodMatcher() {

                    @Override
                    public boolean matches(Method method, Class<?> targetClass) {
                        return method.getAnnotation(AddWaterMark.class) != null;
                    }

                    @Override
                    public boolean isRuntime() {
                        return false;
                    }

                    @Override
                    public boolean matches(Method method, Class<?> targetClass, Object[] args) {
                        return true;
                    }

                };
            }

        };
    }

}