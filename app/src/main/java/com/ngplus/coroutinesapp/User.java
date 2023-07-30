package com.ngplus.coroutinesapp;



import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class User {
    @NotNull
    String name;
    public User(@NotNull String name ){
        this.name = name;
    }

    public void create(Created created){
        created.onCreate(this);
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                '}';
    }

    public User provideData(){
        return this;
    }
    public @NotNull User provideData1(){
        return this;
    }
    public @Nullable User provideData2(){
        return this;
    }


}
