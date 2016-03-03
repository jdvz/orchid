    package resources
    //
    // Built on Fri Jul 20 07:12:34 CEST 2012 by logback-translator
    // For more information on configuration files in Groovy
    // please see http://logback.qos.ch/manual/groovy.html

    // For assistance related to this tool or configuration files
    // in general, please contact the logback user mailing list at
    //    http://qos.ch/mailman/listinfo/logback-user

    // For professional support please see
    //   http://www.qos.ch/shop/products/professionalSupport


    import ch.qos.logback.classic.encoder.PatternLayoutEncoder
    import ch.qos.logback.core.ConsoleAppender
    import ch.qos.logback.core.rolling.RollingFileAppender
    import ch.qos.logback.core.rolling.TimeBasedRollingPolicy

    import static ch.qos.logback.classic.Level.DEBUG
    import static ch.qos.logback.classic.Level.INFO

    setupAppenders()
    setupLogger()

    def setupAppenders() {
        def logpath = "log"
        def logfileDate = timestamp('yyyy-MM-dd')
        def filePatternFormat = "%d{HH:mm:ss.SSS} %-5level %logger - %msg%n"

        appender("STDOUT", ConsoleAppender) {
            encoder(PatternLayoutEncoder) {
                pattern = filePatternFormat
            }
        }
        appender("ROOT_FILE_LOGGER", RollingFileAppender) {
            prudent = true
            append = true
            file = "${logpath}/http.log"
            rollingPolicy(TimeBasedRollingPolicy) {
                fileNamePattern = "${logpath}/http.%d{yyyy-MM-dd}.log"
            }
            encoder(PatternLayoutEncoder) {
                pattern = filePatternFormat
            }
        }
        appender("APP_FILE_LOGGER", RollingFileAppender) {
            prudent = true
            append = true
            file = "${logpath}/fluser.log"
            rollingPolicy(TimeBasedRollingPolicy) {
                fileNamePattern = "${logpath}/user.%d{yyyy-MM-dd}.log"
            }
            encoder(PatternLayoutEncoder) {
                pattern = filePatternFormat
            }
        }
    }

    def setupLogger() {
        root(getLogLevel(), ["STDOUT", "ROOT_FILE_LOGGER"])
        logger "ru.club.orchids", getLogLevel(), ["APP_FILE_LOGGER"]
    }

    def getLogLevel() {
        INFO
    //    (isDevelopmentEnv() ? DEBUG : INFO)
    }

    def isDevelopmentEnv() {
        def env =  System.properties['app.env'] ?: 'DEV'
        env == 'DEV'
    }