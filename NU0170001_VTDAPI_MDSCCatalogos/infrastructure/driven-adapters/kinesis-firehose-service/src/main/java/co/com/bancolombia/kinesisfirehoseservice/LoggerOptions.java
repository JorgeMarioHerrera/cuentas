package co.com.bancolombia.kinesisfirehoseservice;

public class LoggerOptions {

    public static final class Actions{

        // KINESIS FIREHOSE SERVICE VALUES
        public static final String KFS_RESOURCE_NOT_FOUND = "The resources on the kinesis library where not found";
        public static final String KFS_SERVICE_UNAVAILABLE = "The service where not found available";
        public static final String KFS_AWS_SERVICE_EXCEPTION = "AWS Exception wasn't available";
        public static final String KFS_SDK_SERVICE_EXCEPTION = "The SDK service isn't available";
        public static final String KFS_SAVE_SUCCESS = "Response from the Step functional, after saving on Kinesis";
        public static final String KFS_ILLEGAL_ARGUMENT_EXCEPTION = "Logs when a method has been passed an " +
                "illegal or inappropriate argument";
    }
    public static final class Services{
        public static final String KFS_SERVICE_NAME = "Kinesis Firehose Service";

        private Services() {}
    }

}
