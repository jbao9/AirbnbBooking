package com.joanna.staybooking.exception;

                        //RuntimeException： 程序只有在启动起来的时候，才有可能抛出exception
public class StayNotExistException extends RuntimeException {
    public StayNotExistException(String message) {
        super(message);  //super() 调用父类的constructor
    }
}
