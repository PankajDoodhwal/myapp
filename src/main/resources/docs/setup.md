# Server setup:-

1. Creating a  jar 

    Use this command to create a jar of gradle project
    > ./gradlew bootJar
   
   2. Starting the server

       Use this command to run the jar with external configuration.
   >    java -jar myapp-0.0.1-SNAPSHOT.jar --logging.config=./config/logback-spring.xml
   
      3. Create a new Folder for the server (server)
         1. .jar file
         2. create a log folder
         3. create a config folder

          ### jar file
          Just upload the jar file here

          ### log folder
          Just create the log folder and leave it empty

          #### Config folder
          1. application.properties
         2. logback-spring.xml

          ### application.properties

          this file contains some of the properties we need to configure rest of the properties like database and profile and username and password of the database and port of the server.

          ```properties
          spring.application.name=myapp
          spring.profiles.active=uat
    
          #for developers
          #spring.datasource.url=jdbc:mysql://localhost:3306/demo
          #for testing
          spring.datasource.url=jdbc:mysql://localhost:3306/testdemo
          #for production
          #spring.datasource.url=jdbc:mysql://localhost:3306/proddemo
          spring.datasource.username=root
          spring.datasource.password=Pankaj@2004
          # office lap pass
          #spring.datasource.password=Password@123
          spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
    
          spring.jpa.hibernate.ddl-auto=update
          spring.jpa.show-sql=false
          spring.jpa.properties.hibernate.format_sql=false
          spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
    
          server.port=8084
    
    
          # Show your own logs
          logging.level.com.example=DEBUG
    
          # Mute or reduce framework logs
          logging.level.org.hibernate.SQL=DEBUG
          logging.level.org.hibernate.type.descriptor.sql=TRACE
          logging.level.org.springframework.web=INFO
          logging.level.org.springframework.boot.autoconfigure=INFO
          logging.level.org.apache.coyote.http11=INFO
    
          # spring.jpa.properties.hibernate.session_factory.statement_inspector=com.example.myapp.sql.CustomStatementInspector
    
          spring.security.app.jwtSecret = =========================Spring=Secrity====================
          spring.security.app.jwtExpirationMs = 86400000
          ```
        
           ### logback-spring.xml
           This file is already configured if we want to reduce and increase the log file size then we configure this file. 
            ```xml
              <configuration scan="true">
              <!--  DEV Profile: Unchanged, focuses on simple console and single file logging  -->
              <springProfile name="dev">
              <appender name="DEV_CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
              <encoder>
              <pattern> %cyan(%d{yyyy-MM-dd HH:mm:ss.SSS}) | %highlight(%-5level) | %green([%thread]) | %blue(%logger{36}) : %msg%n </pattern>
              </encoder>
              </appender>
              <appender name="DEV_FILE" class="ch.qos.logback.core.FileAppender">
              <file>logs/dev.log</file>
              <append>true</append>
              <encoder>
              <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
              </encoder>
              </appender>
              <!--  Quieten down Hibernate logging to prevent SQL queries from showing  -->
              <logger name="org.hibernate.SQL" level="INFO"/>
              <logger name="org.hibernate.type.descriptor.sql" level="INFO"/>
              <!--  Example root for dev: logging DEBUG to console and file  -->
              <root level="DEBUG">
              <appender-ref ref="DEV_CONSOLE"/>
              <appender-ref ref="DEV_FILE"/>
              </root>
              </springProfile>
              <!--  PROD Profile: Updated with Size and Time Based Rolling  -->
              <springProfile name="prod">
              <appender name="PROD_FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
              <!--  The currently active log file  -->
              <file>logs/prodApp.log</file>
              <!--  Use SizeAndTimeBasedRollingPolicy  -->
              <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
              <!-- 
                                Pattern for archived files.
                                %d{yyyy-MM-dd} rolls over daily.
                                %i is the index (0, 1, 2, ...) for files on the same day.
                               -->
              <fileNamePattern>logs/archived/app-%d{yyyy-MM-dd}.%i.log.gz</fileNamePattern>
              <!-- 
                                Maximum size of each individual log file.
                                Can use KB, MB, GB. When this size is reached, a new file is created.
                               -->
              <maxFileSize>10MB</maxFileSize>
              <!-- 
                                Total size of all archived files. When the total size exceeds this,
                                the oldest archives will be deleted.
                               -->
              <totalSizeCap>1GB</totalSizeCap>
              <!-- 
                                How long to keep archived files (e.g., 30 days).
                                This works alongside totalSizeCap.
                               -->
              <maxHistory>30</maxHistory>
              </rollingPolicy>
              <encoder>
              <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} %-5level [%thread] %logger{36} - %msg%n</pattern>
              </encoder>
              </appender>
              <!--  Quieten down Hibernate logging in production as well  -->
              <logger name="org.hibernate.SQL" level="INFO"/>
              <logger name="org.hibernate.type.descriptor.sql" level="INFO"/>
              <root level="INFO">
              <appender-ref ref="PROD_FILE"/>
              </root>
              </springProfile>
              <springProfile name="uat">
              <appender name="UAT_FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
              <!--  The currently active log file  -->
              <file>logs/uat.log</file>
              <!--  Use SizeAndTimeBasedRollingPolicy  -->
              <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
              <!-- 
                                Pattern for archived files.
                                %d{yyyy-MM-dd} rolls over daily.
                                %i is the index (0, 1, 2, ...) for files on the same day.
                               -->
              <fileNamePattern>logs/archived/app-%d{yyyy-MM-dd}.%i.log.gz</fileNamePattern>
              <!-- 
                                Maximum size of each individual log file.
                                Can use KB, MB, GB. When this size is reached, a new file is created.
                               -->
              <maxFileSize>10MB</maxFileSize>
              <!-- 
                                Total size of all archived files. When the total size exceeds this,
                                the oldest archives will be deleted.
                               -->
              <totalSizeCap>10GB</totalSizeCap>
              <!-- 
                                How long to keep archived files (e.g., 30 days).
                                This works alongside totalSizeCap.
                               -->
              <maxHistory>30</maxHistory>
              </rollingPolicy>
              <encoder>
              <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} %-5level [%thread] %logger{36} - %msg%n</pattern>
              </encoder>
              </appender>
              <!--  Quieten down Hibernate logging in production as well  -->
              <logger name="org.hibernate.SQL" level="INFO"/>
              <logger name="org.hibernate.type.descriptor.sql" level="INFO"/>
              <root level="INFO">
              <appender-ref ref="UAT_FILE"/>
              </root>
              </springProfile>
              </configuration>
            ```

      ## Important points to remember before hiting a request.
    Check all the tables are created in the database and proper log file is created or not and if everything is okay then start testing and go ahead.