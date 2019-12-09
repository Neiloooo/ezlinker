package com.ezlinker.app;

import delight.nashornsandbox.NashornSandbox;
import delight.nashornsandbox.NashornSandboxes;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.jse.JsePlatform;

import javax.script.*;
import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.Reader;
import java.util.concurrent.Executors;

/**
 * @author wangwenhai
 * @date 2019/12/8
 * File description: Lua test
 */
public class SScriptEngineTest {


    public static void main(String [] args) throws ScriptException {
        NashornSandbox sandbox = NashornSandboxes.create();

        sandbox.setMaxCPUTime(100);
        sandbox.setMaxMemory(50 * 1024);
        sandbox.allowNoBraces(false);
        sandbox.setMaxPreparedStatements(30); // because preparing scripts for execution is expensive
        sandbox.setExecutor(Executors.newSingleThreadExecutor());

        sandbox.eval("var o={}, i=0; while (true) {o[i++]='abc';};");
    }
}
