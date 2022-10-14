package economy.reverse.repurchase.agreement.util;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.io.File;
import java.io.IOException;

/**
 * @author lizhejin
 * Date: 2019/9/10
 */
public class GeneratorUtil {
    /**
     * 数据库配置四要素
     */
    private static final String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://43.143.47.137:3306/economy?useUnicode=true&characterEncoding=utf-8&useSSL=false";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "1qaz!QAZ";

    public static void main(String[] args) {
        moduleGenerator(new String[]{
                "graham_index"
        });
    }

    private static void moduleGenerator(String[] tableNames){

        // 全局配置
        GlobalConfig globalConfig = getGlobalConfig();

        // 数据源配置
        DataSourceConfig dataSourceConfig = getDataSourceConfig();

        // 包配置
        PackageConfig packageConfig = getPackageConfig();

        // 策略配置
        StrategyConfig strategyConfig = getStrategyConfig(tableNames);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig
                .setEntity(new TemplateConfig().getEntity(false))
                //mapper模板采用mybatis-plus自己模板
                .setMapper(new TemplateConfig().getMapper())
                .setXml(new TemplateConfig().getXml())
                .setService(new TemplateConfig().getService())
                .setServiceImpl(null)
                .setController(null);

        // 自定义xml生成位置
//        InjectionConfig injectionConfig = getInjectionConfig();

        new AutoGenerator()
                .setGlobalConfig(globalConfig)
                .setDataSource(dataSourceConfig)
                .setPackageInfo(packageConfig)
//                .setCfg(injectionConfig)
                .setStrategy(strategyConfig)
                .setTemplate(templateConfig)
                .setTemplateEngine(new FreemarkerTemplateEngine())
                .execute();

    }

    private static GlobalConfig getGlobalConfig() {
        GlobalConfig globalConfig = new GlobalConfig();
        String authorName = "xuxianbei";
        try {
            globalConfig.setOpen(false)
                    //new File(module).getAbsolutePath()得到模块根目录路径，因事Maven项目，代码指定路径自定义调整
                    .setOutputDir(new File("ReverseRepurchaseAgreement").getCanonicalFile().getParentFile().getPath()+"/src/main/java")
                    //生成文件的输出目录
                    .setFileOverride(false)
                    //是否覆盖已有文件
                    .setAuthor(authorName)
                    .setBaseResultMap(true)
                    .setBaseColumnList(true)
                    .setEnableCache(false)
                    .setEntityName("%s")
                    .setMapperName("%sMapper")
//                    .setServiceName("%sService")
//                    .setServiceImplName("%sServiceImpl")
                    .setXmlName("%sMapper");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return globalConfig;
    }

    private static DataSourceConfig getDataSourceConfig() {
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL)
                .setDriverName(DRIVER_NAME)
                .setUsername(USERNAME)
                .setPassword(PASSWORD)
                .setUrl(URL);
        return dataSourceConfig;
    }

    private static PackageConfig getPackageConfig() {
        PackageConfig packageConfig = new PackageConfig();
        //不同模块 代码生成具体路径自定义指定
        String basePackage = "economy.reverse.repurchase.agreement";
        packageConfig.setParent(basePackage)
                .setEntity("model")
                .setMapper("dao")
                .setXml("dao.impl")
//                .setService("service")
//                .setServiceImpl("service.impl")
                .setController("controller");
        return packageConfig;
    }

    private static StrategyConfig getStrategyConfig(String[] tableNames) {
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig
                //驼峰命名
                .setCapitalMode(true)
                .setEntityLombokModel(true)
                .setNaming(NamingStrategy.underline_to_camel)
                .setInclude(tableNames);
        return strategyConfig;
    }

}