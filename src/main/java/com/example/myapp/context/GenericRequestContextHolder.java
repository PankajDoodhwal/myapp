package com.example.myapp.context;

public class GenericRequestContextHolder {
    private static final ThreadLocal<GenericRequestContext> holder = new ThreadLocal<>();

    public static void init(){
        holder.set(new GenericRequestContext());
    }

    public static GenericRequestContext get(){
        return holder.get();
    }

    public static void clear(){
        holder.remove();
    }

    public static void set(GenericRequestContext context) {
        holder.set(context);
    }
}
