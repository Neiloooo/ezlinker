package com.ezlinker.app.scriptengine;

/**
 * @author wangwenhai
 * @date 2019/12/8
 * File description: 脚本引擎
 */
public interface ScriptEngine {
    boolean start();
    Object run(String script);
    boolean stop();
}
