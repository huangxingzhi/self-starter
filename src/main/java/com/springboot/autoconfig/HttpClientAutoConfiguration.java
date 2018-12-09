package com.springboot.autoconfig;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration//注明该类是一个Java配置类
@ConditionalOnClass({HttpClient.class})//当HttpClient存在时，才进行自动配置
@EnableConfigurationProperties(HttpClientProperties.class)//读取默认配置
public class HttpClientAutoConfiguration {

    private final HttpClientProperties properties;

    //在application.properties中进行配置，将使用其创建好的配置
    public HttpClientAutoConfiguration(HttpClientProperties properties){
        this.properties = properties;
    }

    @Bean//声明为bean,才能自动注入
    @ConditionalOnMissingBean(HttpClient.class)//当不存在创建好的HttpClient实例，将使用该方法进行创建
    public HttpClient httpClient(){
        //构建requestConfig
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(properties.getConnectTimeOut())//设置连接超时时间，默认1秒
                .setSocketTimeout(properties.getSocketTimeOut()).build();//设置读超时时间，默认10秒
        HttpClient client = HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig) //设置requestConfig
                .setUserAgent(properties.getAgent())//设置User-Agent
                .setMaxConnPerRoute(properties.getMaxConnPerRoute())//设置一个远端IP最大的连接数
                .setMaxConnTotal(properties.getMaxConnTotaol())//设置总的连接数
                .build();
        return client;
    }

    @Bean
    @ConditionalOnBean(name="httpClient",value=HttpClient.class)//已存在HttpClient实例httpClient
    @ConditionalOnProperty(name = "spring.httpclient.testConfig", havingValue = "true")//读取配置是否设置该属性为true
    public HttpClient httpClient1(HttpClient httpClient) throws IOException {
        Long contentLength=httpClient.execute(new HttpGet("https://www.taobao.com")).getEntity().getContentLength();
        if(contentLength>10){
            System.out.println("test success..");
        }else{
            System.out.println("test fail..");
        }
        return httpClient;
    }
}