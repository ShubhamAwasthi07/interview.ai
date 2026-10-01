package com.shubham.interviewai.authModule.Utils;

import java.util.Random;

public class OtpUtil {

    public static Integer generateOtp() {
        Random random = new Random();
        return 100000 + random.nextInt(900000);
    }
}
