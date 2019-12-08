package com.ezlinker.app.scriptengine.jsengine;

import com.ezlinker.app.scriptengine.ScriptEngine;

/**
 * @author wangwenhai
 * @date 2019/12/8
 * File description: Javascript 引擎
 */
public class JsEngine implements ScriptEngine {
    @Override
    public boolean start() {
        return false;
    }

    @Override
    public Object run(String script) {
        return null;
    }

    @Override
    public boolean stop() {
        return false;
    }
}
