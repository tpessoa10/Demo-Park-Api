package com.mballen.demo_park_api.exception;

import lombok.Getter;

@Getter
public class ReciboCheckinNotFoundExcpetion extends RuntimeException{

    private String recibo;

    public ReciboCheckinNotFoundExcpetion(String recibo){
        this.recibo = recibo;
    }
}
