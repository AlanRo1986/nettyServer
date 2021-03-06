#Socket IO Server框架

##1.框架说明
```$xslt
    SocketIOServer框架是一款专门为硬件与计算机设备或计算机数据通信的框架。主要核心功能
    是使用Netty插件作为socket数据通信的服务端，Kafka插件作为消息通知，并使用Redis作为数据缓存，
    持久化数据层使用Mybatis，数据库使用Mysql。
    
    系统架构采用:SpringBoot + Mybatis + Netty + Kafka + Redis 简称SMNKR架构。
    其中Kafka插件需要zookeeper支持，详细的配置，请自行search。
      
    框架核心业务：socket通信及报文解析，同时集成了对websocket&UDP协议支持，
    详情请查阅：com.lanxinbase.socket..包中的代码。
    测试数据：两台计算机作为客户终端（socket），普通电脑作为一个服务终端（HD硬盘），
    每台客户端分别模拟16000~16400个线程，线程发送消息后会自动休眠5s。
    测试结果：可支持〉32546个客户端连接，由于资源有限，所以不做更高的测试，理论上可以支撑到15万socket客户端。
    测试代码：test/java/com/lanxinbase/JavaUI.java
```
##2.框架包说明
```$xslt
    Spring 5.0.6
    Spring-boot 2.1.0
    Spring-data-redis 2.1.0
    Spring-kafka 2.2.0
    Netty-all 4.1.24
    Jedis 2.9.0
    Gson 2.8.2
    Commons-codec 1.10
```
##3.使用方法
```$xslt
    使用git clone 本项目，使用IDEA打开，启动NettyApplication即可。
    框架默认集成了socket服务，配置文件：/config/NettyConfig.class
                   kafka服务，配置文件：/config/KafkaConfig.class
                   redis服务，配置文件：/config/CacheConfig.class
    
    #3.1 向业务服务器注册socket server信息
        KafkaListener(topics = Constant.KAFKA_TOPIC_SERVER)
        public void receiver(String message) throws IllegalServiceException {
            //do something in here....
        }
        
        * 说明：当服务器运行成功后，会发送一条服务信息（SocketIOServerInfo.class）到kafka中的Constant.KAFKA_TOPIC_SERVER的频道。
        
        
    #3.2 发送消息socket.id客户端
        String key = "socket key";
        String msg = "msg body";
        MessageIOModel m = new MessageIOModel(key,msg);
        try {
            kafkaService.send(JsonUtils.ObjectToJson(m,true),Constant.KAFKA_TOPIC_SEND);
        } catch (IllegalServiceException e) {
            e.printStackTrace();
        }
        
        * 注：more code you can see:KafkaServiceImpl.class:138
    
    #3.3 监听socket.io客户端消息
        @KafkaListener(topics = Constant.KAFKA_TOPIC_DEFAULT)
        public void receiver(String message) throws IllegalServiceException {
            //do something in here....
        }
        
        * 注：这里只需要使用kafka监听Constant.KAFKA_TOPIC_DEFAULT频道即可
        
    #3.4 socket.io登入
        每当有客户端连接服务器成功后，服务器都会发送一条消息到kafka中的Constant.KAFKA_TOPIC_DEFAULT频道
        业务服务收到此消息后，可通过key字段进行客户端基础信息缓存。
            如：add to cache.
         
        当有主体消息进入后，业务服务器可根据业务消息字段，进行业务处理。
            如：登入主体：{key:...,time:...} 
                消息主体：{key:...,time:....,username:...,password:...}
        每一个消息主体都包含key字段。
        
    #3.5 socket.io登出
        客户端收到登出指令后，可进行客户端业务离线处理。
    
```

##4.打包编译
    使用mvn clean package命令打包编译前，请确保您的Redis、Zookeeper、Kafka组件已预先启动。
    

